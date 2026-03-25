// ======================
// Cargar Categorías en el select
// ======================
async function cargarCategorias() {
    try {
        const response = await fetch("/api/admin/categorias"); // endpoint de categorías
        const categorias = await response.json();

        const select = document.getElementById("categoria");
        select.innerHTML = `<option value="">--Selecciona una categoría--</option>`;

        categorias.forEach(c => {
            const option = document.createElement("option");
            option.value = c.id_categoria; // usa snake_case como en la DB
            option.textContent = c.nombre;
            select.appendChild(option);
        });
    } catch (err) {
        console.error("Error cargando categorías:", err);
    }
}

// ======================
// Cargar Productos y mostrar en tabla
// ======================
async function cargarProductos() {
    try {
        const response = await fetch("/api/admin/productos");
        const productos = await response.json();
        console.log("Productos recibidos:", productos); // debug

        const tbody = document.querySelector("#tablaProductos tbody");
        tbody.innerHTML = "";

        productos.forEach(p => {
            const fila = `
            <tr>
                <td>${p.id_producto}</td>
                <td>${p.nombre}</td>
				<td>${p.color}</td>
                <td>${p.precio}€</td>
                <td>${p.stock}</td>
                <td>${p.categoria_nombre ?? ""}</td>
                <td>
                    <button onclick="editarProducto(${p.id_producto})">Editar</button>
                    <button onclick="borrarProducto(${p.id_producto})">Borrar</button>
                </td>
            </tr>`;
            tbody.innerHTML += fila;
        });
    } catch (err) {
        console.error("Error cargando productos:", err);
    }
}

// ======================
// Guardar Producto (crear o actualizar)
// ======================
async function guardarProducto() {
    const id = document.getElementById("id").value;

    const producto = {
        nombre: document.getElementById("nombre").value,
        color: document.getElementById("color").value,
        precio: parseFloat(document.getElementById("precio").value),
        stock: parseInt(document.getElementById("stock").value),
        descripcion: document.getElementById("descripcion").value,
        categoria_id: parseInt(document.getElementById("categoria").value) || null
    };

    if (id) {
        await fetch(`/api/admin/productos/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(producto)
        });
    } else {
        await fetch("/api/admin/productos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(producto)
        });
    }

    limpiarFormulario();
    cargarProductos();
}

// ======================
// Borrar Producto
// ======================
async function borrarProducto(id) {
    if (!confirm("¿Borrar producto?")) return;

    await fetch(`/api/admin/productos/${id}`, {
        method: "DELETE"
    });

    cargarProductos();
}

// ======================
// Editar Producto
// ======================
async function editarProducto(id) {
    const response = await fetch(`/api/admin/productos/${id}`);
    const p = await response.json();

    document.getElementById("id").value = p.id_producto;
    document.getElementById("nombre").value = p.nombre;
    document.getElementById("color").value = p.color;
    document.getElementById("precio").value = p.precio;
    document.getElementById("stock").value = p.stock;
    document.getElementById("descripcion").value = p.descripcion;
    document.getElementById("categoria").value = p.categoria_id;

    document.getElementById("formTitle").innerText = "Editar Producto";
}

// ======================
// Limpiar formulario
// ======================
function limpiarFormulario() {
    document.getElementById("id").value = "";
    document.getElementById("nombre").value = "";
    document.getElementById("color").value = "";
    document.getElementById("precio").value = "";
    document.getElementById("stock").value = "";
    document.getElementById("descripcion").value = "";
    document.getElementById("categoria").value = "";

    document.getElementById("formTitle").innerText = "Crear Producto";
}

// ======================
// Inicialización
// ======================
cargarCategorias();
cargarProductos();