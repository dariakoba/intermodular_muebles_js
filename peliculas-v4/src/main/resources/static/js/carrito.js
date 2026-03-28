// 1. Obtener elementos del DOM una sola vez
const contenedor = document.getElementById('carrito-contenido');
const resumen = document.getElementById('carrito-resumen');
const totalHTML = document.getElementById('precio-total');

// 2. Obtener carrito (siempre sincronizado con LocalStorage)
function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('dna-carrito')) || [];
}

// 3. Función para dibujar el carrito en pantalla (READ)
function renderizarCarrito() {
    if (!contenedor) return;

    const carrito = obtenerCarrito();
    contenedor.innerHTML = ''; 

    // Si no hay productos, mostramos mensaje y ocultamos el resumen
    if (carrito.length === 0) {
        contenedor.innerHTML = `<div class="carrito-vacio"><p>Tu carrito está vacío.</p></div>`;
        if (resumen) resumen.style.display = 'none';
        if (totalHTML) totalHTML.innerText = "0.00€";
        return;
    }

    // Si hay productos, mostramos el resumen (total y botones)
    if (resumen) resumen.style.display = 'block';
    let totalAcumulado = 0;

    contenedor.innerHTML = carrito.map((prod, index) => {
        totalAcumulado += prod.precio;
        const rutaImagen = prod.imagen || prod.image || 'images/default.png';
        
        return `
            <div class="item-carrito" style="border-bottom: 1px solid #ddd; padding: 10px; display: flex; align-items: center; gap: 20px;">
                <img src="${rutaImagen}" 
                     width="80" 
                     onerror="this.onerror=null; this.src='https://via.placeholder.com/80?text=Sin+Foto'" 
                     alt="${prod.nombre}">
                <div class="item-info">
                    <h3 style="margin:0;">${prod.nombre}</h3>
                    <p style="margin:5px 0;">${prod.precio.toFixed(2)}€</p>
                </div>
                <button class="btn-eliminar" onclick="eliminarDelCarrito(${index})" style="margin-left: auto; color: red; cursor: pointer;">
                    <i class="fa-solid fa-trash"></i> Eliminar
                </button>
            </div>
        `;
    }).join('');

    // Actualizamos el total en el HTML
    if (totalHTML) {
        totalHTML.innerText = totalAcumulado.toFixed(2) + "€";
    }
}

// 4. FUNCIONES DE GESTIÓN (DELETE / UPDATE)

// Eliminar un solo producto
function eliminarDelCarrito(index) {
    let carrito = obtenerCarrito();
    carrito.splice(index, 1); // Borra el elemento en esa posición
    localStorage.setItem('dna-carrito', JSON.stringify(carrito));
    renderizarCarrito();
}

// VACIAR TODO EL CARRITO (La que te faltaba)
function vaciarCarrito() {
    if (confirm("¿Estás seguro de que quieres vaciar todo el carrito?")) {
        localStorage.removeItem('dna-carrito'); // Borra la clave completa
        renderizarCarrito(); // Refresca la vista
    }
}

// 5. LÓGICA DE COMPRA (CONEXIÓN CON JAVA - CREATE)
async function finalizarCompra() {
    const carrito = obtenerCarrito();
    if (carrito.length === 0) return alert("El carrito está vacío");

    // Calculamos el total real desde el array de objetos
    const precioFinal = carrito.reduce((sum, p) => sum + p.precio, 0);

    const datosCompra = {
        pedido: {
            fechaPedido: new Date().toISOString().split('T')[0],
            precio: precioFinal,
            metodoPago: "Tarjeta",
            envio: "Domicilio",
            id_usuario: 2 // ID de prueba (debe existir en tu tabla usuarios)
        },
        idsEjemplares: carrito.map(prod => prod.id) 
    };

    try {
        const response = await fetch('/api/carrito/comprar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datosCompra)
        });

        if (response.ok) {
            alert("¡Compra realizada con éxito! Se ha guardado en la base de datos.");
            localStorage.removeItem('dna-carrito'); // Limpiamos tras éxito
            window.location.href = "index.html";
        } else {
            const errorText = await response.text();
            alert("Error del servidor: " + errorText);
        }
    } catch (error) {
        console.error("Error en la petición:", error);
        alert("No se pudo conectar con el servidor (¿Está encendido el Java?)");
    }
}

// Ejecutar al cargar la página para mostrar lo que haya guardado
renderizarCarrito();