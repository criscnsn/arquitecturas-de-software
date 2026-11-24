package com.equipo.crud.model;

/**
 * Enumeración que define los roles de acceso disponibles en el manejador de empleados.
 *
 * <p>Determina las vistas y permisos asignados durante la sesión HTTP:
 * {@link #GERENTE} accede al módulo CRUD completo de empleados y {@link #EMPLEADO}
 * accede únicamente al directorio en modo solo lectura.</p>
 *
 * @author Cristopher Israel Cen Santana
 */
public enum Rol {

    /**
     * Usuario administrador con permisos CRUD completos sobre los empleados.
     */
    GERENTE("Gerente"),

    /**
     * Usuario con acceso de solo lectura al directorio y a su propia información laboral.
     */
    EMPLEADO("Empleado");

    /**
     * Etiqueta legible del rol para mostrarse en la interfaz de usuario.
     */
    private final String etiqueta;

    /**
     * Construye una constante de rol con su etiqueta descriptiva.
     *
     * @param etiqueta nombre legible del rol
     */
    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Obtiene la etiqueta legible del rol.
     *
     * @return texto descriptivo del rol
     */
    public String getEtiqueta() {
        return etiqueta;
    }
}
