document.addEventListener("DOMContentLoaded", () => {

  const params = new URLSearchParams(window.location.search);
  const id = params.get("id");

  const form = document.getElementById("form-usuario");

  const nombre    = document.getElementById("nombre");
  const apellidos = document.getElementById("apellidos");
  const email     = document.getElementById("email");
  const telefono  = document.getElementById("telefono");
  const direccion = document.getElementById("direccion");
  const rol       = document.getElementById("rol");
  const password  = document.getElementById("password");
  const estado    = document.getElementById("estado");
  const puntos    = document.getElementById("puntos");
  const salario   = document.getElementById("salario");

  const grupoPuntos  = document.getElementById("grupo-puntos");
  const grupoSalario = document.getElementById("grupo-salario");

  let usuarioOriginal = {};
  let isMe = false;

  // ─── UTILIDADES ───────────────────────────────────────────────

  function normalizar(valor) {
    return (valor || "").toString().trim().toLowerCase();
  }

  // Muestra u oculta el mensaje de error de un campo
  function setError(campo, mensaje) {
    // Buscamos el contenedor hermano con clase .field-error; si no existe, lo creamos
    let errorEl = campo.parentElement.querySelector(".field-error");
    if (!errorEl) {
      errorEl = document.createElement("span");
      errorEl.className = "field-error";
      errorEl.style.cssText = "color:#e53e3e;font-size:0.8rem;display:block;margin-top:2px;";
      campo.parentElement.appendChild(errorEl);
    }
    errorEl.textContent = mensaje;
    campo.style.borderColor = mensaje ? "#e53e3e" : "";
  }

  function clearError(campo) {
    setError(campo, "");
  }

  // ─── VALIDACIONES INDIVIDUALES ────────────────────────────────

  // Solo letras (incluye acentos, ñ, espacios y guiones)
  const soloLetras = /^[A-Za-zÀ-ÖØ-öø-ÿÑñ\s\-]+$/;

  function validarNombre(campo, etiqueta) {
    const v = campo.value.trim();
    if (!v) {
      setError(campo, `${etiqueta} es obligatorio`);
      return false;
    }
    if (v.length < 2) {
      setError(campo, `${etiqueta} debe tener al menos 2 caracteres`);
      return false;
    }
    if (v.length > 60) {
      setError(campo, `${etiqueta} no puede superar 60 caracteres`);
      return false;
    }
    if (!soloLetras.test(v)) {
      setError(campo, `${etiqueta} solo puede contener letras`);
      return false;
    }
    clearError(campo);
    return true;
  }

  function validarEmail(campo) {
    const v = campo.value.trim();
    if (!v) {
      setError(campo, "El email es obligatorio");
      return false;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)) {
      setError(campo, "Formato de email inválido");
      return false;
    }
    if (v.length > 100) {
      setError(campo, "El email no puede superar 100 caracteres");
      return false;
    }
    clearError(campo);
    return true;
  }

  function validarTelefono(campo) {
    const v = campo.value.trim();
    // El teléfono es opcional; si está vacío no da error
    if (!v) {
      clearError(campo);
      return true;
    }
    if (!/^\+?\d{9,15}$/.test(v)) {
      setError(campo, "Teléfono inválido (+ opcional y entre 9-15 dígitos)");
      return false;
    }
    clearError(campo);
    return true;
  }

  function validarDireccion(campo) {
    const v = campo.value.trim();
    // Opcional, pero si rellena tiene límites
    if (!v) {
      clearError(campo);
      return true;
    }
    if (v.length < 5) {
      setError(campo, "La dirección debe tener al menos 5 caracteres");
      return false;
    }
    if (v.length > 150) {
      setError(campo, "La dirección no puede superar 150 caracteres");
      return false;
    }
    clearError(campo);
    return true;
  }

  function validarPassword(campo) {
    const v = campo.value.trim();
    // Opcional: solo valida si el usuario escribe algo
    if (!v) {
      clearError(campo);
      return true;
    }
    if (v.length < 6) {
      setError(campo, "La contraseña debe tener al menos 6 caracteres");
      return false;
    }
    if (v.length > 72) {
      setError(campo, "La contraseña no puede superar 72 caracteres");
      return false;
    }
    
    clearError(campo);
    return true;
  }

  function validarPuntos(campo) {
    const v = campo.value.trim();
    if (normalizar(rol.value) !== "cliente") { clearError(campo); return true; }
    if (v === "") {
      clearError(campo);
      return true; // opcional; se usará el original
    }
    if (!/^\d+$/.test(v)) {
      setError(campo, "Los puntos deben ser un número entero positivo");
      return false;
    }
    const n = parseInt(v);
    if (n < 0 || n > 9999999) {
      setError(campo, "Los puntos deben estar entre 0 y 9 999 999");
      return false;
    }
    clearError(campo);
    return true;
  }

  function validarSalario(campo) {
    const v = campo.value.trim();
    if (normalizar(rol.value) === "cliente") { clearError(campo); return true; }
    if (v === "") {
      clearError(campo);
      return true; // opcional; se usará el original
    }
    if (!/^\d+(\.\d{1,2})?$/.test(v)) {
      setError(campo, "Salario inválido (número con hasta 2 decimales)");
      return false;
    }
    const n = parseFloat(v);
    if (n < 0 || n > 999999.99) {
      setError(campo, "El salario debe estar entre 0 y 999 999,99");
      return false;
    }
    clearError(campo);
    return true;
  }

  // ─── VALIDACIÓN GLOBAL ────────────────────────────────────────

  function validarTodo() {
    const ok = [
      validarNombre(nombre, "El nombre"),
      validarNombre(apellidos, "Los apellidos"),
      validarEmail(email),
      validarTelefono(telefono),
      validarDireccion(direccion),
      validarPassword(password),
      validarPuntos(puntos),
      validarSalario(salario),
    ];
    return ok.every(Boolean);
  }

  // ─── FEEDBACK EN TIEMPO REAL ──────────────────────────────────

  nombre.addEventListener("blur",    () => validarNombre(nombre, "El nombre"));
  apellidos.addEventListener("blur", () => validarNombre(apellidos, "Los apellidos"));
  email.addEventListener("blur",     () => validarEmail(email));
  telefono.addEventListener("blur",  () => validarTelefono(telefono));
  direccion.addEventListener("blur", () => validarDireccion(direccion));
  password.addEventListener("blur",  () => validarPassword(password));
  if (puntos)  puntos.addEventListener("blur",  () => validarPuntos(puntos));
  if (salario) salario.addEventListener("blur", () => validarSalario(salario));

  // ─── CARGA DEL USUARIO ────────────────────────────────────────

  async function cargarUsuario() {
    try {
      const res = await fetch(`/api/admin/usuarios/${id}`);
      const u   = await res.json();

      usuarioOriginal = u;

      const currentUserRes = await fetch("/api/me");
      const currentUser    = await currentUserRes.json();
      isMe = currentUser.id === u.id;

      nombre.value    = u.nombre    || "";
      apellidos.value = u.apellidos || "";
      email.value     = u.email     || "";
      telefono.value  = u.telefono  || "";
      direccion.value = u.direccion || "";
      rol.value       = normalizar(u.rol)    || "cliente";
      estado.value    = normalizar(u.estado) || "activo";

      if (isMe) {
        rol.disabled    = true;
        estado.disabled = true;
        rol.style.backgroundColor    = "#eee";
        estado.style.backgroundColor = "#eee";
      }

      if (puntos)  puntos.value  = u.puntos  ?? "";
      if (salario) salario.value = u.salario ?? "";

      toggleCampos();

    } catch (err) {
      console.error("Error cargando usuario:", err);
    }
  }

  // ─── MOSTRAR / OCULTAR CAMPOS SEGÚN ROL ──────────────────────

  function toggleCampos() {
    const rolActual = normalizar(rol.value);
    if (rolActual === "cliente") {
      grupoPuntos.style.display  = "block";
      grupoSalario.style.display = "none";
    } else {
      grupoPuntos.style.display  = "none";
      grupoSalario.style.display = "block";
    }
  }

  rol.addEventListener("change", toggleCampos);

  // ─── ENVÍO DEL FORMULARIO ─────────────────────────────────────

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Protección: isMe no puede cambiar su rol/estado
    if (isMe) {
      const originalRol    = normalizar(usuarioOriginal.rol);
      const originalEstado = normalizar(usuarioOriginal.estado);
      if (rol.value !== originalRol || estado.value !== originalEstado) {
        alert("No puedes cambiar tu propio rol o estado");
        return;
      }
    }

    // Validación completa antes de enviar
    if (!validarTodo()) {
      // Desplaza al primer campo con error
      const primerError = form.querySelector(".field-error:not(:empty)");
      if (primerError) primerError.scrollIntoView({ behavior: "smooth", block: "center" });
      return;
    }

    const usuario = {
      nombre:    nombre.value.trim()    || usuarioOriginal.nombre,
      apellidos: apellidos.value.trim() || usuarioOriginal.apellidos,
      email:     email.value.trim()     || usuarioOriginal.email,
      telefono:  telefono.value.trim()  || usuarioOriginal.telefono,
      direccion: direccion.value.trim() || usuarioOriginal.direccion,
      rol:       isMe ? usuarioOriginal.rol    : normalizar(rol.value),
      estado:    isMe ? usuarioOriginal.estado : normalizar(estado.value),

      puntos: rol.value === "cliente"
        ? (puntos && puntos.value !== "" ? parseInt(puntos.value) : usuarioOriginal.puntos)
        : 0,

      salario: rol.value !== "cliente"
        ? (salario && salario.value !== "" ? parseFloat(salario.value) : usuarioOriginal.salario)
        : 0,

      fecha_alta: usuarioOriginal.fecha_alta,
    };

    const passValue = password.value.trim();
    if (passValue !== "") {
      usuario.password_hash = passValue;
    }

    try {
      const res = await fetch(`/api/admin/usuarios/${id}`, {
        method:  "PUT",
        headers: { "Content-Type": "application/json" },
        body:    JSON.stringify(usuario),
      });

      let data;
      let msg = "Error desconocido";

      try {
        data = await res.json();
        msg  = data.message || msg;
      } catch {
        msg = await res.text();
      }

      if (!res.ok) {
        alert("❌ No se puede actualizar: " + msg);
        return;
      }

      alert("✅ Usuario actualizado correctamente");
      window.location.href = "index.html";

    } catch (err) {
      console.error(err);
      alert("Error de conexión");
    }
  });

  cargarUsuario();
});