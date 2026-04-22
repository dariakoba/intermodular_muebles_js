import { app } from "/js/core/app.js";
import { api } from "/js/core/api.js";

app.run(async () => {

    const id = obtenerId();

    // Director con imágenes
    const d = await api.get(`/api/admin/directores/${id}`);

    document.getElementById("nombre").textContent = d.nombre;
    document.getElementById("pais").textContent = d.pais;

    const imagenes = d.imagenes;

    if (!imagenes || !imagenes.length) return;

    const imgPrincipal = document.getElementById("imagen-principal");
    const galeria = document.getElementById("galeria");

    // principal
    imgPrincipal.src = imagenes[0].url;
    imgPrincipal.style.display = "block";
    imgPrincipal.style.cursor = "pointer";
    imgPrincipal.onclick = () => abrirModal(imagenes[0].url);

    // galería
    galeria.innerHTML = "";

    imagenes.slice(1).forEach(imagen => {

        const el = document.createElement("img");

        el.src = imagen.url;
        el.style.width = "120px";
        el.style.cursor = "pointer";

        el.onclick = () => abrirModal(imagen.url);

        galeria.appendChild(el);
    });

    // eventos modal
    document.getElementById("cerrar-modal").onclick = cerrarModal;

    document.getElementById("modal").onclick = (e) => {
        if (e.target.id === "modal") cerrarModal();
    };

    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") cerrarModal();
    });
});

function abrirModal(url) {
    const modal = document.getElementById("modal");
    const img = document.getElementById("modal-img");

    img.src = url;
    modal.style.display = "flex";
}

function cerrarModal() {
    document.getElementById("modal").style.display = "none";
}

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}