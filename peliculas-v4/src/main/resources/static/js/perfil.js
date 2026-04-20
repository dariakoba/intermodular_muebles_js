document.addEventListener("DOMContentLoaded", async () => {

  // =========================
  // 👤 DATOS USUARIO
  // =========================
  try {
    const res = await fetch("/api/me");

    if (!res.ok) {
      window.location.href = "/login.html";
      return;
    }

    const user = await res.json();

    document.getElementById("nombre").textContent = user.nombre;
    document.getElementById("email").textContent = user.email;
    document.getElementById("telefono").textContent = user.telefono ?? "-";
    document.getElementById("puntos").textContent = user.puntos + " puntos";

  } catch (err) {
    console.error("Error usuario:", err);
    window.location.href = "/login.html";
  }

  // =========================
  // 📦 PEDIDOS
  // =========================
  try {
    const res = await fetch("/api/carrito/mis");

    if (!res.ok) return;

    const pedidos = await res.json();

    const tbody = document.getElementById("pedidos-body");

    let html = "";

    pedidos.forEach(p => {
      html += `
        <tr>
          <td>${p.idPedido}</td>
          <td>${p.fecha}</td>
          <td>${p.total} €</td>
          <td>
            <span class="estado ${p.estadoPago}">
              ${p.estadoPago}
            </span>
          </td>
        </tr>
      `;
    });

    tbody.innerHTML = html;

  } catch (err) {
    console.error("Error pedidos:", err);
  }

});