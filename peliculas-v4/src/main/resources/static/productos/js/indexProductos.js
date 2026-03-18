async function cargarProductos() {

    const response = await fetch("/api/productos");
    const productos = await response.json();
	console.log(productos);
    const contenedor = document.getElementById("productos-container");

    productos.forEach(p => {

        const card = document.createElement("div");
        card.classList.add("card");

        card.innerHTML =
        `<img src="" alt="${p.nombre}">

        <div class="card-body">

            <h3>${p.nombre}</h3>
			<p class="descripcion">${p.descripcion}</p>

            <p class="precio">${p.precio}€</p>

            <a href="producto.html?id=${p.id}">
                <button>Ver producto</button>
            </a>

        </div>`;

        contenedor.appendChild(card);
    });
}

cargarProductos();