import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";
import { e } from "/js/core/utils.js";

app.run(async () => {
	await guard.requireRole("ADMIN");
	
	const peliculas = await api.get("/api/admin/peliculas");
	
	render(peliculas);
	
	bindEvents();
});

function render(peliculas) {

	const tabla = document.getElementById("tabla-peliculas");

	tabla.innerHTML = peliculas.map(p => `
		<tr>
			<td>${e(p.titulo)}</td>
			<td>${e(p.anyo)}</td>
			<td>${e(p.duracion)}</td>
			<td>
				<a href="show.html?id=${p.id}" class="btn btn-sm btn-info">
					Ver
				</a>

				<a href="edit.html?id=${p.id}" class="btn btn-sm btn-warning">
					Editar
				</a>

				<button
					class="btn btn-sm btn-danger"
					data-action="eliminar"
					data-id="${p.id}">
					Eliminar
				</button>
			</td>
		</tr>
	`).join("");
}

function bindEvents() {
	const tabla = document.getElementById("tabla-peliculas");
	bind(tabla, "click", onAction);
}

async function onAction(e) {

    const el = e.target.closest("[data-action]");
    if (!el) return;

    const action = el.dataset.action;
    const id = Number(el.dataset.id);

    if (action === "eliminar") {

        if (!confirm("¿Eliminar esta película?")) return;

        await api.delete(`/api/admin/peliculas/${id}`);

        el.closest("tr").remove();
    }
}
