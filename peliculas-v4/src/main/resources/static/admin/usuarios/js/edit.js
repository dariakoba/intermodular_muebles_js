document.addEventListener("DOMContentLoaded", () => {

  const params = new URLSearchParams(window.location.search);
  const id = params.get("id");

  const form = document.getElementById("form-usuario");

  const nombre = document.getElementById("nombre");
  const apellidos = document.getElementById("apellidos");
  const email = document.getElementById("email");
  const telefono = document.getElementById("telefono");
  const direccion = document.getElementById("direccion");
  const rol = document.getElementById("rol");
  const password = document.getElementById("password");
  const estado = document.getElementById("estado");

  const puntos = document.getElementById("puntos");
  const salario = document.getElementById("salario");

  let usuarioOriginal = {};

  function normalizar(valor) {
    return (valor || "").toString().trim().toLowerCase();
  }

  async function cargarUsuario() {
    try {
      const res = await fetch(`/api/admin/usuarios/${id}`);
      const u = await res.json();

      usuarioOriginal = u;

      nombre.value = u.nombre || "";
      apellidos.value = u.apellidos || "";
      email.value = u.email || "";
      telefono.value = u.telefono || "";
      direccion.value = u.direccion || "";

      rol.value = normalizar(u.rol) || "cliente";
      estado.value = normalizar(u.estado) || "activo";

      if (puntos) puntos.value = u.puntos ?? "";
      if (salario) salario.value = u.salario ?? "";

      toggleCampos();

    } catch (err) {
      console.error("Error cargando usuario:", err);
    }
  }

  function toggleCampos() {
    if (rol.value === "cliente") {
      if (puntos) puntos.style.display = "block";
      if (salario) salario.style.display = "none";
    } else {
      if (puntos) puntos.style.display = "none";
      if (salario) salario.style.display = "block";
    }
  }

  rol.addEventListener("change", toggleCampos);

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const usuario = {
      nombre: nombre.value.trim() || usuarioOriginal.nombre,
      apellidos: apellidos.value.trim() || usuarioOriginal.apellidos,
      email: email.value.trim() || usuarioOriginal.email,
      telefono: telefono.value.trim() || usuarioOriginal.telefono,
      direccion: direccion.value.trim() || usuarioOriginal.direccion,
      rol: normalizar(rol.value) || normalizar(usuarioOriginal.rol),
      estado: normalizar(estado.value) || normalizar(usuarioOriginal.estado),

      puntos: rol.value === "cliente"
        ? (puntos.value !== "" ? parseInt(puntos.value) : usuarioOriginal.puntos)
        : 0,

      salario: rol.value === "admin"
        ? (salario.value !== "" ? parseFloat(salario.value) : usuarioOriginal.salario)
        : 0,

      fecha_alta: usuarioOriginal.fecha_alta
    };

    // 🔐 CONTRASEÑA: solo incluir si hay valor
    const passValue = password.value.trim();
    if (passValue !== "") {
      usuario.password_hash = passValue; // backend hará hash
    }

    try {
      const res = await fetch(`/api/admin/usuarios/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(usuario)
      });

      if (res.ok) {
        window.location.href = "index.html";
      } else {
        const error = await res.text();
        console.error(error);
        alert("Error: " + error);
      }
    } catch (err) {
      console.error(err);
      alert("Error de conexión");
    }
  });

  cargarUsuario();
});