function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

// --- FUNCIÓN PARA GUARDAR EN EL CARRITO (REUTILIZABLE) ---
function agregarAlCarrito(producto) {
    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    // Usamos id_producto que es como viene de tu base de datos
    const existe = carrito.find(item => item.id === producto.id_producto);
    
    if (existe) {
        existe.cantidad += 1;
    } else {
        carrito.push({ 
            id: producto.id_producto, 
            nombre: producto.nombre, 
            precio: producto.precio, 
            cantidad: 1 
        });
    }
    localStorage.setItem("carrito", JSON.stringify(carrito));
}

async function cargarProducto() {
    const id = obtenerId();

    try {
        const response = await fetch(`/api/productos/${id}`);
        const p = await response.json();
        console.log("Producto cargado:", p);

        // Rellenar los datos en el HTML
        document.title = p.nombre;
        document.getElementById("producto-img").src = `/images/productos/${p.id_producto}.jpg`;
        document.getElementById("producto-img").alt = p.nombre;
        document.getElementById("producto-nombre").textContent = p.nombre;
        document.getElementById("producto-descripcion").textContent = p.descripcion;
        document.getElementById("producto-precio").textContent = `${p.precio}€`;

        // --- BOTÓN: AÑADIR AL CARRITO ---
        document.getElementById("btn-carrito").onclick = () => {
            agregarAlCarrito(p);
            
            // Feedback visual
            const btn = document.getElementById("btn-carrito");
            btn.textContent = "✓ Añadido";
            btn.style.background = "#2a9aa6";
            btn.style.color = "white";
            setTimeout(() => {
                btn.textContent = "🛒 Añadir al carrito";
                btn.style.background = "";
                btn.style.color = "";
            }, 1500);
        };

        // --- BOTÓN: COMPRAR AHORA ---
        document.getElementById("btn-comprar").onclick = () => {
            agregarAlCarrito(p);
            window.location.href = "carrito.html"; 
        };

    } catch (error) {
        console.error("Error al cargar el producto:", error);
    }
}

// Ejecutamos la función
cargarProducto();