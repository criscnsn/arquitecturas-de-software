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
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /** Interceptor que comprueba la sesión y el rol de acceso al módulo de empleados. */
    private final SessionInterceptor sessionInterceptor;

    /**
     * Construye la configuración MVC con el interceptor de sesiones.
     *
     * @param sessionInterceptor interceptor administrado por Spring para proteger las rutas
     */
    public WebConfig(SessionInterceptor sessionInterceptor) {
        this.sessionInterceptor = sessionInterceptor;
    }

    /**
     * Registra el interceptor en las rutas del módulo de empleados.
     *
     * <p>El patrón {@code /empleados/**} comprende la gestión y el directorio.
     * Las rutas públicas de autenticación, recursos estáticos y errores quedan excluidas.</p>
     *
     * @param registry registro de interceptores proporcionado por Spring MVC
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/empleados/**")
                .excludePathPatterns("/login", "/logout", "/css/**", "/js/**", "/error");
    }
}
