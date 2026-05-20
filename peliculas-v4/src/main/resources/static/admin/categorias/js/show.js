import { app } from "/js/core/app.js";
import { api } from "/js/core/api.js";

app.run(async () => {

    const id = obtenerId();

    const p = await api.get(`/api/admin/categorias/${id}`);

    console.log(p);

    document.getElementById("id").textContent =
        p.id_categoria;

    document.getElementById("categoria").textContent =
        p.nombre;

    let estado;

    if (!p.deleted_at) {

        estado = "Activo";

    } else {

        estado = "Inactivo";
    }

    document.getElementById("estado").textContent =
        estado;

    document.getElementById("btn-editar").href =
        `edit.html?id=${id}`;
});

function obtenerId() {

    const params =
        new URLSearchParams(window.location.search);

    return params.get("id");
}