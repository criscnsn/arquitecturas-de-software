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
 * <b>TODO pendientes (Gael):</b>
 * </p>
 * <ul>
 * <li>En {@code buscarEmpleados}: si {@code termino} es nulo o vacío, retornar
 * {@code listarEmpleados()}.</li>
 * <li>En {@code obtenerEmpleadoPorId}: reemplazar el acceso directo a {@code Optional.get()}
 * para lanzar {@code EmpleadoNotFoundException} cuando no exista un empleado con ese ID.</li>
 * </ul>
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    /** Repositorio para consultar y persistir los usuarios del sistema. */
    private final UsuarioRepository repository;

    /** Rol que delimita los registros administrados por este servicio. */
    private static final Rol rol = Rol.EMPLEADO;

    /**
     * Construye el servicio con el repositorio de usuarios.
     *
     * @param repository repositorio utilizado para consultar y modificar empleados
     */
    public EmpleadoServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    /**
     * Consulta los usuarios con rol de empleado, ordenados por ID ascendente.
     *
     * @return lista de empleados registrados, que puede estar vacía
     */
    @Override
    public List<Usuario> listarEmpleados() {
        return repository.findByRolOrderByIdAsc(rol);
    }

    /**
     * Busca empleados por nombre completo, correo electrónico o teléfono, sin consultar la dirección.
     *
     * <p>La implementación actual rechaza términos nulos y delega el resto de la búsqueda
     * al repositorio; la normalización de espacios se realiza en el controlador.</p>
     *
     * @param termino texto no nulo utilizado como criterio de búsqueda
     * @return empleados que coinciden con el criterio, ordenados por ID ascendente
     * @throws IllegalArgumentException si el término es nulo
     */
    @Override
    public List<Usuario> buscarEmpleados(String termino) {
        if (termino == null) {
            throw new IllegalArgumentException("El término de búsqueda no puede ser nulo");
        }
        return repository.buscarPorRolYTermino(rol, termino);
    }

    /**
     * Consulta un usuario por ID comprobando que tenga rol de empleado.
     *
     * <p>El acceso actual al resultado mediante {@code Optional.get()} produce
     * {@code NoSuchElementException} cuando no hay coincidencia. Está pendiente
     * unificar ese caso con la excepción definida en el contrato del servicio.</p>
     *
     * @param id identificador no nulo del empleado
     * @return empleado encontrado
     * @throws EmpleadoNotFoundException si el ID es nulo
     * @throws java.util.NoSuchElementException si no existe un empleado con ese ID
     */
    @Override
    public Usuario obtenerEmpleadoPorId(Long id) {
        if (id == null) {
            throw new EmpleadoNotFoundException("El ID del empleado no puede ser nulo");
        }
        return repository.findByIdAndRol(id, rol).get();
    }

    /**
     * Persiste un empleado nuevo con ID generado por la base de datos y rol {@code EMPLEADO}.
     *
     * <p>Descarta cualquier ID recibido y exige una contraseña no vacía antes de guardar.</p>
     *
     * @param empleado datos del empleado que se dará de alta
     * @return empleado persistido con el ID asignado
     * @throws IllegalArgumentException si el empleado es nulo o su contraseña es nula o está en blanco
     * @throws org.springframework.dao.DataIntegrityViolationException si los datos violan
     *         una restricción de persistencia
     */
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

    /**
     * Actualiza los campos editables de un empleado existente dentro de una transacción.
     *
     * <p>Conserva el ID y el rol del registro existente. Solo sustituye la contraseña
     * cuando la nueva tiene contenido distinto de espacios en blanco.</p>
     *
     * @param id identificador del empleado que se modificará
     * @param datosActualizados nuevos valores de los campos editables
     * @return empleado actualizado
     * @throws EmpleadoNotFoundException si el ID es nulo o no identifica a un empleado existente
     * @throws IllegalArgumentException si los datos actualizados son nulos
     * @throws org.springframework.dao.DataIntegrityViolationException si los nuevos datos
     *         violan una restricción de persistencia
     */
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

    /**
     * Elimina un registro después de comprobar que corresponde a un empleado.
     *
     * @param id identificador del empleado que se eliminará
     * @throws EmpleadoNotFoundException si el ID es nulo o no corresponde a un empleado existente
     * @throws IllegalArgumentException si el repositorio devuelve un registro con rol de gerente
     * @throws org.springframework.dao.DataIntegrityViolationException si una restricción
     *         de persistencia impide eliminar el registro
     */
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

    /**
     * Elimina la selección después de comprobar que todos sus IDs corresponden a empleados.
     *
     * <p>Compara la cantidad de registros recuperados con la cantidad de IDs recibidos
     * antes de solicitar el borrado al repositorio; la selección debe contener IDs únicos.</p>
     *
     * @param ids lista no nula ni vacía de identificadores únicos que se eliminarán
     * @throws IllegalArgumentException si la lista es nula, está vacía o incluye un gerente recuperado
     * @throws EmpleadoNotFoundException si no se recupera un empleado por cada ID solicitado
     * @throws org.springframework.dao.DataIntegrityViolationException si una restricción
     *         de persistencia impide completar la eliminación
     */
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
