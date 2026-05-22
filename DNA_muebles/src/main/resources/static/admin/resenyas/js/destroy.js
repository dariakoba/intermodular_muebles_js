// Borrar individual
window.borrarResenya = async function(id) {
    if (!confirm("¿Eliminar esta reseña permanentemente?")) return;

    try {
        const res = await fetch(`/api/admin/resenyas/${id}`, { method: "DELETE" });
        if (res.ok) {
            alert("Reseña eliminada");
            location.reload();
        } else {
            alert("Error al eliminar");
        }
    } catch (err) {
        console.error(err);
    }
};

// Borrar seleccionados
document.getElementById("btn-eliminar-seleccionados")?.addEventListener("click", async () => {
    const checkboxes = document.querySelectorAll("#tablaResenyas tbody input[type='checkbox']:checked");
    const ids = Array.from(checkboxes).map(cb => cb.dataset.id);

    if (ids.length === 0) return alert("Selecciona alguna reseña");
    if (!confirm(`¿Eliminar ${ids.length} reseñas?`)) return;

    for (const id of ids) {
        await fetch(`/api/admin/resenyas/${id}`, { method: "DELETE" });
    }
    alert("Proceso finalizado");
    location.reload();
});