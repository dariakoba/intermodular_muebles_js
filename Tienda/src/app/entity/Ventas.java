package app.entity;

public class Ventas {

	private Integer id;
	private int articuloId;
	private int cantidad;
	private double total;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getArticuloId() {
		return articuloId;
	}
	public void setArticuloId(int articuloId) {
		this.articuloId = articuloId;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	
	public Ventas(Integer id, int articuloId, int cantidad, double total) {
		super();
		this.id = id;
		this.articuloId = articuloId;
		this.cantidad = cantidad;
		this.total = total;
	}
	
	
	/*
	public Ventas(int articuloId, int cantidad, double total) {
		super();
		this.id=null;
		this.articuloId = articuloId;
		this.cantidad = cantidad;
		this.total = total;
	}
	*/
	
	
	@Override
	public String toString() {
		return "Ventas [id=" + id + ", articuloId=" + articuloId + ", cantidad=" + cantidad + ", total=" + total + "]";
	}
	
	
}
