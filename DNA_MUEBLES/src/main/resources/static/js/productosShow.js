function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

// ======================
// CARRITO 
// ======================
function agregarAlCarrito(producto, cantidad) {

    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    
    
    const idReal = obtenerId() || producto.id_producto || producto.id;

  
    const existe = carrito.find(item => item.id_producto == idReal || item.id == idReal);

    if (existe) {
        existe.cantidad += parseInt(cantidad);
    } else {
        carrito.push({
            id_producto: parseInt(idReal), 
            id: parseInt(idReal),          
            nombre: producto.nombre,
            precio: parseFloat(producto.precio),
            cantidad: parseInt(cantidad)
        });
    }

    localStorage.setItem("carrito", JSON.stringify(carrito));
}

document.addEventListener("DOMContentLoaded", async () => {

    const id = obtenerId();

    try {
        const response = await fetch(`/api/productos/${id}`);
        const p = await response.json();

        console.log("producto:", p);

        // ======================
        // DATOS
        // ======================
        document.title = p.nombre;

        document.getElementById("producto-nombre").textContent = p.nombre;
        document.getElementById("producto-descripcion").textContent = p.descripcion;
        document.getElementById("producto-precio").textContent = `${p.precio} €`;
        
        const imagenes = p.imagenes;
        const imgPrincipal = document.getElementById("producto-img");
        
		if (imagenes && imagenes.length > 0) {
		    imgPrincipal.src = imagenes[0].url;
		    imgPrincipal.onclick = () => abrirModal(imagenes[0].url);
		    galeria.innerHTML = "";
		    let indexActual = 0;

		    function cambiarImagen(index) {
		        indexActual = index;
		        imgPrincipal.src = imagenes[index].url;
		        imgPrincipal.onclick = () => abrirModal(imagenes[index].url);
		        galeria.querySelectorAll("img").forEach(i => i.classList.remove("activa"));
		        galeria.querySelectorAll("img")[index].classList.add("activa");
		    }

		    imagenes.forEach((img, index) => {
		        const el = document.createElement("img");
		        el.src = img.url;
		        if (index === 0) el.classList.add("activa");
		        el.onclick = () => cambiarImagen(index);
		        galeria.appendChild(el);
		    });

		} else {
		    imgPrincipal.src = `/images/productos/productoSinImagen.jpg`;
		    document.getElementById("flecha-izq").style.display = "none";
		    document.getElementById("flecha-der").style.display = "none";
		}
        
        // ======================
        // CANTIDAD
        // ======================
		if (p.stock === 0) {
		    document.getElementById("cantidad-container").style.display = "none";
		    document.getElementById("btn-carrito").style.display = "none";
		    document.getElementById("btn-comprar").style.display = "none";
		} else {
		    // Cantidad
		    document.getElementById("cantidad-container").innerHTML = `
		        <label>Cantidad:</label>
		        <input type="number" id="cantidad" value="1" min="1" max="${p.stock}">
		    `;

		    // Carrito
		    document.getElementById("btn-carrito").onclick = () => {
		        const cantidad = parseInt(document.getElementById("cantidad").value);
		        if (isNaN(cantidad) || cantidad < 1) {
		            alert("No se puede poner una cantidad negativa o menor que 1");
		            return;
		        }
				if (cantidad > p.stock) {
				    alert(`Solo quedan ${p.stock} unidades disponibles. Por favor, ajuste la cantidad.`);
				    return;
				}
		        agregarAlCarrito(p, cantidad);
		        const btn = document.getElementById("btn-carrito");
		        btn.textContent = "✓ Añadido";
		        setTimeout(() => { btn.textContent = "🛒 Añadir al carrito"; }, 1200);
		    };

		    // Comprar
		    document.getElementById("btn-comprar").onclick = () => {
		        const cantidad = parseInt(document.getElementById("cantidad").value);
		        if (isNaN(cantidad) || cantidad < 1) {
		            alert("No se puede poner una cantidad negativa o menor que 1");
		            return;
		        }
				if (cantidad > p.stock) {
				    alert(`Solo quedan ${p.stock} unidades disponibles. Por favor, ajuste la cantidad.`);
				    return;
				}
		        agregarAlCarrito(p, cantidad);
		        window.location.href = "carrito.html";
		    };
		}
		
		// Stock
		const stockDiv = document.createElement("div");

		if (p.stock === 0) {
		    stockDiv.innerHTML = `<span style="
		        background: #fde8e8;
		        color: #c0392b;
		        padding: 6px 14px;
		        border-radius: 6px;
		        font-size: 14px;
		        font-weight: bold;
		    ">● No hay stock</span>`;
		} else if (p.stock < 10) {
		    stockDiv.innerHTML = `<span style="
		        background: #eafaf1;
		        color: #1e8449;
		        padding: 6px 14px;
		        border-radius: 6px;
		        font-size: 14px;
		        font-weight: bold;
		    ">● En stock — Quedan ${p.stock} productos</span>`;
		} else {
		    stockDiv.innerHTML = `<span style="
		        background: #eafaf1;
		        color: #1e8449;
		        padding: 6px 14px;
		        border-radius: 6px;
		        font-size: 14px;
		        font-weight: bold;
		    ">● En stock</span>`;
		}

		document.getElementById("cantidad-container").before(stockDiv);

       

    } catch (error) {
        console.error("Error:", error);
    }
});

// ======================
// MODAL
// ======================
function abrirModal(url) {
    const modal = document.getElementById("imageModal");
    const img = document.getElementById("modalImg");

    img.src = url;
    modal.style.display = "flex";
}

function cerrarImagen() {
    document.getElementById("imageModal").style.display = "none";
}