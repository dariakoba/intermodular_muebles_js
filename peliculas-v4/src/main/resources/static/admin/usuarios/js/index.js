document.addEventListener("DOMContentLoaded", () => {
  cargarUsuarios();
});

async function cargarUsuarios() {
  const tbody = document.querySelector("#tablaUsuarios tbody");

  try {
    const res = await fetch("/api/admin/usuarios");
    const usuarios = await res.json();

    tbody.innerHTML = "";

    usuarios.forEach(u => {
      tbody.innerHTML += `
        <tr>
          <td>${u.id}</td>
          <td>${u.nombre} ${u.apellidos}</td>
          <td>${u.email}</td>
          <td>${u.rol}</td>
          <td>${u.telefono ?? ""}</td>
          <td>
            <a href="show.html?id=${u.id}">
              <button>Ver</button>
            </a>
            <a href="edit.html?id=${u.id}">
              <button>Editar</button>
            </a>
            <button onclick="borrarUsuario(${u.id})">Borrar</button>
          </td>
        </tr>
      `;
    });

  } catch (err) {
    console.error("Error al cargar usuarios:", err);
  }
}