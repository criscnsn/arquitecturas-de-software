package com.equipo.crud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entidad JPA que mapea la tabla única {@code usuarios} en MySQL.
 *
 * <p>Centraliza tanto a los usuarios con rol {@link Rol#GERENTE} como a los de rol
 * {@link Rol#EMPLEADO} para evitar redundancia de esquema. Todos los campos son
 * obligatorios excepto {@link #apellido2}.</p>
 *
 * <p>Los atributos derivados ({@link #getNombreCompleto()} y {@link #getAntiguedad()})
 * se calculan dinámicamente con {@link Transient} sin almacenarse en la base de datos.</p>
 *
 * @author Cristopher Israel Cen Santana
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único autogenerado por MySQL (no editable).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único utilizado para iniciar sesión.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 30, message = "El usuario no puede superar los 30 caracteres")
    @Column(name = "usuario", nullable = false, unique = true, length = 30)
    private String usuario;

    /**
     * Contraseña de acceso (máximo 16 caracteres).
     * Nota: En edición puede recibirse vacía para conservar la contraseña actual;
     * la obligatoriedad en alta se valida en {@code EmpleadoServiceImpl}.
     */
    @Size(max = 16, message = "La contraseña no puede superar los 16 caracteres")
    @Column(name = "contrasena", nullable = false, length = 16)
    private String contrasena;

    /**
     * Nombre o nombres de pila del usuario.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Primer apellido (paterno), obligatorio para todos los registros.
     */
    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 30, message = "El primer apellido no puede superar los 30 caracteres")
    @Column(name = "apellido1", nullable = false, length = 30)
    private String apellido1;

    /**
     * Segundo apellido (materno), opcional para compatibilidad internacional.
     */
    @Size(max = 30, message = "El segundo apellido no puede superar los 30 caracteres")
    @Column(name = "apellido2", nullable = true, length = 30)
    private String apellido2;

    /**
     * Rol de acceso del usuario dentro del sistema ({@link Rol#GERENTE} o {@link Rol#EMPLEADO}).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, columnDefinition = "ENUM('GERENTE', 'EMPLEADO')")
    private Rol rol = Rol.EMPLEADO;

    /**
     * Estado laboral actual del usuario ({@link EstadoEmpleado#ACTIVO},
     * {@link EstadoEmpleado#VACACIONES} o {@link EstadoEmpleado#INCAPACIDAD}).
     */
    @NotNull(message = "El estado laboral es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('ACTIVO', 'VACACIONES', 'INCAPACIDAD')")
    private EstadoEmpleado estado = EstadoEmpleado.ACTIVO;

    /**
     * Fecha en la que el usuario ingresó a la empresa/sistema.
     */
    @NotNull(message = "La fecha de ingreso es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    /**
     * Correo electrónico único del usuario (incluido en el buscador).
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 60, message = "El correo electrónico no puede superar los 60 caracteres")
    @Column(name = "correo_electronico", nullable = false, unique = true, length = 60)
    private String correoElectronico;

    /**
     * Número telefónico de 10 dígitos numéricos (incluido en el buscador).
     */
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    /**
     * Dirección domiciliaria del usuario (excluida del buscador).
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    /**
     * Salario mensual del usuario (campo numérico obligatorio del CRUD).
     */
    @NotNull(message = "El salario es obligatorio")
    @DecimalMin(value = "0.00", message = "El salario no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El salario debe tener máximo 8 enteros y 2 decimales")
    @Column(name = "salario", nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    /**
     * Constructor vacío requerido por JPA/Hibernate y por el enlace de formularios de Spring MVC.
     */
    public Usuario() {
    }

    /**
     * Constructor parametrizado para crear instancias completas de {@link Usuario}.
     *
     * @param usuario nombre de usuario para login
     * @param contrasena contraseña de acceso (máximo 16 caracteres)
     * @param nombre nombre(s) de pila
     * @param apellido1 primer apellido obligatorio
     * @param apellido2 segundo apellido opcional (puede ser {@code null})
     * @param rol rol asignado ({@link Rol#GERENTE} o {@link Rol#EMPLEADO})
     * @param estado estado laboral actual
     * @param fechaIngreso fecha de ingreso a la empresa
     * @param correoElectronico correo electrónico único
     * @param telefono número telefónico de 10 dígitos
     * @param direccion dirección domiciliaria
     * @param salario salario numérico no negativo
     */
    public Usuario(String usuario, String contrasena, String nombre, String apellido1, String apellido2,
                   Rol rol, EstadoEmpleado estado, LocalDate fechaIngreso, String correoElectronico,
                   String telefono, String direccion, BigDecimal salario) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        setApellido2(apellido2);
        this.rol = rol != null ? rol : Rol.EMPLEADO;
        this.estado = estado != null ? estado : EstadoEmpleado.ACTIVO;
        this.fechaIngreso = fechaIngreso;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.direccion = direccion;
        this.salario = salario;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return identificador único
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna el identificador único del usuario.
     *
     * @param id identificador único
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario utilizado para iniciar sesión.
     *
     * @return nombre de usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Asigna el nombre de usuario para acceso al sistema.
     *
     * @param usuario nombre de usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario != null ? usuario.trim() : null;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return contraseña
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Asigna la contraseña del usuario.
     *
     * @param contrasena contraseña (máximo 16 caracteres)
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el nombre o nombres de pila del usuario.
     *
     * @return nombre(s)
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre o nombres de pila del usuario.
     *
     * @param nombre nombre(s)
     */
    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.trim() : null;
    }

    /**
     * Obtiene el primer apellido del usuario.
     *
     * @return primer apellido
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Asigna el primer apellido del usuario.
     *
     * @param apellido1 primer apellido obligatorio
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1 != null ? apellido1.trim() : null;
    }

    /**
     * Obtiene el segundo apellido opcional del usuario.
     *
     * @return segundo apellido o {@code null} si no aplica
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Asigna el segundo apellido opcional. Si se recibe una cadena en blanco,
     * se normaliza a {@code null} para mantener consistencia en MySQL.
     *
     * @param apellido2 segundo apellido opcional
     */
    public void setApellido2(String apellido2) {
        if (apellido2 == null || apellido2.trim().isEmpty()) {
            this.apellido2 = null;
        } else {
            this.apellido2 = apellido2.trim();
        }
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return rol de acceso
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Asigna el rol del usuario.
     *
     * @param rol rol de acceso
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el estado laboral actual del usuario.
     *
     * @return estado laboral
     */
    public EstadoEmpleado getEstado() {
        return estado;
    }

    /**
     * Asigna el estado laboral del usuario.
     *
     * @param estado estado laboral
     */
    public void setEstado(EstadoEmpleado estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha de ingreso a la empresa.
     *
     * @return fecha de ingreso
     */
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    /**
     * Asigna la fecha de ingreso a la empresa.
     *
     * @param fechaIngreso fecha de ingreso
     */
    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return correo electrónico
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Asigna el correo electrónico del usuario.
     *
     * @param correoElectronico correo electrónico
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico != null ? correoElectronico.trim() : null;
    }

    /**
     * Obtiene el número telefónico de 10 dígitos.
     *
     * @return teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Asigna el número telefónico de 10 dígitos.
     *
     * @param telefono teléfono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono != null ? telefono.trim() : null;
    }

    /**
     * Obtiene la dirección domiciliaria del usuario.
     *
     * @return dirección
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Asigna la dirección domiciliaria del usuario.
     *
     * @param direccion dirección
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion != null ? direccion.trim() : null;
    }

    /**
     * Obtiene el salario del usuario.
     *
     * @return salario mensual
     */
    public BigDecimal getSalario() {
        return salario;
    }

    /**
     * Asigna el salario del usuario.
     *
     * @param salario salario mensual no negativo
     */
    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    /**
     * Construye el nombre completo concatenando {@code nombre}, {@code apellido1} y
     * (si está presente) {@code apellido2} para mostrarlo en una sola columna de la vista.
     *
     * @return nombre completo formateado
     */
    @Transient
    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        if (nombre != null && !nombre.isBlank()) {
            sb.append(nombre.trim());
        }
        if (apellido1 != null && !apellido1.isBlank()) {
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(apellido1.trim());
        }
        if (apellido2 != null && !apellido2.isBlank()) {
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(apellido2.trim());
        }
        return sb.toString();
    }

    /**
     * Calcula dinámicamente la antigüedad laboral a partir de {@link #fechaIngreso}
     * hasta la fecha actual sin almacenar datos redundantes en MySQL.
     *
     * @return texto legible con el tiempo transcurrido en la empresa (ej. {@code "2 años, 4 meses"})
     */
    @Transient
    public String getAntiguedad() {
        if (fechaIngreso == null) {
            return "Sin fecha registrada";
        }
        LocalDate hoy = LocalDate.now();
        if (fechaIngreso.isAfter(hoy)) {
            return "Próximo ingreso";
        }

        Period periodo = Period.between(fechaIngreso, hoy);
        int anios = periodo.getYears();
        int meses = periodo.getMonths();
        int dias = periodo.getDays();

        if (anios > 0 && meses > 0) {
            return anios + (anios == 1 ? " año, " : " años, ")
                    + meses + (meses == 1 ? " mes" : " meses");
        }
        if (anios > 0) {
            return anios + (anios == 1 ? " año" : " años");
        }
        if (meses > 0) {
            return meses + (meses == 1 ? " mes" : " meses");
        }
        if (dias > 0) {
            return dias + (dias == 1 ? " día" : " días");
        }
        return "Ingresó hoy";
    }

    /**
     * Indica si el usuario posee rol {@link Rol#GERENTE}.
     *
     * @return {@code true} si el rol es {@code GERENTE}
     */
    @Transient
    public boolean esGerente() {
        return rol == Rol.GERENTE;
    }

    /**
     * Devuelve una representación textual segura del usuario (omitiendo la contraseña).
     *
     * @return cadena descriptiva del usuario
     */
    @Override
    public String toString() {
        return "Usuario{"
                + "id=" + id
                + ", usuario='" + usuario + '\''
                + ", nombreCompleto='" + getNombreCompleto() + '\''
                + ", rol=" + rol
                + ", estado=" + estado
                + ", correoElectronico='" + correoElectronico + '\''
                + '}';
    }
}
