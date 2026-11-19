# Plan de Trabajo — Proyecto Final CRUD con Spring Boot

**Fecha límite de entrega:** 30 de noviembre  
**Stack acordado:** Java 17+, Spring Boot 3 (Spring MVC + Spring Data JPA + Thymeleaf + Validation), MySQL y Maven.  
**Referencia de interfaz:** `vistaProducto.png` | **Requisitos completos:** `Proyecto_Instruccion.md`

---

## 1. Alcance del Proyecto

Desarrollar una aplicación web **CRUD** conectada a **MySQL** que al iniciar (tras autenticarse) muestre el listado de registros de una tabla con al menos **3 campos** (1 identificador único, al menos 1 campo de texto y al menos 1 campo numérico), permitiendo:

- Crear un nuevo registro.
- Editar y eliminar registros de manera individual desde cada fila.
- Marcar registros mediante **casillas de verificación (checkboxes)** y eliminar los seleccionados en lote.
- Manejar al menos una **excepción personalizada** con respuesta controlada (2 pts de rúbrica).

### Mejoras Incluidas (+4 puntos extra en rúbrica, 2 pts c/u)

1. **Buscador:** Barra de búsqueda en la vista principal para filtrar registros por texto desde la base de datos (Spring Data JPA), conservando el término consultado en pantalla y permitiendo limpiar el filtro.
2. **Manejo de sesiones:** Pantalla de inicio de sesión (`/login`) respaldada por la tabla `usuarios` en MySQL y `HttpSession`. Las rutas del CRUD (`/productos/**`) quedan protegidas por un interceptor que redirige a `/login` si no hay sesión activa, e incluye botón de **Cerrar sesión** (`/logout`).

---

## 2. Roles y Distribución del Equipo

Cada integrante es responsable tanto de su **módulo de código + comentarios Javadoc** como de una parte equitativa del **documento PDF y diagramas UML**, de modo que todos puedan avanzar en paralelo sin bloquearse.

### Cristopher Israel Cen Santana (`criscnsn`)
**Área:** Base de Datos, Modelo, Persistencia y Manejo de Excepciones

- **Código y configuración:**
  - Configurar conexión MySQL y JPA en `application.properties`.
  - Escribir el script SQL (`schema_and_data.sql`) con las tablas `productos` y `usuarios` más datos iniciales de prueba.
  - Implementar las entidades `Producto` y `Usuario` (atributos, anotaciones JPA, validaciones, constructores, getters y setters).
  - Definir las consultas en `ProductoRepository` (soporte al **Buscador**) y `UsuarioRepository` (soporte a **Manejo de sesiones**).
  - Implementar el manejo de excepciones: `ProductoNotFoundException` y `GlobalExceptionHandler` (redirigiendo errores a `error/error.html`).
- **Documentación y entregables:**
  - Comentarios **Javadoc** completos en sus clases e interfaces.
  - Sección **Descripción de Funcionalidades** en el PDF (Objetivo principal + listado de funcionalidades — *4 pts*).
  - **Diagrama de Clases UML** y fichas de clases de su módulo (`model`, `repository`, `exception`).

---

### Gael Antonio Ayuso Contreras (`gael-ayuso`)
**Área:** Lógica de Negocio, Controladores Spring MVC y Control de Sesiones

- **Código:**
  - Implementar `ProductoServiceImpl` cumpliendo el contrato de `ProductoService` (CRUD completo, borrado múltiple por lista de IDs, filtrado del **Buscador** y lanzamiento de `ProductoNotFoundException` cuando un ID no exista).
  - Implementar `AuthServiceImpl` cumpliendo `AuthService` (validación de credenciales para **Manejo de sesiones**).
  - Implementar `ProductoController` con todas las rutas HTTP del CRUD y el parámetro de búsqueda `?q=`.
  - Implementar `AuthController` (`GET /`, `GET /login`, `POST /login`, `POST /logout`) gestionando `HttpSession`.
  - Implementar `SessionInterceptor` y registrarlo en `WebConfig` para proteger `/productos/**`.
- **Documentación y entregables:**
  - Comentarios **Javadoc** completos en sus interfaces y clases.
  - Sección **Descripción de Componentes** en el PDF (dependencias, interfaces de entrada/salida, artefactos y **Diagrama de Componentes UML** — *4 pts*).
  - Fichas de clases de su módulo (`service`, `controller`, `config`).

---

### Mateo Alejandro Caamal Tencle (`MateoAlejandroCaamalTencle`)
**Área:** Interfaz de Usuario (Vistas Thymeleaf), Frontend, Integración y Video

- **Código:**
  - Diseñar la vista principal `productos/lista.html` inspirada en `vistaProducto.png`: cabecera con usuario en sesión y botón de cerrar sesión, barra del **Buscador**, botones superiores (**Agregar** y **Eliminar seleccionados**), tabla con checkboxes y acciones por fila (**Editar** / **Eliminar**).
  - Construir `productos/formulario.html` (crear/editar), `auth/login.html` (**Manejo de sesiones**) y `error/error.html` (errores amigables).
  - Agregar estilos en `styles.css` y lógica de cliente en `productos.js` (checkbox "seleccionar todos", habilitar/deshabilitar borrado masivo y confirmaciones).
- **Documentación y entregables:**
  - **Diagramas de Secuencia UML** y su descripción en el PDF (Login, Listar/Buscar, Crear, Editar, Eliminar individual y Eliminar seleccionados — *4 pts*).
  - **Descripción de Caso de Uso + grabación y enlace al video en YouTube** mostrando la app en ejecución (*3 pts*).
  - Generación final del **Javadoc HTML** y armado del `.ZIP` de entrega.

---

## 3. Contratos de Integración (Para trabajar en paralelo sin bloquearse)

El proyecto ya cuenta con el esqueleto compilable y con `TODO`s en cada archivo. Para que nadie tenga que esperar al otro, respetemos estos contratos acordados:

### A. Rutas HTTP (Gael ↔ Mateo)

| Método | Ruta | Función | Vista / Redirección |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Redirección inicial según sesión | `redirect:/productos` o `redirect:/login` |
| `GET` | `/login` | Muestra pantalla de inicio de sesión | `auth/login` |
| `POST` | `/login` | Recibe `username` y `password`, inicia sesión | `redirect:/productos` o `auth/login` |
| `POST` | `/logout` | Invalida la sesión HTTP | `redirect:/login` |
| `GET` | `/productos` | Lista registros o filtra con `?q=texto` (**Buscador**) | `productos/lista` |
| `GET` | `/productos/nuevo` | Muestra formulario vacío para crear | `productos/formulario` |
| `POST` | `/productos/guardar` | Crea o actualiza un registro | `redirect:/productos` |
| `GET` | `/productos/editar/{id}` | Carga el registro a editar | `productos/formulario` |
| `POST` | `/productos/eliminar/{id}` | Elimina un registro individual | `redirect:/productos` |
| `POST` | `/productos/eliminar-seleccionados` | Elimina los IDs marcados (`ids`) | `redirect:/productos` |

### B. Atributos de Modelo y Sesión (Cristopher ↔ Gael ↔ Mateo)

- **Sesión (`HttpSession`):** `usuarioSesion` (objeto `Usuario` autenticado).
- **Vista `productos/lista`:** `productos` (lista de `Producto`), `q` (texto buscado).
- **Vista `productos/formulario`:** `producto` (objeto `Producto`).
- **Vista `auth/login`:** `errorLogin` (mensaje si las credenciales fallan).
- **Vista `error/error`:** `tituloError` y `mensajeError` (enviados desde `GlobalExceptionHandler`).

### C. Contratos Java (Cristopher ↔ Gael)

- Las interfaces `ProductoService` y `AuthService` ya tienen definidas sus firmas base.
- Antes de conectar el repositorio con los servicios, Cristopher confirma los nombres exactos de los atributos de `Producto` y `Usuario` (por ejemplo: `id`, `nombre`, `descripcion`, `precio`, `stock` y `id`, `username`, `password`) para que Gael y Mateo usen exactamente esos nombres.

---

## 4. Flujo de Trabajo en Git y Comandos Útiles

### Ramas sugeridas
Trabajar cada módulo en su propia rama y hacer Pull Request hacia `main`:

- `feat/datos-excepciones` → Cristopher
- `feat/logica-controladores` → Gael
- `feat/vistas-ui` → Mateo

### Cómo levantar el proyecto (IntelliJ IDEA / Maven)
1. Abrir el proyecto en IntelliJ IDEA seleccionando `pom.xml` para que descargue automáticamente las dependencias de Spring Boot.
2. Ejecutar el script `schema_and_data.sql` en MySQL y colocar las credenciales locales en `application.properties`.
3. Ejecutar `CrudApplication.java` desde el IDE o desde terminal con:
   ```bash
   mvn spring-boot:run
   ```
4. Generar la documentación **Javadoc** requerida para la entrega (se guarda automáticamente en `docs/javadoc/`):
   ```bash
   mvn javadoc:javadoc
   ```

---

## 5. Checklist Final de Entrega (Antes del 30 de noviembre)

- [ ] CRUD completo funcionando contra MySQL (crear, listar, editar, eliminar individual y eliminar seleccionados con checkboxes).
- [ ] Manejo de al menos una excepción personalizada (`ProductoNotFoundException` + `GlobalExceptionHandler`).
- [ ] Mejora 1 funcionando: **Buscador**.
- [ ] Mejora 2 funcionando: **Manejo de sesiones** (login, rutas protegidas y logout).
- [ ] Código comentado en **Javadoc** + Javadoc HTML generado en `docs/javadoc/`.
- [ ] Documento **PDF** completo en `docs/pdf/` (Funcionalidades, Componentes + UML, Clases + UML, Secuencias + UML, Caso de Uso + enlace de YouTube).
- [ ] Archivo `.ZIP` empaquetado con el formato solicitado (`Apellido1Apellido2Apellido3.ZIP`).
