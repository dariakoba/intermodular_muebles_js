/**
 * resenya.js - Gestión de reseñas para DNA Mobiliario
 */

document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    const idDesdeUrl = urlParams.get('id');
	const fileInput = document.getElementById("file");

	if (fileInput) {
	    fileInput.addEventListener("change", showLocalPreview);
	}
	
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
/**
 * Carga las reseñas y sus imágenes asociadas.
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

        // --- CAMBIO CLAVE: Promise.all para esperar las imágenes de cada reseña ---
        const tarjetasHTML = await Promise.all(resenyas.map(async (r) => {
            const idResenya = r.idResenya || r.id_resena || r.id_resenya || r.id;
            const idUsuarioResenya = r.usuarioId || r.id_usuario;
            const nombreAutor = r.nombreUsuario || r.nombre_usuario || "Usuario";
            const fechaOriginal = r.fechaPublicacion || r.fecha;
            const puntuacion = r.puntuacion || 0;
            const comentario = r.comentario || "";

            // 1. Obtener imágenes de esta reseña específica
            let imagenesHTML = "";
            try {
                const imgResponse = await fetch(`/api/resenyas/${idResenya}/imagenes`);
                if (imgResponse.ok) {
                    const imagenes = await imgResponse.json();
                    if (imagenes && imagenes.length > 0) {
                        imagenesHTML = `
                            <div class="resenya-imagenes" style="display: flex; flex-wrap: wrap; gap: 10px; margin-top: 15px;">
                                ${imagenes.map(img => `
                                    <img src="${img.url}" 
                                         alt="Foto de reseña" 
                                         style="width: 120px; height: 90px; object-fit: cover; border-radius: 8px; border: 1px solid #ddd; cursor: pointer;"
                                         onclick="window.location.href='imagen.html?url=${img.url}'">
                                `).join("")}
                            </div>`;
                    }
                }
            } catch (err) {
                console.warn(`No se pudieron cargar imágenes para reseña ${idResenya}`);
            }

            // 2. Formatear fecha
            let fechaTexto = "Reciente";
            if (fechaOriginal) {
                const d = new Date(fechaOriginal);
                if (!isNaN(d.getTime())) {
                    fechaTexto = d.toLocaleDateString('es-ES', { 
                        day: '2-digit', month: 'long', year: 'numeric' 
                    });
                }
            }

            // 3. Botón eliminar
            let botonEliminar = "";
            if (userIdLogueado && idUsuarioResenya && idUsuarioResenya == userIdLogueado) {
                botonEliminar = `
                    <button onclick="eliminarResenya(${idResenya}, ${id})" class="btn-eliminar-resenya">
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
                                ${"★".repeat(puntuacion)}${"☆".repeat(5 - puntuacion)}
                            </div>
                        </div>
                        <div class="resenya-actions">
                            <span class="resenya-fecha">
                                <i class="fa-regular fa-calendar-days"></i> ${fechaTexto}
                            </span>
                            ${botonEliminar}
                        </div>
                    </div>
                    <p style="color: #5c4432; line-height: 1.5; margin: 0; font-style: italic;">
                        "${comentario}"
                    </p>
                    ${imagenesHTML} 
                </div>`;
        }));

        contenedor.innerHTML = tarjetasHTML.join("");

    } catch (error) {
        console.error("Error al cargar reseñas:", error);
    }
}

/**
 * Elimina una reseña
 */
async function eliminarResenya(idResenya, idProducto) {
    if (!idResenya || idResenya === "undefined") {
        console.error("Error: ID de reseña no válido detectado:", idResenya);
        alert("No se pudo obtener el identificador de la reseña.");
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
 * Maneja el envío del formulario.
 */
/**
 * Maneja el envío del formulario con creación de reseña y subida de imagen.
 */
/**
 * Maneja el envío del formulario.
 */
function configurarFormulario(idProducto) {
    const form = document.getElementById("form-resenya");
    if (!form) return;

    form.onsubmit = async (e) => {
        e.preventDefault();
        
        const botonEnvio = form.querySelector('button[type="submit"]');
        if (botonEnvio) botonEnvio.disabled = true;

        try {
            // 1. DATOS - Usamos los nombres exactos que espera tu Entity/DTO
			// En resenya.js, dentro de configurarFormulario:
			const data = {
			    id_producto: parseInt(idProducto), // Asegúrate que sea id_producto para que Java lo entienda
			    puntuacion: parseInt(form.puntuacion.value),
			    comentario: form.comentario.value
			    // quitamos usuarioId de aquí, es más seguro
			};

            // 2. CREAR RESEÑA
            const response = await fetch('/api/resenyas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
			

			if (!response.ok) {
			    const mensaje = await response.text(); // 👈 aquí coges el texto del backend
			    alert(mensaje); // 👈 aquí lo muestras
			    return;
			}
            
            
            const resenaCreada = await response.json();
            
            // 3. ID - Usamos id_resenya (con 'y' y snake_case) según tu log de consola
            const idNuevaResena = resenaCreada.id_resenya;

            if (!idNuevaResena) {
                console.error("Respuesta incompleta del servidor:", resenaCreada);
                throw new Error("El servidor no devolvió el id_resenya");
            }

            // 4. SUBIR IMAGEN (Solo si hay archivo seleccionado)
            const fileInput = document.getElementById("file");
            if (fileInput && fileInput.files[0]) {
                const formData = new FormData();
                formData.append("file", fileInput.files[0]);

                // CORRECCIÓN: URL con 'y' para que coincida con la whitelist del backend
                const uploadRes = await fetch("/api/uploads/resenyas", {
                    method: "POST",
                    body: formData
                });

                if (!uploadRes.ok) throw new Error("Error al subir el archivo físico.");
                
                const uploadData = await uploadRes.json();

                // 5. VINCULAR IMAGEN
                const linkRes = await fetch(`/api/resenyas/${idNuevaResena}/imagenes`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ url: uploadData.url })
                });

                if (!linkRes.ok) throw new Error("Error al vincular la imagen en la base de datos.");
            }

            alert("¡Reseña publicada con éxito!");
            window.location.reload();

        } catch (error) {
            console.error("Flujo interrumpido:", error);
            alert("Hubo un problema: " + error.message);
        } finally {
            if (botonEnvio) botonEnvio.disabled = false;
        }
    };
}

/**
 * Función de preview segura
 */
function showLocalPreview() {
    const fileInput = document.getElementById("file");
    const preview = document.getElementById("preview");
    if (!preview) return; // Protección contra null

    const file = fileInput.files[0];
    if (!file) {
        preview.style.display = "none";
        preview.src = "";
        return;
    }

    preview.src = URL.createObjectURL(file);
    preview.style.display = "block";
}

// EXPOSICIÓN GLOBAL (CRÍTICO PARA onclick)
// Esto asegura que aunque el script sea de tipo módulo, el HTML pueda llamar a la función.
window.eliminarResenya = eliminarResenya;