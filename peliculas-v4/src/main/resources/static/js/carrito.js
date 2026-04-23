const contenedorCarrito = document.getElementById('carrito-contenido');
const totalHTML = document.getElementById('precio-total');

function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('carrito')) || [];
}

function renderizarCarrito() {
    if (!contenedorCarrito) return;
    const carrito = obtenerCarrito();
    const btnVaciar = document.getElementById('btn-vaciar-control');
    
    // Si el carrito está vacío
    if (carrito.length === 0) {
        contenedorCarrito.innerHTML = `<div class="vacio-msg">Tu carrito está vacío.</div>`;
        if (totalHTML) totalHTML.innerText = "0.00€";
        if (btnVaciar) btnVaciar.style.display = 'none'; 
        return;
    }

    // Si hay productos, mostramos el botón de vaciar
    if (btnVaciar) btnVaciar.style.display = 'block';

    let total = 0;

    contenedorCarrito.innerHTML = carrito.map((prod, index) => {
        const cantidad = prod.cantidad || 1;
        const subtotal = prod.precio * cantidad;
        total += subtotal;
        
        return `
            <div class="carrito-item" style="display: flex; align-items: center; justify-content: space-between; padding: 15px; border-bottom: 1px solid #eee; background: white; margin-bottom: 10px; border-radius: 8px;">
                <div class="producto-info">
                    <h3 style="margin: 0;">${prod.nombre}</h3>
                    <p style="color: #666; margin: 5px 0;">${parseFloat(prod.precio).toFixed(2)}€ c/u</p>
                </div>
                
                <div style="display: flex; align-items: center; gap: 20px;">
                    <div class="cantidad-control">
                        <label style="font-size: 12px; display: block; text-align: center;">Cant.</label>
                        <input type="number" value="${cantidad}" min="1" 
                               style="width: 50px; padding: 5px; text-align: center; border: 1px solid #ddd; border-radius: 4px;"
                               onchange="actualizarCantidad(${index}, this.value)">
                    </div>
                    
                    <p style="min-width: 70px; text-align: right;"><b>${subtotal.toFixed(2)}€</b></p>
                    
                    <button onclick="eliminarDelCarrito(${index})" style="color: #e74c3c; cursor: pointer; border: none; background: none; font-size: 1.2rem;" title="Eliminar">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </div>
            </div>
        `;
    }).join('');

    if (totalHTML) totalHTML.innerText = total.toFixed(2) + "€";
}

// FUNCIONALIDADES
function actualizarCantidad(index, nuevaCant) {
    let carrito = obtenerCarrito();
    let cant = parseInt(nuevaCant);
    if (isNaN(cant) || cant < 1) cant = 1; 
    
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

function vaciarCarrito() {
    if (confirm("¿Seguro que quieres vaciar todo el carrito?")) {
        localStorage.removeItem('carrito');
        renderizarCarrito();
    }
}

function finalizarCompra() {
    const carrito = obtenerCarrito();
    if (carrito.length === 0) {
        alert("El carrito está vacío. ¡Añade algo primero!");
        return;
    }
    window.location.href = "pago.html";
}

document.addEventListener('DOMContentLoaded', renderizarCarrito);