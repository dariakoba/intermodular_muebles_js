// Configuración de las 3 imágenes
const imagenes = [
    'img/1.png', 
    'img/2.png',        
    'img/3.png'    
];

let indiceActual = 0;
const heroSection = document.getElementById('hero-slider');
const btnNext = document.getElementById('heroNext');
const btnPrev = document.getElementById('heroPrev');
const dots = [document.getElementById('dot0'), document.getElementById('dot1'), document.getElementById('dot2')];

function actualizarSlider(indice) {
    // Cambia la imagen de fondo
    heroSection.style.backgroundImage = `url('${imagenes[indice]}')`;
    
    // Actualiza los puntos (dots)
    dots.forEach(dot => dot.classList.remove('active'));
    dots[indice].classList.add('active');
}

btnNext.addEventListener('click', () => {
    indiceActual++;
    if (indiceActual > imagenes.length - 1) {
        indiceActual = 0; // Vuelve a la primera
    }
    actualizarSlider(indiceActual);
});

btnPrev.addEventListener('click', () => {
    indiceActual--;
    if (indiceActual < 0) {
        indiceActual = imagenes.length - 1; // Va a la última
    }
    actualizarSlider(indiceActual);
});