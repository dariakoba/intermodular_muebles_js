import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {

	await guard.requireRole("ADMIN");

	const directores = await api.get("/api/admin/directores");

	render(directores);

	bindEvents();
});


function render(directores) {

    const select = document.getElementById("director");
	
	select.innerHTML = '<option value="">-- Selecciona director --</option>';

    directores.forEach(d => {

        const option = document.createElement("option");

        option.value = d.id;
        option.textContent = d.nombre;

        select.appendChild(option);
    });
}

function bindEvents() {
	const form = document.getElementById("form-pelicula");
	bind(form, "submit", guardar);
}

async function guardar(e) {

    e.preventDefault();

	const form = e.target;
	
    await api.post("/api/admin/peliculas", 	{

		titulo: form.titulo.value,
		anyo: form.anyo.value,
		duracion: form.duracion.value,
		sinopsis: form.sinopsis.value,
		director_id: form.director.value

	});

    location.replace("index.html");
}
