function obtenerId() {

    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarDirectores() {

    const response = await api.get("/api/admin/directores");
	const directores = await api.parseJson(response);

    const select = document.getElementById("director");
	
	select.innerHTML = '<option value="">-- Selecciona director --</option>';

    directores.forEach(d => {

        const option = document.createElement("option");

        option.value = d.id;
        option.textContent = d.nombre;

        select.appendChild(option);
    });
}

async function cargar() {

    const id = obtenerId();

    const response = await api.get(`/api/admin/peliculas/${id}`);
	const p = await api.parseJson(response);
	
	const form = document.getElementById("form");

	form.titulo.value = p.titulo;
	form.anyo.value = p.anyo;
	form.duracion.value = p.duracion;
	form.sinopsis.value = p.sinopsis;
	form.director.value = p.director_id;
}

async function guardar(e) {

    e.preventDefault();

    const id = obtenerId();
	
	if (!id) {
		location.href = "index.html";
		return;
	}
	
    const form = e.target;

    await api.put(`/api/admin/peliculas/${id}`, {
		titulo: form.titulo.value,
		anyo: form.anyo.value,
		duracion: form.duracion.value,
		sinopsis: form.sinopsis.value,
		director_id: form.director.value
	});

    location.href = "index.html";
}

async function init() {
	await guard.requireRole("ADMIN");
    await cargarDirectores();
    await cargar();
}

init();