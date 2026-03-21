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

    location.href = "index.html";
}

async function init() {
	await guard.requireRole("ADMIN");
	await cargarDirectores();
}

init();


