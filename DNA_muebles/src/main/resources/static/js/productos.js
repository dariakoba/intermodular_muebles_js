// ── INIT ──────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", async () => {
    await cargarCategorias();
    await cargarProductos();

    document.getElementById("aplicar-filtros").addEventListener("click", aplicarFiltros);
    document.getElementById("ordenar").addEventListener("change", aplicarFiltros);

    // Toggle sidebar en móvil
	document.getElementById("btn-filtros").addEventListener("click", () => {
	    const sidebar = document.querySelector(".sidebar");
	    const btn = document.getElementById("btn-filtros");
	    const abierto = sidebar.classList.toggle("sidebar-open");
	    btn.innerHTML = abierto
	        ? '<span class="material-symbols-outlined">close</span> Ocultar filtros'
	        : '<span class="material-symbols-outlined">tune</span> Mostrar filtros';
	});
	
    // Toggle secciones del sidebar
	document.querySelectorAll(".filter-header").forEach(header => {
	    header.addEventListener("click", () => {
	        const options = header.nextElementSibling;
	        const toggle = header.querySelector(".toggle");
	        const abierto = options.style.display === "block";  // ← cambio aquí
	        options.style.display = abierto ? "none" : "block";
	        toggle.textContent = abierto ? "+" : "−";
	    });
	});
});

// ── CARGAR CATEGORÍAS DINÁMICAS ───────────────────────
async function cargarCategorias() {
    try {
        const response = await fetch("/api/categorias");
        const categorias = await response.json();

        const contenedor = document.getElementById("filtro-categorias");
        contenedor.innerHTML = "";

        categorias.forEach(c => {
            const label = document.createElement("label");
            label.innerHTML = `<input type="checkbox" value="${c.nombre}"> ${c.nombre}`;
            contenedor.appendChild(label);
        });

    } catch (error) {
        console.error("Error cargando categorías:", error);
    }
}

// ── CARGAR PRODUCTOS ──────────────────────────────────
async function cargarProductos() {
    try {
        const response = await fetch("/api/productos");
        const data = await response.json();

        if (!response.ok || !Array.isArray(data)) {
            console.error("Error del servidor:", data.message);
            return;
        }

        renderProductos(data);
    } catch (error) {
        console.error("Error de red:", error);
    }
}

// ── RENDER PRODUCTOS ──────────────────────────────────
function renderProductos(productos) {
    const contenedor = document.getElementById("productos-container");
    contenedor.innerHTML = "";

    if (productos.length === 0) {
        contenedor.innerHTML = `<p style="color:#999; padding:20px;">No se encontraron productos.</p>`;
        return;
    }

    productos.forEach(p => {
        const card = document.createElement("div");
        card.classList.add("card");
        card.innerHTML = `
            <img src="${p.imagen ?? '/images/productos/productoSinImagen.jpg'}" alt="${p.nombre}">
            <div class="card-body">
                <h3>${p.nombre}</h3>
                <p class="precio">${p.precio}€</p>
                <a href="productosShow.html?id=${p.id_producto}">
                    <button>Ver producto</button>
                </a>
            </div>
        `;
        contenedor.appendChild(card);
    });
}

// ── APLICAR FILTROS ───────────────────────────────────
async function aplicarFiltros() {
    const params = new URLSearchParams();

    // Categorías — todas las seleccionadas
    const categoriasChecked = [...document.querySelectorAll("#filtro-categorias input:checked")]
        .map(cb => cb.value);
    categoriasChecked.forEach(c => params.append("categoria", c));

    // Colores — todos los seleccionados
    const coloresChecked = [...document.querySelectorAll("#filtro-colores input:checked")]
        .map(cb => cb.value);
    coloresChecked.forEach(c => params.append("color", c));

    // Precio
    const precioMin = document.getElementById("precio-min").value;
    const precioMax = document.getElementById("precio-max").value;
    if (precioMin) params.append("precioMin", precioMin);
    if (precioMax) params.append("precioMax", precioMax);

    // Orden
    params.append("orden", document.getElementById("ordenar").value);

    try {
        const response = await fetch(`/api/productos?${params.toString()}`);
        const productos = await response.json();
        renderProductos(productos);
    } catch (error) {
        console.error("Error aplicando filtros:", error);
    }
}