/**
 * Lógica del lado del cliente para la vista de gestión de empleados (gestion.html).
 * Responsable: Mateo Alejandro Caamal Tencle (MateoAlejandroCaamalTencle)
 */
document.addEventListener('DOMContentLoaded', () => {
    const STORAGE_KEY = 'empleados_seleccionados_ids';

    const selectAllCheckbox = document.getElementById('selectAll');
    const rowCheckboxes = Array.from(document.querySelectorAll('.row-checkbox'));
    const btnEliminarSeleccionados = document.getElementById('btnEliminarSeleccionados');

    // Elementos del Modal de Edición
    const formEditar = document.getElementById('formEditarEmpleado');
    const btnConfirmarEdicion = document.getElementById('btnConfirmarEdicion');
    const camposEditables = Array.from(document.querySelectorAll('.campo-editable'));
    let snapshotEdicion = {};

    // Elementos del Modal de Eliminación Individual
    const formEliminarIndividual = document.getElementById('formEliminarIndividual');
    const textoEliminarIndividual = document.getElementById('textoEliminarIndividual');
    let idAEliminarIndividual = null;

    // Elementos del Modal de Eliminación Múltiple
    const modalEliminarMultiple = document.getElementById('modalEliminarMultiple');
    const formEliminarMultiple = document.getElementById('formEliminarMultiple');
    const textoEliminarMultiple = document.getElementById('textoEliminarMultiple');
    const contenedorIdsSeleccionados = document.getElementById('contenedorIdsSeleccionados');

    // ========================================================================
    // 1. PERSISTENCIA Y SINCRONIZACIÓN DE CHECKBOXES
    // ========================================================================

    function obtenerIdsMarcados() {
        return rowCheckboxes
            .filter(cb => cb.checked)
            .map(cb => String(cb.value));
    }

    function guardarSeleccionEnSesion(ids) {
        sessionStorage.setItem(STORAGE_KEY, JSON.stringify(ids));
    }

    function actualizarEstadoUI() {
        const marcados = obtenerIdsMarcados();
        const total = rowCheckboxes.length;

        // Habilitar botón Eliminar solo si hay al menos 1 seleccionado
        if (btnEliminarSeleccionados) {
            btnEliminarSeleccionados.disabled = marcados.length === 0;
        }

        // Sincronizar estado del checkbox maestro
        if (selectAllCheckbox) {
            if (total === 0) {
                selectAllCheckbox.checked = false;
                selectAllCheckbox.indeterminate = false;
                selectAllCheckbox.disabled = true;
            } else if (marcados.length === 0) {
                selectAllCheckbox.checked = false;
                selectAllCheckbox.indeterminate = false;
            } else if (marcados.length === total) {
                selectAllCheckbox.checked = true;
                selectAllCheckbox.indeterminate = false;
            } else {
                selectAllCheckbox.checked = false;
                selectAllCheckbox.indeterminate = true;
            }
        }
    }

    function restaurarSeleccionDesdeSesion() {
        const guardado = sessionStorage.getItem(STORAGE_KEY);
        if (!guardado) {
            actualizarEstadoUI();
            return;
        }

        try {
            const idsGuardados = JSON.parse(guardado);
            if (Array.isArray(idsGuardados)) {
                rowCheckboxes.forEach(cb => {
                    cb.checked = idsGuardados.includes(String(cb.value));
                });
                // Limpiar IDs que ya no existan en la tabla actual
                guardarSeleccionEnSesion(obtenerIdsMarcados());
            }
        } catch (e) {
            sessionStorage.removeItem(STORAGE_KEY);
        }
        actualizarEstadoUI();
    }

    // Restaurar selección al cargar la página
    restaurarSeleccionDesdeSesion();

    // Evento checkbox maestro ("seleccionar todos")
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', () => {
            const marcar = selectAllCheckbox.checked;
            rowCheckboxes.forEach(cb => {
                cb.checked = marcar;
            });
            guardarSeleccionEnSesion(obtenerIdsMarcados());
            actualizarEstadoUI();
        });
    }

    // Eventos checkboxes por fila
    rowCheckboxes.forEach(cb => {
        cb.addEventListener('change', () => {
            guardarSeleccionEnSesion(obtenerIdsMarcados());
            actualizarEstadoUI();
        });
    });

    // ========================================================================
    // 2. MODAL DE EDICIÓN INDIVIDUAL Y DETECCIÓN DE CAMBIOS
    // ========================================================================

    function verificarCambiosEdicion() {
        const hayCambios = camposEditables.some(campo => {
            const valorOriginal = snapshotEdicion[campo.id] ?? '';
            return campo.value !== valorOriginal;
        });
        btnConfirmarEdicion.disabled = !hayCambios;
    }

    document.querySelectorAll('.btn-editar').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.getAttribute('data-id');

            document.getElementById('editId').value = id;
            document.getElementById('editUsuario').value = btn.getAttribute('data-usuario') || '';
            document.getElementById('editContrasena').value = '';
            document.getElementById('editNombre').value = btn.getAttribute('data-nombre') || '';
            document.getElementById('editApellido1').value = btn.getAttribute('data-apellido1') || '';
            document.getElementById('editApellido2').value = btn.getAttribute('data-apellido2') || '';
            document.getElementById('editEstado').value = btn.getAttribute('data-estado') || 'ACTIVO';
            document.getElementById('editFechaIngreso').value = btn.getAttribute('data-fecha-ingreso') || '';
            document.getElementById('editCorreo').value = btn.getAttribute('data-correo') || '';
            document.getElementById('editTelefono').value = btn.getAttribute('data-telefono') || '';
            document.getElementById('editDireccion').value = btn.getAttribute('data-direccion') || '';
            document.getElementById('editSalario').value = btn.getAttribute('data-salario') || '';

            // Configurar ruta POST /empleados/editar/{id}
            formEditar.setAttribute('action', `/empleados/editar/${id}`);

            // Guardar instantánea de valores originales y bloquear botón
            snapshotEdicion = {};
            camposEditables.forEach(campo => {
                snapshotEdicion[campo.id] = campo.value;
            });
            btnConfirmarEdicion.disabled = true;
        });
    });

    camposEditables.forEach(campo => {
        campo.addEventListener('input', verificarCambiosEdicion);
        campo.addEventListener('change', verificarCambiosEdicion);
    });

    // Conservar los checkboxes marcados antes de enviar una edición individual
    if (formEditar) {
        formEditar.addEventListener('submit', () => {
            guardarSeleccionEnSesion(obtenerIdsMarcados());
        });
    }

    // ========================================================================
    // 3. MODAL DE ELIMINACIÓN INDIVIDUAL
    // ========================================================================

    document.querySelectorAll('.btn-eliminar-individual').forEach(btn => {
        btn.addEventListener('click', () => {
            idAEliminarIndividual = String(btn.getAttribute('data-id'));
            const nombre = btn.getAttribute('data-nombre') || '';

            textoEliminarIndividual.textContent =
                `¿Seguro que quieres borrar al siguiente empleado del sistema: ${nombre}?`;
            formEliminarIndividual.setAttribute('action', `/empleados/eliminar/${idAEliminarIndividual}`);
        });
    });

    // Al confirmar borrado individual, mantener seleccionados los demás checkboxes
    if (formEliminarIndividual) {
        formEliminarIndividual.addEventListener('submit', () => {
            const restantes = obtenerIdsMarcados().filter(id => id !== idAEliminarIndividual);
            guardarSeleccionEnSesion(restantes);
        });
    }

    // ========================================================================
    // 4. MODAL DE ELIMINACIÓN MÚLTIPLE / TOTAL
    // ========================================================================

    if (modalEliminarMultiple) {
        modalEliminarMultiple.addEventListener('show.bs.modal', () => {
            const seleccionados = obtenerIdsMarcados();
            const totalVisibles = rowCheckboxes.length;
            const inputBusqueda = document.querySelector('input[name="q"]');
            const hayFiltroActivo = inputBusqueda && inputBusqueda.value.trim().length > 0;

            if (!hayFiltroActivo && seleccionados.length === totalVisibles && totalVisibles > 0) {
                textoEliminarMultiple.textContent =
                    '¿Seguro que quieres eliminar a todos los empleados del sistema?';
            } else if (seleccionados.length === 1) {
                textoEliminarMultiple.textContent =
                    '¿Seguro que quieres eliminar al empleado seleccionado?';
            } else {
                textoEliminarMultiple.textContent =
                    `¿Seguro que quieres eliminar a los ${seleccionados.length} empleados seleccionados?`;
            }

            // Inyectar inputs ocultos name="ids" dentro del formulario
            contenedorIdsSeleccionados.innerHTML = '';
            seleccionados.forEach(id => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'ids';
                input.value = id;
                contenedorIdsSeleccionados.appendChild(input);
            });
        });
    }

    if (formEliminarMultiple) {
        formEliminarMultiple.addEventListener('submit', () => {
            sessionStorage.removeItem(STORAGE_KEY);
        });
    }
});
