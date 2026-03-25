// Simulación de productos 
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

renderizarCarrito();

function renderizarCarrito() {
    contenedor.innerHTML = '';

    if (carrito.length === 0) {
        contenedor.innerHTML = `...`;
     
        if (resumen) resumen.classList.add('oculto'); 
    } else {
        let total = 0;
        
        if (resumen) resumen.classList.remove('oculto');
      
    }
}

function renderizarCarrito() {
    const contenedor = document.getElementById('carrito-contenido');
    const resumen = document.getElementById('carrito-resumen');
    const totalHTML = document.getElementById('precio-total');

    // 1. Verificación de seguridad
    if (!contenedor) {
        console.error("No se encontró el contenedor 'carrito-contenido'");
        return;
    }

    let carrito = JSON.parse(localStorage.getItem('dna-carrito')) || [];
    contenedor.innerHTML = '';

    if (carrito.length === 0) {
        contenedor.innerHTML = `<p>Tu carrito está vacío.</p>`;
        if (resumen) resumen.style.display = 'none';
    } else {
        if (resumen) resumen.style.display = 'block';
        let total = 0;

        carrito.forEach((prod, index) => {
            total += prod.precio;
            contenedor.innerHTML += `
                <div class="item-carrito" style="border: 1px solid #ccc; margin: 10px; padding: 10px; display: flex; align-items: center; gap: 20px;">
                    <img src="${prod.image || prod.imagen}" width="100">
                    <div>
                        <h3>${prod.nombre}</h3>
                        <p>${prod.precio}€</p>
                        <button onclick="eliminarDelCarrito(${index})">Eliminar</button>
                    </div>
                </div>
            `;
        });
        
        if (totalHTML) totalHTML.innerText = total.toFixed(2);
    }
}