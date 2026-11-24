package com.equipo.crud.model;

/**
 * Enumeración que representa la situación laboral actual de un usuario/empleado.
 *
 * <p>Puede ser modificada por un {@link Rol#GERENTE} desde el modal de edición
 * y consultada por el {@link Rol#EMPLEADO} al iniciar sesión.</p>
 *
 * @author Cristopher Israel Cen Santana
 */
public enum EstadoEmpleado {

    /**
     * El empleado se encuentra laborando normalmente.
     */
    ACTIVO("Activo"),

    /**
     * El empleado se encuentra en periodo vacacional.
     */
    VACACIONES("Vacaciones"),

    /**
     * El empleado se encuentra en incapacidad médica o laboral.
     */
    INCAPACIDAD("Incapacidad");

    /**
     * Etiqueta legible del estado para renderizar en tablas, insignias y selectores.
     */
    private final String etiqueta;

    /**
     * Construye una constante de estado laboral con su etiqueta legible.
     *
     * @param etiqueta texto amigable del estado
     */
    EstadoEmpleado(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Obtiene la etiqueta legible del estado laboral.
     *
     * @return texto descriptivo del estado
     */
    public String getEtiqueta() {
        return etiqueta;
    }
}
