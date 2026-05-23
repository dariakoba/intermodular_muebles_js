function obtenerId() {

    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarPelicula() {

	const id = obtenerId();

	if (!id) {
		location.href = "index.html";
		return;
	}

	const response = await api.get(`/api/peliculas/${id}`);
	const p = await api.parseJson(response);
	
	document.getElementById("titulo").textContent = p.titulo;
	document.getElementById("anyo").textContent = p.anyo;
	document.getElementById("duracion").textContent = p.duracion;
	document.getElementById("director").textContent = p.director;
	document.getElementById("sinopsis").textContent = p.sinopsis;
}

async function init() {
	await auth.meOptional();
    await cargarPelicula();
}

init();