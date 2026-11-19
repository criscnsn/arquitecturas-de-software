package com.equipo.crud.repository;

import com.equipo.crud.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Producto}.
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Proveer las operaciones de persistencia contra MySQL usando
 * Spring Data JPA (CRUD básico heredado de {@link JpaRepository}, borrado por lotes
 * y consultas personalizadas para el <b>Buscador</b>).</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Agregar el/los método(s) de consulta para la mejora del <b>Buscador</b>
 *       (por ejemplo: búsqueda por campo de texto ignorando mayúsculas/minúsculas).</li>
 *   <li>Verificar compatibilidad de firmas con {@code ProductoServiceImpl} (coordinar con Gael).</li>
 *   <li>Documentar con Javadoc cada método personalizado que se agregue.</li>
 * </ul>
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // TODO: Definir métodos de consulta derivados para el Buscador.
    // Ejemplo de referencia (ajustar según los atributos reales de Producto):
    // List<Producto> findByNombreContainingIgnoreCase(String termino);
}
