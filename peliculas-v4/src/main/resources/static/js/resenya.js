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
 * Carga las reseñas y gestiona la visualización del botón de eliminar.
 */
async function cargarResenyas(id) {
    const contenedor = document.getElementById("contenedor-resenyas");
    if (!contenedor) return;

    const userIdLogueado = sessionStorage.getItem("userId");

    try {
        const response = await fetch(`/api/resenyas/producto/${id}`);
        if (!response.ok) throw new Error("No se pudieron cargar las reseñas");

        const resenyas = await response.json();

        if (!resenyas || resenyas.length === 0) {
            contenedor.innerHTML = `
                <div class="resenya-empty" style="text-align: center; padding: 40px; color: #9c8c7e;">
                    <i class="fa-solid fa-comments" style="font-size: 2rem; display: block; margin-bottom: 10px;"></i>
                    <p>Aún no hay opiniones sobre este producto.</p>
                </div>`;
            return;
        }

        contenedor.innerHTML = resenyas.map(r => {
            // CORRECCIÓN: Aseguramos capturar el ID de la reseña bajo cualquier nombre común de base de datos
            const idResenya = r.idResenya || r.id_resena || r.id;
            const idUsuarioResenya = r.usuarioId || r.id_usuario;
            const nombreAutor = r.nombreUsuario || r.nombre_usuario || "Usuario";
            const fechaOriginal = r.fechaPublicacion || r.fecha || r.fecha_publicacion;

            let fechaTexto = "Reciente";
            if (fechaOriginal) {
                const d = new Date(fechaOriginal);
                if (!isNaN(d.getTime())) {
                    fechaTexto = d.toLocaleDateString('es-ES', { 
                        day: '2-digit', month: 'long', year: 'numeric' 
                    });
                }
            }

            let botonEliminar = "";
            // Comparamos con == por si uno es string y el otro number en sessionStorage
            if (userIdLogueado && idUsuarioResenya && idUsuarioResenya == userIdLogueado) {
                botonEliminar = `
                    <button onclick="eliminarResenya(${idResenya}, ${id})" 
                            style="background: none; border: none; color: #d9534f; cursor: pointer; font-size: 0.85rem; margin-top: 8px; display: flex; align-items: center; gap: 5px; padding: 0;">
                        <i class="fa-solid fa-trash-can"></i> Eliminar mi reseña
                    </button>`;
            }

            return `
                <div class="resenya-card" style="background: white; border-radius: 15px; padding: 20px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); border: 1px solid #eee;">
                    <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px;">
                        <div>
                            <div style="font-weight: bold; color: #4a3728; font-size: 1.1rem; display: flex; align-items: center; gap: 8px;">
                                <i class="fa-solid fa-circle-user" style="color: #ae4010; font-size: 1.3rem;"></i>
                                ${nombreAutor}
                            </div>
                            <div style="color: #ae4010; margin-top: 4px; font-size: 0.9rem;">
                                ${"★".repeat(r.puntuacion || 0)}${"☆".repeat(5 - (r.puntuacion || 0))}
                            </div>
                        </div>
                        <div style="text-align: right;">
                            <span style="font-size: 0.8rem; color: #9c8c7e; background: #fdf6ed; padding: 4px 10px; border-radius: 10px; display: inline-block;">
                                <i class="fa-regular fa-calendar-days"></i> ${fechaTexto}
                            </span>
                            ${botonEliminar}
                        </div>
                    </div>
                    <p style="color: #5c4432; line-height: 1.5; margin: 0; font-style: italic;">
                        "${r.comentario}"
                    </p>
                </div>`;
        }).join("");

    } catch (error) {
        console.error("Error al cargar reseñas:", error);
    }
}

/**
 * Elimina una reseña
 */
async function eliminarResenya(idResenya, idProducto) {
    // Verificación de seguridad
    if (!idResenya) {
        console.error("Error: ID de reseña no válido.");
        return;
    }

    if (!confirm("¿Estás seguro de que deseas eliminar tu reseña?")) return;

    try {
        const response = await fetch(`/api/resenyas/${idResenya}`, { method: 'DELETE' });
        
        if (response.ok) {
            alert("Reseña eliminada.");
            cargarResenyas(idProducto); 
        } else {
            const errorData = await response.json().catch(() => ({}));
            alert(errorData.message || "No se pudo eliminar la reseña.");
        }
    } catch (error) {
        console.error("Error al eliminar:", error);
        alert("Error de conexión al intentar eliminar.");
    }
}

/**
 * Maneja el envío del formulario evitando duplicados.
 */
function configurarFormulario(idProducto) {
    const form = document.getElementById("form-resenya");
    if (!form) return;

    form.onsubmit = async (e) => {
        e.preventDefault();
        
        const botonEnvio = form.querySelector('button[type="submit"]');
        if (botonEnvio) botonEnvio.disabled = true;

        const data = {
            productoId: parseInt(idProducto),
            puntuacion: parseInt(form.puntuacion.value),
            comentario: form.comentario.value
        };

        try {
            const response = await fetch('/api/resenyas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                alert("¡Gracias por tu opinión!");
                form.reset();
                cargarResenyas(idProducto); 
            } else {
                const error = await response.json().catch(() => ({}));
                alert(error.message || "Error al publicar. Verifica que hayas comprado el producto.");
            }
        } catch (error) {
            console.error("Error en el envío:", error);
            alert("Error de conexión.");
        } finally {
            if (botonEnvio) botonEnvio.disabled = false;
        }
    };
}

/**
 * CRÍTICO: Exponer la función al objeto window.
 * Esto soluciona el problema de que el clic no haga nada si usas scripts de tipo módulo.
 */
window.eliminarResenya = eliminarResenya;