async function cargarProductos() {

    const response = await fetch("/api/productos");
    const productos = await response.json();

    const contenedor = document.getElementById("productos-container");

    productos.forEach(p => {

        const card = document.createElement("div");

        // clase del CSS de las cards
        card.classList.add("card");

        card.innerHTML =
        `<img src="${p.imagen}" alt="${p.nombre}">

        <div class="card-body">

            <h3>${p.nombre}</h3>

            <p class="precio">${p.precio}€</p>

            <a href="producto.html?id=${p.id}">
                <button>Ver producto</button>
            </a>

        </div>`;

        contenedor.appendChild(card);
    });
}

cargarProductos();