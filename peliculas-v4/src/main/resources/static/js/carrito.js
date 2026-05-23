const contenedorCarrito = document.getElementById('carrito-contenido');
const totalHTML = document.getElementById('precio-total');

function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('carrito')) || [];
}

function renderizarCarrito() {
    if (!contenedorCarrito) return;
    const carrito = obtenerCarrito();
    
    // Capturamos los dos botones
    const btnVaciar = document.getElementById('btn-vaciar-control');
    const btnFinalizar = document.getElementById('btn-finalizar-compra');
    
    // Si el carrito está vacío
    if (carrito.length === 0) {
        contenedorCarrito.innerHTML = `<div class="vacio-msg">Tu carrito está vacío.</div>`;
        if (totalHTML) totalHTML.innerText = "0.00€";
        
        // APAGAMOS LOS BOTONES
        if (btnVaciar) btnVaciar.disabled = true;
        if (btnFinalizar) btnFinalizar.disabled = true; 
        
        return;
    }

    // Si hay productos, ENCENDEMOS LOS BOTONES
    if (btnVaciar) btnVaciar.disabled = false;
    if (btnFinalizar) btnFinalizar.disabled = false;

    let total = 0;

    contenedorCarrito.innerHTML = carrito.map((prod, index) => {
        const cantidad = prod.cantidad || 1;
        const subtotal = prod.precio * cantidad;
        total += subtotal;
        
        return `
            <div class="carrito-item">
                <div class="producto-info">
                    <h3 style="margin: 0; color: #333;">${prod.nombre}</h3>
                    <p style="color: #666; margin: 5px 0;">${parseFloat(prod.precio).toFixed(2)}€ c/u</p>
                </div>
                
                <div style="display: flex; align-items: center; gap: 20px;">
                    <div class="cantidad-control">
                        <label style="font-size: 12px; display: block; text-align: center;">Cant.</label>
                        <input type="number" value="${cantidad}" min="1" 
                               style="width: 50px; padding: 5px; text-align: center; border: 1px solid #ddd; border-radius: 4px;"
                               onchange="actualizarCantidad(${index}, this.value)">
                    </div>
                    
                    <p style="min-width: 70px; text-align: right; font-weight: bold; font-size: 18px; color: #ae4010;">${subtotal.toFixed(2)}€</p>
                    
                    <button class="btn-eliminar-item" onclick="eliminarDelCarrito(${index})" title="Eliminar">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </div>
            </div>
        `;
    }).join('');

    if (totalHTML) totalHTML.innerText = total.toFixed(2) + "€";
}

// --- LAS FUNCIONES PERDIDAS ---

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

// --- EL BOTÓN DE FINALIZAR COMPRA ---
function finalizarCompra() {
    // 1. Buscamos al usuario en la memoria
    const usuarioLogueado = localStorage.getItem('user') || sessionStorage.getItem('user');

    // 2. Comprobación antibalas: Si no hay usuario, o quedó un rastro de texto inválido
    if (!usuarioLogueado || usuarioLogueado === "null" || usuarioLogueado === "undefined" || usuarioLogueado === "") {
        // LE MANDAMOS DIRECTO A INICIAR SESIÓN
        window.location.href = 'login.html'; 
        return; // Cortamos aquí para que no siga ejecutando nada más
    }

    // 3. Si SÍ hay usuario, comprobamos que el carrito no esté vacío
    const carrito = obtenerCarrito();
    if (carrito.length === 0) {
        return; 
    }
    
    // 4. Si todo está correcto, vamos al pago
    window.location.href = "pago.html";
}

// --- LA CONEXIÓN DE LOS BOTONES (Lo que faltaba) ---
document.addEventListener('DOMContentLoaded', () => {
    // Pintamos el carrito al cargar la página
    renderizarCarrito();

    // Enlazamos el clic del botón finalizar con su función
    const btnFinalizar = document.getElementById('btn-finalizar-compra');
    if (btnFinalizar) {
        btnFinalizar.addEventListener('click', finalizarCompra);
    }

    // Enlazamos el clic del botón vaciar con su función
    const btnVaciar = document.getElementById('btn-vaciar-control');
    if (btnVaciar) {
        btnVaciar.addEventListener('click', vaciarCarrito);
    }
});