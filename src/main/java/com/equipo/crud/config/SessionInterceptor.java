package com.equipo.crud.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor HTTP que protege las rutas privadas verificando la existencia de una sesión activa.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Parte de la mejora de <b>Manejo de sesiones</b>. Antes de ejecutar
 * cualquier controlador protegido (como {@code /productos/**}), comprueba si existe el atributo
 * {@code "usuarioSesion"} en la {@code HttpSession}. Si no existe, redirige al usuario a {@code /login}.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Implementar la validación de {@code request.getSession(false)} y del atributo {@code "usuarioSesion"}.</li>
 *   <li>Si no hay sesión válida, redirigir a {@code /login} y retornar {@code false}.</li>
 * </ul>
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO (Gael): Verificar sesión activa y redirigir a /login si no está autenticado.
        return true;
    }
}
