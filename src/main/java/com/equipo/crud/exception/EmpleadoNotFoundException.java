package com.equipo.crud.exception;

/**
 * Excepción personalizada lanzada cuando no se encuentra un empleado solicitado en la base de datos.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Cumplir con el criterio de rúbrica de <b>manejo de al menos una excepción</b>
 * (2 puntos), señalizando cuando un empleado no existe (o cuando se intenta manipular un registro
 * que no tiene rol {@code EMPLEADO}) al consultar, editar o eliminar.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Agregar un constructor conveniente que reciba el {@code Long id} no encontrado.</li>
 *   <li>Completar el Javadoc para incluir esta clase en el Diagrama de Clases y el documento PDF.</li>
 * </ul>
 */
public class EmpleadoNotFoundException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message detalle del error
     */
    public EmpleadoNotFoundException(String message) {
        super(message);
    }

    // TODO: Agregar constructor opcional EmpleadoNotFoundException(Long id).
}
