package com.equipo.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador Spring MVC para la gestión CRUD (rol {@code GERENTE}) y el directorio (rol {@code EMPLEADO}).
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Atender las peticiones HTTP de ambos roles, invocar a {@code EmpleadoService},
 * poblar el {@code Model} (y mensajes flash cuando aplique) y devolver la vista Thymeleaf correspondiente.
 * Todas las operaciones de creación, edición y borrado del gerente se envían desde modales (popups)
 * sin cambiar de pantalla.</p>
 *
 * <p><b>Contrato de rutas acordado con Vistas (Mateo):</b></p>
 * <ul>
 *   <li>{@code GET  /empleados} (parámetro opcional {@code ?q=}) -&gt; <b>Solo GERENTE</b>. Lista empleados o filtra
 *       con el <b>Buscador</b> (nombre completo, correo o teléfono; sin dirección). Retorna {@code "empleados/gestion"}.</li>
 *   <li>{@code POST /empleados/crear} -&gt; <b>Solo GERENTE</b>. Recibe datos del modal "Añadir nuevo empleado"
 *       (sin ID), guarda y redirige a {@code "redirect:/empleados"}.</li>
 *   <li>{@code POST /empleados/editar/{id}} -&gt; <b>Solo GERENTE</b>. Recibe campos editables del modal de edición,
 *       actualiza y redirige a {@code "redirect:/empleados"}.</li>
 *   <li>{@code POST /empleados/eliminar/{id}} -&gt; <b>Solo GERENTE</b>. Elimina únicamente a ese empleado y redirige
 *       a {@code "redirect:/empleados"}.</li>
 *   <li>{@code POST /empleados/eliminar-seleccionados} -&gt; <b>Solo GERENTE</b>. Recibe lista {@code ids} de los
 *       checkboxes marcados y redirige a {@code "redirect:/empleados"}.</li>
 *   <li>{@code GET  /empleados/directorio} -&gt; <b>EMPLEADO</b> (y opcionalmente GERENTE). Muestra la lista de
 *       empleados en modo solo lectura resaltando al usuario en sesión, su estado y antigüedad.
 *       Retorna {@code "empleados/directorio"}.</li>
 * </ul>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code EmpleadoService} por constructor.</li>
 *   <li>Implementar las rutas listadas arriba enviando al {@code Model}:
 *       {@code empleados}, {@code estados} ({@code EstadoEmpleado.values()}), {@code q} (término buscado)
 *       y {@code nuevoEmpleado} (objeto vacío para el modal de alta).</li>
 *   <li>Manejar errores de validación con {@code BindingResult} y mensajes flash ({@code mensajeExito}, {@code mensajeError}).</li>
 * </ul>
 */
@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    // TODO: Inyectar EmpleadoService por constructor.

    // TODO: Implementar endpoints del CRUD (GERENTE) y directorio solo lectura (EMPLEADO).
}
