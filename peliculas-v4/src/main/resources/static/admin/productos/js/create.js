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
    const categorias = await api.get("/api/admin/categoria");

    const select = document.getElementById("categoria");

    categorias.forEach(c => {
        const option = document.createElement("option");
        option.value = c.id_categoria;
        option.textContent = c.nombre;
        select.appendChild(option);
    });
}

async function guardar(e) {
    e.preventDefault();

    const producto = {
        nombre:      document.getElementById("nombre").value,
        color:       document.getElementById("color").value,
        precio:      parseFloat(document.getElementById("precio").value),
        stock:       parseInt(document.getElementById("stock").value),
        descripcion: document.getElementById("descripcion").value,
        categoriaId: parseInt(document.getElementById("categoria").value) || 0
    };

    await api.post("/api/admin/productos", producto);

    location.href = "index.html";
}