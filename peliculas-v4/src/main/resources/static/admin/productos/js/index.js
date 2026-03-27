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
    const tbody = document.querySelector("#tabla-productos tbody");
    tbody.innerHTML = productos.map(p => `
        <tr>
            <td><input type="checkbox" class="check-fila" data-id="${p.id_producto}"></td>
            <td>${e(p.nombre)}</td>
            <td>${e(p.color)}</td>
            <td>${e(p.precio)}€</td>
            <td>${e(p.stock)}</td>
            <td>${e(p.categoria_id || "-")}</td>
            <td class="acciones">
                <a href="show.html?id=${p.id_producto}" class="btn-ver">Ver</a>
                <a href="edit.html?id=${p.id_producto}" class="btn-editar">Editar</a>
             
            </td>
        </tr>
    `).join("");
}

function bindEvents() {
    const tabla = document.getElementById("tabla-productos");
    bind(tabla, "click", onAction);

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