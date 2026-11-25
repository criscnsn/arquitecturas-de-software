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
     * @param termino texto no nulo de búsqueda ingresado por el gerente
     * @return lista de empleados que coinciden con el criterio
     * @throws IllegalArgumentException si el término recibido es nulo en la implementación actual
     */
    List<Usuario> buscarEmpleados(String termino);

    /**
     * Obtiene un empleado específico por su identificador único.
     * Debe lanzar {@code EmpleadoNotFoundException} si no existe o si el registro es un gerente.
     *
     * @param id identificador del empleado
     * @return empleado encontrado
     * @throws com.equipo.crud.exception.EmpleadoNotFoundException si el ID es nulo;
     *         el contrato también requiere esta excepción para un registro inexistente o un gerente
     * @throws java.util.NoSuchElementException si no se encuentra un empleado en la implementación
     *         actual, que todavía debe unificar ese caso con {@code EmpleadoNotFoundException}
     */
    Usuario obtenerEmpleadoPorId(Long id);

    /**
     * Da de alta un nuevo empleado forzando {@code rol = Rol.EMPLEADO}.
     * Descarta el ID recibido para que MySQL asigne uno nuevo y exige una contraseña no vacía.
     *
     * @param empleado datos del nuevo empleado
     * @return empleado persistido con su ID asignado
     * @throws IllegalArgumentException si el empleado es nulo o su contraseña es nula o está en blanco
     * @throws org.springframework.dao.DataIntegrityViolationException si los datos violan una restricción
     *         de persistencia, como un usuario o correo ya registrado
     */
    Usuario crearEmpleado(Usuario empleado);

    /**
     * Actualiza todos los campos editables de un empleado existente (todo menos {@code id} y {@code rol}).
     * Si la contraseña llega vacía, se conserva la contraseña actual.
     *
     * @param id identificador del empleado a modificar
     * @param datosActualizados nuevos valores capturados en el modal de edición
     * @return empleado actualizado
     * @throws com.equipo.crud.exception.EmpleadoNotFoundException si el ID es nulo, no existe
     *         o corresponde a un usuario que no tiene rol de empleado
     * @throws IllegalArgumentException si los datos actualizados son nulos
     * @throws org.springframework.dao.DataIntegrityViolationException si los datos actualizados
     *         violan una restricción de persistencia
     */
    Usuario actualizarEmpleado(Long id, Usuario datosActualizados);

    /**
     * Elimina de forma individual a un empleado por su identificador (protegiendo a los gerentes).
     *
     * @param id identificador del empleado a eliminar
     * @throws com.equipo.crud.exception.EmpleadoNotFoundException si el ID es nulo, no existe
     *         o no corresponde a un empleado
     * @throws org.springframework.dao.DataIntegrityViolationException si una restricción
     *         de persistencia impide eliminar el registro
     */
    void eliminarEmpleadoPorId(Long id);

    /**
     * Elimina en lote a los empleados seleccionados mediante checkboxes (protegiendo a los gerentes).
     *
     * @param ids lista no nula ni vacía de identificadores únicos marcados en la tabla
     * @throws IllegalArgumentException si la lista es nula o está vacía
     * @throws com.equipo.crud.exception.EmpleadoNotFoundException si no se puede resolver
     *         la selección completa como registros con rol de empleado
     * @throws org.springframework.dao.DataIntegrityViolationException si una restricción
     *         de persistencia impide completar el borrado
     */
    void eliminarEmpleadosSeleccionados(List<Long> ids);
}
