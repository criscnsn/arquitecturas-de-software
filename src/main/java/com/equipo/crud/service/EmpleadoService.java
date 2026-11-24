package com.equipo.crud.service;

import com.equipo.crud.model.Usuario;
import java.util.List;

/**
 * Contrato de servicios de negocio para el manejo (CRUD) y búsqueda de empleados.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Desacoplar {@code EmpleadoController} de la lógica de negocio y persistencia,
 * garantizando que todas las operaciones del CRUD actúen exclusivamente sobre usuarios con rol
 * {@code EMPLEADO} (nunca sobre gerentes). Además, funciona como <b>Interfaz de Salida/Entrada</b>
 * para el Diagrama de Componentes UML del documento PDF.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Verificar firmas con Cristopher una vez completado {@code UsuarioRepository}.</li>
 *   <li>Completar el Javadoc detallando parámetros, retornos y excepciones lanzadas.</li>
 * </ul>
 */
public interface EmpleadoService {

    /**
     * Lista únicamente a los usuarios con rol {@code EMPLEADO} (excluyendo gerentes).
     *
     * @return lista de empleados registrados
     */
    List<Usuario> listarEmpleados();

    /**
     * Filtra empleados por nombre completo, correo electrónico o teléfono (<b>dirección NO</b>).
     * (Mejora: <b>Buscador</b>).
     *
     * @param termino texto de búsqueda ingresado por el gerente
     * @return lista de empleados que coinciden con el criterio
     */
    List<Usuario> buscarEmpleados(String termino);

    /**
     * Obtiene un empleado específico por su identificador único.
     * Debe lanzar {@code EmpleadoNotFoundException} si no existe o si el registro es un gerente.
     *
     * @param id identificador del empleado
     * @return empleado encontrado
     */
    Usuario obtenerEmpleadoPorId(Long id);

    /**
     * Da de alta un nuevo empleado forzando {@code rol = Rol.EMPLEADO}.
     * El {@code id} lo asigna automáticamente MySQL.
     *
     * @param empleado datos del nuevo empleado
     * @return empleado persistido con su ID asignado
     */
    Usuario crearEmpleado(Usuario empleado);

    /**
     * Actualiza todos los campos editables de un empleado existente (todo menos {@code id} y {@code rol}).
     * Si la contraseña llega vacía, se conserva la contraseña actual.
     *
     * @param id identificador del empleado a modificar
     * @param datosActualizados nuevos valores capturados en el modal de edición
     * @return empleado actualizado
     */
    Usuario actualizarEmpleado(Long id, Usuario datosActualizados);

    /**
     * Elimina de forma individual a un empleado por su identificador (protegiendo a los gerentes).
     *
     * @param id identificador del empleado a eliminar
     */
    void eliminarEmpleadoPorId(Long id);

    /**
     * Elimina en lote a los empleados seleccionados mediante checkboxes (protegiendo a los gerentes).
     *
     * @param ids lista de identificadores marcados en la tabla
     */
    void eliminarEmpleadosSeleccionados(List<Long> ids);
}
