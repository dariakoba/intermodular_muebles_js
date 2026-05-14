function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function renderImagenes(imagenes) {

    if (!imagenes || imagenes.length === 0) return;

    const contenedor = document.getElementById("imagenes");
    contenedor.innerHTML = "";

    const principal = document.createElement("img");
    principal.src = imagenes[0].url;
    principal.style.width = "300px";

    contenedor.appendChild(principal);

    imagenes.slice(1).forEach(img => {
        const el = document.createElement("img");
        el.src = img.url;
        el.style.width = "100px";
        contenedor.appendChild(el);
    });
}


async function cargarProducto() {

    const id = obtenerId();

    const p = await fetch(`/api/admin/productos/${id}`).then(r => r.json());
	console.log(p);
    document.getElementById("nombre").textContent = p.nombre;
    document.getElementById("id").textContent = p.id_producto;
    document.getElementById("categoria").textContent = p.categoria_id;
    document.getElementById("color").textContent = p.color;
    document.getElementById("precio").textContent = p.precio + " €";
    document.getElementById("stock").textContent = p.stock;
    document.getElementById("descripcion").textContent = p.descripcion;

    document.getElementById("estado").textContent = p.deleted_at ? "Inactivo" : "Activo";

    document.getElementById("btn-editar").href = `edit.html?id=${id}`;

    renderImagenes(p.imagenes);
}

function abrirModal(url) {
    const modal = document.getElementById("modal");
    const img = document.getElementById("modal-img");

    img.src = url;
    modal.style.display = "flex";
}

function cerrarModal() {
    document.getElementById("modal").style.display = "none";
}

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

cargarProducto();