package com.equipo.crud.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que mapea la tabla {@code usuarios} en MySQL (almacena tanto gerentes como empleados).
 *
 * <p><b>Responsable:</b> Cristopher Israel Cen Santana ({@code criscnsn})</p>
 * <p><b>Función:</b> Representar en Java los datos de identidad, acceso, contacto y situación
 * laboral de cada usuario del sistema, centralizando ambos roles ({@link Rol#GERENTE} y
 * {@link Rol#EMPLEADO}) en una sola tabla para evitar redundancia.</p>
 *
 * <p><b>TODO (Cristopher):</b></p>
 * <ul>
 *   <li>Declarar todos los atributos acordados (todos obligatorios excepto {@code apellido2}):
 *       {@code usuario} (String, único, máx 30), {@code contrasena} (String, máx 16),
 *       {@code nombre} (String), {@code apellido1} (String), {@code apellido2} (String, opcional),
 *       {@code rol} ({@link Rol}, {@code @Enumerated(EnumType.STRING)}),
 *       {@code estado} ({@link EstadoEmpleado}, {@code @Enumerated(EnumType.STRING)}),
 *       {@code fechaIngreso} ({@code LocalDate}, columna {@code fecha_ingreso}),
 *       {@code correoElectronico} (String, único, columna {@code correo_electronico}),
 *       {@code telefono} (String, máx 10 dígitos), {@code direccion} (String),
 *       {@code salario} ({@code BigDecimal}, campo numérico obligatorio de la rúbrica, {@code >= 0}).</li>
 *   <li>Agregar anotaciones JPA ({@code @Column}) y de validación ({@code @NotBlank}, {@code @NotNull},
 *       {@code @Size}, {@code @Email}, {@code @Pattern}, {@code @PositiveOrZero}).</li>
 *   <li>Implementar métodos {@code @Transient} calculados (sin columna en BD para evitar redundancia):
 *       <ul>
 *         <li>{@code getNombreCompleto()} -&gt; concatena {@code nombre + apellido1 + apellido2} (si existe) para mostrar en una sola celda.</li>
 *         <li>{@code getAntiguedad()} -&gt; calcula cuánto tiempo lleva en la empresa a partir de {@code fechaIngreso}.</li>
 *       </ul>
 *   </li>
 *   <li>Implementar constructor vacío (obligatorio para JPA), constructor parametrizado, getters y setters con Javadoc.</li>
 * </ul>
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único autogenerado por MySQL (no editable; llave primaria mínima para que el esqueleto compile).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Agregar el resto de atributos, anotaciones JPA/Validation, getters, setters y métodos @Transient.
}
