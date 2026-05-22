document.addEventListener("DOMContentLoaded", async () => {
	let userData = null;
    // 1. CARGAR DATOS DEL USUARIO
    try {
        const res = await fetch("/api/me");

        if (!res.ok) {
            window.location.href = "/login.html";
            return; 
        }

        const user = await res.json();

		userData = user;
		
        document.getElementById("nombre").textContent = user.nombre + " " + (user.apellidos ?? "");
        document.getElementById("email").textContent = user.email;
        document.getElementById("telefono").textContent = user.telefono ?? "-";
        document.getElementById("direccion").textContent = user.direccion ?? "No definida";
        document.getElementById("puntos").textContent = (user.puntos ?? 0) + " puntos";

        

    } catch (err) {
        console.error("Error usuario:", err);
        window.location.href = "/login.html";
        return;
    }


    // 2. CARGAR PEDIDOS (CON NUEVO DISEÑO Y PRECIOS SEPARADOS ALINEADOS A LA DERECHA)
    try {
        const res = await fetch("/api/carrito/mis");
        const tbody = document.getElementById("pedidos-body");

        if (!res.ok) {
            tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;">No se pudieron cargar los pedidos</td></tr>`;
        } else {
            const pedidos = await res.json();

            if (pedidos.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;">Aún no has realizado ningún pedido.</td></tr>`;
            } else {
                let html = "";
                pedidos.forEach(p => {
                    const estadoClase = p.estadoPago ? p.estadoPago.toLowerCase() : "pendiente";
                    const fechaLimpia = p.fecha ? p.fecha.split('T')[0] : "---";
                    
                    // Formateamos los productos para el mini-ticket separando nombre y precio
                    let productosLista = "";
                    if (p.nombreProducto && p.nombreProducto.trim() !== "") {
                        productosLista = p.nombreProducto.split('|').map(item => {
                            // Aquí separamos el texto usando los " : " que nos manda Java
                            let partes = item.split(' : ');
                            let nombreMueble = partes[0];
                            let precioMueble = partes.length > 1 ? partes[1] : '';

                            // Dibujamos la fila, empujando el precio a la derecha
                            return `
                            <div style="display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f0eae1; font-size: 14.5px; color: #5c4432;">
                                <span><i class="fa-solid fa-box-open" style="color: #c5a992; margin-right: 8px;"></i> ${nombreMueble}</span>
                                <span style="font-weight: bold; color: #887a69;">${precioMueble}</span>
                            </div>
                            `;
                        }).join('');
                    } else {
                        productosLista = `
                            <div style="display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f0eae1; font-size: 14.5px; color: #5c4432;">
                                <span><i class="fa-solid fa-box-open" style="color: #c5a992; margin-right: 8px;"></i> Productos del pedido (Sin detallar)</span>
                            </div>`;
                    }

                    html += `
                        <tr onclick="toggleDetalles(${p.idPedido})" style="cursor: pointer; transition: background 0.2s;" onmouseover="this.style.background='#fdfbf6'" onmouseout="this.style.background='transparent'">
                            <td style="font-weight: 600;">#${p.idPedido}</td>
                            <td>${fechaLimpia}</td>
                            <td><strong>${p.total.toFixed(2)} €</strong></td>
                            <td style="display: flex; align-items: center; justify-content: space-between;">
                                <span class="estado ${estadoClase}">${p.estadoPago}</span>
                                <span class="material-symbols-outlined" style="font-size: 22px; color: #887a69; transition: transform 0.2s;" id="icon-${p.idPedido}">expand_more</span>
                            </td>
                        </tr>
                        
                        <tr id="detalles-${p.idPedido}" style="display: none; background-color: #f7f3ee;">
                            <td colspan="4" style="padding: 0;">
                                <div style="padding: 20px 40px; border-top: 1px solid #eaddcd; border-bottom: 2px solid #eaddcd; box-shadow: inset 0 3px 10px rgba(0,0,0,0.02);">
                                    
                                    <div style="background: white; padding: 20px; border-radius: 8px; border: 1px solid #eaddcd; max-width: 600px; margin: 0 auto;">
                                        <h4 style="margin: 0 0 15px 0; color: #4a3b32; font-size: 13px; text-transform: uppercase; letter-spacing: 1px; border-bottom: 2px solid #eee; padding-bottom: 10px;">
                                            Resumen del Pedido #${p.idPedido}
                                        </h4>
                                        
                                        ${productosLista}
                                        
                                        <div style="display: flex; justify-content: space-between; padding-top: 15px; margin-top: 5px; font-weight: bold; color: rgb(159, 80, 0); font-size: 18px;">
                                            <span>TOTAL PAGADO:</span>
                                            <span>${p.total.toFixed(2)} €</span>
                                        </div>
                                    </div>

                                </div>
                            </td>
                        </tr>`;
                });
                tbody.innerHTML = html;
            }
        }
    } catch (err) {
        console.error("Error pedidos:", err);
        const tbody = document.getElementById("pedidos-body");
        if(tbody) tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;">Error de conexión.</td></tr>`;
    }


    // 3. LÓGICA DE EDICIÓN DE PERFIL 
    const btnEdit = document.getElementById("btn-edit");
    const btnCancel = document.getElementById("btn-cancel");
    const btnSave = document.getElementById("btn-save");
    const infoDiv = document.getElementById("perfil-info");
    const formDiv = document.getElementById("perfil-form");

    if (btnEdit) {
        btnEdit.addEventListener("click", () => {
			document.getElementById("input-nombre").value = userData.nombre;
			document.getElementById("input-apellidos").value = userData.apellidos ?? "";
            document.getElementById("input-email").value = document.getElementById("email").textContent;
            document.getElementById("input-telefono").value = document.getElementById("telefono").textContent;
            document.getElementById("input-direccion").value = document.getElementById("direccion").textContent;
            
            infoDiv.style.display = "none";
            formDiv.style.display = "block";
            btnEdit.style.display = "none";
        });
    }

    if (btnCancel) {
        btnCancel.addEventListener("click", () => {
            infoDiv.style.display = "block";
            formDiv.style.display = "none";
            btnEdit.style.display = "inline-flex";
        });
    }

	if (btnSave) {
	    btnSave.addEventListener("click", async () => {

	        const updatedUser = {
	            nombre: document.getElementById("input-nombre").value.trim(),
	            apellidos: document.getElementById("input-apellidos").value.trim(),
	            email: document.getElementById("input-email").value.trim(),
	            telefono: document.getElementById("input-telefono").value.trim(),
	            direccion: document.getElementById("input-direccion").value.trim()
	        };

	        // VALIDACIONES
	        if (!updatedUser.nombre || updatedUser.nombre.length < 2) {
	            alert("El nombre debe tener al menos 2 caracteres.");
	            return;
	        }

	        if (!updatedUser.apellidos || updatedUser.apellidos.length < 2) {
	            alert("Los apellidos deben tener al menos 2 caracteres.");
	            return;
	        }

	        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

	        if (!emailRegex.test(updatedUser.email)) {
	            alert("Introduce un email válido.");
	            return;
	        }

	        const telefonoRegex = /^[0-9]{9}$/;

	        if (!telefonoRegex.test(updatedUser.telefono)) {
	            alert("El teléfono debe tener 9 números.");
	            return;
	        }

	        if (!updatedUser.direccion || updatedUser.direccion.length < 5) {
	            alert("Introduce una dirección válida.");
	            return;
	        }

	        // CONFIRMACIÓN
	        const confirmar = confirm(
	            "¿Estás segura de que quieres guardar los cambios?"
	        );

	        if (!confirmar) return;

	        try {

	            const res = await fetch("/api/users/update-me", {
	                method: "PUT",
	                headers: {
	                    "Content-Type": "application/json"
	                },
	                body: JSON.stringify(updatedUser)
	            });

	            if (res.ok) {
	                alert("¡Datos actualizados!");
	                location.reload();
	            } else {
	                alert("Error al actualizar.");
	            }

	        } catch (err) {
	            console.error(err);
	            alert("Error de conexión.");
	        }
	    });
	}

});


// --- FUNCIONES GLOBALES ---
function openModal(url) {
    const modal = document.getElementById("imageModal");
    const img = document.getElementById("modalImg");

    img.src = url;
    modal.style.display = "flex";
}

function closeModal() {
    document.getElementById("imageModal").style.display = "none";
}

// NUEVA FUNCIÓN: Abre el ticket y gira la flecha
window.toggleDetalles = function(idPedido) {
    const filaDetalles = document.getElementById(`detalles-${idPedido}`);
    const icono = document.getElementById(`icon-${idPedido}`);

    if (filaDetalles.style.display === "none") {
        filaDetalles.style.display = "table-row";
        icono.style.transform = "rotate(180deg)"; // Anima la flecha girándola
    } else {
        filaDetalles.style.display = "none";
        icono.style.transform = "rotate(0deg)"; // Vuelve la flecha a su sitio
    }
};

