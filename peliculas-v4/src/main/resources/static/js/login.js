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

            if (!loginRes.ok) throw new Error("Login fallido");

            // 🔹 Paso 2: obtener usuario actual
            const meRes = await fetch("/api/me", {
                method: "GET",
                credentials: "include"
            });

            if (!meRes.ok) throw new Error("No autenticado");

            const user = await meRes.json();

            // Guardar usuario en localStorage por si lo necesitas en otras páginas
            localStorage.setItem("user", JSON.stringify(user));

            // 🔹 Paso 3: redirigir según rol
            if (user.rol === "admin") {
                window.location.href = "adminhome.html";
            } else {
                window.location.href = "index.html";
            }

        } catch (err) {
            console.error(err);
            // Mostrar mensaje de error
            errorDiv.style.display = "block";
        }
    });
});