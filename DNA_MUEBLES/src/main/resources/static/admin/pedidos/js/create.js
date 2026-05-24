import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {
    await guard.requireRole("admin");

    bind(document.getElementById("form-create"), "submit", async (e) => {
        e.preventDefault();
        
        const payload = {
            pedido: {
                clienteNombre: document.getElementById("cliente").value,
                total: parseFloat(document.getElementById("total").value),
                metodoPago: document.getElementById("metodo").value,
                estadoPago: document.getElementById("estado").value,
                activo: 1
            },
            direccion: document.getElementById("direccion").value,
            productos: [] // Al ser manual por admin, los productos se añadirían en otra lógica avanzada
        };

        try {
            await api.post(`/api/carrito/comprar`, payload); // Reutilizamos el endpoint de compra
            alert("Pedido creado correctamente");
            location.href = "index.html";
        } catch (err) {
            alert("Error al crear el pedido en el servidor.");
        }
    });
});