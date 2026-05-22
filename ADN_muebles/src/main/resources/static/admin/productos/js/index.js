import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";
import { e }     from "/js/core/utils.js";

app.run(async () => {
    await guard.requireRole("admin");

    const productos = await api.get("/api/admin/productos");

    render(productos);
    bindEvents();
});

function render(productos) {
	console.log(productos);
    const tbody = document.querySelector("#tabla-productos tbody");
    tbody.innerHTML = productos.map(p => `
        <tr>
            <td><input type="checkbox" class="check-fila" data-id="${p.id_producto}"></td>
			<td>${e(p.id_producto)}</td>
            <td>${e(p.nombre)}</td>
           
            <td>${e(p.precio)}€</td>
            <td>${e(p.stock)}</td>
			<td>
			  ${
			    p.estado === "activo"
			      ? `<span class="badge badge-activo">Activo</span>`
			      : `<span class="badge badge-inactivo">Inactivo</span>`
			  }
			</td>            <td>${e(p.categoria || "-")}</td>
			<td>
			  <div class="acciones">

			    <a href="show.html?id=${p.id_producto}" class="btn-ver" title="Ver detalles">
						<i class="fa-solid fa-eye"></i>
			    </a>

			    <a href="edit.html?id=${p.id_producto}" class="btn-editar" title="Editar producto">
					<i class="fa-solid fa-pen"></i>
			    </a>

			    ${
			      p.estado === "activo"
			        ? `
			          <button class="btn-desactivar"
			                  onclick="desactivarProducto(${p.id_producto})"
			                  title="Desactivar producto">
			            <span class="material-symbols-outlined">block</span>
			          </button>
			        `
			        : `
			          <button class="btn-activar"
			                  onclick="activarProducto(${p.id_producto})"
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

async function desactivarProducto(id) {

    try {
        await api.put(`/api/admin/productos/${id}/desactivar`);

        // Recargar lista completa
        const productos = await api.get("/api/admin/productos");
        render(productos);

    } catch (err) {
        console.error(err);
        alert("Error al desactivar producto");
    }
}

async function activarProducto(id) {
    try {
        await api.put(`/api/admin/productos/${id}/activar`);

        const productos = await api.get("/api/admin/productos");
        render(productos);

    } catch (err) {
        console.error(err);
        alert("Error al activar producto");
    }
}

function bindEvents() {
    const tabla = document.getElementById("tabla-productos");
    //bind(tabla, "click", onAction);

    // Seleccionar todo
    document.getElementById("check-all").addEventListener("change", (ev) => {
        document.querySelectorAll(".check-fila").forEach(cb => {
            cb.checked = ev.target.checked;
        });
    });

    // Eliminar seleccionados
    document.getElementById("btn-eliminar-seleccionados").addEventListener("click", async () => {
        const seleccionados = [...document.querySelectorAll(".check-fila:checked")]
            .map(cb => Number(cb.dataset.id));

        if (seleccionados.length === 0) {
            alert("No has seleccionado ningún producto.");
            return;
        }

        if (!confirm(`¿Eliminar ${seleccionados.length} producto(s)?`)) return;

        await Promise.all(seleccionados.map(id =>
            api.delete(`/api/admin/productos/${id}`)
        ));

        // Eliminar filas del DOM
        seleccionados.forEach(id => {
            const cb = document.querySelector(`.check-fila[data-id="${id}"]`);
            if (cb) cb.closest("tr").remove();
        });

        // Resetear check-all
        document.getElementById("check-all").checked = false;
    });
}


window.desactivarProducto = desactivarProducto;
window.activarProducto = activarProducto;