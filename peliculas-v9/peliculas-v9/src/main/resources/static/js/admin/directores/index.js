import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";
import { e }     from "/js/core/utils.js";

app.run(async () => {

    await guard.requireRole("ADMIN");

    const directores = await api.get("/api/admin/directores");

    render(directores);

    bindEvents();
});

function render(directores) {

    const tabla = document.getElementById("tabla-directores");

    tabla.innerHTML = directores.map(d => `
        <tr>
            <td>${e(d.id)}</td>

            <td>
                ${
                    d.imagen
                        ? `<img src="${e(d.imagen)}"
                                alt="${e(d.nombre)}"
                                style="width:60px;height:60px;object-fit:cover"
                                class="img-thumbnail">`
                        : `<span class="text-muted">-</span>`
                }
            </td>

            <td>${e(d.nombre)}</td>
            <td>${e(d.pais ?? "")}</td>

            <td>
				<div class="d-flex gap-1">
					<a href="show.html?id=${d.id}" class="btn btn-sm btn-info">
				        Ver
				    </a>
					
	                <a href="edit.html?id=${d.id}" class="btn btn-sm btn-warning">
	                    Editar
	                </a>
	
	                <button
	                    class="btn btn-sm btn-danger"
	                    data-action="eliminar"
	                    data-id="${d.id}">
	                    Eliminar
	                </button>
				</div>
            </td>
        </tr>
    `).join("");
}

function bindEvents() {
    const tabla = document.getElementById("tabla-directores");
    bind(tabla, "click", onAction);
}

async function onAction(e) {

    const el = e.target.closest("[data-action]");
    if (!el) return;

    const action = el.dataset.action;
    const id = Number(el.dataset.id);

    if (action === "eliminar") {

        if (!confirm("¿Eliminar este director?")) return;

        await api.delete(`/api/admin/directores/${id}`);

        el.closest("tr").remove();
    }
}