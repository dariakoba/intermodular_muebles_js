import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {
    await guard.requireRole("admin");

    await cargarCategorias();

	const form = document.getElementById("form-create");
	const inputFile = document.getElementById("imagen");

	bind(form, "submit", guardar);
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



async function cargarCategorias() {
    const categorias = await api.get("/api/admin/categorias");

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

    const producto = await api.post("/api/admin/productos", {
        nombre: document.getElementById("nombre").value,
        color: document.getElementById("color").value,
        precio: parseFloat(document.getElementById("precio").value),
        stock: parseInt(document.getElementById("stock").value),
        descripcion: document.getElementById("descripcion").value,
        categoriaId: document.getElementById("categoria").value
            ? parseInt(document.getElementById("categoria").value)
            : null,
        deletedAt: document.getElementById("estado").value === "inactivo"
            ? new Date().toISOString()
            : null
    });
	
	console.log(producto);

    const file = document.getElementById("imagen").files[0];

    if (file) {
        const fd = new FormData();
        fd.append("file", file);

        await api.post(`/api/admin/productos/${producto.id_producto}/imagenes`, fd);
    }

    location.href = "index.html";
}




