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
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Completar el Javadoc para reflejar esta interfaz en el Diagrama de Componentes.</li>
 * </ul>
 */
public interface AuthService {

    /**
     * Verifica si el nombre de usuario y la contraseña coinciden con un registro válido.
     *
     * @param usuario identificador de acceso ingresado en el login
     * @param contrasena contraseña ingresada (máx. 16 caracteres)
     * @return {@link Optional} con el {@link Usuario} autenticado (incluyendo su {@code Rol}), o vacío si falla
     */
    Optional<Usuario> autenticar(String usuario, String contrasena);
}
