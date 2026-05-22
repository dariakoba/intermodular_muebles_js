async function cargarPedidos() {
    try {
        const response = await fetch('/api/admin/pedidos');
        const pedidos = await response.json();
        
        const tbody = document.getElementById('tabla-pedidos-body');
        tbody.innerHTML = "";

        pedidos.forEach(p => {
            // Ajustamos los nombres de los campos a tu Clase Pedido de Java
            // Si en tu Java es idPedido, usamos p.idPedido
            const id = p.idPedido || p.id; 
            const fecha = p.fechaPedido || p.fecha || "Reciente";
            const metodo = p.metodoPago || "No especificado";
            const estado = p.estadoPago || "Pendiente";
            const total = p.precio || p.total || 0;

            const claseEstado = estado.toLowerCase() === 'pagado' ? 'pago-si' : 'pago-no';

            tbody.innerHTML += `
                <tr>
                    <td>#${id}</td>
                    <td>${fecha}</td>
                    <td>${total}€</td>
                    <td>${metodo}</td>
                    <td><span class="pago-badge ${claseEstado}">${estado}</span></td>
                    <td>
                        <button class="btn-delete" onclick="eliminarPedido(${id})">Eliminar</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("Error al cargar pedidos:", error);
    }
}

// Función para conectar con el @DeleteMapping("/{id}") de tu controlador
async function eliminarPedido(id) {
    if (!confirm("¿Seguro que quieres borrar el pedido #" + id + "?")) return;

    try {
        const res = await fetch(`/api/admin/pedidos/${id}`, { method: 'DELETE' });
        if (res.ok) {
            alert("Pedido eliminado");
            cargarPedidos(); // Recargamos la lista
        }
    } catch (e) {
        alert("Error al borrar");
    }
}

document.addEventListener("DOMContentLoaded", cargarPedidos);