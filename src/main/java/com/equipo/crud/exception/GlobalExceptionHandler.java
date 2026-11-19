package com.equipo.crud.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Manejador global de excepciones para los controladores Spring MVC.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Interceptar excepciones lanzadas desde cualquier controlador o servicio
 * (especialmente {@link ProductoNotFoundException} y errores generales) y redirigir a una
 * vista amigable ({@code templates/error/error.html}) en lugar de mostrar una traza cruda.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Implementar un método anotado con {@code @ExceptionHandler(ProductoNotFoundException.class)}
 *       que envíe el mensaje al modelo ({@code tituloError}, {@code mensajeError}) y retorne {@code "error/error"}.</li>
 *   <li>Implementar un método genérico {@code @ExceptionHandler(Exception.class)} para errores inesperados.</li>
 *   <li>Coordinar con Mateo los nombres de los atributos que mostrará {@code error/error.html}.</li>
 * </ul>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: Implementar @ExceptionHandler para ProductoNotFoundException.

    // TODO: Implementar @ExceptionHandler para excepciones generales.
}
