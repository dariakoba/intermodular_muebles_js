// 1. Obtener elementos del DOM una sola vez
const contenedor = document.getElementById('carrito-contenido');
const resumen = document.getElementById('carrito-resumen');
const totalHTML = document.getElementById('precio-total');

// 2. Obtener carrito (siempre sincronizado)
function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('dna-carrito')) || [];
}

function renderizarCarrito() {
    if (!contenedor) return;

    const carrito = obtenerCarrito();
    contenedor.innerHTML = ''; // Limpiar antes de dibujar

    if (carrito.length === 0) {
        contenedor.innerHTML = `<div class="carrito-vacio"><p>Tu carrito está vacío.</p></div>`;
        if (resumen) resumen.style.display = 'none';
        return;
    }

    if (resumen) resumen.style.display = 'block';
    let total = 0;

    // Usamos map para crear todos los items de una vez y unirlos con .join('')
    // Es más limpio que usar += dentro del bucle
    contenedor.innerHTML = carrito.map((prod, index) => {
        total += prod.precio;
        return `
            <div class="item-carrito">
                <img src="${prod.imagen || prod.image}" width="100">
                <div class="item-info">
                    <h3>${prod.nombre}</h3>
                    <p>${prod.precio.toFixed(2)}€</p>
                </div>
                <button class="btn-eliminar" onclick="eliminarDelCarrito(${index})">
                    <i class="fa-solid fa-trash"></i> Eliminar
                </button>
            </div>
        `;
    }).join('');

    if (totalHTML) totalHTML.innerText = total.toFixed(2) + "€";
}

// 3. Funciones de gestión
function eliminarDelCarrito(index) {
    let carrito = obtenerCarrito();
    carrito.splice(index, 1);
    localStorage.setItem('dna-carrito', JSON.stringify(carrito));
    renderizarCarrito();
}

// --- LOGICA DE COMPRA (CONEXIÓN CON JAVA) ---
async function finalizarCompra() {
    const carrito = obtenerCarrito();
    if (carrito.length === 0) return alert("El carrito está vacío");

    // Preparamos el objeto EXACTAMENTE como lo espera tu CarritoRequest en Java
    const datosCompra = {
        pedido: {
            fechaPedido: new Date().toISOString().split('T')[0],
            precio: parseFloat(totalHTML.innerText),
            metodoPago: "Tarjeta", // Esto podrías sacarlo de un input radio
            envio: "Domicilio",
            idCliente: 2 // Aquí deberías poner el ID del usuario logueado
        },
        // Extraemos solo los IDs de los ejemplares del carrito
        idsEjemplares: carrito.map(prod => prod.id) 
    };

    try {
        const response = await fetch('/api/carrito/comprar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datosCompra)
        });

        if (response.ok) {
            alert("¡Gracias por tu compra!");
            localStorage.removeItem('dna-carrito'); // Vaciar tras comprar
            window.location.href = "index.html"; // Redirigir
        } else {
            alert("Error al procesar el pedido.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("No se pudo conectar con el servidor.");
    }
}

// Inicializar al cargar la página
renderizarCarrito();