function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarCategorias() {
    const id = obtenerId();

    const response = await fetch(`/api/admin/categorias/${id}`);
    const p = await response.json();
	console.log(p)
	
	document.getElementById("id").textContent = p.id_categoria;

    document.getElementById("categoria").textContent = p.nombre;
	
	let estado;
		if (!p.deleted_at){
			estado = 'Activo';
		} else {
			estado='Inactivo';
		}
		document.getElementById("estado").textContent = estado;
		

    document.getElementById("btn-editar").href = `edit.html?id=${id}`;
}

cargarCategorias();