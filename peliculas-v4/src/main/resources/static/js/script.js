function cambiarColor(color){
    let imagen = document.getElementById("imagenProducto");

    if(color=="gris"){
        imagen.src="images/producto/griscama.png";
    }

    if(color=="beige"){
        imagen.src="images/producto/cama01_beige.png";
    }

    if(color=="marron"){
        imagen.src="images/producto/cama01_marron.png";
    }

    if(color=="oscuro"){
        imagen.src="images/producto/cama01_oscuro.png";
    }
}