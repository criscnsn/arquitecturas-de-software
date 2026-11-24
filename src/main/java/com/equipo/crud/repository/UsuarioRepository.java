package com.equipo.crud.repository;

import com.equipo.crud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Usuario} (gerentes y empleados).
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Proveer las operaciones de persistencia contra MySQL mediante Spring Data JPA:
 * autenticación por {@code usuario}, listado filtrado exclusivamente por {@code Rol.EMPLEADO},
 * consultas del <b>Buscador</b> (nombre completo, correo electrónico o teléfono; sin dirección)
 * y verificaciones de unicidad.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Agregar consulta para el login: {@code Optional<Usuario> findByUsuario(String usuario)}.</li>
 *   <li>Agregar consulta para listar solo empleados (nunca gerentes): {@code List<Usuario> findByRol(Rol rol)}.</li>
 *   <li>Agregar consulta {@code @Query} para el <b>Buscador</b> filtrando únicamente {@code rol = :rol}
 *       y buscando coincidencias (insensible a mayúsculas) en:
 *       nombre completo ({@code nombre}, {@code apellido1}, {@code apellido2}),
 *       {@code correoElectronico} o {@code telefono} (<b>EXCLUIR {@code direccion}</b>).</li>
 *   <li>Agregar métodos de apoyo si Gael los necesita (ej. {@code findByIdAndRol(Long id, Rol rol)},
 *       {@code existsByUsuario(String usuario)}, {@code existsByCorreoElectronico(String correo)}).</li>
 *   <li>Documentar con Javadoc cada método agregado.</li>
 * </ul>
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // TODO: Definir consultas para login, listado por rol y buscador (nombre, correo o teléfono; sin dirección).
}
