package com.equipo.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador Spring MVC encargado de atender las peticiones HTTP del módulo CRUD y del Buscador.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Recibir solicitudes del navegador, invocar a {@code ProductoService},
 * poblar el {@code Model} y devolver el nombre de la vista Thymeleaf correspondiente
 * (coordinada con Mateo).</p>
 *
 * <p><b>Contrato de rutas acordado con Vistas (Mateo):</b></p>
 * <ul>
 *   <li>{@code GET  /productos} (parámetro opcional {@code ?q=}) -&gt; Lista todos o filtra con el <b>Buscador</b>; retorna {@code "productos/lista"}.</li>
 *   <li>{@code GET  /productos/nuevo} -&gt; Muestra el formulario para crear un registro; retorna {@code "productos/formulario"}.</li>
 *   <li>{@code POST /productos/guardar} -&gt; Guarda (crea o actualiza) un registro y redirige a {@code "redirect:/productos"}.</li>
 *   <li>{@code GET  /productos/editar/{id}} -&gt; Carga un registro existente en {@code "productos/formulario"}.</li>
 *   <li>{@code POST /productos/eliminar/{id}} -&gt; Elimina un registro individual y redirige a {@code "redirect:/productos"}.</li>
 *   <li>{@code POST /productos/eliminar-seleccionados} -&gt; Recibe lista {@code ids} de checkboxes marcados y redirige a {@code "redirect:/productos"}.</li>
 * </ul>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code ProductoService} por constructor.</li>
 *   <li>Implementar los métodos manejadores de las rutas listadas arriba.</li>
 *   <li>Enviar en el {@code Model} los atributos acordados: {@code productos}, {@code producto}, {@code q} (término buscado).</li>
 * </ul>
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    // TODO: Inyectar ProductoService por constructor.

    // TODO: Implementar rutas GET y POST del CRUD + Buscador.
}
