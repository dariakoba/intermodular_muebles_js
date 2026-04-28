const params = new URLSearchParams(window.location.search);
const id = params.get("id");

document.addEventListener("DOMContentLoaded", async () => {
  try {
    const res = await fetch(`/api/admin/resenyas/${id}`);
    const r = await res.json();

    document.getElementById("resenya-detalle").innerHTML = `
      <table>
        <tr><th>ID</th><td>${r.idResenya}</td></tr>
        <tr><th>Producto</th><td>${r.nombreProducto}</td></tr>
        <tr><th>Usuario</th><td>${r.nombreUsuario}</td></tr>
        <tr><th>Puntuación</th><td>${"★".repeat(r.puntuacion)}</td></tr>
        <tr><th>Comentario</th><td>${r.comentario}</td></tr>
        <tr><th>Fecha</th><td>${new Date(r.fecha).toLocaleString()}</td></tr>
      </table>
    `;
  } catch (err) {
    document.getElementById("resenya-detalle").innerHTML = `<p>Error al cargar detalle.</p>`;
  }
});