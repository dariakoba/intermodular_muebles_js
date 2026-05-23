document.getElementById("form-usuario").addEventListener("submit", async (e) => {
  e.preventDefault();

  // INPUTS
  const nombreValue = nombre.value.trim();
  const apellidosValue = apellidos.value.trim();
  const emailValue = email.value.trim();
  const telefonoValue = telefono.value.trim();
  const direccionValue = direccion.value.trim();
  const passwordValue = password.value.trim();

  // VALIDACIONES

  // Nombre
  if (nombreValue.length < 2) {
    alert("El nombre debe tener mínimo 2 caracteres");
    return;
  }

  // Apellidos
  if (apellidosValue.length < 2) {
    alert("Los apellidos deben tener mínimo 2 caracteres");
    return;
  }

  // Email
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!emailRegex.test(emailValue)) {
    alert("Introduce un email válido");
    return;
  }

  
  // Teléfono internacional (9-15 dígitos, opcional +)
  if (telefonoValue && !/^\+?\d{9,15}$/.test(telefonoValue)) {
    alert("El teléfono debe tener entre 9 y 15 números y puede incluir '+'");
    return;
  }

  // Contraseña segura
  if (passwordValue.length < 6) {
    alert("La contraseña debe tener al menos 6 caracteres");
    return;
  }

  // OBJETO USUARIO
  const usuario = {
    nombre: nombreValue,
    apellidos: apellidosValue,
    email: emailValue,
    telefono: telefonoValue,
    direccion: direccionValue,
    rol: rol.value,
    password_hash: passwordValue,
    estado: "activo"
  };

  try {

    const res = await fetch("/api/admin/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(usuario)
    });

    let data;

    try {
      data = await res.json();
    } catch {
      data = await res.text();
    }

    if (!res.ok) {

      const msg =
        typeof data === "object"
          ? data.message
          : data;

      alert(" Error: " + msg);
      return;
    }

    alert("Usuario creado correctamente");

    window.location.href = "index.html";

  } catch (err) {

    console.error(err);

    alert("Error de conexión");
  }
});