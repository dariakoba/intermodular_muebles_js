async function borrarUsuario(id) {
  if (!confirm("¿Borrar usuario?")) return;

  await fetch(`/api/admin/usuarios/${id}`, {
    method: "DELETE"
  });

  location.reload();
}