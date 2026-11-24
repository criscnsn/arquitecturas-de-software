package com.equipo.crud.model;

/**
 * Enumeración que define los roles de acceso disponibles en el sistema.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Controlar los permisos dentro de la mejora de <b>Manejo de sesiones</b>:
 * {@link #GERENTE} accede al módulo completo CRUD de empleados y {@link #EMPLEADO} accede
 * únicamente a la vista de directorio en modo solo lectura.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Completar la documentación Javadoc de cada constante y, si se requiere,
 *       agregar una etiqueta legible para mostrar en pantalla.</li>
 * </ul>
 */
public enum Rol {
    /** Usuario con permisos de administración (CRUD) sobre empleados. */
    GERENTE,
    /** Usuario con acceso de solo lectura al directorio y a su propia información laboral. */
    EMPLEADO
}
