async function logout() {
  try {
    await fetch("/logout", {
      method: "POST"
    });

    window.location.href = "/login.html";
  } catch (err) {
    console.error("Error al cerrar sesión:", err);
  }
}