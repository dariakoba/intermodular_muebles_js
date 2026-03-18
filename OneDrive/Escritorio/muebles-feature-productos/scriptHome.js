const sliderGrid = document.getElementById('sliderGrid');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');

let counter = 0;

nextBtn.addEventListener('click', () => {
    const cardWidth = document.querySelector('.category-card').clientWidth + 20; // Ancho + gap
    const maxScroll = sliderGrid.scrollWidth - sliderGrid.clientWidth;
    
    if (Math.abs(counter) < maxScroll) {
        counter -= cardWidth;
        sliderGrid.style.transform = `translateX(${counter}px)`;
    } else {
        
        counter = 0;
        sliderGrid.style.transform = `translateX(0px)`;
    }
});

prevBtn.addEventListener('click', () => {
    const cardWidth = document.querySelector('.category-card').clientWidth + 20;
    
    if (counter < 0) {
        counter += cardWidth;
        sliderGrid.style.transform = `translateX(${counter}px)`;
    }
});