import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {
    await guard.requireRole("admin");

    await cargarCategorias();

    bind(document.getElementById("form-create"), "submit", guardar);
});

async function cargarCategorias() {
    const categorias = await api.get("/api/admin/categorias");


}

async function guardar(e) {
    e.preventDefault();
	const estadoVal = document.getElementById("estado").value;

    const c = {
        nombre:      document.getElementById("nombre").value,
		deleted_at:   estadoVal === "inactivo" ? new Date().toISOString() : null
  
    };
	console.log(c);
    await api.post("/api/admin/categorias", c);
	
    location.href = "index.html";
}