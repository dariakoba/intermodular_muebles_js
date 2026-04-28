let todasLasResenyas = [];

function escapeHTML(text) {
    if (!text) return '';
    return text
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#039;');
}

function renderizarTabla(resenyas) {
    const tbody = document.querySelector("#tablaResenyas tbody");
    if (!tbody) return;

    let html = "";
    resenyas.forEach(r => {
        // 1. ID de la reseña (según tu log es id_resenya)
        const id = r.id_resenya;
        
        // 2. Producto (según tu log es nombre_producto)
        const producto = r.nombre_producto || 'Mueble';
        
        // 3. IDENTIFICAR EL ID DE USUARIO: 
        // Si en tu consola no ves 'usuario_id', es probable que Jackson 
        // esté usando 'usuarioId' o que falte en la entidad. 
        // Como solución robusta, buscaremos ambas:
        const idUsuario = r.usuario_id || r.usuarioId || "---";
        const email = r.email_usuario || "Sin email";

        const usuarioHTML = `
            <div style="line-height: 1.2;">
                <strong>ID: ${idUsuario}</strong><br>
                <span style="font-size: 11px; color: #666;">${email}</span>
            </div>
        `;

        const comentario = r.comentario || '';
        const puntuacion = r.puntuacion || 0;
        const estrellas = "★".repeat(puntuacion) + "☆".repeat(5 - puntuacion);

        // Fecha
        let fechaTxt = "---";
        if (r.fecha_publicacion) {
            const f = new Date(r.fecha_publicacion);
            fechaTxt = f.toLocaleDateString('es-ES');
        }

        html += `
            <tr>
                <td><input type="checkbox" data-id="${id}"></td>
                <td>#${id}</td>
                <td>${producto}</td>
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
    id = Number(id);

    if (!confirm("¿Seguro que deseas eliminar esta reseña?")) return;

    try {
        const res = await fetch(`/api/admin/resenyas/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            todasLasResenyas = todasLasResenyas.filter(r => Number(r.idResenya) !== id);
            renderizarTabla(todasLasResenyas);
        }

    } catch (err) {
        alert("No se pudo borrar la reseña");
    }
}

document.addEventListener("DOMContentLoaded", cargarResenyas);