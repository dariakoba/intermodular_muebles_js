import { auth } from "/js/auth/auth.js";
import { app }  from "/js/core/app.js";
import { api }  from "/js/core/api.js";
import { bind } from "/js/core/events.js";
import { e }    from "/js/core/utils.js";

app.run(async () => {

    await auth.meOptional();
    
	const id = obtenerId();
    if (!id) {
        location.href = "index.html";
        return;
    }

    const pelicula = await api.get(`/api/peliculas/${id}`);
	
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
    document.getElementById("director").textContent = pelicula.director;
    document.getElementById("sinopsis").textContent = pelicula.sinopsis;

    renderRating(pelicula.rating);
	renderVoto(pelicula);
    renderUltimosVotos(pelicula.ultimos_votos);

    document.getElementById("enlace-votos").href =
        `votos.html?id=${pelicula.id}`;
}

function renderRating(rating) {

    const media = document.getElementById("rating-media");
    const total = document.getElementById("rating-total");

    media.textContent = rating.media.toFixed(1);
    total.textContent = rating.total;
}

function renderVoto(pelicula) {

    const container = document.getElementById("seccion-voto");

    if (!auth.currentUser) {
        container.innerHTML = `
            <p class="text-muted">
                Inicia sesión para votar.
            </p>
        `;
        return;
    }

    if (pelicula.user_vote) {
        container.innerHTML = `
            <h2>Tu voto</h2>
            <p>
                <strong>Puntuación:</strong>
                ${e(pelicula.user_vote.puntuacion)}/10
            </p>
            <p>
                ${e(pelicula.user_vote.critica || "")}
            </p>
        `;
        return;
    }

    // puede votar
    container.innerHTML = `
        <h2>Votar</h2>

        <form id="form-voto">

            <div class="mb-3">
                <label class="form-label">Puntuación</label>
                <input id="puntuacion" name="puntuacion" type="number" min="1" max="10" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Crítica</label>
                <textarea id="critica" name="critica" class="form-control"></textarea>
            </div>

            <button class="btn btn-primary">
                Enviar voto
            </button>

        </form>
    `;

	const form = document.getElementById("form-voto");
	
    bind(form, "submit", async (e) => {
		e.preventDefault();

		const form = e.target;
				
		await api.post(`/api/peliculas/${pelicula.id}/voto`, {
			puntuacion: form.puntuacion.value,
			critica: form.critica.value,
		});
		
		location.reload();
	});
}

function renderUltimosVotos(votos) {

    const container = document.getElementById("ultimos-votos");

    if (!votos || votos.length === 0) {
        container.innerHTML = `
            <p class="text-muted">Todavía no hay críticas.</p>
        `;
        return;
    }

    container.innerHTML = votos.map(v => `
        <div class="card mb-3">
            <div class="card-body">
                <h3 class="h6 mb-2">
                    ${e(v.user)} · ${e(v.puntuacion)}/10
                </h3>

                <p class="mb-2">${e(v.critica || "")}</p>

                <small class="text-muted">
                    ${e(v.fecha)}
                </small>
            </div>
        </div>
    `).join("");
}