async function logout() {
  try {
    await fetch("/api/logout", {
      method: "POST",
      credentials: "include"
    });

    sessionStorage.clear();
    localStorage.clear();

    window.location.href = "/index.html";

  } catch (err) {
    console.error("Error al cerrar sesión:", err);
  }
}