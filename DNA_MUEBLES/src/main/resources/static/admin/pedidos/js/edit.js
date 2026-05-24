import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

const flujoEstados = {
    'pendiente de pago': ['Pendiente de pago', 'Pagado'],
    'pagado': ['Pagado', 'En proceso'],
    'en proceso': ['En proceso', 'Enviando'],
    'enviando': ['Enviando', 'Recibido'],
    'recibido': ['Recibido', 'Devuelto'], 
    'devuelto': ['Devuelto'],             
    'cancelado': ['Cancelado']            
};

app.run(async () => {
    await guard.requireRole("admin");
    const id = new URLSearchParams(window.location.search).get("id");
    if (!id) return (location.href = "index.html");

    const pedidos = await api.get("/api/carrito/admin/lista");
    const p = pedidos.find(x => x.idPedido == id);
    
    document.getElementById("titulo").textContent = "Editar Pedido #" + id;
    document.getElementById("cliente").value = p.clienteNombre || "";
    document.getElementById("email").value = p.email || "";
    document.getElementById("telefono").value = p.telefono || "";
    document.getElementById("direccion").value = p.direccion || "";
    
    const inputFecha = document.getElementById("fecha");
    
    const hoy = new Date().toISOString().split('T')[0];
    inputFecha.setAttribute('max', hoy);
    
    if (p.fecha) {
        inputFecha.value = new Date(p.fecha).toISOString().split('T')[0];
    }

    const estadoActual = p.estadoPago || 'Pendiente de pago';
    const selectEstado = document.getElementById("estado");
    
    selectEstado.innerHTML = ''; 
    
    const estadoKey = estadoActual.toLowerCase();
    const opcionesPermitidas = flujoEstados[estadoKey] || [estadoActual]; 
    
    opcionesPermitidas.forEach(estado => {
        const option = document.createElement('option');
        option.value = estado;
        option.textContent = estado;
        if (estado.toLowerCase() === estadoKey) option.selected = true; 
        selectEstado.appendChild(option);
    });

   
    bind(document.getElementById("form-pedido"), "submit", async (e) => {
        e.preventDefault();
        
        if (document.getElementById("fecha").value > hoy) {
            alert("Error: No puedes poner una fecha en el futuro.");
            return;
        }

        const payload = {
            clienteNombre: document.getElementById("cliente").value,
            email: document.getElementById("email").value,
            telefono: document.getElementById("telefono").value,
            direccion: document.getElementById("direccion").value,
            fecha: document.getElementById("fecha").value,
            estado: document.getElementById("estado").value
        };
        try {
            await api.put(`/api/carrito/admin/editar/${id}`, payload);
            alert("Pedido actualizado correctamente");
            location.href = "index.html";
        } catch (err) {
            alert("Error al actualizar el pedido");
        }
    });
});