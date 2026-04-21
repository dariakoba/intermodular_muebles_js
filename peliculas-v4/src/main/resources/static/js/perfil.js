document.addEventListener("DOMContentLoaded", async () => {

  // =========================
  // 👤 1. CARGAR DATOS USUARIO
  // =========================
  try {
    const res = await fetch("/api/me");

    if (!res.ok) {
      window.location.href = "/login.html";
      return;
    }

    const user = await res.json();

    // Rellenamos los campos de texto
    document.getElementById("nombre").textContent = user.nombre;
    document.getElementById("email").textContent = user.email;
    document.getElementById("telefono").textContent = user.telefono ?? "-";
    document.getElementById("direccion").textContent = user.direccion ?? "No definida"; // Dirección añadida
    document.getElementById("puntos").textContent = (user.puntos ?? 0) + " puntos";

  } catch (err) {
    console.error("Error usuario:", err);
    window.location.href = "/login.html";
  }

  // =========================
    // 📦 2. CARGAR PEDIDOS (Actualizado para mostrar productos)
    // =========================
    try {
      const res = await fetch("/api/carrito/mis");
      const tbody = document.getElementById("pedidos-body");

      if (!res.ok) {
          tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;">No se pudieron cargar los pedidos</td></tr>`;
          return;
      }

      const pedidos = await res.json();

      if (pedidos.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;">Aún no has realizado ningún pedido.</td></tr>`;
        return;
      }

      let html = "";
      pedidos.forEach(p => {
        const estadoClase = p.estadoPago ? p.estadoPago.toLowerCase() : "pendiente";
        
        html += `
          <tr>
            <td>#${p.idPedido}</td>
            <td>${p.fecha || "---"}</td>
            <td>
              <strong>${p.total.toFixed(2)} €</strong>
              <br>
              <small style="color: #887a69; display: block; margin-top: 4px;">
                  ${p.nombreProducto || "Mueble DNA"}
              </small>
            </td>
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
      document.getElementById("pedidos-body").innerHTML = `<tr><td colspan="4">Error de conexión.</td></tr>`;
    }

  // =========================
  // ✏️ 3. LÓGICA DE EDICIÓN
  // =========================
  const btnEdit = document.getElementById("btn-edit");
  const btnCancel = document.getElementById("btn-cancel");
  const btnSave = document.getElementById("btn-save");
  const infoDiv = document.getElementById("perfil-info");
  const formDiv = document.getElementById("perfil-form");

  // --- ABRIR EDICIÓN ---
  btnEdit.addEventListener("click", () => {
    // Pasamos el texto actual a los inputs
    document.getElementById("input-nombre").value = document.getElementById("nombre").textContent;
    document.getElementById("input-email").value = document.getElementById("email").textContent;
    document.getElementById("input-telefono").value = document.getElementById("telefono").textContent;
    document.getElementById("input-direccion").value = document.getElementById("direccion").textContent;
    
    // Cambiamos visibilidad
    infoDiv.style.display = "none";
    formDiv.style.display = "block";
    btnEdit.style.display = "none";
  });

  // --- CANCELAR EDICIÓN ---
  btnCancel.addEventListener("click", () => {
    infoDiv.style.display = "block";
    formDiv.style.display = "none";
    btnEdit.style.display = "inline-flex";
  });

  // --- GUARDAR CAMBIOS ---
  btnSave.addEventListener("click", async () => {
    const nombre = document.getElementById("input-nombre").value.trim();
    const email = document.getElementById("input-email").value.trim();
    const telefono = document.getElementById("input-telefono").value.trim();
    const direccion = document.getElementById("input-direccion").value.trim();

    // Validación simple
    if (!nombre || !email || !telefono || !direccion) {
      alert("Por favor, rellena todos los campos obligatorios.");
      return;
    }

    const updatedUser = { nombre, email, telefono, direccion };

    try {
      const res = await fetch("/api/users/update-me", { // Asegúrate que esta ruta coincide con tu Controller
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedUser)
      });

      if (res.ok) {
        alert("¡Datos actualizados correctamente!");
        location.reload(); // Recarga para ver los cambios reflejados
      } else {
        alert("Error al actualizar los datos.");
      }
    } catch (err) {
      console.error("Error en el PUT:", err);
      alert("Error de conexión con el servidor.");
    }
  });

});