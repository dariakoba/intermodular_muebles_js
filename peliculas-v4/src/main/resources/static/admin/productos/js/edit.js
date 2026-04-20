import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {
    await guard.requireRole("admin");

    const id = obtenerId();
    if (!id) {
        location.href = "index.html";
        return;
    }

    const producto   = await api.get(`/api/admin/productos/${id}`);
    const categorias = await api.get("/api/admin/categorias");

    render(producto, categorias);
    bindEvents();
});

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function render(producto, categorias) {
    const form   = document.getElementById("form-producto");
    const select = document.getElementById("categoria");

    select.innerHTML = '<option value="">-- Selecciona categoría --</option>';
    categorias.forEach(c => {
        const option = document.createElement("option");
        option.value = c.id_categoria;
        option.textContent = c.nombre;
        select.appendChild(option);
    });

	
	
    form.nombre.value      = producto.nombre;
    form.color.value       = producto.color;
    form.precio.value      = producto.precio;
    form.stock.value       = producto.stock;
    form.descripcion.value = producto.descripcion;
    form.categoria.value   = producto.categoria_id;
	form.estado.value = producto.deleted_at ? "inactivo" : "activo";
}

function bindEvents() {
    const form = document.getElementById("form-producto");
	console.log("form encontrado:", form); 

    bind(form, "submit", guardar);
}

async function guardar(e) {
    e.preventDefault();

    const id   = obtenerId();
    const form = e.target;
	console.log("nombre:",      form.nombre.value);
	console.log("color:",       form.color.value);
	console.log("precio:",      form.precio.value);
	console.log("stock:",       form.stock.value);
	console.log("descripcion:", form.descripcion.value);
	console.log("categoria:",   form.categoria.value);
	console.log("estado:",   form.estado.value);

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

    location.replace("index.html");
}