package muebles.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Resenya {
    private Integer idResenya; 
    
    @JsonProperty("id_usuario") // Añadimos soporte para snake_case
    private int usuarioId;
    
    @JsonProperty("id_producto") // Añadimos soporte para snake_case
    private int productoId;

    // ... (restos de campos)

    // Asegúrate de que los setters también tengan la anotación si el problema persiste
   
    
    private int puntuacion;
    private String comentario;
    private LocalDate fechaPublicacion;

    // Campos adicionales para mostrar en el Admin (JOINs)
    @JsonProperty("nombre_usuario")
    private String nombreUsuario;

    @JsonProperty("nombre_producto")
    private String nombreProducto;
    
    @JsonProperty("email_usuario")
    private String emailUsuario;

    

    public Resenya() {}

    // Getters y Setters existentes...
 // Getter y Setter
    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    
    public Integer getIdResenya() { return idResenya; }
    public void setIdResenya(Integer idResenya) { this.idResenya = idResenya; }

    public int getUsuarioId() { return usuarioId; }
    

    public int getProductoId() { return productoId; }
    @JsonProperty("id_producto")
    public void setProductoId(int productoId) { this.productoId = productoId; }
    
    @JsonProperty("id_usuario")
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    // Nuevos Getters y Setters para el Admin
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}