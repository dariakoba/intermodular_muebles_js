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

//validacion
function limpiarErrores() {
    document.querySelectorAll(".error").forEach(e => e.textContent = "");
}
function error(id, msg) {
    const el = document.getElementById(id);

    if (!el) {
        console.warn("No existe el error:", id);
        return;
    }

    el.textContent = msg;
}
function validar() {

    limpiarErrores();

    let ok = true;

    const nombre = document.getElementById("nombre").value.trim();


    // NOMBRE
    if (nombre.length < 2) {
        error("error-nombre", "El nombre debe tener al menos 2 caracteres");
        ok = false;
    }
	return ok;

}
//

async function guardar(e) {
    e.preventDefault();
	if (!validar()) return;

		
	const estadoVal = document.getElementById("estado").value;

    const c = {
        nombre:      document.getElementById("nombre").value,
		deleted_at:   estadoVal === "inactivo" ? new Date().toISOString() : null
  
    };
	console.log(c);
    await api.post("/api/admin/categorias", c);
	alert("Categoria creada");
    location.href = "index.html";
}