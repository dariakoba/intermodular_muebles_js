package com.example.peliculas.entity;

public class User {
    private int id;                   // id SERIAL -> int
    private String passwordHash;      // password_hash VARCHAR(255)
    private String rol;               // rol VARCHAR(20)
    private String telefono;          // telefono VARCHAR(20)
    private String estado;            // estado VARCHAR(20)
    private String nombre;            // nombre VARCHAR(50)
    private String apellidos;         // apellidos VARCHAR(50)
    private String direccion;         // direccion VARCHAR(100)
    private String email;
    private int puntos;
    private int nivelAcceso;
    private float salario;
	public User(int id, String passwordHash, String rol, String telefono, String estado, String nombre,
			String apellidos, String direccion, String email, int puntos, int nivelAcceso, float salario) {
		super();
		this.id = id;
		this.passwordHash = passwordHash;
		this.rol = rol;
		this.telefono = telefono;
		this.estado = estado;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.email = email;
		this.puntos = puntos;
		this.nivelAcceso = nivelAcceso;
		this.salario = salario;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	public int getNivelAcceso() {
		return nivelAcceso;
	}
	public void setNivelAcceso(int nivelAcceso) {
		this.nivelAcceso = nivelAcceso;
	}
	public float getSalario() {
		return salario;
	}
	public void setSalario(float salario) {
		this.salario = salario;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", passwordHash=" + passwordHash + ", rol=" + rol + ", telefono=" + telefono
				+ ", estado=" + estado + ", nombre=" + nombre + ", apellidos=" + apellidos + ", direccion=" + direccion
				+ ", email=" + email + ", puntos=" + puntos + ", nivelAcceso=" + nivelAcceso + ", salario=" + salario
				+ "]";
	}
    
    

	    
	
}
