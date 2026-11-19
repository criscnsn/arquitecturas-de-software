package com.equipo.crud.exception;

/**
 * Excepción personalizada lanzada cuando no se encuentra un registro solicitado en la base de datos.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Cumplir con el criterio de rúbrica de <b>manejo de al menos una excepción</b>
 * (2 puntos), señalizando de forma clara cuándo un {@code Producto} no existe al intentar
 * consultarlo, editarlo o eliminarlo.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Definir constructores útiles (ej. recibiendo el {@code id} no encontrado o un mensaje personalizado).</li>
 *   <li>Documentar con Javadoc para incluirla en el Diagrama de Clases y el documento PDF.</li>
 * </ul>
 */
public class ProductoNotFoundException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message detalle del error
     */
    public ProductoNotFoundException(String message) {
        super(message);
    }

    // TODO: Agregar constructor opcional por ID si el equipo lo requiere.
}
