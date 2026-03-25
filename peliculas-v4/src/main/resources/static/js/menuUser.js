const personIcon = document.getElementById("person-icon");
const personMenu = document.querySelector(".person-menu");

// Alterna la clase 'show' al hacer clic
personIcon.addEventListener("click", (e) => {
  e.stopPropagation(); // evita que se cierre inmediatamente
  personMenu.classList.toggle("show");
});

// Cierra el menú si se hace clic fuera
document.addEventListener("click", () => {
  personMenu.classList.remove("show");
});