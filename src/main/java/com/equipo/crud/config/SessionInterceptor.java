package com.equipo.crud.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Implementar la comprobación de sesión activa y validación de rol en {@link #preHandle}.</li>
 * </ul>
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO (Gael): Validar sesión activa ("usuarioSesion") y permisos por Rol (GERENTE vs EMPLEADO).
        return true;
    }
}
