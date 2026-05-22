const params = new URLSearchParams(window.location.search);
const id = params.get("id");

document.addEventListener("DOMContentLoaded", async () => {

  const res = await fetch(`/api/admin/usuarios/${id}`);
  const u = await res.json();

  document.getElementById("titulo").textContent =
    "Usuario " + u.id;

  document.getElementById("nombre").textContent =
    (u.nombre ?? "") + " " + (u.apellidos ?? "");

  document.getElementById("email").textContent =
    u.email ?? "---";

  document.getElementById("telefono").textContent =
    u.telefono ?? "---";

  document.getElementById("direccion").textContent =
    u.direccion ?? "---";

  document.getElementById("rol").textContent =
    u.rol ?? "---";

  document.getElementById("estado").textContent =
    u.estado ?? "---";

  let fecha = "---";
  if (u.fecha_alta) {
    const f = new Date(u.fecha_alta);
    fecha = f.toLocaleString("es-ES");
  }
  document.getElementById("fecha_alta").textContent = fecha;

  // 🔥 CAMPOS DINÁMICOS (cliente / admin)
  const extraBox = document.getElementById("extra-box");
  const extraLabel = document.getElementById("extra-label");
  const extraValue = document.getElementById("extra-value");

  if (u.rol && u.rol.toLowerCase() === "cliente") {
    extraBox.style.display = "block";
    extraLabel.textContent = "Puntos";
    extraValue.textContent = u.puntos ?? 0;
  }

  if (u.rol && u.rol.toLowerCase() === "admin") {
    extraBox.style.display = "block";
    extraLabel.textContent = "Salario";
    extraValue.textContent = (u.salario ?? 0) + " €";
  }

});