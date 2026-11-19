package com.equipo.crud.service;

import com.equipo.crud.model.Producto;
import java.util.List;

/**
 * Contrato de servicios de negocio para las operaciones CRUD y búsqueda de {@link Producto}.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Desacoplar la capa de controladores ({@code ProductoController})
 * de la implementación concreta de negocio y persistencia. Además, sirve directamente como
 * <b>Interfaz de Salida/Entrada</b> para el Diagrama de Componentes UML del entregable PDF.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Ajustar las firmas si el equipo decide agregar parámetros adicionales de filtrado.</li>
 *   <li>Completar el Javadoc de cada método detallando parámetros, retornos y excepciones.</li>
 * </ul>
 */
public interface ProductoService {

    /**
     * Obtiene el listado completo de productos.
     *
     * @return lista de productos registrados
     */
    List<Producto> listarTodos();

    /**
     * Filtra los productos que coincidan con un término de búsqueda (Mejora: <b>Buscador</b>).
     *
     * @param termino texto ingresado en el buscador
     * @return lista de productos coincidentes
     */
    List<Producto> buscar(String termino);

    /**
     * Busca un producto por su identificador único.
     * Debe lanzar {@code ProductoNotFoundException} si no existe.
     *
     * @param id identificador del producto
     * @return producto encontrado
     */
    Producto obtenerPorId(Long id);

    /**
     * Crea un nuevo producto o actualiza uno existente.
     *
     * @param producto entidad con los datos a persistir
     * @return producto guardado
     */
    Producto guardar(Producto producto);

    /**
     * Elimina un producto individual por su identificador.
     *
     * @param id identificador del producto a eliminar
     */
    void eliminarPorId(Long id);

    /**
     * Elimina en lote todos los productos seleccionados mediante casillas de verificación.
     *
     * @param ids lista de identificadores marcados en la tabla
     */
    void eliminarSeleccionados(List<Long> ids);
}
