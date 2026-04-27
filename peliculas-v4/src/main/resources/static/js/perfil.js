document.addEventListener("DOMContentLoaded", async () => {

    // 1. CARGAR DATOS DEL USUARIO
    try {
        const res = await fetch("/api/me");

        if (!res.ok) {
            window.location.href = "/login.html";
            return; // Aquí sí dejamos el return porque si no hay usuario, no tiene sentido seguir
        }

        const user = await res.json();

        document.getElementById("nombre").textContent = user.nombre;
        document.getElementById("email").textContent = user.email;
        document.getElementById("telefono").textContent = user.telefono ?? "-";
        document.getElementById("direccion").textContent = user.direccion ?? "No definida";
        document.getElementById("puntos").textContent = (user.puntos ?? 0) + " puntos";

        const imgPerfil = document.getElementById("img-perfil");
        if (user.fotoUrl) {
            imgPerfil.src = user.fotoUrl;
        } else {
            imgPerfil.src = "/images/fotoperfil/default-avatar.png"; 
        }

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
            document.getElementById("input-nombre").value = document.getElementById("nombre").textContent;
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
                email: document.getElementById("input-email").value.trim(),
                telefono: document.getElementById("input-telefono").value.trim(),
                direccion: document.getElementById("input-direccion").value.trim()
            };

            if (Object.values(updatedUser).some(val => !val)) {
                alert("Por favor, rellena todos los campos.");
                return;
            }

            try {
                const res = await fetch("/api/users/update-me", {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(updatedUser)
                });

                if (res.ok) {
                    alert("¡Datos actualizados!");
                    location.reload();
                } else {
                    alert("Error al actualizar.");
                }
            } catch (err) {
                alert("Error de conexión.");
            }
        });
    }


    // 4. LÓGICA DE SUBIDA DE FOTO
    const fileInput = document.getElementById('file-input');
    if (fileInput) {
        fileInput.addEventListener('change', async function(e) {
            const file = e.target.files[0];
            if (!file) return;

            const formData = new FormData();
            formData.append('file', file);

            try {
                const response = await fetch('/api/users/upload-photo', {
                    method: 'POST',
                    body: formData
                });

                if (response.ok) {
                    const data = await response.json();
                    document.getElementById('img-perfil').src = data.fotoUrl + "?t=" + new Date().getTime();
                    alert("Foto actualizada correctamente");
                } else {
                    alert("Error al subir la foto");
                }
            } catch (error) {
                console.error("Error subida:", error);
                alert("Error de conexión al subir la foto");
            }
        });
    }
});