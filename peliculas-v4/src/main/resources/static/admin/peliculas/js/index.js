async function cargarPeliculas() {

    const response = await fetch("/api/admin/peliculas");
    const peliculas = await response.json();

    const tabla = document.getElementById("tabla-peliculas");

    peliculas.forEach(p => {

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${p.titulo}</td>
            <td>${p.anyo}</td>
			<td>${p.duracion}</td>
            <td>

                <a href="show.html?id=${p.id}"
                   class="btn btn-sm btn-info">
                   Ver
                </a>

                <a href="edit.html?id=${p.id}"
                   class="btn btn-sm btn-warning">
                   Editar
                </a>

                <button class="btn btn-sm btn-danger"
                        onclick="eliminar(${p.id})">
                        Eliminar
                </button>

            </td>
        `;

        tabla.appendChild(tr);
    });
}

async function eliminar(id) {

    if (!confirm("¿Eliminar esta película?")) return;

    await fetch(`/api/admin/peliculas/${id}`, {
        method: "DELETE"
    });

    location.reload();
}

cargarPeliculas();