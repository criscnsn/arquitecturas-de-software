package com.equipo.crud.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.equipo.crud.model.Usuario;
import com.equipo.crud.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador Spring MVC para el inicio y cierre de sesión con control por
 * roles
 * (Mejora: <b>Manejo de sesiones</b>).
 *
 * <p>
 * <b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})
 * </p>
 * <p>
 * <b>Función:</b> Gestionar {@code HttpSession}, autenticar mediante
 * {@code AuthService}
 * y redirigir a cada usuario a la pantalla que corresponde a su {@code Rol}:
 * {@code GERENTE} -&gt; {@code /empleados}, {@code EMPLEADO} -&gt;
 * {@code /empleados/directorio}.
 * </p>
 *
 * <p>
 * <b>Contrato de rutas acordado con Vistas (Mateo):</b>
 * </p>
 * <ul>
 * <li>{@code GET  /} -&gt; Si no hay sesión redirige a {@code /login}; si hay
 * sesión redirige según el rol.</li>
 * <li>{@code GET  /login} -&gt; Muestra {@code "auth/login"} (si ya tiene
 * sesión activa, redirige según su rol).</li>
 * <li>{@code POST /login} -&gt; Recibe {@code usuario} y {@code contrasena}. Si
 * autentica, guarda
 * {@code "usuarioSesion"} en {@code HttpSession} y redirige según su rol; si
 * falla retorna
 * {@code "auth/login"} con atributo {@code "errorLogin"}.</li>
 * <li>{@code POST /logout} -&gt; Invalida la {@code HttpSession} y redirige a
 * {@code /login}.</li>
 * </ul>
 *
 */
@Controller
public class AuthController {

    /** Servicio que comprueba las credenciales de acceso. */
    private final AuthService authService;

    /**
     * Construye el controlador con el servicio de autenticación administrado por Spring.
     *
     * @param authService servicio encargado de validar el usuario y la contraseña
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Atiende {@code GET /} y selecciona la pantalla inicial según el rol en sesión.
     *
     * @param usuarioSesion usuario autenticado, o {@code null} si no existe el atributo de sesión
     * @return redirección a gestión para el gerente, al directorio para el empleado
     *         o al login si no hay usuario en sesión
     */
    @GetMapping("/")
    public String home(
            @SessionAttribute(name = "usuarioSesion", required = false) Usuario usuarioSesion) {
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        switch (usuarioSesion.getRol()) {
            case GERENTE:
                return "redirect:/empleados";
            case EMPLEADO:
                return "redirect:/empleados/directorio";
            default:
                return "redirect:/login";
        }
    }

    /**
     * Atiende {@code GET /login} y muestra el formulario de acceso.
     *
     * @param usuarioSesion usuario autenticado, o {@code null} si todavía no ha iniciado sesión
     * @return vista {@code auth/login}, o redirección al inicio si ya existe un usuario autenticado
     */
    @GetMapping("/login")
    public String login(
            @SessionAttribute(name = "usuarioSesion", required = false) Usuario usuarioSesion

    ) {
        if (usuarioSesion != null) {
            return "redirect:/";
        }
        return "auth/login";
    }

    /**
     * Atiende {@code POST /login}, comprueba las credenciales e inicia la sesión autenticada.
     *
     * <p>Si las credenciales son válidas, obtiene o crea la sesión, renueva su identificador
     * y guarda el usuario bajo el atributo {@code usuarioSesion}. Si fallan, devuelve
     * el formulario con el atributo {@code errorLogin}, sin crear una sesión autenticada.</p>
     *
     * @param usuario nombre de acceso enviado por el formulario
     * @param contrasena contraseña enviada por el formulario
     * @param request petición utilizada para obtener la sesión y renovar su identificador
     * @param model modelo que recibe el mensaje de credenciales inválidas
     * @return redirección al inicio si autentica, o vista {@code auth/login} si falla
     */
    @PostMapping("/login")
    public String loginPost(
            @RequestParam("usuario") String usuario,
            @RequestParam("contrasena") String contrasena,
            HttpServletRequest request,
            Model model) {
        Optional<Usuario> autenticado = authService.autenticar(usuario, contrasena);
        if (autenticado.isEmpty()) {
            model.addAttribute("errorLogin", "Credenciales inválidas");
            return "auth/login";
        }
        HttpSession session = request.getSession(true);
        request.changeSessionId();
        session.setAttribute("usuarioSesion", autenticado.get());

        return "redirect:/";
    }

    /**
     * Atiende {@code POST /logout} e invalida la sesión existente.
     *
     * <p>Si la petición no tiene sesión, realiza la redirección sin crear una nueva.</p>
     *
     * @param request petición de la que se obtiene la sesión actual, si existe
     * @return redirección a {@code /login}
     */
    @PostMapping("/logout")
    public String logout(
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

}
