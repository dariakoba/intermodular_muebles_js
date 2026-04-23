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

  try {
    const res = await fetch("/api/admin/usuarios", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(usuario)
    });

    // 🔥 leer respuesta del backend
    let data;

    try {
      data = await res.json();
    } catch {
      data = await res.text();
    }

    // ❌ si hay error
    if (!res.ok) {
      const msg = typeof data === "object" ? data.message : data;
      alert("❌ Error: " + msg);
      return;
    }

    // ✅ éxito
    alert("✅ Usuario creado correctamente");
    window.location.href = "index.html";

  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }
});