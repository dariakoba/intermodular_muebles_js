async function logout(event) {
    event.preventDefault();

    if (!confirm("¿Quieres cerrar tu sesión?")) return;

    try {
        await fetch("/api/logout", {
            method: "POST",
            credentials: "include"
        });

        sessionStorage.clear();
        localStorage.clear();

        window.location.href = "/index.html";

    } catch (err) {
        console.error("Error al cerrar sesión:", err);
        alert("No se pudo cerrar la sesión correctamente.");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("logoutBtn");

    if (btn) {
        btn.addEventListener("click", logout);
    }
});