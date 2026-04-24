let currentUser = null;
let todosLosUsuarios = []; // Almacén local para filtrado rápido

// ======================
// 👤 USUARIO LOGUEADO
// ======================
async function cargarMe() {
    try {
        const res = await fetch("/api/me");
        currentUser = await res.json();
    } catch (e) {
        console.error("No se pudo obtener usuario logueado");
    }
}

// ======================
// 📋 RENDERIZAR TABLA
// ======================
// Esta función se encarga solo de pintar los datos que recibe
function renderizarTabla(usuarios) {
    const tbody = document.querySelector("#tablaUsuarios tbody");
    if (!tbody) return;

    let html = "";

    usuarios.forEach(u => {
        const estado = (u.estado ?? "").toLowerCase();
        const isMe = currentUser && currentUser.id === u.id;

        html += `
            <tr class="${isMe ? 'fila-yo' : ''}">
                <td>
                    <input type="checkbox"
                           data-id="${u.id}"
                           ${isMe ? "disabled" : ""}>
                </td>
                <td>${u.id}</td>
                <td>${u.nombre} ${u.apellidos}</td>
                <td>${u.email}</td>
                <td>${u.rol}</td>
                <td>${u.telefono ?? ""}</td>
                <td>
                    <span class="${estado === 'activo' ? 'estado-activo' : 'estado-inactivo'}">
                        ${estado}
                    </span>
                </td>
                <td class="acciones">
                    <a href="show.html?id=${u.id}" class="btn-ver">Ver</a>
                    <a href="edit.html?id=${u.id}" class="btn-editar">Editar</a>
                    <button
                        class="${estado === 'activo' ? 'btn-desactivar' : 'btn-activar'}"
                        onclick="toggleUsuario(${u.id}, '${estado}')"
                        ${isMe ? "disabled" : ""}>
                        ${estado === 'activo' ? 'Desactivar' : 'Activar'}
                    </button>
                </td>
            </tr>
        `;
    });

    tbody.innerHTML = html;
}

// ======================
// 🔍 FILTRADO LÓGICO
// ======================
function filtrarUsuarios() {
    const query = document.getElementById("inputBuscar").value.toLowerCase();
    const filtroRol = document.getElementById("filterRol").value.toLowerCase();
    const filtroEstado = document.getElementById("filterEstado").value.toLowerCase();

    const usuariosFiltrados = todosLosUsuarios.filter(u => {
        const nombreCompleto = `${u.nombre} ${u.apellidos}`.toLowerCase();
        const email = u.email.toLowerCase();
        
        // Coincidencia por texto (Nombre o Email)
        const coincideTexto = nombreCompleto.includes(query) || email.includes(query);
        
        // Coincidencia por Selects
        const coincideRol = filtroRol === "" || u.rol.toLowerCase() === filtroRol;
        const coincideEstado = filtroEstado === "" || (u.estado ?? "").toLowerCase() === filtroEstado;

        return coincideTexto && coincideRol && coincideEstado;
    });

    renderizarTabla(usuariosFiltrados);
}

// ======================
// 📥 CARGAR USUARIOS (API)
// ======================
async function cargarUsuarios() {
    try {
        const res = await fetch("/api/admin/usuarios");
        todosLosUsuarios = await res.json();
        
        // Al cargar, aplicamos filtros por si hay algo escrito
        filtrarUsuarios(); 
    } catch (err) {
        console.error("Error al cargar usuarios:", err);
    }
}

// ======================
// 🔄 TOGGLE ESTADO
// ======================
async function toggleUsuario(id, estadoActual) {
    if (currentUser && currentUser.id === id) {
        alert("❌ No puedes cambiar tu propio estado.");
        return;
    }

    const nuevoEstado = estadoActual === "activo" ? "inactivo" : "activo";

    try {
        const res = await fetch(`/api/admin/usuarios/${id}/estado`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ estado: nuevoEstado })
        });

        if (res.ok) {
            cargarUsuarios(); // Recarga y actualiza la lista global
        } else {
            alert("❌ No se pudo cambiar el estado");
        }
    } catch (err) {
        alert("Error de conexión");
    }
}

// ======================
// 🚀 INIT
// ======================
document.addEventListener("DOMContentLoaded", async () => {

    await cargarMe();
    await cargarUsuarios();

    // --- Listeners para Buscador y Filtros ---
    const inputBuscar = document.getElementById("inputBuscar");
    const filterRol = document.getElementById("filterRol");
    const filterEstado = document.getElementById("filterEstado");

    if (inputBuscar) inputBuscar.addEventListener("input", filtrarUsuarios);
    if (filterRol) filterRol.addEventListener("change", filtrarUsuarios);
    if (filterEstado) filterEstado.addEventListener("change", filtrarUsuarios);

    // --- CHECK ALL ---
    const checkAll = document.getElementById("checkAll");
    if (checkAll) {
        checkAll.addEventListener("change", () => {
            document.querySelectorAll("#tablaUsuarios tbody input[type='checkbox']")
                .forEach(cb => cb.checked = checkAll.checked);
        });
    }

    // --- DELETE SELECCIONADOS ---
    const btnEliminar = document.getElementById("btn-eliminar-seleccionados");
    if (btnEliminar) {
        const nuevoBtn = btnEliminar.cloneNode(true);
        btnEliminar.parentNode.replaceChild(nuevoBtn, btnEliminar);

        nuevoBtn.addEventListener("click", async () => {
            const checkboxes = document.querySelectorAll(
                "#tablaUsuarios tbody input[type='checkbox']:checked"
            );

            const ids = Array.from(checkboxes).map(cb => parseInt(cb.dataset.id));

            if (ids.length === 0) {
                alert("Selecciona al menos un usuario");
                return;
            }

            if (!confirm(`¿Eliminar ${ids.length} usuarios?`)) return;

            let errores = [];
            let ok = 0;

            for (const id of ids) {
                if (currentUser && currentUser.id === id) {
                    errores.push(`ID ${id}: No puedes eliminarte a ti mismo`);
                    continue;
                }

                try {
                    const res = await fetch(`/api/admin/usuarios/${id}`, {
                        method: "DELETE"
                    });
                    const data = await res.json().catch(() => ({}));

                    if (data.success) ok++;
                    else errores.push(`ID ${id}: ${data.message || "Error"}`);
                } catch {
                    errores.push(`ID ${id}: Error de conexión`);
                }
            }

            if (errores.length > 0) {
                alert(`✅ ${ok} eliminados\n❌ ERRORES:\n${errores.join("\n")}`);
            } else {
                alert(`✅ ${ok} eliminados correctamente`);
            }

            cargarUsuarios();
            if (checkAll) checkAll.checked = false;
        });
    }
});