# Plan de Trabajo — Manejador de Empleados (CRUD con Spring Boot)

**Fecha límite de entrega:** 30 de noviembre  
**Stack acordado:** Java 17+, Spring Boot 3 (Spring MVC + Spring Data JPA + Thymeleaf + Validation), Bootstrap 5, MySQL y Maven.  
**Referencia visual:** Interfaz de ejemplo publicada en la plataforma.

---

## 1. Alcance del Proyecto

Desarrollar una aplicación web **CRUD para la gestión de empleados** conectada a **MySQL**, con acceso autenticado y diferenciación por **2 roles** (`GERENTE` y `EMPLEADO`):

- **Rol `GERENTE`:** Accede a la pantalla principal de administración de empleados (estética basada en la interfaz de ejemplo de la plataforma). En la tabla aparecen **únicamente los empleados** (nunca gerentes, evitando borrados accidentales). Puede:
  - Listar empleados mostrando su nombre completo en una sola columna.
  - Buscar empleados por **nombre completo, correo electrónico o teléfono** (**dirección excluida**).
  - Seleccionar empleados con casillas de verificación (checkbox por fila + checkbox maestro en la cabecera para seleccionar a todos).
  - Usar los botones superiores: **Eliminar** (habilitado únicamente si hay al menos 1 empleado seleccionado) y **Añadir nuevo empleado** (siempre habilitado).
  - Editar o eliminar un empleado de manera individual desde su fila **sin perder la selección** de los demás checkboxes marcados.
  - Realizar altas, ediciones y confirmaciones de borrado mediante **ventanas flotantes (modales)** sin cambiar de pantalla.
  - Editar todos los campos del empleado excepto el `id`; en el modal de edición el botón **"Confirmar edición"** solo se activa si se modificó al menos un campo.
- **Rol `EMPLEADO`:** Al iniciar sesión accede a una vista de **solo lectura** donde consulta su propio resumen laboral (estado actual, fecha de ingreso y antigüedad en la empresa) y ve la lista de empleados con su propia fila resaltada en otro color para identificarse.
- **Alta de gerentes:** Los usuarios con rol `GERENTE` se registran directamente desde el script SQL inicial (el CRUD administra únicamente `EMPLEADO`).
- **Manejo de excepciones (2 pts de rúbrica):** Captura controlada de `EmpleadoNotFoundException`, registros duplicados (`usuario` / `correo_electronico`) y errores generales mediante `GlobalExceptionHandler`.

### Mejoras Incluidas (Puntos Extra — Tope de 4 pts, 2 pts c/u)

1. **Buscador:** Filtrado por nombre completo, correo electrónico o teléfono (excluyendo dirección) mediante Spring Data JPA.
2. **Manejo de sesiones con 2 roles (`GERENTE` y `EMPLEADO`):** Login/Logout con `HttpSession`, redirección automática según rol y protección de rutas del gerente con `SessionInterceptor`.
3. **Ventanas flotantes (Modales Bootstrap 5):** Modales para crear empleado, editar empleado, confirmar eliminación individual y confirmar eliminación múltiple/total sin cambiar de vista.

---

## 2. Modelo de Datos Acordado (Tabla única `usuarios`)

Gerentes y empleados comparten exactamente los mismos atributos, por lo que se almacenan en una **única tabla `usuarios`** diferenciados por `rol` (evitando duplicar tablas, entidades y consultas de login).

**Todos los campos son obligatorios (`NOT NULL`) excepto `apellido2`:**

| Columna en MySQL | Atributo Java (`Usuario`) | Tipo / Regla | Notas |
| :--- | :--- | :--- | :--- |
| `id` | `id` | `BIGINT AUTO_INCREMENT PK` | No editable; no se muestra al crear, solo lectura al editar |
| `usuario` | `usuario` | `VARCHAR(30) UNIQUE` | Nombre de usuario para iniciar sesión |
| `contrasena` | `contrasena` | `VARCHAR(16)` | Máx. 16 caracteres (texto plano); al editar es opcional (si queda vacía se conserva la actual) |
| `nombre` | `nombre` | `VARCHAR(60)` | Nombre(s) del usuario |
| `apellido1` | `apellido1` | `VARCHAR(50)` | Primer apellido (obligatorio) |
| `apellido2` | `apellido2` | `VARCHAR(50) NULL` | Segundo apellido (**opcional**) |
| `rol` | `rol` | `ENUM('GERENTE','EMPLEADO')` | Define permisos y vista de inicio |
| `estado` | `estado` | `ENUM('ACTIVO','VACACIONES','INCAPACIDAD')` | Editable por el gerente y visible para el empleado |
| `fecha_ingreso` | `fechaIngreso` | `DATE` | Base para calcular la antigüedad en Java (sin columna redundante) |
| `correo_electronico` | `correoElectronico` | `VARCHAR(100) UNIQUE` | Incluido en el buscador |
| `telefono` | `telefono` | `VARCHAR(10)` | Máx. 10 dígitos; incluido en el buscador |
| `direccion` | `direccion` | `VARCHAR(200)` | Excluida del buscador |
| `salario` | `salario` | `DECIMAL(10,2) CHECK (>= 0)` | Campo numérico exigido por la rúbrica |

> En pantalla el nombre se muestra unido en una sola columna mediante el método calculado `getNombreCompleto()` (sin columna duplicada en BD). La antigüedad se calcula con `getAntiguedad()` a partir de `fechaIngreso`.

---

## 3. Roles y Distribución del Equipo

Cada integrante es responsable de su **módulo de código + Javadoc** y de su parte del **documento PDF y diagramas UML**, trabajando en paralelo sin bloquearse.

### Cristopher Israel Cen Santana (`criscnsn`)
**Área:** Base de Datos, Modelo, Persistencia y Manejo de Excepciones

- **Código y configuración:**
  - Configurar conexión a MySQL (`empleados_crud_db`) y JPA en `application.properties`.
  - Escribir el script `schema_and_data.sql` con la tabla `usuarios`, restricciones de integridad, índices para el buscador y datos semilla (gerentes + empleados).
  - Completar los enums `Rol` (`GERENTE`, `EMPLEADO`) y `EstadoEmpleado` (`ACTIVO`, `VACACIONES`, `INCAPACIDAD`) y la entidad `Usuario` (validaciones, getters/setters y métodos `@Transient` `getNombreCompleto()` y `getAntiguedad()`).
  - Implementar las consultas en `UsuarioRepository`: login (`findByUsuario`), listado solo de empleados (`findByRol`) y consulta del **Buscador** (nombre completo, correo o teléfono; excluyendo dirección).
  - Implementar `EmpleadoNotFoundException` y `GlobalExceptionHandler` (404 para no encontrado, manejo de duplicados y respaldo general hacia `error/error.html`).
- **Documentación y entregables:**
  - Comentarios **Javadoc** completos en sus paquetes.
  - Sección **Descripción de Funcionalidades** en el PDF (Objetivo principal + listado de funcionalidades — *4 pts*).
  - **Diagrama de Clases UML** y fichas de clases de su módulo (`model`, `repository`, `exception`).

---

### Gael Antonio Ayuso Contreras (`gael-ayuso`)
**Área:** Lógica de Negocio, Controladores Spring MVC y Seguridad de Sesiones/Roles

- **Código:**
  - Implementar `EmpleadoServiceImpl` cumpliendo `EmpleadoService`: listar solo empleados, buscador, crear forzando `Rol.EMPLEADO`, editar todos los campos menos `id`/`rol` (conservando la contraseña si viene vacía), eliminar individual y eliminar seleccionados protegiendo siempre a los gerentes y lanzando `EmpleadoNotFoundException`.
  - Implementar `AuthServiceImpl` cumpliendo `AuthService` para validar `usuario` y `contrasena`.
  - Implementar `EmpleadoController` (rutas CRUD del gerente + ruta `/empleados/directorio` del empleado) y `AuthController` (login, logout y redirección según rol).
  - Implementar `SessionInterceptor` y registrarlo en `WebConfig` para exigir sesión activa y bloquear que un `EMPLEADO` entre a rutas de `GERENTE`.
- **Documentación y entregables:**
  - Comentarios **Javadoc** completos en sus interfaces y clases.
  - Sección **Descripción de Componentes** en el PDF (dependencias, interfaces de entrada/salida, artefactos y **Diagrama de Componentes UML** — *4 pts*).
  - Fichas de clases de su módulo (`service`, `controller`, `config`).

---

### Mateo Alejandro Caamal Tencle (`MateoAlejandroCaamalTencle`)
**Área:** Interfaz Gráfica (Vistas + Modales), Frontend, Integración y Video

- **Código:**
  - Diseñar `empleados/gestion.html` (vista del `GERENTE`) basándose en **la interfaz de ejemplo de la plataforma**: cabecera con botones **Eliminar** y **Añadir nuevo empleado**, barra del **Buscador**, tabla con checkboxes (individual + seleccionar todos), acciones Editar/Eliminar por fila y los **4 modales de Bootstrap 5** (Crear sin campo ID, Editar con ID solo lectura y botón deshabilitado hasta modificar algún campo, Confirmar borrado individual y Confirmar borrado múltiple/todos).
  - Diseñar `empleados/directorio.html` (vista del `EMPLEADO`) con tarjeta personal (estado laboral, fecha de ingreso, antigüedad y propio salario) y tabla solo lectura resaltando su fila en otro color.
  - Diseñar `auth/login.html` y `error/error.html`, estilos en `styles.css` e interactividad en `empleados.js` (habilitar Delete solo si hay selección, conservar checkboxes de otras filas vía `sessionStorage` en acciones individuales, mensajes dinámicos de borrado y detección de cambios en el modal de edición).
- **Documentación y entregables:**
  - **Diagramas de Secuencia UML** y su descripción en el PDF (Login por rol, Listar/Buscar, Crear en modal, Editar en modal, Eliminar individual y Eliminar seleccionados/todos — *4 pts*).
  - **Descripción de Caso de Uso + grabación y enlace al video en YouTube** mostrando la app en ejecución (*3 pts*).
  - Generación final del **Javadoc HTML** y empaquetado del `.ZIP` de entrega.

---

## 4. Contratos de Integración (Trabajo en Paralelo)

### A. Rutas HTTP (Gael ↔ Mateo)

| Método | Ruta | Acceso | Función | Vista / Redirección |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | Público | Redirige a `/login` o a la vista de su rol | `redirect:...` |
| `GET` | `/login` | Público | Pantalla de inicio de sesión | `auth/login` |
| `POST` | `/login` | Público | Valida `usuario` y `contrasena`, abre sesión | `redirect:/empleados` o `redirect:/empleados/directorio` |
| `POST` | `/logout` | Sesión | Invalida la sesión HTTP | `redirect:/login` |
| `GET` | `/empleados` | **Solo GERENTE** | Lista empleados o filtra con `?q=` (**Buscador**) | `empleados/gestion` |
| `POST` | `/empleados/crear` | **Solo GERENTE** | Da de alta un empleado desde el modal (sin ID) | `redirect:/empleados` |
| `POST` | `/empleados/editar/{id}` | **Solo GERENTE** | Actualiza empleado desde el modal de edición | `redirect:/empleados` |
| `POST` | `/empleados/eliminar/{id}` | **Solo GERENTE** | Elimina un empleado individual | `redirect:/empleados` |
| `POST` | `/empleados/eliminar-seleccionados` | **Solo GERENTE** | Elimina la lista `ids` de checkboxes marcados | `redirect:/empleados` |
| `GET` | `/empleados/directorio` | **EMPLEADO** | Directorio solo lectura resaltando al usuario actual | `empleados/directorio` |

### B. Atributos de Sesión y Modelo

- **Sesión (`HttpSession`):** `usuarioSesion` (instancia de `Usuario` autenticado).
- **`empleados/gestion`:** `empleados`, `estados` (`EstadoEmpleado.values()`), `q`, `nuevoEmpleado`, `mensajeExito`, `mensajeError`.
- **`empleados/directorio`:** `empleados` (más `session.usuarioSesion`).
- **`auth/login`:** `errorLogin`.
- **`error/error`:** `tituloError`, `mensajeError`.

### C. Textos de Confirmación Acordados para Modales (Mateo)

- **Eliminar individual:** *"¿Seguro que quieres borrar al siguiente empleado del sistema: `<nombre>`?"* (botones Cancelar / Borrar).
- **Eliminar todos seleccionados:** *"¿Seguro que quieres eliminar a todos los empleados del sistema?"* (sin borrar gerentes).
- **Eliminar varios seleccionados:** *"¿Seguro que quieres eliminar a los `N` empleados seleccionados?"*.
- **Editar empleado:** Botón *"Confirmar edición"* deshabilitado al abrir; se habilita únicamente cuando se modifica al menos un campo.

---

## 5. Flujo de Trabajo en Git y Comandos Útiles

### Ramas sugeridas
- `feat/datos-excepciones` → Cristopher
- `feat/logica-controladores` → Gael
- `feat/vistas-ui` → Mateo

### Comandos principales (IntelliJ IDEA / Maven)
1. Abrir/recargar `pom.xml` en IntelliJ IDEA como proyecto Maven.
2. Ejecutar `schema_and_data.sql` en MySQL y colocar credenciales locales en `application.properties`.
3. Levantar la aplicación:
   ```bash
   mvn spring-boot:run
   ```
4. Generar el Javadoc HTML (se guarda en `docs/javadoc/`):
   ```bash
   mvn javadoc:javadoc
   ```

---

## 6. Checklist Final de Entrega (Antes del 30 de noviembre)

- [ ] CRUD completo de empleados con MySQL (crear, listar, editar, eliminar individual y eliminar seleccionados con checkboxes).
- [ ] Manejo de excepciones (`EmpleadoNotFoundException` + `GlobalExceptionHandler`).
- [ ] Mejoras funcionando: **Buscador** (sin dirección), **Manejo de sesiones con 2 roles** (`GERENTE` / `EMPLEADO`) y **Ventanas flotantes (Modales)**.
- [ ] Código documentado con **Javadoc** + Javadoc HTML generado en `docs/javadoc/`.
- [ ] Documento **PDF** completo en `docs/pdf/` (Funcionalidades, Componentes + UML, Clases + UML, Secuencias + UML, Caso de Uso + enlace de YouTube).
- [ ] Archivo `.ZIP` empaquetado con el formato solicitado (`Apellido1Apellido2Apellido3.ZIP`).
