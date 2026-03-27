/*
async function cargarProductos() {
    const response = await fetch("/api/productos");
    const productos = await response.json();
    console.log(productos);
    const contenedor = document.getElementById("productos-container");

    productos.forEach(p => {
        const card = document.createElement("div");
        card.classList.add("card");

        card.innerHTML = `
            <img src="images/productos/${p.id}.jpg" alt="${p.nombre}">

            <div class="card-body">
                <h3>${p.nombre}</h3>
                <p class="descripcion">${p.descripcion}</p>

                <p class="precio">${p.precio}€</p>

                <a href="productosShow.html?id=${p.id}">
                    <button>Ver producto</button>
                </a>
            </div>
        `;

        contenedor.appendChild(card);
    });
}

cargarProductos();
*/

//pruebas
async function cargarProductos() {
    const response = await fetch("/api/productos");
    const productos = await response.json();
	console.log(productos)
    renderProductos(productos);
}

function renderProductos(productos) {
    const contenedor = document.getElementById("productos-container");
    contenedor.innerHTML = "";

    if (productos.length === 0) {
        contenedor.innerHTML = `<p style="color:#999; padding:20px;">No se encontraron productos.</p>`;
        return;
    }

    productos.forEach(p => {
        const card = document.createElement("div");
        card.classList.add("card");
        card.innerHTML = `
            <img src="/images/productos/${p.id_producto}.jpg" alt="${p.nombre}">
            <div class="card-body">
                <h3>${p.nombre}</h3>
                <p class="descripcion">${p.descripcion}</p>
                <p class="precio">${p.precio}€</p>
                <a href="productosShow.html?id=${p.id_producto}">
                    <button>Ver producto</button>
                </a>
            </div>
        `;
        contenedor.appendChild(card);
    });
}

async function aplicarFiltros() {
    const params = new URLSearchParams();

    const categorias = [...document.querySelectorAll(".filter-options input[type='checkbox']:checked")]
        .map(cb => cb.value);
    if (categorias.length > 0) {
        params.append("categoria", categorias[0]);
    }

    const inputs = document.querySelectorAll(".filter-options input[type='number']");
    const precioMin = inputs[0].value;
    const precioMax = inputs[1].value;
    if (precioMin) params.append("precioMin", precioMin);
    if (precioMax) params.append("precioMax", precioMax);

    const orden = document.getElementById("ordenar").value;
    params.append("orden", orden);

    const response = await fetch(`/api/productos?${params.toString()}`);
    const productos = await response.json();
    renderProductos(productos);
}

document.getElementById("aplicar-filtros").addEventListener("click", aplicarFiltros);

document.getElementById("ordenar").addEventListener("change", aplicarFiltros);

document.querySelectorAll(".filter-header").forEach(header => {
    header.addEventListener("click", () => {
        const options = header.nextElementSibling;
        const toggle = header.querySelector(".toggle");
        const abierto = options.style.display !== "none";
        options.style.display = abierto ? "none" : "block";
        toggle.textContent = abierto ? "+" : "−";
    });
});

cargarProductos();