package com.equipo.crud.service.impl;

import com.equipo.crud.model.Usuario;
import com.equipo.crud.service.AuthService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de autenticación para {@link AuthService}.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Consultar {@code UsuarioRepository} y validar la contraseña del usuario
 * para la mejora de <b>Manejo de sesiones</b>.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code UsuarioRepository} por constructor.</li>
 *   <li>Buscar el usuario por {@code username} y comprobar que la contraseña coincida.</li>
 * </ul>
 */
@Service
public class AuthServiceImpl implements AuthService {

    // TODO: Inyectar UsuarioRepository por constructor.

    @Override
    public Optional<Usuario> autenticar(String username, String password) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar autenticar(username, password)");
    }
}
