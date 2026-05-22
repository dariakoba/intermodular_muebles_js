import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";

app.run(async () => {
	await guard.requireRole("ADMIN");
	
	const id = obtenerId();
		
	if (!id) {
		location.href = "index.html";
		return;
	}
	
	const pelicula = await api.get(`/api/admin/peliculas/${id}`);
	
	if (!pelicula) return;
	
	render(pelicula);
});

function obtenerId() {

    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function render(pelicula) {

    document.getElementById("titulo").textContent = pelicula.titulo;
    document.getElementById("anyo").textContent = pelicula.anyo;
    document.getElementById("duracion").textContent = pelicula.duracion;
    document.getElementById("director_id").textContent = pelicula.director_id;
    document.getElementById("sinopsis").textContent = pelicula.sinopsis;

    document.getElementById("editar").href = `edit.html?id=${pelicula.id}`;
}