package com.equipo.crud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Spring MVC para registrar interceptores y rutas protegidas.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Registrar {@link SessionInterceptor} sobre {@code /empleados/**}
 * excluyendo las rutas públicas ({@code /login}, {@code /logout}, {@code /css/**}, {@code /js/**}, {@code /error}).</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@link SessionInterceptor} por constructor.</li>
 *   <li>Registrar el interceptor en {@link #addInterceptors(InterceptorRegistry)}.</li>
 * </ul>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // TODO: Inyectar SessionInterceptor por constructor.

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO (Gael): Registrar SessionInterceptor para /empleados y /empleados/** excluyendo rutas públicas.
    }
}
