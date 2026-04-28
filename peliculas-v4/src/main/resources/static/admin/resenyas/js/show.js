const params = new URLSearchParams(window.location.search);
const id = params.get("id");

document.addEventListener("DOMContentLoaded", async () => {
    const contenedor = document.getElementById("resenya-detalle");
    
    if (!id) {
        contenedor.innerHTML = "<p>Error: No se proporcionó ID.</p>";
        return;
    }

    try {
        const res = await fetch(`/api/admin/resenyas/${id}`);
        const r = await res.json();

        // Mapeo seguro de datos según tu JSON
        const producto = r.nombre_producto || "Producto Desconocido";
        const email = r.email_usuario || "Anónimo";
        const idUser = r.usuario_id || r.usuarioId || "---";
        const puntuacion = Number(r.puntuacion || 0);
        const estrellas = "★".repeat(puntuacion) + "☆".repeat(5 - puntuacion);

        // Formateo de fecha
        let fechaLimpia = "---";
        if (r.fecha_publicacion) {
            const f = new Date(r.fecha_publicacion);
            fechaLimpia = f.toLocaleDateString('es-ES', { 
                year: 'numeric', month: 'long', day: 'numeric', 
                hour: '2-digit', minute: '2-digit' 
            });
        }

        contenedor.innerHTML = `
            <div class="info-row">
                <span class="info-label">ID Reseña:</span>
                <span class="info-value">${r.id_resenya}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Producto:</span>
                <span class="info-value"><strong>${producto}</strong></span>
            </div>
            <div class="info-row">
                <span class="info-label">Cliente:</span>
                <span class="info-value">${email} (ID: ${idUser})</span>
            </div>
            <div class="info-row">
                <span class="info-label">Calificación:</span>
                <span class="info-value estrellas">${estrellas}</span>
            </div>
            <div class="info-row" style="flex-direction: column; align-items: flex-start; border-bottom: none;">
                <span class="info-label">Comentario del Cliente:</span>
                <div class="info-value comentario-box">"${r.comentario || 'Sin texto'}"</div>
            </div>
            <div class="info-row">
                <span class="info-label">Publicado el:</span>
                <span class="info-value">${fechaLimpia}</span>
            </div>
        `;
    } catch (err) {
        contenedor.innerHTML = `<div style="text-align:center; color:red;">No se pudo cargar la información de la reseña.</div>`;
    }
});