package com.equipo.crud.service.impl;

import com.equipo.crud.model.Usuario;
import com.equipo.crud.repository.UsuarioRepository;
import com.equipo.crud.service.AuthService;

import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de autenticación para {@link AuthService}.
 *
 * <p>
 * <b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})
 * </p>
 * <p>
 * <b>Función:</b> Consultar {@code UsuarioRepository} por {@code usuario} y
 * validar
 * la contraseña (texto plano acordado por el equipo) para abrir la sesión
 * correspondiente al rol.
 * </p>
 *
 */
@Service
public class AuthServiceImpl implements AuthService {
    /** Repositorio utilizado para localizar las credenciales registradas. */
    private final UsuarioRepository repository;

    /**
     * Construye el servicio de autenticación con el repositorio de usuarios.
     *
     * @param usuarioRepository repositorio utilizado para buscar usuarios por su nombre de acceso
     */
    public AuthServiceImpl(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;
    }

    /**
     * Busca el usuario y compara su contraseña registrada con la recibida.
     *
     * <p>La comparación se realiza con {@link Objects#equals(Object, Object)}.
     * Este método no crea sesiones; esa responsabilidad corresponde al controlador.</p>
     *
     * @param usuario nombre de acceso, o {@code null} para un intento inválido
     * @param contrasena contraseña recibida, o {@code null} para un intento inválido
     * @return usuario autenticado, o un {@link Optional} vacío si falta un dato,
     *         no existe el usuario o la contraseña no coincide
     */
    @Override
    public Optional<Usuario> autenticar(String usuario, String contrasena) {
        if (usuario == null || contrasena == null) {
            return Optional.empty();
        }
        return repository.findByUsuario(usuario)
                .filter(u -> Objects.equals(u.getContrasena(), contrasena));

    }
}
