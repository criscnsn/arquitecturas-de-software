package com.equipo.crud.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un usuario autorizado para iniciar sesión en la aplicación.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Dar soporte a la mejora de <b>Manejo de sesiones</b>, almacenando
 * las credenciales de los usuarios que pueden acceder al módulo CRUD.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Definir atributos: {@code username} (único y no nulo), {@code password} y opcionalmente {@code nombreCompleto}.</li>
 *   <li>Añadir anotaciones JPA ({@code @Column}) y validaciones.</li>
 *   <li>Implementar constructores, getters y setters con su Javadoc.</li>
 * </ul>
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único del usuario (llave primaria mínima para que el esqueleto compile).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Agregar campos username, password y datos adicionales del usuario.

    // TODO: Agregar constructores, getters y setters.
}
