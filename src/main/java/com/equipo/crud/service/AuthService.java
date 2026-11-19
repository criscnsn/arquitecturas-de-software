package com.equipo.crud.service;

import com.equipo.crud.model.Usuario;
import java.util.Optional;

/**
 * Contrato de servicio para la autenticación de usuarios (Mejora: <b>Manejo de sesiones</b>).
 *
 * <p><b>Responsable:</b> Gael Antonio Ayuso Contreras ({@code gael-ayuso})</p>
 * <p><b>Función:</b> Verificar las credenciales ingresadas en el formulario de inicio de sesión
 * contra la base de datos antes de abrir la sesión HTTP del usuario.</p>
 *
 * <p><b>TODO (Gael):</b></p>
 * <ul>
 *   <li>Definir si se agregarán operaciones extra (por ejemplo, registro o cambio de contraseña).</li>
 *   <li>Documentar con Javadoc para reflejar esta interfaz en el Diagrama de Componentes.</li>
 * </ul>
 */
public interface AuthService {

    /**
     * Valida las credenciales de un usuario.
     *
     * @param username nombre de usuario
     * @param password contraseña ingresada
     * @return {@link Optional} con el {@link Usuario} si las credenciales son válidas, o vacío en caso contrario
     */
    Optional<Usuario> autenticar(String username, String password);
}
