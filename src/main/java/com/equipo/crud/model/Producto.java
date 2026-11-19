package com.equipo.crud.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un registro de la tabla principal en MySQL.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Mapear los registros de la tabla {@code productos} a objetos Java
 * y validar los datos de entrada antes de persistirlos.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Agregar los atributos requeridos por la rúbrica (mínimo 3 campos):
 *       identificador único ({@code id}), al menos 1 campo de texto (ej. {@code nombre}, {@code descripcion})
 *       y al menos 1 campo numérico (ej. {@code precio}, {@code stock}).</li>
 *   <li>Añadir anotaciones JPA ({@code @Column}) y de validación ({@code @NotBlank}, {@code @NotNull}, {@code @PositiveOrZero}).</li>
 *   <li>Implementar constructores (vacío obligatorio para JPA y parametrizado), getters y setters.</li>
 *   <li>Completar el Javadoc de cada atributo y método para el entregable.</li>
 * </ul>
 */
@Entity
@Table(name = "productos")
public class Producto {

    /**
     * Identificador único del registro (llave primaria mínima para que el esqueleto compile).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Agregar campos de texto y numéricos acordados con el equipo.

    // TODO: Agregar constructores, getters y setters.
}
