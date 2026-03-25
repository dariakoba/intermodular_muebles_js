function obtenerId() {
	const params = new URLSearchParams(window.location.search);
	return params.get("id");
}

async function cargarProducto() {
    const id = obtenerId();

    const response = await fetch(`/api/productos/${id}`);
    const p = await response.json();

    document.title = p.nombre;
	document.getElementById("producto-img").src = `/images/productos/${p.id}.jpg`;
    document.getElementById("producto-img").alt = p.nombre;
    document.getElementById("producto-nombre").textContent = p.nombre;
    document.getElementById("producto-descripcion").textContent = p.descripcion;
    document.getElementById("producto-precio").textContent = `${p.precio}€`;


	document.getElementById("producto-id").textContent = p.id_producto;
	
	document.getElementById("btn-comprar").addEventListener("click", () => {
		alert(`Redirigiendo al pago de "${p.nombre}"...`);
	});
	
	document.getElementById("btn-carrito").addEventListener("click", () => {
		const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
		const existe = carrito.find(item => item.id === p.id);
		
		if (existe) {
			existe.cantidad += 1;
		} else {
			carrito.push({ id: p.id, nombre: p.nombre, precio: p.precio, cantidad: 1 });
		}
	
		localStorage.setItem("carrito", JSON.stringify(carrito));
		
		const btn = document.getElementById("btn-carrito");
		btn.textContent = "✓ Añadido";
		btn.style.background = "#2a9aa6";
		btn.style.color = "white";
		btn.style.borderColor = "#2a9aa6";
		setTimeout(() => {
			btn.textContent = "🛒 Añadir al carrito";
			btn.style.background = "";
			btn.style.color = "";
			btn.style.borderColor = "";
		}, 1500);
	});
}

cargarProducto();