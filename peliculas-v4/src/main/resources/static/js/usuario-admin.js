const form = document.getElementById("form-usuario");
const errorDiv = document.getElementById("error");
const successDiv = document.getElementById("success");

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("userId").value;
  const password = document.getElementById("passwordHash").value;

  const usuario = {
    nombre: document.getElementById("nombre").value.trim(),
    apellidos: document.getElementById("apellidos").value.trim(),
    email: document.getElementById("email").value.trim(),
    telefono: document.getElementById("telefono").value.trim(),
    direccion: document.getElementById("direccion").value.trim(),
    rol: document.getElementById("rol").value,
    estado: "activo"
  };

  if(password && password.trim() !== ""){
    usuario.passwordHash = password;
  } else if(!id){ // creación requiere contraseña
    errorDiv.textContent = "La contraseña es obligatoria al crear un usuario";
    errorDiv.style.display = "block";
    return;
  }

  try {
    let res;
    if(id){
      res = await fetch(`/api/admin/usuarios/${id}`, {
        method:"PUT",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(usuario)
      });
    } else {
      res = await fetch("/api/admin/usuarios", {
        method:"POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(usuario)
      });
    }

    if(res.ok){
      errorDiv.style.display = "none";
      successDiv.style.display = "block";
      limpiarFormulario();
      cargarUsuarios();
    } else {
      const text = await res.text();
      errorDiv.textContent = "Error: " + text;
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

async function cargarUsuarios(){
  const res = await fetch("/api/admin/usuarios");
  const usuarios = await res.json();
  const tbody = document.querySelector("#tablaUsuarios tbody");
  tbody.innerHTML = "";

  usuarios.forEach(u => {
    const fila = `
      <tr>
        <td>${u.id}</td>
        <td>${u.nombre} ${u.apellidos}</td>
        <td>${u.email}</td>
        <td>${u.rol}</td>
        <td>${u.telefono ?? ""}</td>
        <td>
          <button onclick="editarUsuario(${u.id})">Editar</button>
          <button onclick="borrarUsuario(${u.id})">Borrar</button>
        </td>
      </tr>
    `;
    tbody.innerHTML += fila;
  });
}

async function borrarUsuario(id){
  if(!confirm("¿Borrar usuario?")) return;
  await fetch(`/api/admin/usuarios/${id}`, {method:"DELETE"});
  cargarUsuarios();
}

async function editarUsuario(id){
  const res = await fetch(`/api/admin/usuarios/${id}`);
  const u = await res.json();

  document.getElementById("userId").value = u.id;
  document.getElementById("nombre").value = u.nombre;
  document.getElementById("apellidos").value = u.apellidos;
  document.getElementById("email").value = u.email;
  document.getElementById("telefono").value = u.telefono;
  document.getElementById("direccion").value = u.direccion;
  document.getElementById("rol").value = u.rol;
  document.getElementById("passwordHash").value = "";
  document.getElementById("formTitle").innerText = "Editar Usuario";
}

function limpiarFormulario(){
  document.getElementById("userId").value = "";
  document.getElementById("nombre").value = "";
  document.getElementById("apellidos").value = "";
  document.getElementById("email").value = "";
  document.getElementById("telefono").value = "";
  document.getElementById("direccion").value = "";
  document.getElementById("passwordHash").value = "";
  document.getElementById("rol").value = "cliente";
  document.getElementById("formTitle").innerText = "Crear Usuario";
}

// Cargar la tabla al abrir
cargarUsuarios();