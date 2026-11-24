package com.equipo.crud.service.impl;

import com.equipo.crud.model.Usuario;
import com.equipo.crud.service.EmpleadoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio para {@link EmpleadoService}.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Ejecutar las reglas de negocio del manejador de empleados sobre
 * {@code UsuarioRepository}: filtrar siempre por {@code Rol.EMPLEADO}, ejecutar el buscador
 * (nombre completo, correo o teléfono; sin dirección), impedir borrado/edición de gerentes,
 * conservar la contraseña cuando no se modifique al editar y lanzar {@code EmpleadoNotFoundException}.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code UsuarioRepository} mediante constructor.</li>
 *   <li>Implementar cada método reemplazando las excepciones temporales de este esqueleto.</li>
 *   <li>En {@code crearEmpleado}: asignar {@code Rol.EMPLEADO} (los gerentes solo se crean desde BD).</li>
 *   <li>En {@code actualizarEmpleado}: actualizar todos los campos editables excepto {@code id} y {@code rol};
 *       si {@code contrasena} viene nula o en blanco, mantener la contraseña actual.</li>
 *   <li>En {@code eliminarEmpleadoPorId} y {@code eliminarEmpleadosSeleccionados}: verificar que solo
 *       se eliminen registros con {@code Rol.EMPLEADO} y lanzar {@code EmpleadoNotFoundException} si no existe.</li>
 *   <li>En {@code buscarEmpleados}: si {@code termino} es nulo o vacío, retornar {@code listarEmpleados()}.</li>
 * </ul>
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    // TODO: Inyectar UsuarioRepository por constructor.

    @Override
    public List<Usuario> listarEmpleados() {
        throw new UnsupportedOperationException("TODO (Gael): Implementar listarEmpleados() filtrando por Rol.EMPLEADO");
    }

    @Override
    public List<Usuario> buscarEmpleados(String termino) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar buscarEmpleados(termino) por nombre, correo o teléfono");
    }

    @Override
    public Usuario obtenerEmpleadoPorId(Long id) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar obtenerEmpleadoPorId(id) y lanzar EmpleadoNotFoundException");
    }

    @Override
    public Usuario crearEmpleado(Usuario empleado) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar crearEmpleado(empleado) forzando Rol.EMPLEADO");
    }

    @Override
    public Usuario actualizarEmpleado(Long id, Usuario datosActualizados) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar actualizarEmpleado(id, datosActualizados)");
    }

    @Override
    public void eliminarEmpleadoPorId(Long id) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar eliminarEmpleadoPorId(id) protegiendo gerentes");
    }

    @Override
    public void eliminarEmpleadosSeleccionados(List<Long> ids) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar eliminarEmpleadosSeleccionados(ids) protegiendo gerentes");
    }
}
