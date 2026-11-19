package com.equipo.crud.service.impl;

import com.equipo.crud.model.Producto;
import com.equipo.crud.service.ProductoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio para {@link ProductoService}.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Coordinar las validaciones de negocio, llamadas a {@code ProductoRepository},
 * el filtrado del <b>Buscador</b>, el borrado múltiple y el lanzamiento de
 * {@code ProductoNotFoundException} cuando corresponda.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code ProductoRepository} mediante constructor.</li>
 *   <li>Implementar cada método reemplazando las excepciones temporales de este esqueleto.</li>
 *   <li>Lanzar {@code ProductoNotFoundException} en {@code obtenerPorId} y {@code eliminarPorId} si el ID no existe.</li>
 *   <li>Manejar el caso de lista vacía o nula en {@code eliminarSeleccionados} y término vacío en {@code buscar}.</li>
 * </ul>
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    // TODO: Inyectar ProductoRepository por constructor.

    @Override
    public List<Producto> listarTodos() {
        throw new UnsupportedOperationException("TODO (Gael): Implementar listarTodos()");
    }

    @Override
    public List<Producto> buscar(String termino) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar buscar(termino) para el Buscador");
    }

    @Override
    public Producto obtenerPorId(Long id) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar obtenerPorId(id) y lanzar ProductoNotFoundException");
    }

    @Override
    public Producto guardar(Producto producto) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar guardar(producto)");
    }

    @Override
    public void eliminarPorId(Long id) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar eliminarPorId(id)");
    }

    @Override
    public void eliminarSeleccionados(List<Long> ids) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar eliminarSeleccionados(ids)");
    }
}
