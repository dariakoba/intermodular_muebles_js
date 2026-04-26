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

    const categoria   = await api.get(`/api/admin/categorias/${id}`);

    render(categoria);
    bindEvents();
});

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function render(c) {
    const form   = document.getElementById("form-producto");
   
    form.nombre.value      = c.nombre;
	form.estado.value = c.deleted_at ? "inactivo" : "activo";
}

function bindEvents() {
    const form = document.getElementById("form-producto");
	console.log("form encontrado:", form); // <-es null?

    bind(form, "submit", guardar);
}

async function guardar(e) {
    e.preventDefault();
	 
    const id   = obtenerId();
    const form = e.target;
	console.log("nombre:",      form.nombre.value);
	console.log("estado:",       form.estado.value);
	const deleted_at = form.estado.value === "inactivo"
			       ? new Date().toISOString()
			       : null;
				  

    await api.put(`/api/admin/categorias/${id}`, {
        nombre:       form.nombre.value,
        deleted_at:        deleted_at
  
    });

    location.replace("index.html");
}