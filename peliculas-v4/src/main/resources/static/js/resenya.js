/**
 * resenya.js - Gestión de reseñas para DNA Mobiliario
 */

document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    const idDesdeUrl = urlParams.get('id');

    if (idDesdeUrl) {
        cargarResenyas(idDesdeUrl);
        configurarFormulario(idDesdeUrl);
    } else {
        console.error("No se encontró el ID del producto en la URL (?id=X)");
    }
});

/**
 * Carga las reseñas desde el servidor y las muestra con el nombre real y fecha real.
 */
async function cargarResenyas(id) {
    const contenedor = document.getElementById("contenedor-resenyas");
    if (!contenedor) return;

    try {
        const response = await fetch(`/api/resenyas/producto/${id}`);
        
        if (!response.ok) {
            throw new Error("No se pudieron cargar las reseñas");
        }

        const resenyas = await response.json();

        if (!resenyas || resenyas.length === 0) {
            contenedor.innerHTML = `
                <div class="resenya-empty" style="text-align: center; padding: 40px; color: #9c8c7e;">
                    <i class="fa-solid fa-comments" style="font-size: 2rem; display: block; margin-bottom: 10px;"></i>
                    <p>Aún no hay opiniones sobre este producto. ¡Sé el primero en compartir tu experiencia!</p>
                </div>`;
            return;
        }

        contenedor.innerHTML = resenyas.map(r => {
            // 1. PROCESAMIENTO DE FECHA (Usando fecha_publicacion que viene de tu JSON)
            let fechaTexto = "Fecha no disponible";
            if (r.fecha_publicacion) {
                const d = new Date(r.fecha_publicacion);
                if (!isNaN(d.getTime())) {
                    fechaTexto = d.toLocaleDateString('es-ES', { 
                        day: '2-digit', 
                        month: 'long', 
                        year: 'numeric' 
                    });
                }
            }

            // 2. PROCESAMIENTO DE NOMBRE (Usando nombre_usuario que viene de tu JSON)
            const autor = r.nombre_usuario ? r.nombre_usuario : "Usuario no identificado";

            return `
                <div class="resenya-card" style="background: white; border-radius: 15px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); border: 1px solid #eee;">
                    <div class="resenya-header" style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px;">
                        <div>
                            <div style="font-weight: bold; color: #4a3728; font-size: 1.1rem; display: flex; align-items: center; gap: 8px;">
                                <i class="fa-solid fa-circle-user" style="color: #ae4010; font-size: 1.3rem;"></i>
                                ${autor}
                            </div>
                            <div class="stars" style="color: #ae4010; margin-top: 4px; font-size: 0.9rem;">
                                ${"★".repeat(r.puntuacion || 0)}${"☆".repeat(5 - (r.puntuacion || 0))}
                            </div>
                        </div>
                        <span class="resenya-date" style="font-size: 0.8rem; color: #9c8c7e; background: #fdf6ed; padding: 4px 10px; border-radius: 10px;">
                            <i class="fa-regular fa-calendar-days"></i> ${fechaTexto}
                        </span>
                    </div>
                    <p class="resenya-text" style="color: #5c4432; line-height: 1.5; margin: 0; font-style: italic;">
                        "${r.comentario}"
                    </p>
                </div>
            `;
        }).join("");

    } catch (error) {
        console.error("Error al cargar reseñas:", error);
        contenedor.innerHTML = "<p>Error al conectar con el servidor para cargar opiniones.</p>";
    }
}

/**
 * Maneja el envío del formulario.
 */
function configurarFormulario(idProducto) {
    const form = document.getElementById("form-resenya");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const idNumerico = parseInt(idProducto);
        if (isNaN(idNumerico)) {
            alert("Error: No se pudo identificar el producto.");
            return;
        }

        const data = {
            productoId: idNumerico,
            puntuacion: parseInt(form.puntuacion.value),
            comentario: form.comentario.value
        };

        try {
            const response = await fetch('/api/resenyas', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                alert("¡Gracias por tu opinión!");
                form.reset();
                cargarResenyas(idNumerico); // Recarga la lista para ver la nueva reseña
            } else if (response.status === 403) {
                alert("🚫 No puedes reseñar este mueble porque no aparece en tu historial de compras.");
            } else if (response.status === 401) {
                alert("Debes iniciar sesión primero para comentar.");
                window.location.href = "login.html";
            } else {
                const error = await response.json();
                alert("Error: " + (error.message || "No se pudo procesar tu reseña."));
            }

        } catch (error) {
            console.error("Error en el envío:", error);
            alert("Error de conexión al intentar enviar la reseña.");
        }
    });
}