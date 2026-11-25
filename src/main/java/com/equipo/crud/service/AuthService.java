package com.equipo.crud.service;

import com.equipo.crud.model.Usuario;
import java.util.Optional;

/**
 * Contrato de servicio para la autenticación de usuarios (Mejora: <b>Manejo de sesiones</b>).
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Validar credenciales de gerentes y empleados contra la tabla {@code usuarios}
 * antes de iniciar su sesión HTTP.</p>
 *
 */
public interface AuthService {

    /**
     * Verifica si el nombre de usuario y la contraseña coinciden con un registro válido.
     * No crea ni modifica una sesión HTTP.
     *
     * @param usuario identificador de acceso ingresado en el login, que puede ser {@code null}
     * @param contrasena contraseña ingresada en el login, que puede ser {@code null}
     * @return {@link Optional} con el {@link Usuario} autenticado y su rol,
     *         o vacío si algún argumento es nulo, el usuario no existe o la contraseña no coincide
     */
    Optional<Usuario> autenticar(String usuario, String contrasena);
}
