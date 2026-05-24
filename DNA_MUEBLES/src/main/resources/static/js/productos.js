document.addEventListener("DOMContentLoaded", async () => {

    await cargarCategorias();
    await cargarProductos();

    aplicarCategoriaURL();

    // Quitar filtros
    document.getElementById("quitar-filtros")
        .addEventListener("click", quitarFiltros);
		
	// ocultar filtros
	document.getElementById("btn-cerrar-sidebar").addEventListener("click", () => {
	    document.querySelector(".sidebar").classList.remove("sidebar-open");
	    document.getElementById("btn-cerrar-sidebar").style.display = "none";
	    document.getElementById("btn-filtros").innerHTML =
	        '<span class="material-symbols-outlined">tune</span> Mostrar filtros';
	});
	
	window.addEventListener("resize", () => {
	    if (window.innerWidth > 900) {
	        document.getElementById("btn-cerrar-sidebar").style.display = "none";
	        document.querySelector(".sidebar").classList.remove("sidebar-open");
	        document.getElementById("btn-filtros").innerHTML =
	            '<span class="material-symbols-outlined">tune</span> Mostrar filtros';
	    }
	});
	
	//responsive hamburguesa
    document.getElementById("btn-hamburguesa").addEventListener("click", () => {
        document.getElementById("nav-menu").classList.toggle("abierto");
    });

    window.addEventListener("resize", () => {
        if (window.innerWidth > 900) {
            document.getElementById("nav-menu").classList.remove("abierto");
        }
    });
	//
	// sidebar no toca el footer
	    function ajustarSidebar() {
	        const sidebar = document.querySelector(".sidebar");
	        const footer = document.querySelector(".main-footer");
	        const footerTop = footer.getBoundingClientRect().top + window.scrollY;
	        const scrollY = window.scrollY;
	        const topbarHeight = 65;

	        const alturaMaxima = footerTop - scrollY - topbarHeight;
	        const alturaViewport = window.innerHeight - topbarHeight;

	        sidebar.style.height = Math.min(alturaMaxima, alturaViewport) + "px";
	    }

	    ajustarSidebar();
	    window.addEventListener("scroll", ajustarSidebar);
	    window.addEventListener("resize", ajustarSidebar);

	//fullscrean filtros
	document.addEventListener("fullscreenchange", () => {
	    const sidebar = document.querySelector(".sidebar");
	    const btnFiltros = document.getElementById("btn-filtros");
	    const cerrar = document.getElementById("btn-cerrar-sidebar");

	    if (!document.fullscreenElement) {
	        sidebar.classList.remove("sidebar-open");
	        sidebar.style.transform = "TranslateX(-100%)";
	        btnFiltros.style.display = "flex";
	        cerrar.style.display = "none";
	    } else {
	        sidebar.style.transform = "translateX(0)";
	        btnFiltros.style.display = "none";
	        cerrar.style.display = "none";
	    }
	});
	
    // Ordenar
    document.getElementById("ordenar")
        .addEventListener("change", aplicarFiltros);

    // Precio dinámico
    document.getElementById("precio-min")
        .addEventListener("change", () => {
            actualizarBotonQuitarFiltros();
            aplicarFiltros();
        });

    document.getElementById("precio-max")
        .addEventListener("change", () => {
            actualizarBotonQuitarFiltros();
            aplicarFiltros();
        });

    // Sidebar móvil
    document.getElementById("btn-filtros").addEventListener("click", () => {
		
		
        const sidebar = document.querySelector(".sidebar");
        const btn = document.getElementById("btn-filtros");

        const abierto = sidebar.classList.toggle("sidebar-open");

        btn.innerHTML = abierto
            ? '<span class="material-symbols-outlined">close</span> Ocultar filtros'
            : '<span class="material-symbols-outlined">tune</span> Mostrar filtros';
		const cerrar = document.getElementById("btn-cerrar-sidebar");
		cerrar.style.display = abierto ? "block" : "none";
    });

    document.querySelectorAll(".filter-header").forEach(header => {

        header.addEventListener("click", () => {

            const options = header.nextElementSibling;
            const toggle = header.querySelector(".toggle");

            const abierto = options.style.display === "block";

            options.style.display = abierto ? "none" : "block";
            toggle.textContent = abierto ? "+" : "−";
        });
    });
});



async function cargarCategorias() {

    try {

        const response = await fetch("/api/categorias");
        const categorias = await response.json();

        const contenedor = document.getElementById("filtro-categorias");

        contenedor.innerHTML = "";

        categorias.forEach(c => {

            const label = document.createElement("label");

            label.innerHTML = `
                <input type="checkbox" value="${c.nombre}">
                ${c.nombre}
            `;

            contenedor.appendChild(label);
        });

        // Eventos dinámicos categorías + colores
        document.querySelectorAll(
            '#filtro-categorias input, #filtro-colores input'
        ).forEach(input => {

            input.addEventListener("change", () => {

                actualizarBotonQuitarFiltros();
                aplicarFiltros();
            });
        });

    } catch (error) {

        console.error("Error cargando categorías:", error);
    }
}



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


function renderProductos(productos) {

    const contenedor = document.getElementById("productos-container");

    contenedor.innerHTML = "";

    if (productos.length === 0) {

        contenedor.innerHTML = `
            <p style="color:#999; padding:20px;">
                No se encontraron productos.
            </p>
        `;

        return;
    }

    const lista = document.createElement("ul");
    lista.classList.add("productos-lista");

    productos.forEach(p => {

        const item = document.createElement("li");

        item.classList.add("card");

        item.innerHTML = `
            <img src="${p.imagen ?? '/images/productos/productoSinImagen.jpg'}" alt="${p.nombre}">

            <div class="card-body">

                <h3>${p.nombre}</h3>

                <p class="precio">${p.precio}€</p>

                <a href="productosShow.html?id=${p.id_producto}">
                    <button>Ver producto</button>
                </a>

            </div>
        `;

        lista.appendChild(item);
    });

    contenedor.appendChild(lista);
}

async function aplicarFiltros() {

    const params = new URLSearchParams();

    // Categorías
    const categoriasChecked = [
        ...document.querySelectorAll("#filtro-categorias input:checked")
    ].map(cb => cb.value);

    categoriasChecked.forEach(c => {
        params.append("categoria", c);
    });

    // Colores
    const coloresChecked = [
        ...document.querySelectorAll("#filtro-colores input:checked")
    ].map(cb => cb.value);

    coloresChecked.forEach(c => {
        params.append("color", c);
    });

    // Precio
    const precioMin = document.getElementById("precio-min").value;
    const precioMax = document.getElementById("precio-max").value;

    if (precioMin) {
        params.append("precioMin", precioMin);
    }

    if (precioMax) {
        params.append("precioMax", precioMax);
    }

    // Orden
    params.append(
        "orden",
        document.getElementById("ordenar").value
    );

    actualizarBotonQuitarFiltros();

    try {

        const response = await fetch(
            `/api/productos?${params.toString()}`
        );

        const productos = await response.json();

        renderProductos(productos);

    } catch (error) {

        console.error("Error aplicando filtros:", error);
    }
}



function aplicarCategoriaURL() {

    const params = new URLSearchParams(window.location.search);

    const categoria = params.get("categoria");

    if (!categoria) return;

    setTimeout(() => {

        const checkbox = [
            ...document.querySelectorAll("#filtro-categorias input")
        ].find(cb =>
            cb.value.toLowerCase() === categoria.toLowerCase()
        );

        if (checkbox) {

            checkbox.checked = true;

            actualizarBotonQuitarFiltros();

            aplicarFiltros();
        }

    }, 300);
}


function quitarFiltros() {

    // Categorías
    document.querySelectorAll("#filtro-categorias input")
        .forEach(cb => cb.checked = false);

    // Colores
    document.querySelectorAll("#filtro-colores input")
        .forEach(cb => cb.checked = false);

    // Precio
    document.getElementById("precio-min").value = "";
    document.getElementById("precio-max").value = "";

    // Orden
    document.getElementById("ordenar").value = "nombre-asc";

    actualizarBotonQuitarFiltros();

    cargarProductos();
}



function actualizarBotonQuitarFiltros() {

    const hayCategorias =
        document.querySelectorAll(
            "#filtro-categorias input:checked"
        ).length > 0;

    const hayColores =
        document.querySelectorAll(
            "#filtro-colores input:checked"
        ).length > 0;

    const hayPrecioMin =
        document.getElementById("precio-min").value !== "";

    const hayPrecioMax =
        document.getElementById("precio-max").value !== "";

    const boton =
        document.getElementById("quitar-filtros");

    if (
        hayCategorias ||
        hayColores ||
        hayPrecioMin ||
        hayPrecioMax
    ) {

        boton.style.display = "block";

    } else {

        boton.style.display = "none";
    }
}