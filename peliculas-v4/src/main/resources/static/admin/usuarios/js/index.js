document.addEventListener("DOMContentLoaded", () => { 
  cargarUsuarios();

  // Seleccionar todos
  const checkAll = document.getElementById("checkAll");
  checkAll.addEventListener("change", () => {
    document.querySelectorAll("#tablaUsuarios tbody input[type='checkbox']")
            .forEach(cb => cb.checked = checkAll.checked);
  });

  // Eliminar seleccionados
  document.getElementById("btn-eliminar-seleccionados").addEventListener("click", async () => {
    const checkboxes = document.querySelectorAll("#tablaUsuarios tbody input[type='checkbox']:checked");
    const ids = Array.from(checkboxes).map(cb => parseInt(cb.dataset.id));

    if (ids.length === 0) {
      alert("Selecciona al menos un usuario para eliminar");
      return;
    }

    if (!confirm(`¿Seguro que quieres eliminar ${ids.length} usuario(s)?`)) return;

    try {
      await Promise.all(ids.map(id => fetch(`/api/admin/usuarios/${id}`, { method: "DELETE" })));
      cargarUsuarios();
    } catch (err) {
      console.error(err);
      alert("Error al eliminar usuarios");
    }
  });
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
          <td><input type="checkbox" data-id="${u.id}"></td>
          <td>${u.id}</td>
          <td>${u.nombre} ${u.apellidos}</td>
          <td>${u.email}</td>
          <td>${u.rol}</td>
          <td>${u.telefono ?? ""}</td>
          <td class="acciones">
            <a href="show.html?id=${u.id}" class="btn-ver">Ver</a>
            <a href="edit.html?id=${u.id}" class="btn-editar">Editar</a>
            <button class="btn-eliminar" onclick="borrarUsuario(${u.id})">Borrar</button>
          </td>
        </tr>
      `;
    });

  } catch (err) {
    console.error("Error al cargar usuarios:", err);
  }
}

async function borrarUsuario(id) {
  if (!confirm("¿Seguro que quieres eliminar este usuario?")) return;

  try {
    await fetch(`/api/admin/usuarios/${id}`, { method: "DELETE" });
    cargarUsuarios();
  } catch (err) {
    console.error(err);
    alert("Error al eliminar usuario");
  }
}