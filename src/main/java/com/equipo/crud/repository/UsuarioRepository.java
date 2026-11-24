package com.equipo.crud.repository;

import com.equipo.crud.model.Rol;
import com.equipo.crud.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para el acceso a datos de la tabla {@code usuarios}.
 *
 * <p>Provee las consultas necesarias para la autenticación de sesiones, el listado
 * seguro de empleados (aislando siempre a los gerentes) y el buscador por nombre completo,
 * correo electrónico o teléfono.</p>
 *
 * @author Cristopher Israel Cen Santana
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de acceso único (utilizado en el inicio de sesión).
     *
     * @param usuario nombre de usuario ingresado en el login
     * @return {@link Optional} con el usuario encontrado, o vacío si no existe
     */
    Optional<Usuario> findByUsuario(String usuario);

    /**
     * Obtiene todos los usuarios pertenecientes a un rol específico ordenados por ID ascendente.
     *
     * @param rol rol a filtrar (por ejemplo, {@link Rol#EMPLEADO} para excluir gerentes)
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRolOrderByIdAsc(Rol rol);

    /**
     * Busca un registro específico verificando además que pertenezca al rol indicado,
     * evitando operar accidentalmente sobre un gerente desde el CRUD de empleados.
     *
     * @param id identificador único del usuario
     * @param rol rol esperado ({@link Rol#EMPLEADO})
     * @return {@link Optional} con el usuario si existe y tiene ese rol, o vacío en caso contrario
     */
    Optional<Usuario> findByIdAndRol(Long id, Rol rol);

    /**
     * Obtiene únicamente los registros cuyos IDs estén en la lista proporcionada y coincidan
     * con el rol indicado, para respaldar el borrado múltiple sin riesgo de eliminar gerentes.
     *
     * @param ids colección de identificadores seleccionados mediante checkboxes
     * @param rol rol permitido para la operación ({@link Rol#EMPLEADO})
     * @return lista de usuarios que cumplen ambas condiciones
     */
    List<Usuario> findAllByIdInAndRol(List<Long> ids, Rol rol);

    /**
     * Filtra usuarios de un rol dado cuyo nombre completo (nombre, primer apellido y segundo
     * apellido opcional), correo electrónico o teléfono coincidan parcialmente con el término
     * indicado, sin distinguir mayúsculas ni minúsculas y excluyendo la dirección.
     *
     * @param rol rol sobre el cual filtrar ({@link Rol#EMPLEADO})
     * @param termino texto de búsqueda ingresado en el buscador
     * @return lista de usuarios coincidentes ordenados por ID ascendente
     */
    @Query("""
            SELECT u FROM Usuario u
            WHERE u.rol = :rol
              AND (
                  LOWER(CONCAT(u.nombre, ' ', u.apellido1, ' ', COALESCE(u.apellido2, ''))) LIKE LOWER(CONCAT('%', :termino, '%'))
                  OR LOWER(u.correoElectronico) LIKE LOWER(CONCAT('%', :termino, '%'))
                  OR u.telefono LIKE CONCAT('%', :termino, '%')
              )
            ORDER BY u.id ASC
            """)
    List<Usuario> buscarPorRolYTermino(@Param("rol") Rol rol, @Param("termino") String termino);
}
