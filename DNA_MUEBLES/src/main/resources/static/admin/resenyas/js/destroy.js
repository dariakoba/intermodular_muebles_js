// Borrar seleccionados
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("btn-eliminar-seleccionados")?.addEventListener("click", async () => {
        const checkboxes = document.querySelectorAll("#tablaResenyas tbody input[type='checkbox']:checked");
        const ids = Array.from(checkboxes).map(cb => cb.dataset.id);

        if (ids.length === 0) return alert("Selecciona alguna reseña");
        if (!confirm(`¿Eliminar ${ids.length} reseñas?`)) return;

        for (const id of ids) {
            await fetch(`/api/admin/resenyas/${id}`, { method: "DELETE" });
            // Actualizamos la lista local sin recargar la página
            todasLasResenyas = todasLasResenyas.filter(r => Number(r.id_resenya) !== Number(id));
        }

        renderizarTabla(todasLasResenyas);
        alert("Proceso finalizado");
    });
});