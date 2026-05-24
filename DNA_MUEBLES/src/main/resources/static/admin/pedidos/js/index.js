import { guard } from "/js/auth/guard.js";
import { app }   from "/js/core/app.js";
import { api }   from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

let pedidosGlobal = [];
let pedidosBorrandoIds = []; 

// Función para asignar el color exacto
function obtenerClaseEstado(estado) {
    const e = (estado || '').toLowerCase();
    if (e.includes('pendiente')) return 'bg-pendiente';
    if (e.includes('pagado')) return 'bg-pagado';
    if (e.includes('proceso')) return 'bg-proceso';
    if (e.includes('enviando') || e.includes('enviado')) return 'bg-enviando';
    if (e.includes('recibido')) return 'bg-recibido';
    if (e.includes('devuelto')) return 'bg-devuelto';
    if (e.includes('cancelado')) return 'bg-cancelado';
    return 'bg-pendiente';
}

app.run(async () => {
    await guard.requireRole("admin");

    const inputFiltro = document.getElementById("filtro-fecha");
    if (inputFiltro) {
        const hoy = new Date().toISOString().split('T')[0];
        inputFiltro.setAttribute("max", hoy);
    }

    await cargarYRenderizar();
    bindEvents();
});

async function cargarYRenderizar() {
    try {
        pedidosGlobal = await api.get("/api/carrito/admin/lista");
        aplicarFiltros(); // En vez de renderizar todo de golpe, pasamos por el filtro
    } catch (err) {
        console.error("Error cargando pedidos", err);
    }
}

// 🚀 NUEVA FUNCIÓN: Filtra por fecha Y por estado simultáneamente
function aplicarFiltros() {
    const fechaSelect = document.getElementById("filtro-fecha").value;
    const estadoSelect = document.getElementById("filtro-estado").value.toLowerCase();
    
    let filtrados = pedidosGlobal;

    if (fechaSelect) {
        filtrados = filtrados.filter(p => p.fecha && new Date(p.fecha).toISOString().split('T')[0] === fechaSelect);
    }

    if (estadoSelect) {
        filtrados = filtrados.filter(p => (p.estadoPago || '').toLowerCase() === estadoSelect);
    }

    render(filtrados);
}

function render(lista) {
    const tbody = document.querySelector("#tabla-pedidos tbody");
    if (lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="padding:20px;">No se encontraron pedidos con esos filtros</td></tr>`;
        return;
    }

    tbody.innerHTML = lista.map(p => {
        const estado = p.estadoPago || 'Pendiente';
        const estadoLower = estado.toLowerCase();
        
        const clase = obtenerClaseEstado(estado);
        const fecha = p.fecha ? new Date(p.fecha).toLocaleDateString() : '---';

        const bloqueadoCancelacion = estadoLower.includes('enviando') || 
                                     estadoLower.includes('recibido') || 
                                     estadoLower.includes('devuelto') || 
                                     estadoLower.includes('cancelado');

        const bloqueadoEdicion = estadoLower.includes('devuelto') || 
                                 estadoLower.includes('cancelado');

        return `
            <tr>
                <td><input type="checkbox" class="check-fila" data-id="${p.idPedido}"></td>
                <td><strong>${p.idPedido}</strong></td>
                <td>${fecha}</td>
                <td>${p.clienteNombre || 'Usuario'}</td>
                <td>${(p.total || 0).toFixed(2)}€</td>
                <td><span class="badge ${clase}">${estado}</span></td>
                <td>
                  <div class="acciones">
                    <a href="show.html?id=${p.idPedido}" class="btn-ver" title="Ver detalles"><i class="fa-solid fa-eye"></i></a>
                    
                    <a href="edit.html?id=${p.idPedido}" class="btn-editar" style="${bloqueadoEdicion ? 'pointer-events: none; opacity: 0.4;' : ''}" title="${bloqueadoEdicion ? 'Estado finalizado' : 'Editar'}">
                        <i class="fa-solid fa-pen"></i>
                    </a>
                    
                    <button class="btn-desactivar" data-action="cancelar" data-id="${p.idPedido}" title="${bloqueadoCancelacion ? 'No se puede cancelar en esta etapa' : 'Cancelar pedido'}" ${bloqueadoCancelacion ? 'disabled style="opacity: 0.4; cursor: not-allowed;"' : ''}>
                        <i class="fa-solid fa-ban"></i>
                    </button>
                  </div>
                </td>
            </tr>
        `;
    }).join("");
}

function bindEvents() {
    bind(document.getElementById("check-all"), "change", (ev) => {
        document.querySelectorAll(".check-fila").forEach(cb => cb.checked = ev.target.checked);
    });

    // Eventos para que se filtren solos al cambiar cualquier desplegable
    bind(document.getElementById("filtro-fecha"), "change", aplicarFiltros);
    bind(document.getElementById("filtro-estado"), "change", aplicarFiltros);

    bind(document.querySelector("#tabla-pedidos tbody"), "click", async (e) => {
        const btn = e.target.closest("[data-action='cancelar']");
        if (!btn || btn.disabled) return; 
        
        if (confirm("¿Estás seguro de cancelar este pedido?")) {
            await api.put(`/api/carrito/admin/estado/${btn.dataset.id}`, { estado: "Cancelado" });
            await cargarYRenderizar();
        }
    });

    bind(document.getElementById("btn-eliminar-seleccionados"), "click", () => {
        const seleccionados = [...document.querySelectorAll(".check-fila:checked")].map(cb => cb.dataset.id);
        if (seleccionados.length === 0) return alert("Selecciona algún pedido.");
        
        pedidosBorrandoIds = seleccionados; 
        document.getElementById("modal-borrar-count").textContent = seleccionados.length;
        document.getElementById("input-borrar").value = "";
        document.getElementById("error-borrar").style.display = "none";
        document.getElementById("modalBorrar").style.display = "flex";
        setTimeout(() => document.getElementById("input-borrar").focus(), 100);
    });

    bind(document.getElementById("btn-cancelar-borrar"), "click", () => {
        document.getElementById("modalBorrar").style.display = "none";
        pedidosBorrandoIds = [];
    });

    bind(document.getElementById("btn-confirmar-borrar"), "click", async () => {
        const textoEscrito = document.getElementById("input-borrar").value.trim().toUpperCase();
        
        if (textoEscrito === "BORRAR") {
            try {
                for (let id of pedidosBorrandoIds) {
                    await api.delete(`/api/carrito/admin/eliminar/${id}`);
                }
                document.getElementById("modalBorrar").style.display = "none";
                pedidosBorrandoIds = [];
                await cargarYRenderizar();
                document.getElementById("check-all").checked = false;
            } catch (err) {
                alert("Error al eliminar del servidor.");
            }
        } else {
            document.getElementById("error-borrar").style.display = "block";
            document.getElementById("input-borrar").focus();
        }
    });

    bind(document.getElementById("btn-actualizar"), "click", async () => {
        // Limpiamos AMBOS filtros antes de recargar
        document.getElementById("filtro-fecha").value = ""; 
        document.getElementById("filtro-estado").value = ""; 
        await cargarYRenderizar();
    });
}