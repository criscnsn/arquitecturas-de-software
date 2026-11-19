package com.equipo.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal de la aplicación Spring Boot.
 *
 * <p>Función: Inicializa el contexto de Spring, escanea los componentes
 * ({@code controller}, {@code service}, {@code repository}, {@code config}, {@code exception})
 * y levanta el servidor web embebido.</p>
 */
@SpringBootApplication
public class CrudApplication {

    /**
     * Arranca la aplicación CRUD.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }
}
