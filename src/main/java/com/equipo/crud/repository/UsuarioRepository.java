package com.equipo.crud.repository;

import com.equipo.crud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Usuario}.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Consultar los registros de la tabla {@code usuarios} en MySQL
 * para validar el inicio de sesión (mejora: <b>Manejo de sesiones</b>).</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Agregar método de búsqueda por {@code username} (ej. {@code Optional<Usuario> findByUsername(String username)}).</li>
 *   <li>Coordinar la firma con {@code AuthServiceImpl} (Gael) y documentar con Javadoc.</li>
 * </ul>
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // TODO: Definir consulta para buscar un usuario por su nombre de usuario.
    // Ejemplo de referencia:
    // Optional<Usuario> findByUsername(String username);
}
