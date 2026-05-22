function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

// ======================
// CARRITO
// ======================
function agregarAlCarrito(producto, cantidad) {

    const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");

    const existe = carrito.find(item => item.id === producto.id_producto);

    if (existe) {
        existe.cantidad += cantidad;
    } else {
        carrito.push({
            id: producto.id_producto,
            nombre: producto.nombre,
            precio: producto.precio,
            cantidad: cantidad
        });
    }

    localStorage.setItem("carrito", JSON.stringify(carrito));
}

// ======================
// APP (MISMO ESTILO QUE TU ADMIN)
// ======================
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

        // ======================
        // IMÁGENES (IGUAL QUE TU ADMIN)
        // ======================
        const imagenes = p.imagenes;

        const imgPrincipal = document.getElementById("producto-img");
        const galeria = document.getElementById("galeria");

        if (imagenes && imagenes.length > 0) {

            imgPrincipal.src = imagenes[0].url;

            imgPrincipal.onclick = () => abrirModal(imagenes[0].url);

            galeria.innerHTML = "";

            imagenes.slice(1).forEach(img => {

                const el = document.createElement("img");

                el.src = img.url;
                el.style.width = "80px";
                el.style.cursor = "pointer";
                el.style.borderRadius = "8px";

                el.onclick = () => {
                    imgPrincipal.src = img.url;
                };

                galeria.appendChild(el);
            });

        } else {
            // fallback si no hay imágenes
            imgPrincipal.src = `/uploads/productos/${p.id_producto}.jpg`;
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