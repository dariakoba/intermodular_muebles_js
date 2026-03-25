import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {
	await guard.requireRole("ADMIN");

	const id = obtenerId();

	if (!id) {
		location.href = "index.html";
		return;
	}
	
	const pelicula = await api.get(`/api/admin/peliculas/${id}`);
	const directores = await api.get("/api/admin/directores");

	render(pelicula, directores);

	bindEvents();
});


function obtenerId() {

    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function render(pelicula, directores) {

	const form = document.getElementById("form-pelicula");
	const select = document.getElementById("director");
	
	select.innerHTML = '<option value="">-- Selecciona director --</option>';

	directores.forEach(d => {

		const option = document.createElement("option");
		option.value = d.id;
		option.textContent = d.nombre;

		select.appendChild(option);
	});

	form.titulo.value = pelicula.titulo;
	form.anyo.value = pelicula.anyo;
	form.duracion.value = pelicula.duracion;
	form.sinopsis.value = pelicula.sinopsis;
	form.director.value = pelicula.director_id;
}

function bindEvents() {
	const form = document.getElementById("form-pelicula");
	bind(form, "submit", guardar);
}

async function guardar(e) {

    e.preventDefault();
	
	const id = obtenerId();

	const form = e.target;
	
    await api.put(`/api/admin/peliculas/${id}`, {
		titulo: form.titulo.value,
		anyo: form.anyo.value,
		duracion: form.duracion.value,
		sinopsis: form.sinopsis.value,
		director_id: form.director.value
	});

    location.replace("index.html");
}