async function cargarPeliculas() {

	const response = await api.get("/api/admin/peliculas");
	const peliculas = await api.parseJson(response);
	
    const tabla = document.getElementById("tabla-peliculas");
	
	tabla.innerHTML = "";

    peliculas.forEach(p => {

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${p.titulo}</td>
            <td>${p.anyo}</td>
			<td>${p.duracion}</td>
		`;
			
		const tdAcciones = document.createElement("td");

		const ver = document.createElement("a");
		ver.href = `show.html?id=${p.id}`;
		ver.className = "btn btn-sm btn-info";
		ver.textContent = "Ver";

		const editar = document.createElement("a");
		editar.href = `edit.html?id=${p.id}`;
		editar.className = "btn btn-sm btn-warning";
		editar.textContent = "Editar";

		const eliminarBtn = document.createElement("button");
		eliminarBtn.className = "btn btn-sm btn-danger";
		eliminarBtn.textContent = "Eliminar";
		eliminarBtn.addEventListener("click", () => eliminar(p.id));

		tdAcciones.appendChild(ver);
		tdAcciones.appendChild(editar);
		tdAcciones.appendChild(eliminarBtn);

		tr.appendChild(tdAcciones);

		tabla.appendChild(tr);
    });
}

async function eliminar(id) {

    if (!confirm("¿Eliminar esta película?")) return;

    await api.delete(`/api/admin/peliculas/${id}`);

    await cargarPeliculas();
}

async function init() {
	await guard.requireRole("ADMIN");
	await cargarPeliculas();
}

init();
