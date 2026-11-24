package com.equipo.crud.exception;

/**
 * Excepción personalizada lanzada cuando no se encuentra un empleado solicitado en MySQL
 * o cuando el identificador proporcionado no corresponde a un usuario con rol {@code EMPLEADO}.
 *
 * @author Cristopher Israel Cen Santana
 */
public class EmpleadoNotFoundException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo personalizado.
     *
     * @param message detalle legible del error
     */
    public EmpleadoNotFoundException(String message) {
        super(message);
    }

    /**
     * Crea la excepción a partir del identificador único que no fue encontrado.
     *
     * @param id identificador del empleado buscado
     */
    public EmpleadoNotFoundException(Long id) {
        super("No se encontró el empleado con ID: " + id);
    }
}
