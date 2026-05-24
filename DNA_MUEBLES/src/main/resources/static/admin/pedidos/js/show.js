import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";

app.run(async () => {
    await guard.requireRole("admin");
    const id = new URLSearchParams(window.location.search).get("id");
    if (!id) return (location.href = "index.html");

    const pedidos = await api.get("/api/carrito/admin/lista");
    const p = pedidos.find(x => x.idPedido == id);
    if (!p) return (location.href = "index.html");

    document.getElementById("titulo-pedido").textContent = "Pedido #" + p.idPedido;
    document.getElementById("cliente").textContent = p.clienteNombre || "Usuario";
    document.getElementById("fecha").textContent = p.fecha ? new Date(p.fecha).toLocaleDateString() : "---";
    document.getElementById("email").textContent = p.email || "No registrado";
    document.getElementById("telefono").textContent = p.telefono || "No registrado";
    document.getElementById("direccion").textContent = p.direccion || "No registrada";
    document.getElementById("estado").textContent = p.estadoPago || "Pendiente";
    document.getElementById("metodo").textContent = p.metodoPago || "Tarjeta";
    document.getElementById("total").textContent = (p.total || 0).toFixed(2) + "€";

    if (p.nombreProducto) {
        document.getElementById("productos-lista").innerHTML = p.nombreProducto.split('|').join("<br>");
    } else {
        document.getElementById("productos-lista").textContent = "Sin detalles de productos.";
    }
});