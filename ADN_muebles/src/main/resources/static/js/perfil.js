document.addEventListener("DOMContentLoaded", async () => {
	let userData = null;
    // 1. CARGAR DATOS DEL USUARIO
    try {
        const res = await fetch("/api/me");

        if (!res.ok) {
            window.location.href = "/login.html";
            return; // Aquí sí dejamos el return porque si no hay usuario, no tiene sentido seguir
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


    // 2. CARGAR PEDIDOS (CORREGIDO: Sin 'return' que bloquee el resto)
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
                            <td><span class="estado ${estadoClase}">${p.estadoPago}</span></td>
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


    // 3. LÓGICA DE EDICIÓN DE PERFIL (Ahora siempre se ejecutará)
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
    
