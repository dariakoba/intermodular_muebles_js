// js/destroy.js
window.borrarUsuario = async function(id) {
    console.log("Intentando borrar ID:", id); // Esto aparecerá en consola
    alert("Iniciando borrado..."); // Alert de prueba inmediata

    if (!confirm("¿Borrar usuario?")) return;

    try {
        const res = await fetch(`/api/admin/usuarios/${id}`, {
            method: "DELETE"
        });

        // Importante: No uses res.json() todavía para probar
        const texto = await res.text();
        console.log("Respuesta servidor:", texto);

        alert("Servidor respondió: " + texto);
        location.reload();

    } catch (err) {
        console.error(err);
        alert("Error de conexión: " + err.message);
    }
}