// login.js
document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("form-login");
    const errorDiv = document.getElementById("error");

    form.addEventListener("submit", async (e) => {
        e.preventDefault(); // prevenir submit normal

        // Ocultar error
        errorDiv.style.display = "none";

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        try {
            // 🔹 Paso 1: login
            const loginRes = await fetch("/api/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include", // MUY IMPORTANTE para sesión
                body: JSON.stringify({
                    email: email,
                    password_hash: password
                })
            });
			//cambio de desactivar
			if (!loginRes.ok) {
			    if (loginRes.status === 401) {
			        throw new Error("Credenciales incorrectas");
			    } else if (loginRes.status === 403) {
			        throw new Error("Usuario desactivado");
			    } else {
			        throw new Error("Error en el servidor");
			    }
			}

            // 🔹 Paso 2: obtener usuario actual
            const meRes = await fetch("/api/me", {
                method: "GET",
                credentials: "include"
            });

            if (!meRes.ok) throw new Error("No autenticado");

            const user = await meRes.json();

            localStorage.setItem("user", JSON.stringify(user));

            // 🔹 Paso 3: redirigir según rol
			/*
            if (user.rol === "admin") {
                window.location.href = "adminhome.html";
            } else {
                window.location.href = "index.html";
            }
			*/
			const rol = (user.rol || user.role || "")
			    .toString()
			    .toLowerCase()
			    .trim();

			console.log("ROL NORMALIZADO:", rol);
			
			if (rol === "admin") {
			    window.location.href = "adminhome.html";
			} else {
			    window.location.href = "index.html";
			}
			
        } catch (err) {
			console.error(err);

			errorDiv.textContent = err.message;
			errorDiv.style.display = "block";
        }
    });
});