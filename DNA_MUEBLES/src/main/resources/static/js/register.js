const form = document.getElementById("form-register");
const errorDiv = document.getElementById("error");
const successDiv = document.getElementById("success");

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const data = {
    nombre: document.getElementById("nombre").value.trim(),
    apellidos: document.getElementById("apellidos").value.trim(),
    email: document.getElementById("email").value.trim(),
    telefono: document.getElementById("telefono").value.trim(),
    password_hash: document.getElementById("password").value
  };
	
  if (data.password_hash.length < 6) {
      errorDiv.textContent = "La contraseña debe tener mínimo 6 caracteres";
      errorDiv.style.display = "block";
      successDiv.style.display = "none";
      return;
    }
	
	if (data.telefono && !/^\+?\d{9,15}$/.test(data.telefono)) {
	  errorDiv.textContent = "Teléfono inválido (puede incluir + y entre 9-15 números)";
	  errorDiv.style.display = "block";
	  successDiv.style.display = "none";
	  return;
	}
	  if (data.nombre.length < 2) {
	    errorDiv.textContent = "El nombre debe tener al menos 2 caracteres";
	    errorDiv.style.display = "block";
	    successDiv.style.display = "none";
	    return;
	  }

	  if (data.apellidos.length < 2) {
	    errorDiv.textContent = "Los apellidos deben tener al menos 2 caracteres";
	    errorDiv.style.display = "block";
	    successDiv.style.display = "none";
	    return;
	  }
  console.log("Datos a enviar:", data); 

  try {
    const res = await fetch("/api/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (res.ok) {
      alert("Registro completado correctamente ");

      form.reset();

      // redirección a login
      window.location.href = "login.html";
      return;
    }

    if (res.status === 409) {
      alert("El email ya está registrado");
      return;
    }

    const errorText = await res.text();
    alert("Error al registrar: " + errorText);

  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }

  
});