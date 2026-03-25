function obtenerId() {

    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarDirectores() {

    const response = await fetch("/api/admin/categorias");
    const directores = await response.json();

    const select = document.getElementById("director");

    directores.forEach(d => {

        const option = document.createElement("option");

        option.value = d.id;
        option.textContent = d.nombre;

        select.appendChild(option);
    });
}

async function cargar() {

    const id = obtenerId();

    const response = await fetch(`/api/admin/productos/${id}`);
    const p = await response.json();

    titulo.value = p.titulo;
    anyo.value = p.anyo;
    duracion.value = p.duracion;
    sinopsis.value = p.sinopsis;
    director.value = p.director_id;
}

async function guardar(e) {

    e.preventDefault();

    const id = obtenerId();

    const pelicula = {

        titulo: titulo.value,
        anyo: anyo.value,
        duracion: duracion.value,
        sinopsis: sinopsis.value,
        director_id: director.value
    };

    await fetch(`/api/admin/productos/${id}`, {

        method: "PUT",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(pelicula)
    });

    location.href = "index.html";
}

async function init() {

    await cargarDirectores();
    await cargar();
}

init();