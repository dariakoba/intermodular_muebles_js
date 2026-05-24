document.addEventListener("DOMContentLoaded", async () => {

// Hamburguesa menú nav
    document.getElementById("btn-hamburguesa").addEventListener("click", () => {
        document.getElementById("nav-menu").classList.toggle("abierto");
    });

    // Cerrar menú nav al hacer resize
    window.addEventListener("resize", () => {
        if (window.innerWidth > 900) {
            document.getElementById("nav-menu").classList.remove("abierto");
        }
    });

})