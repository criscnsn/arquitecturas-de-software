package com.equipo.crud.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.equipo.crud.model.Rol;
import com.equipo.crud.model.Usuario;

/**
 * Interceptor HTTP que protege las rutas según sesión activa y rol de usuario.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Parte central de la mejora de <b>Manejo de sesiones</b>:</p>
 * <ol>
 *   <li>Verifica que exista una {@code HttpSession} con el atributo {@code "usuarioSesion"}.
 *       Si no existe, redirige a {@code /login} y bloquea el acceso.</li>
 *   <li>Valida autorización por {@code Rol}: si el usuario tiene rol {@code EMPLEADO} e intenta
 *       acceder a rutas exclusivas de {@code GERENTE} ({@code /empleados}, {@code /empleados/crear},
 *       {@code /empleados/editar/**}, {@code /empleados/eliminar/**}, {@code /empleados/eliminar-seleccionados}),
 *       lo redirige a {@code /empleados/directorio} y retorna {@code false}.</li>
 * </ol>
 *
 * <p>Las sesiones de usuarios con un rol inválido se invalidan antes de redirigir al login.</p>
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    /**
     * Comprueba la sesión y autoriza la petición antes de ejecutar el controlador.
     *
     * <p>El gerente puede acceder a las rutas registradas; el empleado solo al directorio.
     * Los intentos de un empleado de acceder a otras rutas se redirigen al directorio
     * conservando su sesión. Las sesiones con un rol inválido se invalidan.</p>
     *
     * @param request petición utilizada para consultar la sesión y la ruta solicitada
     * @param response respuesta utilizada para redirigir al login o al directorio
     * @param handler manejador seleccionado por Spring MVC para atender la petición
     * @return {@code true} si puede ejecutarse el controlador; {@code false} si se redirige
     * @throws java.io.IOException si no se puede enviar una redirección HTTP
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String contexto = request.getContextPath();
        if (session == null || session.getAttribute("usuarioSesion") == null) {
            response.sendRedirect(contexto + "/login");
            return false;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioSesion");
        if (usuarioSesion.getRol() == Rol.GERENTE) {
            return true;
        }

        String ruta = request.getRequestURI().substring(contexto.length());
        if (usuarioSesion.getRol() == Rol.EMPLEADO) {
            if ("/empleados/directorio".equals(ruta)) {
                return true;
            }
            response.sendRedirect(contexto + "/empleados/directorio");
            return false;
        }

        session.invalidate();
        response.sendRedirect(contexto + "/login");
        return false;
    }
}
