package com.equipo.crud.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Manejador global de excepciones para los controladores Spring MVC.
 *
 * <p>Intercepta las excepciones lanzadas durante las peticiones HTTP y redirige
 * a la vista amigable {@code error/error.html} cargando los atributos {@code tituloError}
 * y {@code mensajeError} acordados con la capa de vistas.</p>
 *
 * @author Cristopher Israel Cen Santana
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Atiende los casos en los que un empleado no existe en la base de datos.
     *
     * @param ex excepción lanzada por el servicio
     * @param model modelo de Spring MVC para enviar los mensajes a la vista
     * @return nombre de la vista de error ({@code "error/error"})
     */
    @ExceptionHandler(EmpleadoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String manejarEmpleadoNoEncontrado(EmpleadoNotFoundException ex, Model model) {
        model.addAttribute("tituloError", "Empleado no encontrado");
        model.addAttribute("mensajeError", ex.getMessage());
        return "error/error";
    }

    /**
     * Atiende violaciones de integridad de MySQL (por ejemplo, intento de registrar
     * un nombre de usuario o correo electrónico que ya existe).
     *
     * @param ex excepción de integridad de datos lanzada por Spring/Hibernate
     * @param model modelo de Spring MVC para enviar los mensajes a la vista
     * @return nombre de la vista de error ({@code "error/error"})
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String manejarIntegridadDatos(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("tituloError", "Datos duplicados o inválidos");
        model.addAttribute(
                "mensajeError",
                "No se completó la operación porque el usuario/correo electrónico ya está registrado o algún dato incumple las reglas de la base de datos."
        );
        return "error/error";
    }

    /**
     * Respaldo general para excepciones no previstas, evitando mostrar trazas técnicas al usuario.
     *
     * @param ex excepción inesperada
     * @param model modelo de Spring MVC para enviar los mensajes a la vista
     * @return nombre de la vista de error ({@code "error/error"})
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String manejarErrorGeneral(Exception ex, Model model) {
        log.error("Error inesperado en la aplicación", ex);
        model.addAttribute("tituloError", "Ocurrió un error inesperado");
        model.addAttribute(
                "mensajeError",
                "No fue posible procesar tu solicitud en este momento. Intenta de nuevo o verifica la conexión con la base de datos."
        );
        return "error/error";
    }
}
