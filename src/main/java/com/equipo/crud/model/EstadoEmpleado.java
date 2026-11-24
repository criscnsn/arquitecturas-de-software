package com.equipo.crud.model;

/**
 * Enumeración que representa el estado laboral actual de un usuario/empleado.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Permitir al gerente actualizar la situación laboral del empleado
 * y al empleado consultar su estado actual al iniciar sesión.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Completar el Javadoc y, si ayuda a las vistas de Mateo, agregar un texto
 *       amigable por constante (ej. {@code "Activo"}, {@code "Vacaciones"}, {@code "Incapacidad"}).</li>
 * </ul>
 */
public enum EstadoEmpleado {
    /** El empleado labora normalmente. */
    ACTIVO,
    /** El empleado se encuentra en periodo vacacional. */
    VACACIONES,
    /** El empleado se encuentra en incapacidad. */
    INCAPACIDAD
}
