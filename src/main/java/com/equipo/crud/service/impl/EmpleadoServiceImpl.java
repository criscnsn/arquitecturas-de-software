package com.equipo.crud.service.impl;

import com.equipo.crud.exception.EmpleadoNotFoundException;
import com.equipo.crud.model.Rol;
import com.equipo.crud.model.Usuario;
import com.equipo.crud.repository.UsuarioRepository;
import com.equipo.crud.service.EmpleadoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la lógica de negocio para {@link EmpleadoService}.
 *
 * <p>
 * <b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})
 * </p>
 * <p>
 * <b>Función:</b> Ejecutar las reglas de negocio del manejador de empleados
 * sobre
 * {@code UsuarioRepository}: filtrar siempre por {@code Rol.EMPLEADO}, ejecutar
 * el buscador
 * (nombre completo, correo o teléfono; sin dirección), impedir borrado/edición
 * de gerentes,
 * conservar la contraseña cuando no se modifique al editar y lanzar
 * {@code EmpleadoNotFoundException}.
 * </p>
 *
 * <p>
 * <b>TODO (Gael):</b>
 * </p>
 * <ul>
 * <li>Inyectar {@code UsuarioRepository} mediante constructor.</li>
 * <li>Implementar cada método reemplazando las excepciones temporales de este
 * esqueleto.</li>
 * <li>En {@code crearEmpleado}: asignar {@code Rol.EMPLEADO} (los gerentes solo
 * se crean desde BD).</li>
 * <li>En {@code actualizarEmpleado}: actualizar todos los campos editables
 * excepto {@code id} y {@code rol};
 * si {@code contrasena} viene nula o en blanco, mantener la contraseña
 * actual.</li>
 * <li>En {@code eliminarEmpleadoPorId} y
 * {@code eliminarEmpleadosSeleccionados}: verificar que solo
 * se eliminen registros con {@code Rol.EMPLEADO} y lanzar
 * {@code EmpleadoNotFoundException} si no existe.</li>
 * <li>En {@code buscarEmpleados}: si {@code termino} es nulo o vacío, retornar
 * {@code listarEmpleados()}.</li>
 * </ul>
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    // TODO: Inyectar UsuarioRepository por constructor.
    private final UsuarioRepository repository;

    private static final Rol rol = Rol.EMPLEADO;

    public EmpleadoServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Usuario> listarEmpleados() {
        return repository.findByRolOrderByIdAsc(rol);
    }

    @Override
    public List<Usuario> buscarEmpleados(String termino) {
        if (termino == null) {
            throw new IllegalArgumentException("El término de búsqueda no puede ser nulo");
        }
        return repository.buscarPorRolYTermino(rol, termino);
    }

    @Override
    public Usuario obtenerEmpleadoPorId(Long id) {
        if (id == null) {
            throw new EmpleadoNotFoundException("El ID del empleado no puede ser nulo");
        }
        return repository.findByIdAndRol(id, rol).get();
    }

    @Override
    public Usuario crearEmpleado(Usuario empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }
        if (empleado.getContrasena() == null || empleado.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria para crear un empleado");
        }
        // Un alta siempre inserta un registro nuevo, aunque el llamador proporcione un ID.
        empleado.setId(null);
        empleado.setRol(rol);
        return repository.save(empleado);
    }

    @Override
    @Transactional
    public Usuario actualizarEmpleado(Long id, Usuario datosActualizados) {
        if (id == null) {
            throw new EmpleadoNotFoundException("El ID del empleado no puede ser nulo");
        }
        if (datosActualizados == null) {
            throw new IllegalArgumentException("Los datos actualizados no pueden ser nulos");
        }

        Usuario existente = repository.findByIdAndRol(id, rol)
                .orElseThrow(() -> new EmpleadoNotFoundException(id));

        existente.setUsuario(datosActualizados.getUsuario());
        existente.setNombre(datosActualizados.getNombre());
        existente.setApellido1(datosActualizados.getApellido1());
        existente.setApellido2(datosActualizados.getApellido2());
        existente.setEstado(datosActualizados.getEstado());
        existente.setFechaIngreso(datosActualizados.getFechaIngreso());
        existente.setCorreoElectronico(datosActualizados.getCorreoElectronico());
        existente.setTelefono(datosActualizados.getTelefono());
        existente.setDireccion(datosActualizados.getDireccion());
        existente.setSalario(datosActualizados.getSalario());

        if (datosActualizados.getContrasena() != null && !datosActualizados.getContrasena().isBlank()) {
            existente.setContrasena(datosActualizados.getContrasena());
        }

        return repository.save(existente);
    }

    @Override
    public void eliminarEmpleadoPorId(Long id) {
        if (id == null) {
            throw new EmpleadoNotFoundException("El ID del empleado no puede ser nulo");
        }

        Usuario empleado = repository.findByIdAndRol(id, rol)
                .orElseThrow(() -> new EmpleadoNotFoundException(id));
        if (empleado.getRol() == Rol.GERENTE) {
            throw new IllegalArgumentException("No se puede eliminar un gerente");
        }
        repository.delete(empleado);
    }

    @Override
    public void eliminarEmpleadosSeleccionados(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs no puede ser nula o vacía");
        }

        List<Usuario> empleados = repository.findAllByIdInAndRol(ids, rol);
        if (empleados.size() != ids.size()) {
            throw new EmpleadoNotFoundException("Algunos empleados no fueron encontrados");
        }

        for (Usuario empleado : empleados) {
            if (empleado.getRol() == Rol.GERENTE) {
                throw new IllegalArgumentException("No se puede eliminar un gerente");
            }
        }

        repository.deleteAll(empleados);
    }
}
