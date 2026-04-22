import { app } from "/js/core/app.js";
import { api } from "/js/core/api.js";
import { bind } from "/js/core/events.js";

app.run(async () => {

    const id = obtenerId();

    const d = await api.get(`/api/admin/directores/${id}`);

    render(d);

    bind(document.getElementById("form-director"), "submit", handleSubmit);
    bind(document.getElementById("imagen"), "change", handleUpload);
});

function render(d) {
    renderForm(d);
    renderImagenes(d.imagenes);
}

function renderForm(d) {
	document.getElementById("id").value = d.id;
	document.getElementById("nombre").value = d.nombre;
	document.getElementById("pais").value = d.pais;
}

function renderImagenes(imagenes) {

    const galeria = document.getElementById("galeria");
    galeria.innerHTML = "";

    imagenes.forEach(img => {

        const wrapper = document.createElement("div");

        wrapper.innerHTML = `
            <img src="${img.url}" style="height:120px;display:block">
            <button data-action="eliminar" data-id="${img.id}">
                Eliminar
            </button>
        `;

        galeria.appendChild(wrapper);
    });

    bind(galeria, "click", onEliminar);
}

async function handleSubmit(e) {

    e.preventDefault();

    const id = document.getElementById("id").value;

    await api.put(`/api/admin/directores/${id}`, {
        nombre: document.getElementById("nombre").value,
        pais: document.getElementById("pais").value
    });

    location.href = "/admin/directores/index.html";
}

async function handleUpload() {

    const id = document.getElementById("id").value;
    const file = document.getElementById("imagen").files[0];

    if (!file) return;

    const fd = new FormData();
    fd.append("file", file);

    await api.post(`/api/admin/directores/${id}/imagenes`, fd);

    const d = await api.get(`/api/admin/directores/${id}`);
    render(d);
}

async function onEliminar(e) {

    const btn = e.target.closest("[data-action]");
    if (!btn) return;

    if (!confirm("¿Eliminar imagen?")) return;

    const idImagen = btn.dataset.id;
    const directorId = document.getElementById("id").value;

    await api.delete(`/api/admin/directores/${directorId}/imagenes/${idImagen}`);

    const d = await api.get(`/api/admin/directores/${directorId}`);
    render(d);
}

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}