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
    document.getElementById("puntos").textContent = (user.puntos ?? 0) + " puntos";

  } catch (err) {
    console.error("Error usuario:", err);
    window.location.href = "/login.html";
  }

  // =========================
  // 📦 PEDIDOS
  // =========================
  try {
    const res = await fetch("/api/carrito/mis");
    const tbody = document.getElementById("pedidos-body");

    if (!res.ok) {
        tbody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No se pudieron cargar los pedidos</td></tr>`;
        return;
    }

    const pedidos = await res.json();

    if (pedidos.length === 0) {
      tbody.innerHTML = `<tr><td colspan="5" style="text-align:center;">Aún no has realizado ningún pedido.</td></tr>`;
      return;
    }

    let html = "";

    pedidos.forEach(p => {
      // Formateamos el estado para CSS (ej: "Pagado" -> "pagado")
      const estadoClase = p.estadoPago ? p.estadoPago.toLowerCase() : "pendiente";
      
      html += `
        <tr>
          <td>#${p.idPedido}</td>
          <td>${p.fecha || "---"}</td>
          <td><strong>${p.nombreProducto || "Mueble Intermodular"}</strong></td>
          <td>${p.total.toFixed(2)} €</td>
          <td>
            <span class="estado ${estadoClase}">
              ${p.estadoPago}
            </span>
          </td>
        </tr>
      `;
    });

    tbody.innerHTML = html;

  } catch (err) {
    console.error("Error pedidos:", err);
    document.getElementById("pedidos-body").innerHTML = `<tr><td colspan="5">Error de conexión al cargar pedidos.</td></tr>`;
  }

});