import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {
	document.getElementById("cerrar-modal").onclick = cerrarModal;

	document.getElementById("modal").onclick = (e) => {
	    if (e.target.id === "modal") cerrarModal();
	};
    await guard.requireRole("admin");

    const id = obtenerId();
    if (!id) {
        location.href = "index.html";
        return;
    }

    const producto   = await api.get(`/api/admin/productos/${id}`);
    const categorias = await api.get("/api/admin/categorias");

    render(producto, categorias);

    bind(document.getElementById("form-producto"), "submit", guardar);

    bind(document.getElementById("imagen"), "change", handleUpload);
});
function abrirModal(url) {
    const modal = document.getElementById("modal");
    const img = document.getElementById("modal-img");

    img.src = url;
    modal.style.display = "flex";
}

function cerrarModal() {
    document.getElementById("modal").style.display = "none";
}
function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

/* ---------------- RENDER ---------------- */

function render(producto, categorias) {

    const form   = document.getElementById("form-producto");
    const select = document.getElementById("categoria");

    // categorías
    select.innerHTML = '<option value="">-- Selecciona categoría --</option>';

    categorias.forEach(c => {
        const option = document.createElement("option");
        option.value = c.id_categoria;
        option.textContent = c.nombre;
        select.appendChild(option);
    });

    // campos producto
    form.nombre.value      = producto.nombre;
    form.color.value       = producto.color;
    form.precio.value      = producto.precio;
    form.stock.value       = producto.stock;
    form.descripcion.value = producto.descripcion;
    form.categoria.value   = producto.categoria_id;
    form.estado.value      = producto.deleted_at ? "inactivo" : "activo";

    // imágenes
    renderImagenes(producto.imagenes);
}

/* ---------------- IMÁGENES ---------------- */

function renderImagenes(imagenes) {

    const galeria = document.getElementById("galeria");
    galeria.innerHTML = "";

    if (!imagenes || !imagenes.length) return;

    imagenes.forEach(img => {

        const wrapper = document.createElement("div");

		wrapper.innerHTML = `
		    <img src="${img.url}" class="mini-img">

		    <button class="btn-eliminar" data-action="eliminar" data-id="${img.id}">
		        Eliminar
		    </button>
		`;
		const imgEl = wrapper.querySelector("img");

		imgEl.onclick = () => abrirModal(img.url);

        galeria.appendChild(wrapper);
    });

    bind(galeria, "click", onEliminar);
}

/* ---------------- SUBMIT UPDATE ---------------- */

async function guardar(e) {

    e.preventDefault();

    const id = obtenerId();

    const form = e.target;

    const deleted_at = form.estado.value === "inactivo"
        ? new Date().toISOString()
        : null;

    await api.put(`/api/admin/productos/${id}`, {
        nombre:       form.nombre.value,
        color:        form.color.value,
        precio:       form.precio.value,
        stock:        form.stock.value,
        descripcion:  form.descripcion.value,
        categoria_id: form.categoria.value,
        deleted_at:   deleted_at
    });

    // opcional: recargar datos como tu profe
    const producto   = await api.get(`/api/admin/productos/${id}`);
    const categorias = await api.get("/api/admin/categorias");

    render(producto, categorias);
}

async function handleUpload() {

    const id = obtenerId();
    const file = document.getElementById("imagen").files[0];

    if (!file) return;

    const fd = new FormData();
    fd.append("file", file);

    await api.post(`/api/admin/productos/${id}/imagenes`, fd);

    const producto   = await api.get(`/api/admin/productos/${id}`);
    const categorias = await api.get("/api/admin/categorias");

    render(producto, categorias);
}

/* ---------------- ELIMINAR IMAGEN ---------------- */

async function onEliminar(e) {

    const btn = e.target.closest("[data-action]");
    if (!btn) return;

    if (!confirm("¿Eliminar imagen?")) return;

    const idImagen = btn.dataset.id;
    const productoId = obtenerId();

    await api.delete(`/api/admin/productos/${productoId}/imagenes/${idImagen}`);

    const producto = await api.get(`/api/admin/productos/${productoId}`);
    const categorias = await api.get("/api/admin/categorias");

    render(producto, categorias);
}