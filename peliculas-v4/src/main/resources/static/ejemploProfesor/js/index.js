async function cargarPeliculas() {

    const response = await fetch("/api/peliculas");
    const peliculas = await response.json();

    const lista = document.getElementById("lista-peliculas");

    peliculas.forEach(p => {

        const li = document.createElement("li");

        // clase de Bootstrap
        li.classList.add("list-group-item");

        li.innerHTML =
            `<a href="show.html?id=${p.id}">
                ${p.titulo} (${p.anyo})
             </a>`;

        lista.appendChild(li);
    });
}

cargarPeliculas();