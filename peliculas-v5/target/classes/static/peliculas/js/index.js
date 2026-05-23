async function cargarPeliculas() {

	const response = await api.get("/api/peliculas");
	const peliculas = await api.parseJson(response);
	
	const lista = document.getElementById("lista-peliculas");

	lista.innerHTML = "";

	if (peliculas.length === 0) {
		lista.innerHTML = `
			<li class="list-group-item text-muted">
				No hay películas
			</li>
		`;
		return;
	}

	peliculas.forEach(p => {

		const li = document.createElement("li");

		li.className = "list-group-item d-flex justify-content-between align-items-center";

		li.innerHTML = `
			<a href="show.html?id=${p.id}" 
			   class="text-decoration-none text-dark">
				${p.titulo} (${p.anyo})
			</a>

			<a href="show.html?id=${p.id}" 
			   class="btn btn-sm btn-outline-primary">
				Ver
			</a>
		`;

		lista.appendChild(li);
	});
}

async function init() {
	await auth.meOptional();
	await cargarPeliculas();
}

init();