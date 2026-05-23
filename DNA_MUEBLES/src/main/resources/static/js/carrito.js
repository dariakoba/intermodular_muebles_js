document.addEventListener("DOMContentLoaded", function() {
    // 1. Cargamos los productos al abrir la página
    cargarCarrito();

    // 2. Asignamos los eventos a los botones
    const btnVaciar = document.getElementById("btn-vaciar-control");
    if (btnVaciar) {
        btnVaciar.addEventListener("click", vaciarCarrito);
    }

    const btnFinalizar = document.getElementById("btn-finalizar-compra");
    if (btnFinalizar) {
        btnFinalizar.addEventListener("click", finalizarCompra);
    }
});

// ======================
// DIBUJAR EL CARRITO
// ======================
function cargarCarrito() {
    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    const contenedor = document.getElementById("carrito-contenido");
    const precioTotal = document.getElementById("precio-total");

    // Limpiamos lo que haya
    contenedor.innerHTML = "";
    let total = 0;

    // Si está vacío
    if (carrito.length === 0) {
        contenedor.innerHTML = "<p style='text-align:center; padding: 20px; color: #887a69;'>Tu carrito está vacío. </p>";
        precioTotal.textContent = "0.00€";
        return;
    }

    // Si hay productos, los dibujamos
    carrito.forEach(function(p, index) {
        const subtotal = p.precio * p.cantidad;
        total += subtotal;

        const div = document.createElement("div");
        div.className = "carrito-item"; 

        // Todo el texto usando concatenación clásica
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

        // Creamos el botón eliminar como elemento independiente
        const divBoton = document.createElement("div");
        divBoton.style.marginLeft = "15px";

        const btnEliminar = document.createElement("button");
        btnEliminar.className = "btn-eliminar-item";
        btnEliminar.title = "Eliminar del carrito";
        btnEliminar.innerHTML = '<i class="fa-solid fa-trash"></i>';
        
        // Le asignamos la función directamente
        btnEliminar.onclick = function() {
            eliminarProducto(index);
        };

        divBoton.appendChild(btnEliminar);
        div.appendChild(divBoton);
        contenedor.appendChild(div);
    });

    // Actualizamos el total a pagar
    precioTotal.textContent = total.toFixed(2) + "€";
}

// ======================
// CAMBIAR CANTIDAD DE UN PRODUCTO
// ======================
window.cambiarCantidad = function(index, nuevoValor) {
    let cantidad = parseInt(nuevoValor);
    
    // Si meten un número raro o menor a 1, lo forzamos a 1
    if (isNaN(cantidad) || cantidad < 1) {
        cantidad = 1;
    }

    let carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    carrito[index].cantidad = cantidad; // Actualizamos la cantidad
    localStorage.setItem("carrito", JSON.stringify(carrito)); // Guardamos en memoria
    
    cargarCarrito(); // Recargamos la interfaz para que recalcule precios
};

// ======================
// ELIMINAR UN PRODUCTO SUELTO
// ======================
window.eliminarProducto = function(index) {
    if (confirm("¿Estás seguro de que quieres eliminar este producto del carrito?")) {
        let carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
        carrito.splice(index, 1);
        localStorage.setItem("carrito", JSON.stringify(carrito));
        cargarCarrito(); // Recargamos para que desaparezca
    }
};

// ======================
// VACIAR TODO EL CARRITO
// ======================
function vaciarCarrito() {
    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    if (carrito.length === 0) return;

    if (confirm("¿Estás seguro de que quieres vaciar todo el carrito?")) {
        localStorage.removeItem("carrito");
        cargarCarrito();
    }
}

// ======================
// FINALIZAR COMPRA (TE LLEVA A LA PASARELA)
// ======================
async function finalizarCompra() {
    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");

    // Validamos que haya algo que comprar
    if (carrito.length === 0) {
        alert("Tu carrito está vacío. Añade algún producto primero.");
        return;
    }

    try {
        // Hacemos una llamada rápida a Java solo para ver si hay sesión
        const response = await fetch("/api/me");
        
        if (!response.ok) {
            // Si Java dice que no estamos logueados, al login
            window.location.href = "login.html";
            return;
        }

        // 🚀 SI TODO ESTÁ BIEN, VAMOS A LA PASARELA 🚀
        // Cambia "pago.html" por el nombre real de tu archivo si es diferente
        window.location.href = "pago.html"; 

    } catch (error) {
        console.error("Error comprobando la sesión:", error);
        window.location.href = "login.html";
    }
}