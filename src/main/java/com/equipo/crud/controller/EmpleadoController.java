package com.equipo.crud.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.equipo.crud.exception.EmpleadoNotFoundException;
import com.equipo.crud.model.EstadoEmpleado;
import com.equipo.crud.model.Rol;
import com.equipo.crud.model.Usuario;
import com.equipo.crud.service.EmpleadoService;
import java.util.List;

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
 * <p><b>Manejo de formularios:</b></p>
 * <ul>
 *   <li>Los formularios de alta y edición utilizan {@code BindingResult} para corregir datos inválidos.</li>
 *   <li>Las eliminaciones reciben identificadores y redirigen al listado con mensajes flash.</li>
 * </ul>
 */
@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    /** Servicio de negocio para consultar, crear, actualizar y eliminar empleados. */
    private final EmpleadoService empleadoService;

    /**
     * Construye el controlador con el servicio de empleados administrado por Spring.
     *
     * @param empleadoService servicio encargado de aplicar las reglas del CRUD y del buscador
     */
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * Limita las propiedades que el formulario de alta puede asignar a {@code nuevoEmpleado}.
     *
     * <p>Permite los datos editables del empleado y excluye {@code id} y {@code rol},
     * cuyos valores se determinan en el servidor y en la base de datos.</p>
     *
     * @param binder enlazador de datos del formulario de creación
     */
    @InitBinder("nuevoEmpleado")
    public void configurarFormularioNuevoEmpleado(WebDataBinder binder) {
        // El formulario solo puede asignar campos editables; nunca el ID ni el rol.
        binder.setAllowedFields("usuario", "contrasena", "nombre", "apellido1", "apellido2",
                "estado", "fechaIngreso", "correoElectronico", "telefono", "direccion", "salario");
    }

    /**
     * Atiende {@code GET /empleados} y prepara la vista de gestión para un gerente.
     *
     * <p>Sin término de búsqueda lista todos los empleados; con término filtra por
     * nombre completo, correo o teléfono. Agrega al modelo {@code empleados},
     * {@code estados}, {@code q} y un {@code nuevoEmpleado} vacío para el alta.</p>
     *
     * @param q término opcional del buscador, que puede ser nulo o estar en blanco
     * @param usuarioSesion usuario autenticado, o {@code null} si no hay sesión autenticada
     * @param model modelo que recibe los datos necesarios para la vista de gestión
     * @return vista {@code empleados/gestion}, o redirección al login o directorio según el acceso
     */
    @GetMapping
    public String listar(
            @RequestParam(name = "q", required = false) String q,
            @SessionAttribute(name = "usuarioSesion", required = false) Usuario usuarioSesion,
            Model model) {
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        if (usuarioSesion.getRol() != Rol.GERENTE) {
            return "redirect:/empleados/directorio";
        }

        String termino = q == null ? "" : q.trim();
        List<Usuario> empleados;
        if (termino.isBlank()) {
            empleados = empleadoService.listarEmpleados();
        } else {
            empleados = empleadoService.buscarEmpleados(termino);
        }

        model.addAttribute("empleados", empleados);
        model.addAttribute("estados", EstadoEmpleado.values());
        model.addAttribute("q", termino);
        model.addAttribute("nuevoEmpleado", new Usuario());
        return "empleados/gestion";
    }

    /**
     * Atiende {@code POST /empleados/crear} y registra un empleado si el formulario es válido.
     *
     * <p>Exige rol de gerente y contraseña no vacía. Ante errores de validación o
     * integridad conserva los datos y vuelve a la gestión con el modal de alta marcado
     * para abrirse. Después de guardar agrega un mensaje flash y redirige al listado.</p>
     *
     * @param usuarioSesion usuario autenticado, o {@code null} si no hay sesión autenticada
     * @param nuevoEmpleado datos enlazados y validados del formulario de alta
     * @param bindingResult errores de enlace, validación o integridad asociados al formulario
     * @param model modelo utilizado cuando se debe volver a mostrar el formulario
     * @param redirectAttributes atributos flash que transportan el mensaje de éxito
     * @return redirección al listado si guarda, vista de gestión si hay errores de datos,
     *         o redirección de acceso si falta sesión o permiso
     */
    @PostMapping("/crear")
    public String crearEmpleado(
            @SessionAttribute(name = "usuarioSesion", required = false) Usuario usuarioSesion,
            @Valid @ModelAttribute("nuevoEmpleado") Usuario nuevoEmpleado,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        if (usuarioSesion.getRol() != Rol.GERENTE) {
            return "redirect:/empleados/directorio";
        }

        if (nuevoEmpleado.getContrasena() == null || nuevoEmpleado.getContrasena().isBlank()) {
            bindingResult.rejectValue("contrasena", "NotBlank", "La contraseña es obligatoria");
        }
        if (bindingResult.hasErrors()) {
            return mostrarErroresAlta(model, "Revisa los datos del formulario antes de guardar.");
        }

        try {
            empleadoService.crearEmpleado(nuevoEmpleado);
        } catch (DataIntegrityViolationException ex) {
            String mensaje = "No se pudo crear el empleado. El usuario o correo ya existe, o algún dato no es válido.";
            bindingResult.reject("empleado.datosInvalidos", mensaje);
            return mostrarErroresAlta(model, mensaje);
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "Empleado creado exitosamente.");
        return "redirect:/empleados";
    }

    /**
     * Atiende {@code POST /empleados/editar/{id}} y actualiza los datos de un empleado.
     *
     * <p>Exige rol de gerente, valida el formulario y comprueba que su ID coincida con
     * el de la ruta. Conserva el formulario ante errores de datos; al guardar redirige
     * al listado con un mensaje flash. El servicio conserva la contraseña si llega vacía.</p>
     *
     * @param usuarioSesion usuario autenticado, o {@code null} si no hay sesión autenticada
     * @param id identificador del empleado recibido en la ruta
     * @param editarEmpleado datos enlazados y validados del formulario de edición
     * @param bindingResult errores asociados al formulario de edición
     * @param model modelo utilizado para conservar los datos cuando falla la edición
     * @param redirectAttributes atributos flash para mostrar la confirmación de actualización
     * @return redirección al listado si actualiza, vista de gestión si hay errores de datos,
     *         o redirección de acceso si falta sesión o permiso
     * @throws EmpleadoNotFoundException si el ID no corresponde a un empleado existente
     */
    @PostMapping("/editar/{id}")
    public String editarEmpleado(
        @SessionAttribute (name = "usuarioSesion", required = false) Usuario usuarioSesion,
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("editarEmpleado") Usuario editarEmpleado,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ){
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        if (usuarioSesion.getRol() != Rol.GERENTE) {
            return "redirect:/empleados/directorio";
        }
        if(id == null || !id.equals(editarEmpleado.getId())){
            bindingResult.reject("empleado.idInvalido", "El ID del empleado no coincide con la ruta.");
        }
        if(bindingResult.hasErrors()){
            return mostrarErroresEdicion(model, "Revisa los datos del formulario antes de guardar.");
        }
        try {
            empleadoService.actualizarEmpleado(id, editarEmpleado);
        } catch (DataIntegrityViolationException ex) {
            String mensaje = "No se pudo actualizar el empleado. El usuario o correo ya existe, o algún dato no es válido.";
            bindingResult.reject("empleado.datosInvalidos", mensaje);
            return mostrarErroresEdicion(model, mensaje);
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Empleado actualizado exitosamente.");
        return "redirect:/empleados";
    }

    /**
     * Atiende {@code POST /empleados/eliminar/{id}} y elimina un empleado por su ID.
     *
     * <p>Exige rol de gerente y utiliza exclusivamente el identificador de la ruta.
     * Los casos de empleado inexistente o restricciones de integridad se comunican
     * mediante un mensaje flash de error al volver al listado.</p>
     *
     * @param usuarioSesion usuario autenticado, o {@code null} si no hay sesión autenticada
     * @param id identificador del empleado que se solicita eliminar
     * @param redirectAttributes atributos flash para comunicar el éxito o el error de eliminación
     * @return redirección al listado después del intento, o al login o directorio si falta acceso
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(
            @SessionAttribute(name = "usuarioSesion", required = false) Usuario usuarioSesion,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        if (usuarioSesion.getRol() != Rol.GERENTE) {
            return "redirect:/empleados/directorio";
        }

        try {
            empleadoService.eliminarEmpleadoPorId(id);
        } catch (EmpleadoNotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se pudo eliminar el empleado. No existe o no tiene el rol EMPLEADO.");
            return "redirect:/empleados";
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se pudo eliminar el empleado porque existen datos relacionados o restricciones de integridad.");
            return "redirect:/empleados";
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Empleado eliminado exitosamente.");
        return "redirect:/empleados";
    }

    /**
     * Atiende {@code POST /empleados/eliminar-seleccionados} y elimina los empleados marcados.
     *
     * <p>Exige rol de gerente. Si la selección es nula o vacía, no invoca el servicio.
     * Tanto la confirmación como los errores de selección, existencia o integridad
     * se envían como mensajes flash al listado.</p>
     *
     * @param usuarioSesion usuario autenticado, o {@code null} si no hay sesión autenticada
     * @param ids identificadores enviados por los checkboxes, o {@code null} si no se envían
     * @param redirectAttributes atributos flash para comunicar el resultado del borrado múltiple
     * @return redirección al listado después del intento, o al login o directorio si falta acceso
     */
    @PostMapping("/eliminar-seleccionados")
    public String eliminarEmpleadosSeleccionados(
            @SessionAttribute(name = "usuarioSesion", required = false) Usuario usuarioSesion,
            @RequestParam(name = "ids", required = false) List<Long> ids,
            RedirectAttributes redirectAttributes) {
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        if (usuarioSesion.getRol() != Rol.GERENTE) {
            return "redirect:/empleados/directorio";
        }

        if (ids == null || ids.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se seleccionaron empleados para eliminar.");
            return "redirect:/empleados";
        }
        try {
            empleadoService.eliminarEmpleadosSeleccionados(ids);
        } catch (EmpleadoNotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se pudieron eliminar los empleados seleccionados. Alguno no existe o no tiene el rol EMPLEADO.");
            return "redirect:/empleados";
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se pudieron eliminar los empleados seleccionados porque existen datos relacionados o restricciones de integridad.");
            return "redirect:/empleados";
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Empleados eliminados exitosamente.");
        return "redirect:/empleados";
    }

    /**
     * Atiende {@code GET /empleados/directorio} y prepara el directorio de solo lectura.
     *
     * <p>Agrega al modelo la lista de empleados y el usuario autenticado para que
     * la vista pueda identificar su fila y mostrar su información personal.</p>
     *
     * @param usuarioSesion usuario autenticado, o {@code null} si no hay sesión autenticada
     * @param model modelo que recibe {@code empleados} y {@code usuarioSesion}
     * @return vista {@code empleados/directorio}, o redirección al login si no hay usuario
     */
    @GetMapping ("/directorio")
    public String directorio(
    @SessionAttribute (name = "usuarioSesion", required = false) Usuario usuarioSesion,
    Model model
    ){
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        List<Usuario> empleados = empleadoService.listarEmpleados();
        model.addAttribute("empleados", empleados);
        model.addAttribute("usuarioSesion", usuarioSesion);
        return "empleados/directorio";
    }

    /**
     * Prepara la gestión para corregir el formulario de alta sin sustituir sus datos enlazados.
     *
     * @param model modelo que ya contiene {@code nuevoEmpleado} y su resultado de validación
     * @param mensaje explicación general del error que se mostrará al usuario
     * @return nombre de la vista {@code empleados/gestion}
     */
    private String mostrarErroresAlta(Model model, String mensaje) {
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        model.addAttribute("estados", EstadoEmpleado.values());
        model.addAttribute("q", "");
        model.addAttribute("mensajeError", mensaje);
        model.addAttribute("abrirModalNuevoEmpleado", true);
        return "empleados/gestion";
    }

    /**
     * Prepara la gestión para corregir una edición conservando los datos del empleado.
     *
     * <p>Repone el objeto vacío del formulario de alta y marca la reapertura del modal
     * de edición, sin sustituir {@code editarEmpleado} ni su resultado de validación.</p>
     *
     * @param model modelo que ya contiene {@code editarEmpleado} y sus errores
     * @param mensaje explicación general del error que se mostrará al usuario
     * @return nombre de la vista {@code empleados/gestion}
     */
    private String mostrarErroresEdicion(Model model, String mensaje) {
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        model.addAttribute("estados", EstadoEmpleado.values());
        model.addAttribute("q", "");
        model.addAttribute("nuevoEmpleado", new Usuario());
        model.addAttribute("mensajeError", mensaje);
        model.addAttribute("abrirModalEditarEmpleado", true);
        return "empleados/gestion";
    }
}
