let todasLasResenyas = [];

function renderizarTabla(resenyas) {
    const tbody = document.querySelector("#tablaResenyas tbody");
    if (!tbody) return;

    let html = "";
    resenyas.forEach(r => {
        const id = r.id_resenya;
        const producto = r.nombre_producto || 'Mueble';
        const idUsuario = r.usuario_id || r.usuarioId || "---";
        const email = r.email_usuario || "Sin email";
        const comentario = r.comentario || '';
        const puntuacion = Number(r.puntuacion || 0);
        const estrellas = "★".repeat(puntuacion) + "☆".repeat(5 - puntuacion);

        let fechaTxt = "---";
        if (r.fecha_publicacion) {
            const f = new Date(r.fecha_publicacion);
            fechaTxt = !isNaN(f) ? f.toLocaleDateString('es-ES') : "---";
        }

        const usuarioHTML = `
            <div style="line-height: 1.2;">
                <strong>ID: ${idUsuario}</strong><br>
                <span style="font-size: 11px; color: #666;">${email}</span>
            </div>`;

        html += `
            <tr>
                <td><input type="checkbox" data-id="${id}"></td>
                <td>${id}</td> 
                <td><strong>${producto}</strong></td>
                <td>${usuarioHTML}</td>
                <td style="color:#f39c12">${estrellas}</td>
                <td class="comentario-celda">${comentario}</td>
                <td>${fechaTxt}</td>
                <td>
                    <div class="acciones">
                        <a href="show.html?id=${id}" class="btn-ver"><i class="fa-solid fa-eye"></i></a>
                        <button class="btn-desactivar" onclick="borrarResenya(${id})"><i class="fa-solid fa-trash-can"></i></button>
                    </div>
                </td>
            </tr>`;
    });
    tbody.innerHTML = html;
}

function aplicarFiltros() {
    const inputBuscar = document.getElementById("inputBuscar");
    const selectPuntos = document.getElementById("filterPuntos");
    
    if (!inputBuscar || !selectPuntos) return;

    const busqueda = limpiarTexto(inputBuscar.value);
    const puntosFiltro = selectPuntos.value;

    const filtradas = todasLasResenyas.filter(r => {
        // Normalizamos todos los campos disponibles
        const idResenya = limpiarTexto(r.id_resenya);
        const producto = limpiarTexto(r.nombre_producto);
        const email = limpiarTexto(r.email_usuario);
        const comentario = limpiarTexto(r.comentario);
        
        // --- NUEVO: Buscamos el ID del usuario ---
        // Intentamos obtenerlo de usuario_id o usuarioId
        const idUsuario = limpiarTexto(r.usuario_id || r.usuarioId);

        // Verificamos si la búsqueda está en alguno de los campos
        const coincideTexto = busqueda === "" || 
                             idResenya.includes(busqueda) || 
                             producto.includes(busqueda) || 
                             email.includes(busqueda) || 
                             comentario.includes(busqueda) ||
                             idUsuario.includes(busqueda); // <--- Filtro por ID de user

        // Filtro por estrellas
        const coincidePuntos = puntosFiltro === "" || r.puntuacion.toString() === puntosFiltro;

        return coincideTexto && coincidePuntos;
    });

    renderizarTabla(filtradas);
}
// Esta función quita tildes y convierte a minúsculas
function limpiarTexto(texto) {
    if (!texto) return "";
    return texto
        .toString()
        .toLowerCase()
        .normalize("NFD") // Separa la letra de la tilde (á -> a + ´)
        .replace(/[\u0300-\u036f]/g, ""); // Borra la tilde suelta
}

async function cargarResenyas() {
    try {
        const res = await fetch("/api/admin/resenyas");
        if (!res.ok) throw new Error("Error en el servidor");
        todasLasResenyas = await res.json();
        console.log("DEBUG datos:", todasLasResenyas);
        renderizarTabla(todasLasResenyas);
    } catch (err) {
        console.error("Error al cargar:", err);
    }
}

async function borrarResenya(id) {
    if (!confirm("¿Seguro que deseas eliminar esta reseña?")) return;
    try {
        const res = await fetch(`/api/admin/resenyas/${id}`, { method: 'DELETE' });
        if (res.ok) {
            // USAMOS id_resenya para filtrar la lista local
            todasLasResenyas = todasLasResenyas.filter(r => Number(r.id_resenya) !== Number(id));
            renderizarTabla(todasLasResenyas);
        }
    } catch (err) {
        alert("No se pudo borrar la reseña");
    }
}

// TODO EN UN SOLO DOMContentLoaded
document.addEventListener("DOMContentLoaded", () => {
    cargarResenyas();

    const inputBuscar = document.getElementById("inputBuscar");
    if (inputBuscar) inputBuscar.addEventListener("input", aplicarFiltros);

    const selectPuntos = document.getElementById("filterPuntos");
    if (selectPuntos) selectPuntos.addEventListener("change", aplicarFiltros);
});