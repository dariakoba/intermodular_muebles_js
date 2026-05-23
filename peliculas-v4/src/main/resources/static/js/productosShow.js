function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

// ======================
// CARRITO 
// ======================
function agregarAlCarrito(producto, cantidad) {

    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
    
    // Capturamos el ID de forma 100% segura (de la URL o del objeto)
    const idReal = obtenerId() || producto.id_producto || producto.id;

    // Buscamos si ya existe comprobando ambos posibles nombres
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
		        // quitar clase activa de todas
		        galeria.querySelectorAll("img").forEach(i => i.classList.remove("activa"));
		        // añadir a la seleccionada
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
        document.getElementById("cantidad-container").innerHTML = `
            <label>Cantidad:</label>
            <input type="number" id="cantidad" value="1" min="1" max="${p.stock}">
        `;

        // ======================
        // CARRITO
        // ======================
		document.getElementById("btn-carrito").onclick = () => {

		    const cantidad = parseInt(document.getElementById("cantidad").value);

		    // VALIDACIÓN
		    if (isNaN(cantidad) || cantidad < 1) {
		        alert("No se puede poner una cantidad negativa o menor que 1");
		        return;
		    }

		    agregarAlCarrito(p, cantidad);

		    const btn = document.getElementById("btn-carrito");
		    btn.textContent = "✓ Añadido";

		    setTimeout(() => {
		        btn.textContent = "🛒 Añadir al carrito";
		    }, 1200);
		};
        // ======================
        // COMPRAR
        // ======================
		document.getElementById("btn-comprar").onclick = () => {

		    const cantidad = parseInt(document.getElementById("cantidad").value);

		    // VALIDACIÓN
		    if (isNaN(cantidad) || cantidad < 1) {
		        alert("No se puede poner una cantidad negativa o menor que 1");
		        return;
		    }

		    agregarAlCarrito(p, cantidad);

		    window.location.href = "carrito.html";
		};

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