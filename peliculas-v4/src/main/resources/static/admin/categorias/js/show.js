function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarProducto() {
    const id = obtenerId();

    const response = await fetch(`/api/admin/productos/${id}`);
    const p = await response.json();

    document.getElementById("nombre").textContent = p.nombre;
    document.getElementById("id").textContent = p.id_producto;
    document.getElementById("categoria").textContent = p.categoria_id;
    document.getElementById("color").textContent = p.color;
    document.getElementById("precio").textContent = p.precio + " €";
    document.getElementById("stock").textContent = p.stock;
    document.getElementById("descripcion").textContent = p.descripcion;

    document.getElementById("btn-editar").href = `edit.html?id=${id}`;
}

cargarProducto();