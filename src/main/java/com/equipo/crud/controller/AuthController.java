package com.equipo.crud.controller;

import org.springframework.stereotype.Controller;

/**
 * Controlador Spring MVC para el inicio y cierre de sesión (Mejora: <b>Manejo de sesiones</b>).
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Gestionar el ciclo de vida de la {@code HttpSession}: mostrar el login,
 * validar credenciales mediante {@code AuthService}, guardar el usuario en sesión y cerrar sesión.</p>
 *
 * <p><b>Contrato de rutas acordado con Vistas (Mateo):</b></p>
 * <ul>
 *   <li>{@code GET  /} -&gt; Redirige a {@code /productos} si hay sesión activa, o a {@code /login}.</li>
 *   <li>{@code GET  /login} -&gt; Muestra la vista {@code "auth/login"}.</li>
 *   <li>{@code POST /login} -&gt; Valida {@code username} y {@code password}; si es válido guarda el atributo
 *       {@code "usuarioSesion"} en {@code HttpSession} y redirige a {@code /productos}; si falla retorna
 *       {@code "auth/login"} con atributo {@code "errorLogin"}.</li>
 *   <li>{@code POST /logout} -&gt; Invalida la {@code HttpSession} y redirige a {@code /login}.</li>
 * </ul>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code AuthService} por constructor.</li>
 *   <li>Implementar los métodos {@code GET /}, {@code GET /login}, {@code POST /login} y {@code POST /logout}.</li>
 * </ul>
 */
@Controller
public class AuthController {

    // TODO: Inyectar AuthService por constructor.

    // TODO: Implementar endpoints de login, logout y redirección raíz.
}
