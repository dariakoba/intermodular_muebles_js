document.addEventListener("DOMContentLoaded", function() {
    cargarCarrito();

    // Eventos (asegúrate de que los IDs en tu HTML coincidan exactamente con estos)
    const btnVaciar = document.getElementById("btn-vaciar-control"); 
    if (btnVaciar) {
        btnVaciar.addEventListener("click", vaciarCarrito);
    }

    const btnFinalizar = document.getElementById("btn-finalizar-compra"); 
    if (btnFinalizar) {
        btnFinalizar.addEventListener("click", finalizarCompra);
    }
});

function cargarCarrito() {
    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    const contenedor = document.getElementById("carrito-contenido");
    const precioTotal = document.getElementById("precio-total");
    
    // Referencia a los botones para bloquearlos
    const btnVaciar = document.getElementById("btn-vaciar-control");
    const btnFinalizar = document.getElementById("btn-finalizar-compra");

    // Limpiamos
    contenedor.innerHTML = "";
    let total = 0;

    // LÓGICA DE BLOQUEO DE BOTONES
    if (carrito.length === 0) {
        if (btnVaciar) { btnVaciar.disabled = true; btnVaciar.style.opacity = "0.5"; btnVaciar.style.cursor = "not-allowed"; }
        if (btnFinalizar) { btnFinalizar.disabled = true; btnFinalizar.style.opacity = "0.5"; btnFinalizar.style.cursor = "not-allowed"; }
        
        contenedor.innerHTML = "<p style='text-align:center; padding: 20px; color: #887a69;'>Tu carrito está vacío. </p>";
        precioTotal.textContent = "0.00€";
        return;
    } else {
        // Si hay productos, los activamos
        if (btnVaciar) { btnVaciar.disabled = false; btnVaciar.style.opacity = "1"; btnVaciar.style.cursor = "pointer"; }
        if (btnFinalizar) { btnFinalizar.disabled = false; btnFinalizar.style.opacity = "1"; btnFinalizar.style.cursor = "pointer"; }
    }

    // Dibujamos productos
    carrito.forEach(function(p, index) {
        const subtotal = p.precio * p.cantidad;
        total += subtotal;

        const div = document.createElement("div");
        div.className = "carrito-item"; 

        div.innerHTML = 
            '<div style="flex: 2;">' +
                '<h3 style="margin: 0; color: #5c4432; font-size: 16px;">' + p.nombre + '</h3>' +
                '<p style="margin: 5px 0 0 0; color: #887a69; font-size: 14px;">Precio unitario: ' + p.precio.toFixed(2) + '€</p>' +
            '</div>' +
            '<div style="flex: 1; text-align: center;">' +
                '<input type="number" min="1" value="' + p.cantidad + '" onchange="cambiarCantidad(' + index + ', this.value)" style="width: 40px; text-align: center; border: none; background: transparent; padding: 5px; color: #5c4432; font-weight: bold; font-family: inherit; font-size: 16px; outline: none;">' +
            '</div>' +
            '<div style="flex: 1; text-align: right;">' +
                '<strong style="color: #ae4010; font-size: 16px;">' + subtotal.toFixed(2) + '€</strong>' +
            '</div>';

        const divBoton = document.createElement("div");
        divBoton.style.marginLeft = "15px";

        const btnEliminar = document.createElement("button");
        btnEliminar.className = "btn-eliminar-item";
        btnEliminar.title = "Eliminar del carrito";
        btnEliminar.innerHTML = '<i class="fa-solid fa-trash"></i>';
        btnEliminar.onclick = function() { eliminarProducto(index); };

        divBoton.appendChild(btnEliminar);
        div.appendChild(divBoton);
        contenedor.appendChild(div);
    });

    precioTotal.textContent = total.toFixed(2) + "€";
}

window.cambiarCantidad = function(index, nuevoValor) {
    let cantidad = parseInt(nuevoValor);
    if (isNaN(cantidad) || cantidad < 1) cantidad = 1;
    let carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    carrito[index].cantidad = cantidad;
    localStorage.setItem("carrito", JSON.stringify(carrito));
    cargarCarrito();
};

window.eliminarProducto = function(index) {
    if (confirm("¿Estás seguro de eliminar este producto?")) {
        let carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
        carrito.splice(index, 1);
        localStorage.setItem("carrito", JSON.stringify(carrito));
        cargarCarrito();
    }
};

function vaciarCarrito() {
    if (confirm("¿Vaciar todo el carrito?")) {
        localStorage.removeItem("carrito");
        cargarCarrito();
    }
}

async function finalizarCompra() {
    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    if (carrito.length === 0) return;

    try {
        // 1. Verificamos sesión
        const response = await fetch("/api/me");
        if (!response.ok) {
            window.location.href = "login.html";
            return;
        }

        // ==========================================
        // NUEVO: COMPROBACIÓN DE STOCK ANTES DE IR A PAGO
        // ==========================================
        try {
            // Suponemos que en /api/productos está tu catálogo. Si tu ruta es diferente, cámbiala aquí.
            const resProductos = await fetch("/api/productos"); 
            if (resProductos.ok) {
                const catalogoBD = await resProductos.json();
                
                // Revisamos cada producto del carrito
                for (let item of carrito) {
                    // Lo buscamos en la BD por nombre
                    const productoBD = catalogoBD.find(p => p.nombre === item.nombre);
                    
                    if (productoBD) {
                        if (productoBD.stock <= 0) {
                            alert(`Lo sentimos, el producto "${item.nombre}" está agotado. Elimínalo del carrito para continuar.`);
                            return; // Frena en seco, no va a pago.html
                        }
                        if (item.cantidad > productoBD.stock) {
                            alert(`Solo nos quedan ${productoBD.stock} unidades de "${item.nombre}". Por favor, ajusta la cantidad.`);
                            return; // Frena en seco
                        }
                    }
                }
            }
        } catch (errorStock) {
            console.warn("No se pudo pre-validar el stock", errorStock);
            // Si falla esta comprobación por red, dejamos que pase y el backend de Java lo bloqueará de forma segura.
        }
        // ==========================================

        // Si hay sesión y todo tiene stock, avanzamos
        window.location.href = "pago.html";
        
    } catch (error) {
        window.location.href = "login.html";
    }
}

//responsive hamburguesa
document.getElementById("btn-hamburguesa").addEventListener("click", () => {
    document.getElementById("nav-menu").classList.toggle("abierto");
});

window.addEventListener("resize", () => {
    if (window.innerWidth > 900) {
        document.getElementById("nav-menu").classList.remove("abierto");
    }
});