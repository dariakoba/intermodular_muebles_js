const params = new URLSearchParams(window.location.search);
const id = params.get("id");

document.addEventListener("DOMContentLoaded", async () => {

    const r = await fetch(`/api/admin/resenyas/${id}`)
        .then(res => res.json());

    document.getElementById("titulo").textContent = "Reseña " + r.id_resenya;

    document.getElementById("id_resenya").textContent = r.id_resenya;

    document.getElementById("producto").textContent =
        r.nombre_producto ?? "Desconocido";

    document.getElementById("usuario").textContent =
        r.nombre_usuario ?? r.id_usuario ?? "---";

    document.getElementById("email").textContent =
        r.email_usuario ?? "---";

    document.getElementById("comentario").textContent =
        r.comentario ?? "Sin comentario";

    // ⭐ PUNTUACIÓN
    const puntuacion = Number(r.puntuacion || 0);
    document.getElementById("puntuacion").textContent =
        "★".repeat(puntuacion) + "☆".repeat(5 - puntuacion);

    // 📅 FECHA
    let fecha = "---";
    if (r.fecha_publicacion) {
        const f = new Date(r.fecha_publicacion);
        fecha = f.toLocaleString("es-ES");
    }
    document.getElementById("fecha").textContent = fecha;

    // 🖼️ IMÁGENES
    const imagenes = await fetch(`/api/resenyas/${id}/imagenes`)
        .then(res => res.json());

    const galeria = document.getElementById("galeria");

    if (galeria) {
        galeria.innerHTML = "";

        if (imagenes && imagenes.length) {

            imagenes.forEach(img => {

                const el = document.createElement("img");
                el.src = img.url;

                el.style.width = "140px";
                el.style.height = "140px";
                el.style.objectFit = "cover";
                el.style.borderRadius = "8px";
                el.style.cursor = "pointer";
                el.style.transition = "0.2s";

                el.onmouseenter = () => el.style.transform = "scale(1.05)";
                el.onmouseleave = () => el.style.transform = "scale(1)";

                el.onclick = () => abrirModal(img.url);

                galeria.appendChild(el);
            });
        }
    }
});


// 🧠 MODAL
function abrirModal(url) {
    const modal = document.getElementById("modal");
    const img = document.getElementById("modal-img");

    img.src = url;
    modal.style.display = "flex";
}

function cerrarModal() {
    const modal = document.getElementById("modal");
    modal.style.display = "none";
}

// 🔒 eventos modal
document.addEventListener("DOMContentLoaded", () => {

    const cerrar = document.getElementById("cerrar-modal");
    const modal = document.getElementById("modal");

    if (!cerrar || !modal) return;

    cerrar.onclick = cerrarModal;

    modal.onclick = (e) => {
        if (e.target.id === "modal") {
            cerrarModal();
        }
    };

    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") {
            cerrarModal();
        }
    });
});