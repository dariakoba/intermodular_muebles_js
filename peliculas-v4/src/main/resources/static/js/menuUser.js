/**
 * menuUser.js - Gestión del menú de usuario y sesión
 */

document.addEventListener("DOMContentLoaded", async () => {
    const icon = document.getElementById("person-icon");
    // Usamos querySelector porque en tu HTML es class="person-menu"
    const menu = document.querySelector(".person-menu");

    // Protección: Si no existen los elementos, salimos para no dar error
    if (!icon || !menu) return;

    // --- GESTIÓN DEL MENÚ DESPLEGABLE ---
    icon.addEventListener("click", (e) => {
        e.stopPropagation();
        // Alternamos entre block y none
        const isVisible = menu.style.display === "block";
        menu.style.display = isVisible ? "none" : "block";
    });

    // Cerrar el menú si se hace clic fuera
    document.addEventListener("click", (e) => {
        if (!e.target.closest(".person-menu-wrapper")) {
            menu.style.display = "none";
        }
    });

    // --- GESTIÓN DE LA SESIÓN ---
    try {
        const res = await fetch("/api/me");

        if (!res.ok) throw new Error("No hay sesión activa");

        const user = await res.json();

        // 1. GUARDAR ID PARA LAS RESEÑAS
        // Verificamos si el campo es 'id' o 'id_usuario' según tu API
        const userId = user.id || user.id_usuario;
        if (userId) {
            sessionStorage.setItem("userId", userId);
        }

        // 2. ACTUALIZAR EL MENÚ CON DATOS DEL USUARIO
        menu.innerHTML = `
            <div style="padding:12px; border-bottom:1px solid #eee;">
                <span style="display:block; font-size:0.85rem; color:#9c8c7e;">Conectado como:</span>
                <strong style="color:#4a3728;">${user.nombre}</strong>
            </div>
            <a href="perfil.html" style="display:block; padding:10px 12px; text-decoration:none; color:#5c4432;">Ver mi cuenta</a>
            <a href="#" onclick="logout(event)" style="display:block; padding:10px 12px; text-decoration:none; color:#ae4010; font-weight:bold;">Cerrar sesión</a>
        `;

    } catch (err) {
        // Si no hay sesión, borramos el ID y mostramos el link de login
        sessionStorage.removeItem("userId");
        menu.innerHTML = `
            <a href="login.html" style="display:block; padding:12px; text-decoration:none; color:#4a3728; font-weight:bold;">Iniciar sesión</a>
            <a href="registro.html" style="display:block; padding:12px; text-decoration:none; color:#5c4432;">Crear cuenta</a>
        `;
    }
});

/**
 * Función global para cerrar sesión
 */
async function logout(event) {
    if (event) event.preventDefault();
    
    if (!confirm("¿Quieres cerrar tu sesión?")) return;

    try {
        const res = await fetch("/logout", { method: "POST" });
        
        // Limpiamos todo rastro del usuario
        sessionStorage.removeItem("userId");
        
        // Redirigir al inicio o login
        window.location.href = "index.html";
    } catch (err) {
        console.error("Error al cerrar sesión:", err);
        alert("No se pudo cerrar la sesión correctamente.");
    }
}