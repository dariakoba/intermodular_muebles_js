const personIcon = document.getElementById("person-icon");
const personMenu = document.querySelector(".person-menu");

personIcon.addEventListener("click", (e) => {
  e.stopPropagation(); 
  personMenu.classList.toggle("show");
});

document.addEventListener("click", () => {
  personMenu.classList.remove("show");
});
document.addEventListener("DOMContentLoaded", async () => {
  const icon = document.getElementById("person-icon");
  const menu = document.getElementById("person-menu");

  icon.addEventListener("click", () => {
    menu.style.display = menu.style.display === "block" ? "none" : "block";
  });

  try {
    const res = await fetch("/api/me");

    if (!res.ok) throw new Error();

    const user = await res.json();

    menu.innerHTML = `
      <span style="padding:10px; display:block;">Hola, ${user.nombre}</span>
      <a href="perfil.html">Ver cuenta</a>
      <a href="#" onclick="logout()">Cerrar sesión</a>
    `;

  } catch {
    menu.innerHTML = `
      <a href="login.html">Iniciar sesión</a>
    `;
  }

  document.addEventListener("click", (e) => {
    if (!e.target.closest(".person-menu-wrapper")) {
      menu.style.display = "none";
    }
  });
});
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
