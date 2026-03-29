const params = new URLSearchParams(window.location.search);
const id = params.get("id");

document.addEventListener("DOMContentLoaded", async () => {
  const res = await fetch(`/api/admin/usuarios/${id}`);
  const u = await res.json();

  document.getElementById("usuario").innerHTML = `
    <p><b>Nombre:</b> ${u.nombre} ${u.apellidos}</p>
    <p><b>Email:</b> ${u.email}</p>
    <p><b>Teléfono:</b> ${u.telefono}</p>
    <p><b>Rol:</b> ${u.rol}</p>
	<p><b>Fecha alta:</b> ${u.fecha_alta}</p>
  `;
});