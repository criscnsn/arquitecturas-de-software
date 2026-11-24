package com.equipo.crud.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Manejador global de excepciones para los controladores Spring MVC.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Capturar excepciones lanzadas desde controladores o servicios
 * ({@link EmpleadoNotFoundException}, violaciones de integridad como usuario/correo duplicado
 * y errores generales) y mostrar una respuesta amigable en {@code templates/error/error.html}.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Implementar {@code @ExceptionHandler(EmpleadoNotFoundException.class)} con estado 404,
 *       enviando {@code tituloError} y {@code mensajeError} al {@code Model} y retornando {@code "error/error"}.</li>
 *   <li>Implementar {@code @ExceptionHandler(DataIntegrityViolationException.class)} para informar
 *       amigablemente cuando un {@code usuario} o {@code correo_electronico} ya está registrado.</li>
 *   <li>Implementar {@code @ExceptionHandler(Exception.class)} como respaldo general (estado 500).</li>
 *   <li>Respetar el contrato de atributos acordado con Mateo ({@code tituloError}, {@code mensajeError}).</li>
 * </ul>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: Implementar manejadores @ExceptionHandler para EmpleadoNotFoundException, duplicados y errores generales.
}
