document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    // Buscamos 'id' en la URL. Asegúrate de que tu URL sea detalle.html?id=1
    const idDesdeUrl = urlParams.get('id');

    if (idDesdeUrl) {
        cargarResenyas(idDesdeUrl);
        configurarFormulario(idDesdeUrl);
    } else {
        console.error("No se encontró el ID del producto en la URL");
    }
});

async function cargarResenyas(id) {
    const contenedor = document.getElementById("contenedor-resenyas");
    if (!contenedor) return;

    try {
        const response = await fetch(`/api/resenyas/producto/${id}`);
        const resenyas = await response.json();

        if (resenyas.length === 0) {
            contenedor.innerHTML = "<p class='resenya-empty'>Aún no hay reseñas de este mueble.</p>";
            return;
        }

        contenedor.innerHTML = resenyas.map(r => `
            <div class="resenya-card">
                <div class="resenya-header">
                    <div class="stars">
                        ${"★".repeat(r.puntuacion)}${"☆".repeat(5 - r.puntuacion)}
                    </div>
                </div>
                <p class="resenya-text">${r.comentario}</p>
            </div>
        `).join("");

    } catch (error) {
        console.error("Error al cargar reseñas:", error);
    }
}

function configurarFormulario(idProducto) {
    const form = document.getElementById("form-resenya");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Forzamos la conversión a número entero
        const idNumerico = parseInt(idProducto);

        if (isNaN(idNumerico) || idNumerico <= 0) {
            alert("Error: ID de producto no válido.");
            return;
        }

        // El objeto 'data' debe tener exactamente los nombres de tu clase Java
        const data = {
            productoId: idNumerico, // Asegúrate que en Java el atributo sea 'productoId'
            puntuacion: parseInt(form.puntuacion.value),
            comentario: form.comentario.value
        };

        console.log("Enviando datos al servidor:", data); // Mira la consola del navegador (F12)

        try {
            const response = await fetch('/api/resenyas', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                alert("¡Gracias por tu opinión!");
                form.reset();
                cargarResenyas(idNumerico);
            } else if (response.status === 403) {
                alert("🚫 No puedes reseñar este mueble porque no aparece en tu historial de compras.");
            } else if (response.status === 401) {
                alert("Debes iniciar sesión primero.");
                window.location.href = "login.html";
            } else {
                const error = await response.json();
                alert("Error: " + (error.message || "No se pudo enviar"));
            }

        } catch (error) {
            console.error("Error en fetch:", error);
            alert("Error de conexión con el servidor.");
        }
    });
}