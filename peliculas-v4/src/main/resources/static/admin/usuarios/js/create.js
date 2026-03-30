document.getElementById("form-usuario").addEventListener("submit", async (e) => {
  e.preventDefault();

  const usuario = {
    nombre: nombre.value,
    apellidos: apellidos.value,
    email: email.value,
    telefono: telefono.value,
    direccion: direccion.value,
    rol: rol.value,
    password_hash: password.value,
    estado: "activo"
  };

  const res = await fetch("/api/admin/usuarios", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(usuario)
  });

  if (res.ok) {
    window.location.href = "index.html";
  }
  
});