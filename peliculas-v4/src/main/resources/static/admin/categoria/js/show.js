function obtenerId() {

    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarPelicula() {

    const id = obtenerId();

    const response = await fetch(`/api/admin/peliculas/${id}`);
    const p = await response.json();

    document.getElementById("titulo").textContent = p.titulo;
    document.getElementById("anyo").textContent = p.anyo;
    document.getElementById("duracion").textContent = p.duracion;
    document.getElementById("director").textContent = p.director;
    document.getElementById("sinopsis").textContent = p.sinopsis;

    document.getElementById("editar").href = `edit.html?id=${id}`;
}

cargarPelicula();