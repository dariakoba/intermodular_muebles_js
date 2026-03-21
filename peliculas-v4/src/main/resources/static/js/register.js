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
    passwordHash: document.getElementById("password").value
  };

  try {
    const res = await fetch("/api/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if(res.status === 201 || res.status === 200){
      errorDiv.style.display = "none";
      successDiv.style.display = "block";
      form.reset();
    } else if(res.status === 409){
      errorDiv.textContent = "El email ya está registrado";
      errorDiv.style.display = "block";
      successDiv.style.display = "none";
	  else {
	    const errorText = await res.text();
	    errorDiv.textContent = "Error al registrar: " + errorText;
	    errorDiv.style.display = "block";
	    successDiv.style.display = "none";
	  }

  } catch(err){
    console.error(err);
    errorDiv.textContent = "Error de conexión";
    errorDiv.style.display = "block";
    successDiv.style.display = "none";
  }
});