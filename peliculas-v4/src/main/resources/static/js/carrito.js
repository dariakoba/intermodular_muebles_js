// --- 1. CONFIGURACIÓN DE IDS (Ajustados a tu HTML) ---
// Asegúrate de que en tu carrito.html existan estos IDs
const contenedorCarrito = document.getElementById('carrito-contenido') || document.getElementById('lista-carrito');
const totalHTML = document.getElementById('precio-total') || document.getElementById('total-precio');

// --- 2. GESTIÓN DEL CARRITO (Lectura de la memoria) ---
function obtenerCarrito() {
    // Usamos "carrito" que es donde se guardan tus sofás y mesas
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
        // Calculamos el subtotal (precio * cantidad)
        const subtotal = prod.precio * (prod.cantidad || 1);
        total += subtotal;
        
        return `
            <div class="carrito-item" style="display: flex; justify-content: space-between; margin-bottom: 10px; border-bottom: 1px solid #eee; padding: 10px;">
                <div class="producto-info">
                    <h3>${prod.nombre} (x${prod.cantidad || 1})</h3>
                    <p>${prod.precio}€ c/u</p>
                </div>
                <div style="text-align: right;">
                    <p><b>${subtotal.toFixed(2)}€</b></p>
                    <button onclick="eliminarDelCarrito(${index})" style="color: red; cursor: pointer; border: none; background: none;">
                        🗑️ Borrar
                    </button>
                </div>
            </div>
        `;
    }).join('');

    if (totalHTML) totalHTML.innerText = total.toFixed(2) + "€";
}

// --- 3. ACCIONES ---
function eliminarDelCarrito(index) {
    let carrito = obtenerCarrito();
    carrito.splice(index, 1); // Quitamos el producto
    localStorage.setItem('carrito', JSON.stringify(carrito));
    renderizarCarrito(); // Refrescamos la vista
}

function vaciarCarrito() {
    if (confirm("¿Quieres vaciar todo el carrito?")) {
        localStorage.removeItem('carrito');
        renderizarCarrito();
    }
}

// --- 4. INICIO ---
document.addEventListener('DOMContentLoaded', () => {
    renderizarCarrito();
});