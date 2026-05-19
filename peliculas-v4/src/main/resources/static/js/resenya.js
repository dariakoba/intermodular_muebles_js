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
			console.log("RESEÑA:", r);
            const idResenya = r.idResenya || r.id_resena || r.id_resenya || r.id;
            const idUsuarioResenya = r.usuarioId || r.id_usuario;
            const nombreAutor = r.nombreUsuario || r.nombre_usuario || "Usuario";
            const fechaOriginal = r.fecha_publicacion;
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
                                         onclick="mostrarImagen('${img.url}')">
                                `).join("")}
                            </div>`;
                    }
                }
            } catch (err) {
                console.warn(`No se pudieron cargar imágenes para reseña ${idResenya}`);
            }

            // 2. Formatear fecha
			// 2. Formatear fecha REAL
			// 2. Formatear fecha REAL
			let fechaTexto = "Fecha no disponible";

			if (fechaOriginal) {

			    const fechaParseada = fechaOriginal.toString().replace(' ', 'T');

			    const d = new Date(fechaParseada);

			    if (!isNaN(d.getTime())) {

			        fechaTexto = d.toLocaleString('es-ES', {
			            day: '2-digit',
			            month: 'long',
			            year: 'numeric',
			            hour: '2-digit',
			            minute: '2-digit'
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
			const comentario = form.comentario.value.trim();

			if (!comentario) {
			    alert("Debes escribir una reseña.");
			    return;
			}

			const data = {
			    id_producto: parseInt(idProducto),
			    puntuacion: parseInt(form.puntuacion.value),
			    comentario: comentario
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
			// 4. SUBIR HASTA 5 IMÁGENES
			const fileInput = document.getElementById("file");

			if (fileInput && fileInput.files.length > 0) {

			    const files = Array.from(fileInput.files);

			    if (files.length > 5) {
			        alert("Solo puedes subir máximo 5 imágenes.");
			        return;
			    }

			    for (const file of files) {

			        const formData = new FormData();
			        formData.append("file", file);

			        const uploadRes = await fetch("/api/uploads/resenyas", {
			            method: "POST",
			            body: formData
			        });

			        if (!uploadRes.ok) {
			            throw new Error("Error al subir una imagen.");
			        }

			        const uploadData = await uploadRes.json();

			        const linkRes = await fetch(`/api/resenyas/${idNuevaResena}/imagenes`, {
			            method: "POST",
			            headers: {
			                "Content-Type": "application/json"
			            },
			            body: JSON.stringify({
			                url: uploadData.url
			            })
			        });

			        if (!linkRes.ok) {
			            throw new Error("Error al guardar imagen en BD.");
			        }
			    }
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
    const previewContainer = document.getElementById("preview-container");

    previewContainer.innerHTML = "";

    const files = fileInput.files;

    if (!files || files.length === 0) return;

    if (files.length > 5) {
        alert("Solo puedes subir máximo 5 imágenes.");
        fileInput.value = "";
        return;
    }

    Array.from(files).forEach(file => {

        const img = document.createElement("img");

        img.src = URL.createObjectURL(file);

        img.style.width = "120px";
        img.style.height = "90px";
        img.style.objectFit = "cover";
        img.style.borderRadius = "10px";
        img.style.border = "2px solid #ae4010";
        img.style.marginTop = "10px";

        previewContainer.appendChild(img);
    });
}

function mostrarImagen(url) {
    const modal = document.getElementById("imageModal");
    const img = document.getElementById("modalImg");

    img.src = url;
    modal.style.display = "flex";
}

function cerrarImagen() {
    document.getElementById("imageModal").style.display = "none";
}

// IMPORTANTE
window.mostrarImagen = mostrarImagen;
window.cerrarImagen = cerrarImagen;
// EXPOSICIÓN GLOBAL (CRÍTICO PARA onclick)
// Esto asegura que aunque el script sea de tipo módulo, el HTML pueda llamar a la función.
window.eliminarResenya = eliminarResenya;