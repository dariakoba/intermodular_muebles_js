// Simulación de productos para que pruebes (esto vendrá del backend o de tus cards luego)
let carrito = JSON.parse(localStorage.getItem('dna-carrito')) || [];

const contenedor = document.getElementById('carrito-contenido');
const resumen = document.getElementById('carrito-resumen');
const totalHTML = document.getElementById('precio-total');

function renderizarCarrito() {
    contenedor.innerHTML = '';

    if (carrito.length === 0) {
        contenedor.innerHTML = `
            <div class="carrito-vacio">
                <p>Tu carrito está actualmente vacío.</p>
                <a href="home.html" class="btn-main" style="margin-top:20px; display:inline-block;">Volver a la tienda</a>
            </div>
        `;
        resumen.classList.add('oculto');
    } else {
        let total = 0;
        resumen.classList.remove('oculto');

        carrito.forEach((prod, index) => {
            total += prod.precio;
            contenedor.innerHTML += `
                <div class="item-carrito">
                    <img src="${prod.imagen}">
                    <div class="item-info">
                        <h3>${prod.nombre}</h3>
                        <p>${prod.precio.toFixed(2)}€</p>
                    </div>
                    <button class="btn-eliminar" onclick="eliminarDelCarrito(${index})">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </div>
            `;
        });
        totalHTML.innerText = total.toFixed(2) + "€";
    }
}

function eliminarDelCarrito(index) {
    carrito.splice(index, 1);
    localStorage.setItem('dna-carrito', JSON.stringify(carrito));
    renderizarCarrito();
}

function vaciarCarrito() {
    carrito = [];
    localStorage.removeItem('dna-carrito');
    renderizarCarrito();
}

// Ejecutar al cargar la página
renderizarCarrito();