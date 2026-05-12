import { auth } from "/js/auth/auth.js";
import { app }  from "/js/core/app.js";
import { api }  from "/js/core/api.js";
import { e }    from "/js/core/utils.js";

app.run(async () => {

    await auth.meOptional();

    const id = obtenerId();

    if (!id) {
        location.href = "index.html";
        return;
    }

    const votos = await api.get(`/api/peliculas/${id}/votos`);

    render(votos);

    document.getElementById("volver").href =
        `show.html?id=${id}`;
});

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function render(votos) {

    const container = document.getElementById("lista-votos");

    if (!votos || votos.length === 0) {
        container.innerHTML = `
            <p class="text-muted">No hay votos todavía.</p>
        `;
        return;
    }

    container.innerHTML = votos.map(v => `
        <div class="card mb-3">
            <div class="card-body">

                <h3 class="h6 mb-2">
                    ${e(v.user)} · ${e(v.puntuacion)}/10
                </h3>

                <p class="mb-2">
                    ${e(v.critica || "")}
                </p>

                <small class="text-muted">
                    ${e(v.fecha)}
                </small>

            </div>
        </div>
    `).join("");
}