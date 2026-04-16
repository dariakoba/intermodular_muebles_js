const contenedorCarrito = document.getElementById('carrito-contenido') || document.getElementById('lista-carrito');
const totalHTML = document.getElementById('precio-total') || document.getElementById('total-precio');

function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('carrito')) || [];
}

function renderizarCarrito() {
    if (!contenedorCarrito) return;
    const carrito = obtenerCarrito();
    
    if (carrito.length === 0) {
        contenedorCarrito.innerHTML = `<p class="vacio">Tu carrito está vacío.</p>`;
        if (totalHTML) totalHTML.innerText = "0.00€";
        return;
    }

    let total = 0;

    contenedorCarrito.innerHTML = carrito.map((prod, index) => {
        const cantidad = prod.cantidad || 1;
        const subtotal = prod.precio * cantidad;
        total += subtotal;
        
        return `
            <div class="carrito-item" style="display: flex; align-items: center; justify-content: space-between; padding: 15px; border-bottom: 1px solid #eee;">
                <div class="producto-info">
                    <h3>${prod.nombre}</h3>
                    <p>${prod.precio}€ c/u</p>
                </div>
                
                <div style="display: flex; align-items: center; gap: 15px;">
                    <input type="number" value="${cantidad}" min="1" 
                           style="width: 50px; padding: 5px; text-align: center;"
                           onchange="actualizarCantidad(${index}, this.value)">
                    
                    <p><b>${subtotal.toFixed(2)}€</b></p>
                    
                    <button onclick="eliminarDelCarrito(${index})" style="color: red; cursor: pointer; border: none; background: none; font-size: 1.2rem;">
                        🗑️
                    </button>
                </div>
            </div>
        `;
    }).join('');

    if (totalHTML) totalHTML.innerText = total.toFixed(2) + "€";
}

// Función nueva para cambiar la cantidad desde el carrito
function actualizarCantidad(index, nuevaCant) {
    let carrito = obtenerCarrito();
    let cant = parseInt(nuevaCant);
    if (cant < 1) cant = 1; 
    
    carrito[index].cantidad = cant;
    localStorage.setItem('carrito', JSON.stringify(carrito));
    renderizarCarrito(); 
}

function eliminarDelCarrito(index) {
    let carrito = obtenerCarrito();
    carrito.splice(index, 1);
    localStorage.setItem('carrito', JSON.stringify(carrito));
    renderizarCarrito();
}

async function finalizarCompra() {
    const carrito = obtenerCarrito(); // Usamos tu función existente
    
    if (carrito.length === 0) {
        alert("El carrito está vacío.");
        return;
    }

	window.location.href = "pago.html";
	}

document.addEventListener('DOMContentLoaded', renderizarCarrito);