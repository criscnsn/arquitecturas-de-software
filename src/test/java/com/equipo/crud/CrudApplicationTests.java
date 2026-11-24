package com.equipo.crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.equipo.crud.exception.EmpleadoNotFoundException;
import com.equipo.crud.exception.GlobalExceptionHandler;
import com.equipo.crud.model.EstadoEmpleado;
import com.equipo.crud.model.Rol;
import com.equipo.crud.model.Usuario;
import com.equipo.crud.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

/**
 * Pruebas de integración del módulo de datos, persistencia y manejo de excepciones
 * contra la base de datos MySQL {@code empleados_crud_db}.
 *
 * <p>Las pruebas que escriben en la base de datos usan {@link Transactional} para
 * revertir los cambios automáticamente al finalizar y mantener intactos los datos iniciales.</p>
 *
 * @author Cristopher Israel Cen Santana
 */
@SpringBootTest
class CrudApplicationTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void debeListarSoloEmpleadosYEncontrarGerentePorUsuario() {
        List<Usuario> empleados = usuarioRepository.findByRolOrderByIdAsc(Rol.EMPLEADO);

        assertFalse(empleados.isEmpty(), "Debe existir al menos un empleado en los datos iniciales");
        assertTrue(
                empleados.stream().allMatch(u -> u.getRol() == Rol.EMPLEADO),
                "El listado del CRUD nunca debe incluir gerentes"
        );

        Optional<Usuario> gerente = usuarioRepository.findByUsuario("gerente1");
        assertTrue(gerente.isPresent(), "Debe existir el gerente semilla gerente1");
        assertEquals(Rol.GERENTE, gerente.get().getRol());
    }

    @Test
    void buscadorDebeFiltrarPorNombreCorreoOTelefonoSinDireccionNiGerentes() {
        // Por nombre completo (con apellido2)
        List<Usuario> porNombre = usuarioRepository.buscarPorRolYTermino(Rol.EMPLEADO, "Juan Pérez");
        assertTrue(porNombre.stream().anyMatch(u -> "jperez".equals(u.getUsuario())));

        // Por nombre completo (con apellido2 NULL)
        List<Usuario> sinApellido2 = usuarioRepository.buscarPorRolYTermino(Rol.EMPLEADO, "Thomas Hardy");
        assertTrue(sinApellido2.stream().anyMatch(u -> "thardy".equals(u.getUsuario())));

        // Por correo electrónico
        List<Usuario> porCorreo = usuarioRepository.buscarPorRolYTermino(Rol.EMPLEADO, "maria.lopez@empresa.com");
        assertTrue(porCorreo.stream().anyMatch(u -> "mlopez".equals(u.getUsuario())));

        // Por teléfono
        List<Usuario> porTelefono = usuarioRepository.buscarPorRolYTermino(Rol.EMPLEADO, "999456");
        assertTrue(porTelefono.stream().anyMatch(u -> "acastillo".equals(u.getUsuario())));

        // Dirección excluida del buscador
        List<Usuario> porDireccion = usuarioRepository.buscarPorRolYTermino(Rol.EMPLEADO, "Portland");
        assertTrue(porDireccion.isEmpty(), "El buscador no debe coincidir por dirección");

        // Gerentes excluidos del buscador
        List<Usuario> porGerente = usuarioRepository.buscarPorRolYTermino(Rol.EMPLEADO, "Carlos Mendoza");
        assertTrue(porGerente.isEmpty(), "El buscador no debe devolver gerentes");
    }

    @Test
    @Transactional
    void debeCrearEmpleadoValidarCalculosYProtegerGerentes() {
        LocalDate fechaIngreso = LocalDate.now().minusYears(1).minusMonths(2);
        Usuario nuevo = new Usuario(
                "test_emp_tmp",
                "clave1234",
                "Sofía",
                "Ramírez",
                "",
                Rol.EMPLEADO,
                EstadoEmpleado.ACTIVO,
                fechaIngreso,
                "sofia.test@empresa.com",
                "9990001122",
                "Calle de Prueba 123, Mérida",
                new BigDecimal("15000.00")
        );

        Usuario guardado = usuarioRepository.saveAndFlush(nuevo);
        assertNotNull(guardado.getId(), "MySQL debe asignar automáticamente el ID");
        assertEquals("Sofía Ramírez", guardado.getNombreCompleto());
        assertEquals("1 año, 2 meses", guardado.getAntiguedad());

        assertTrue(usuarioRepository.findByIdAndRol(guardado.getId(), Rol.EMPLEADO).isPresent());

        // Proteger a un gerente ante consultas/borrados del módulo de empleados
        Long idGerente = usuarioRepository.findByUsuario("gerente1").orElseThrow().getId();
        assertTrue(usuarioRepository.findByIdAndRol(idGerente, Rol.EMPLEADO).isEmpty());

        List<Usuario> seleccionables = usuarioRepository.findAllByIdInAndRol(
                List.of(guardado.getId(), idGerente),
                Rol.EMPLEADO
        );
        assertEquals(1, seleccionables.size(), "El borrado múltiple solo debe incluir empleados");
        assertEquals(guardado.getId(), seleccionables.get(0).getId());
    }

    @Test
    @Transactional
    void debeLanzarExcepcionPorDuplicadoYManejarExcepcionesEnHandler() {
        // 1. Verificar respuestas controladas de GlobalExceptionHandler
        Model modelNotFound = new ConcurrentModel();
        String vistaNotFound = globalExceptionHandler.manejarEmpleadoNoEncontrado(
                new EmpleadoNotFoundException(999L),
                modelNotFound
        );
        assertEquals("error/error", vistaNotFound);
        assertEquals("Empleado no encontrado", modelNotFound.getAttribute("tituloError"));
        assertEquals("No se encontró el empleado con ID: 999", modelNotFound.getAttribute("mensajeError"));

        Model modelIntegridad = new ConcurrentModel();
        String vistaIntegridad = globalExceptionHandler.manejarIntegridadDatos(
                new DataIntegrityViolationException("duplicado"),
                modelIntegridad
        );
        assertEquals("error/error", vistaIntegridad);
        assertEquals("Datos duplicados o inválidos", modelIntegridad.getAttribute("tituloError"));

        // 2. Verificar que MySQL rechaza un usuario duplicado lanzando DataIntegrityViolationException
        Usuario duplicado = new Usuario(
                "jperez",
                "clave1234",
                "Otro",
                "Usuario",
                null,
                Rol.EMPLEADO,
                EstadoEmpleado.ACTIVO,
                LocalDate.now(),
                "otro.correo@empresa.com",
                "9998887766",
                "Calle Falsa 123",
                new BigDecimal("12000.00")
        );

        assertThrows(
                DataIntegrityViolationException.class,
                () -> usuarioRepository.saveAndFlush(duplicado),
                "Un nombre de usuario repetido debe disparar DataIntegrityViolationException"
        );
    }
}
