window.borrarUsuario = async function(id) {
    console.log("Intentando borrar ID:", id); 
    alert("Iniciando borrado...");

    if (!confirm("¿Borrar usuario?")) return;

    try {
        const res = await fetch(`/api/admin/usuarios/${id}`, {
            method: "DELETE"
        });

        
        const texto = await res.text();
        console.log("Respuesta servidor:", texto);

        alert("Servidor respondió: " + texto);
        location.reload();

    } catch (err) {
        console.error(err);
        alert("Error de conexión: " + err.message);
    }
}