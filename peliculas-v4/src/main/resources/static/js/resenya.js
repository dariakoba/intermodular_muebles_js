document.addEventListener("DOMContentLoaded", () => {

    const productoId = new URLSearchParams(window.location.search).get("id");

    if (!productoId) return;

    cargarResenas(productoId);
    comprobarPermiso(productoId);

    document.getElementById("btn-send-review")?.addEventListener("click", () => {
        enviarResena(productoId);
    });
});

async function cargarResenas(productoId) {

    const res = await fetch(`/api/resenas/producto/${productoId}`);
    const data = await res.json();

    const container = document.getElementById("reviews-container");
    container.innerHTML = "";

    data.forEach(r => {

        const estrellas = "★".repeat(r.puntuacion) + "☆".repeat(5 - r.puntuacion);

        container.innerHTML += `
            <div class="review-card">
                <div class="review-header">
                    <span class="stars">${estrellas}</span>
                </div>
                <p class="review-text">${r.comentario}</p>
                <span class="review-user">— ${r.nombreUsuario || "Usuario"}</span>
            </div>
        `;
    });
}

async function comprobarPermiso(productoId) {

    const res = await fetch(`/api/resenas/puede/${productoId}`);

    if (res.ok) {
        document.getElementById("review-form").style.display = "block";
    }
}

async function enviarResena(productoId) {

    const puntuacion = document.getElementById("rating").value;
    const comentario = document.getElementById("comentario").value;

    const res = await fetch("/api/resenas", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            idProducto: productoId,
            puntuacion: parseInt(puntuacion),
            comentario: comentario
        })
    });

    if (res.ok) {
        alert("Reseña enviada");
        cargarResenas(productoId);
    } else {
        alert("No puedes reseñar este producto");
    }
}