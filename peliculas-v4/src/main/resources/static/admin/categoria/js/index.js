async function cargarProductos() {

    const response = await fetch("/api/admin/productos");
    const productos = await response.json();

    const tabla = document.getElementById("tabla-body");
    tabla.innerHTML = "";

    productos.forEach(p => {

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>
                <input type="checkbox" class="check-item" value="${p.id}">
            </td>

            <td>${p.id}</td>
            <td>${p.nombre}</td>
       
        `;

        tabla.appendChild(tr);
    });
}

/* SELECT ALL */
document.getElementById("selectAll").addEventListener("change", function () {
    document.querySelectorAll(".check-item")
        .forEach(c => c.checked = this.checked);
});

/* OBTENER SELECCIONADOS */
function getSeleccionados() {
    return [...document.querySelectorAll(".check-item:checked")]
        .map(c => c.value);
}

/* ELIMINAR */
document.getElementById("eliminar").addEventListener("click", async () => {

    const ids = getSeleccionados();

    if (ids.length === 0) {
        alert("Seleccione al menos un producto");
        return;
    }

    if (!confirm("¿Desea eliminar los productos seleccionados?")) return;

    for (let id of ids) {
        await fetch(`/api/admin/productos/${id}`, {
            method: "DELETE"
        });
    }

    location.reload();
});

/* EDITAR */
document.getElementById("editar").addEventListener("click", () => {

    const ids = getSeleccionados();

    if (ids.length !== 1) {
        alert("Seleccione un único producto");
        return;
    }

    window.location.href = `editarProducto.html?id=${ids[0]}`;
});

/* CREAR */
document.getElementById("crear").addEventListener("click", () => {
    window.location.href = "crearProducto.html";
});

cargarProductos();