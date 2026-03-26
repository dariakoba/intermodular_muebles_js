document.addEventListener("DOMContentLoaded", () => {

  const form = document.getElementById("form-usuario");
  const errorDiv = document.getElementById("error");
  const successDiv = document.getElementById("success");

  async function cargarUsuarios() {
    const tbody = document.querySelector("#tablaUsuarios tbody");
    if (!tbody) return;

    try {
      const res = await fetch("/api/admin/usuarios");
      const usuarios = await res.json();

      tbody.innerHTML = "";
      usuarios.forEach(u => {
        const fila = `
          <tr>
            <td>${u.id}</td>
            <td>${u.nombre} ${u.apellidos}</td>
            <td>${u.email}</td>
            <td>${u.rol}</td>
            <td>${u.telefono ?? ""}</td>
            <td>
              <button onclick="editarUsuario(${u.id})">Editar</button>
              <button onclick="borrarUsuario(${u.id})">Borrar</button>
            </td>
          </tr>
        `;
        tbody.innerHTML += fila;
      });

    } catch (err) {
      console.error("Error al cargar usuarios:", err);
    }
  }

  async function borrarUsuario(id) {
    if (!confirm("¿Borrar usuario?")) return;
    await fetch(`/api/admin/usuarios/${id}`, { method: "DELETE" });
    cargarUsuarios();
  }

  window.editarUsuario = async function(id) {
    try {
      const res = await fetch(`/api/admin/usuarios/${id}`);
      const u = await res.json();

      document.getElementById("userId").value = u.id;
      document.getElementById("nombre").value = u.nombre;
      document.getElementById("apellidos").value = u.apellidos;
      document.getElementById("email").value = u.email;
      document.getElementById("telefono").value = u.telefono;
      document.getElementById("direccion").value = u.direccion;
      document.getElementById("rol").value = u.rol;

      const passInput = document.getElementById("password");
      if (passInput) passInput.value = "";

      const title = document.getElementById("formTitle");
      if (title) title.innerText = "Editar Usuario";

    } catch (err) {
      console.error("Error al editar usuario:", err);
    }
  };

  function limpiarFormulario() {
    const campos = ["userId","nombre","apellidos","email","telefono","direccion","password","rol"];
    campos.forEach(id => {
      const el = document.getElementById(id);
      if (el) el.value = "";
    });

    const title = document.getElementById("formTitle");
    if (title) title.innerText = "Crear Usuario";
  }

  form?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("userId")?.value.trim();
    const password = document.getElementById("password")?.value;

    const usuario = {
      nombre: document.getElementById("nombre")?.value.trim(),
      apellidos: document.getElementById("apellidos")?.value.trim(),
      email: document.getElementById("email")?.value.trim(),
      telefono: document.getElementById("telefono")?.value.trim(),
      direccion: document.getElementById("direccion")?.value.trim(),
      rol: document.getElementById("rol")?.value,
      estado: "activo"
    };

    if (password && password.trim() !== "") {
      usuario.password_hash = password.trim(); // 🔹 solo si tiene valor
    } else if (!id) {
      errorDiv.textContent = "La contraseña es obligatoria al crear un usuario";
      errorDiv.style.display = "block";
      return;
    }

    try {
      let res;
      if (id) {
		Console.log(usuario);
        res = await fetch(`/api/admin/usuarios/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(usuario)
        });
      } else {
        res = await fetch("/api/admin/usuarios", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(usuario)
        });
      }

      if (res.ok) {
        errorDiv.style.display = "none";
        successDiv.style.display = "block";
        limpiarFormulario();
        cargarUsuarios();
      } else {
        const text = await res.text();
        errorDiv.textContent = "Error: " + text;
        errorDiv.style.display = "block";
        successDiv.style.display = "none";
      }

    } catch (err) {
      console.error(err);
      errorDiv.textContent = "Error de conexión";
      errorDiv.style.display = "block";
      successDiv.style.display = "none";
    }
  });

  // cargar la tabla al inicio
  cargarUsuarios();
});