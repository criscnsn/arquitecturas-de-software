package com.equipo.crud.service.impl;

import com.equipo.crud.model.Usuario;
import com.equipo.crud.service.AuthService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de autenticación para {@link AuthService}.
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Consultar {@code UsuarioRepository} por {@code usuario} y validar
 * la contraseña (texto plano acordado por el equipo) para abrir la sesión correspondiente al rol.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Inyectar {@code UsuarioRepository} por constructor.</li>
 *   <li>Buscar al usuario por {@code usuario} y comparar {@code contrasena} de forma segura contra nulos.</li>
 * </ul>
 */
@Service
public class AuthServiceImpl implements AuthService {

    // TODO: Inyectar UsuarioRepository por constructor.

    @Override
    public Optional<Usuario> autenticar(String usuario, String contrasena) {
        throw new UnsupportedOperationException("TODO (Gael): Implementar autenticar(usuario, contrasena)");
    }
}
