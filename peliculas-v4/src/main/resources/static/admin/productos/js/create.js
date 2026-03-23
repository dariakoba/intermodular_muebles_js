async function cargarDirectores() {

    const response = await fetch("/api/admin/directores");
    const directores = await response.json();

    const select = document.getElementById("director");

    directores.forEach(d => {

        const option = document.createElement("option");

        option.value = d.id;
        option.textContent = d.nombre;

        select.appendChild(option);
    });
}

async function guardar(e) {

    e.preventDefault();

    const pelicula = {

        titulo: titulo.value,
        anyo: anyo.value,
        duracion: duracion.value,
        sinopsis: sinopsis.value,
        director_id: director.value

    };

    await fetch("/api/admin/peliculas", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(pelicula)
    });

    location.href = "index.html";
}

cargarDirectores();