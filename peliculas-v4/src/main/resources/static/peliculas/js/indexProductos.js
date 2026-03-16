async function cargarProductos(){

    const response = await fetch("/api/productos");
    const productos = await response.json();

    const grid = document.getElementById("productos-grid");

    productos.forEach(p => {

        const card = document.createElement("div");
        card.classList.add("producto-card");

        card.innerHTML = `
            <img class="producto-img" src="/images/producto/default.png">

            <div class="producto-nombre">
                ${p.nombre}
            </div>

            <div class="producto-precio">
                ${p.precio} €
            </div>

            <button class="btn-comprar">
                Añadir al carrito
            </button>
        `;

        grid.appendChild(card);
    });

}

cargarProductos();