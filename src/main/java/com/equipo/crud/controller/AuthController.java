package com.equipo.crud.controller;

import org.springframework.stereotype.Controller;

/**
 * Controlador Spring MVC para el inicio y cierre de sesión con control por roles
 * (Mejora: <b>Manejo de sesiones</b>).
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Gestionar {@code HttpSession}, autenticar mediante {@code AuthService}
 * y redirigir a cada usuario a la pantalla que corresponde a su {@code Rol}:
 * {@code GERENTE} -&gt; {@code /empleados}, {@code EMPLEADO} -&gt; {@code /empleados/directorio}.</p>
 *
 * <p><b>Contrato de rutas acordado con Vistas (Mateo):</b></p>
 * <ul>
 *   <li>{@code GET  /} -&gt; Si no hay sesión redirige a {@code /login}; si hay sesión redirige según el rol.</li>
 *   <li>{@code GET  /login} -&gt; Muestra {@code "auth/login"} (si ya tiene sesión activa, redirige según su rol).</li>
 *   <li>{@code POST /login} -&gt; Recibe {@code usuario} y {@code contrasena}. Si autentica, guarda
 *       {@code "usuarioSesion"} en {@code HttpSession} y redirige según su rol; si falla retorna
 *       {@code "auth/login"} con atributo {@code "errorLogin"}.</li>
 *   <li>{@code POST /logout} -&gt; Invalida la {@code HttpSession} y redirige a {@code /login}.</li>
 * </ul>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code AuthService} por constructor.</li>
 *   <li>Implementar {@code GET /}, {@code GET /login}, {@code POST /login} y {@code POST /logout}.</li>
 * </ul>
 */
@Controller
public class AuthController {

    // TODO: Inyectar AuthService por constructor.

    // TODO: Implementar endpoints de login, logout y redirección por rol.
}
