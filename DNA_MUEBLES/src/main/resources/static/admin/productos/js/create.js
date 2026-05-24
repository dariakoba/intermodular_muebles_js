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

//VALIDACION
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
    const color = document.getElementById("color").value;
    const precioRaw = document.getElementById("precio").value;



    // NOMBRE
    if (nombre.length < 2) {
        error("error-nombre", "El nombre debe tener al menos 2 caracteres");
        ok = false;
    }



    // COLOR
    if (!color) {
        error("error-color", "Debes seleccionar un color");
        ok = false;
    }



    // PRECIO
    const precio = parseFloat(precioRaw);

    if (precioRaw === "") {
        error("error-precio", "El precio es obligatorio");
        ok = false;

    } else if (isNaN(precio)) {
        error("error-precio", "El precio debe ser un número válido");
        ok = false;

    } else if (precio < 0) {
        error("error-precio", "El precio no puede ser negativo");
        ok = false;
    }



    // STOCK
	const stockRaw = document.getElementById("stock").value;

	if (stockRaw === "") {
	    error("error-stock", "El stock es obligatorio");
	    ok = false;

	} else if (!/^\d+$/.test(stockRaw)) {
	    error("error-stock", "El stock debe ser un número entero sin decimales");
	    ok = false;

	} else {
	    const stock = parseInt(stockRaw, 10);

	    if (stock < 0) {
	        error("error-stock", "El stock no puede ser negativo");
	        ok = false;
	    }
	}


	const descripcion = document.getElementById("descripcion").value;

	if (descripcion !== "" && descripcion.trim() === "") {
	    error("error-descripcion", "La descripción no puede contener solo espacios en blanco");
	    ok = false;
	}

    return ok;
}




async function guardar(e) {
    e.preventDefault();
	
	//
	if (!validar()) return;

	//
	
	
    const producto = await api.post("/api/admin/productos", {
        nombre: document.getElementById("nombre").value,
        color: document.getElementById("color").value,
        precio: parseFloat(document.getElementById("precio").value),
        stock: parseInt(document.getElementById("stock").value),
        descripcion: document.getElementById("descripcion").value,
        categoria_id: document.getElementById("categoria").value
            ? parseInt(document.getElementById("categoria").value)
            : null,
        deleted_at: document.getElementById("estado").value === "inactivo"
            ? new Date().toISOString()
            : null
    });
	console.log("PAYLOAD:", JSON.stringify(producto)); // <-- aquí

	console.log(producto);

    const file = document.getElementById("imagen").files[0];

    if (file) {
        const fd = new FormData();
        fd.append("file", file);

        await api.post(`/api/admin/productos/${producto.id_producto}/imagenes`, fd);
    }
	alert("Producto creado");


    location.href = "index.html";
}




