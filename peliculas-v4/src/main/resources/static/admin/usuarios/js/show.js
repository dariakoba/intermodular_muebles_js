const params = new URLSearchParams(window.location.search);
const id = params.get("id");

document.addEventListener("DOMContentLoaded", async () => {
  try {
    const res = await fetch(`/api/admin/usuarios/${id}`);
    const u = await res.json();

    document.getElementById("usuario").innerHTML = `
      <table>
        <tr><th>Nombre</th><td>${u.nombre} ${u.apellidos}</td></tr>
        <tr><th>Email</th><td>${u.email}</td></tr>
        <tr><th>Teléfono</th><td>${u.telefono}</td></tr>
        <tr><th>Dirección</th><td>${u.direccion}</td></tr>
        <tr><th>Rol</th><td>${u.rol}</td></tr>
        <tr><th>Estado</th><td>${u.estado}</td></tr>
        <tr><th>Fecha alta</th><td>${u.fecha_alta}</td></tr>
        ${u.rol.toLowerCase() === "cliente" ? `<tr><th>Puntos</th><td>${u.puntos ?? 0}</td></tr>` : ""}
        ${u.rol.toLowerCase() === "admin" ? `<tr><th>Salario</th><td>${u.salario ?? 0} €</td></tr>` : ""}
      </table>
    `;
  } catch (err) {
    console.error("Error cargando usuario:", err);
    document.getElementById("usuario").innerHTML = `<p class="error">Error al cargar el usuario.</p>`;
  }
});