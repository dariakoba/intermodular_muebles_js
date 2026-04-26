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
			<td>${e(p.estado)}</td>

            <td class="acciones">
                <a href="show.html?id=${p.id_categoria}" class="btn-ver">Ver</a>
                <a href="edit.html?id=${p.id_categoria}" class="btn-editar">Editar</a>
				
			
				 
				 
				 ${
			 		    p.estado === "activo"
			 		        ? `<button class="btn-desactivar" onclick="desactivarCategoria(${p.id_categoria})">
			 		                Desactivar
			 		           </button>`
			 		        : `<button class="btn-activar" onclick="activarCategoria(${p.id_categoria})">
			 		                Activar
			 		           </button>`
				 }
				 
            </td>
        </tr>
    `).join("");
}

async function desactivarCategoria(id) {
   // if (!confirm("¿Seguro que quieres eliminar esta categoria?")) return;

    try {
        await api.put(`/api/admin/categorias/${id}/desactivar`);

        // Recargar lista completa
        const categorias = await api.get("/api/admin/categorias");
        render(categorias);

    } catch (err) {
        console.error(err);
        alert("Error al desactivarr producto");
    }
}


/*
async function borrarProducto(id) {
    if (!confirm("¿Seguro que quieres eliminar este producto?")) return;

    try {
        await api.delete(`/api/admin/productos/${id}`);

        // Recargar lista completa
        const productos = await api.get("/api/admin/productos");
        render(productos);

    } catch (err) {
        console.error(err);
        alert("Error al eliminar producto");
    }
}
*/

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
            alert("No has seleccionado ningún producto.");
            return;
        }

        if (!confirm(`¿Eliminar ${seleccionados.length} producto(s)?`)) return;

        await Promise.all(seleccionados.map(id =>
            api.delete(`/api/admin/categorias/${id}`)
        ));

        seleccionados.forEach(id => {
            const cb = document.querySelector(`.check-fila[data-id="${id}"]`);
            if (cb) cb.closest("tr").remove();
        });

        // Resetear check-all
        document.getElementById("check-all").checked = false;
    });
}

/*
async function onAction(e) {
    const el = e.target.closest("[data-action]");
    if (!el) return;

    const action = el.dataset.action;
    const id = Number(el.dataset.id);

    if (action === "eliminar") {
        if (!confirm("¿Eliminar este producto?")) return;
        await api.delete(`/api/admin/productos/${id}`);
        el.closest("tr").remove();
    }
}
*/
window.desactivarCategoria = desactivarCategoria;