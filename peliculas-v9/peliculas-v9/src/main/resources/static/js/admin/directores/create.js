import { app } from "/js/core/app.js";
import { api } from "/js/core/api.js";
import { bind } from "/js/core/events.js";

app.run(() => {

    const form = document.getElementById("form-director");
    const inputFile = document.getElementById("imagen");

    bind(form, "submit", handleSubmit);
    bind(inputFile, "change", previewImagen);
});

function previewImagen() {
    const file = document.getElementById("imagen").files[0];
    const preview = document.getElementById("preview");

	if (!file) {
		preview.style.display = "none";
		preview.src = "";
		return;
	}

    preview.src = URL.createObjectURL(file);
    preview.style.display = "block";
}

async function handleSubmit(e) {

    e.preventDefault();

	const director = await api.post("/api/admin/directores", {
		nombre: document.getElementById("nombre").value,
		pais: document.getElementById("pais").value,
	});

    const file = document.getElementById("imagen").files[0];

    // subir imagen (si existe)
    if (file) {
		const fd = new FormData();
        fd.append("file", file);
		
		await api.post(`/api/admin/directores/${director.id}/imagenes`, fd);
    }

    location.href = "/admin/directores/index.html";
}