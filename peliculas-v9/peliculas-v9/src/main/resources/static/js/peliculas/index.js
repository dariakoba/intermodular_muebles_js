import { auth } from "/js/auth/auth.js";
import { api }  from "/js/core/api.js";
import { app }  from "/js/core/app.js";
import { e }    from "/js/core/utils.js";

app.run(async () => {
    const user = await auth.meOptional();
    const peliculas = await api.get("/api/peliculas");
    render(peliculas, user);
});

function render(peliculas, user) {

    const lista = document.getElementById("lista-peliculas");

    lista.innerHTML = "";

    if (peliculas.length === 0) {
        lista.innerHTML = `
            <li class="list-group-item text-muted">
                No hay películas
            </li>
        `;
        return;
    }

    peliculas.forEach(p => {

        const li = document.createElement("li");

        const url = `show.html?id=${p.id}`;

        li.className = "list-group-item d-flex justify-content-between align-items-center";

        li.innerHTML = `
            <a href="${url}" 
               class="text-decoration-none text-dark">
                ${e(p.titulo)} (${e(p.anyo)})
            </a>

            <a href="${url}" 
               class="btn btn-sm btn-outline-primary">
                Ver
            </a>
        `;

        lista.appendChild(li);
    });
}