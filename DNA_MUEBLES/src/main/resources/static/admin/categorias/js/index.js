import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";
import { e }     from "/js/core/utils.js";

app.run(async () => {
    await guard.requireRole("admin");

    const categorias = await api.get("/api/admin/categorias");

    render(categorias);
    bindEvents();
});

function render(categorias) {
	console.log(categorias);
    const tbody = document.querySelector("#tabla-productos tbody");
    tbody.innerHTML = categorias.map(p => `
        <tr>
            <td><input type="checkbox" class="check-fila" data-id="${p.id_categoria}"></td>
			<td>${e(p.id_categoria)}</td>
            <td>${e(p.nombre)}</td>
			<td>
			${
						    p.estado === "activo"
						      ? `<span class="badge badge-activo">Activo</span>`
						      : `<span class="badge badge-inactivo">Inactivo</span>`
						  }
			</td>

			<td>
						  <div class="acciones">

						    <a href="show.html?id=${p.id_categoria}" class="btn-ver" title="Ver detalles">
									<i class="fa-solid fa-eye"></i>
						    </a>

						    <a href="edit.html?id=${p.id_categoria}" class="btn-editar" title="Editar producto">
								<i class="fa-solid fa-pen"></i>
						    </a>

						    ${
						      p.estado === "activo"
						        ? `
						          <button class="btn-desactivar"
						                  onclick="desactivarCategoria(${p.id_categoria})"
						                  title="Desactivar producto">
						            <span class="material-symbols-outlined">block</span>
						          </button>
						        `
						        : `
						          <button class="btn-activar"
						                  onclick="activarCategoria(${p.id_categoria})"
						                  title="Activar producto">
						            <span class="material-symbols-outlined">check_circle</span>
						          </button>
						        `
						    }

						  </div>
						</td>
        </tr>
    `).join("");
}

async function desactivarCategoria(id) {
    try {
        await api.put(`/api/admin/categorias/${id}/desactivar`);

        const categorias = await api.get("/api/admin/categorias");
        render(categorias);

    } catch (err) {
        console.error(err);
        alert("No se puede desactivar una categoría con productos asociados.");
    }
}


async function activarCategoria(id) {

    try {
        await api.put(`/api/admin/categorias/${id}/activar`);

        // Recargar lista completa
        const categorias = await api.get("/api/admin/categorias");
        render(categorias);

    } catch (err) {
        console.error(err);
        alert("Error al activarr producto");
    }
}



function bindEvents() {
    const tabla = document.getElementById("tabla-productos");

    document.getElementById("check-all").addEventListener("change", (ev) => {
        document.querySelectorAll(".check-fila").forEach(cb => {
            cb.checked = ev.target.checked;
        });
    });

    document.getElementById("btn-eliminar-seleccionados").addEventListener("click", async () => {
        const seleccionados = [...document.querySelectorAll(".check-fila:checked")]
            .map(cb => Number(cb.dataset.id));

        if (seleccionados.length === 0) {
            alert("No has seleccionado ningúna categoría.");
            return;
        }

        if (!confirm(`¿Eliminar ${seleccionados.length} categoría(s)?`)) return;

		try {

		    await Promise.all(
		        seleccionados.map(id =>
		            api.delete(`/api/admin/categorias/${id}`)
		        )
		    );

		    seleccionados.forEach(id => {
		        const cb = document.querySelector(`.check-fila[data-id="${id}"]`);
		        if (cb) cb.closest("tr").remove();
		    });

		    document.getElementById("check-all").checked = false;

		} catch (err) {

		    console.error(err);

		    alert("No se puede eliminar una categoría con productos asociados.");
		}
		

        // Resetear check-all
        document.getElementById("check-all").checked = false;
    });
}


window.desactivarCategoria = desactivarCategoria;
window.activarCategoria = activarCategoria;