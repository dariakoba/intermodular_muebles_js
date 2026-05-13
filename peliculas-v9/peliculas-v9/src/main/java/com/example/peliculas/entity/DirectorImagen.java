package com.example.peliculas.entity;

public class DirectorImagen {

    private Integer id;
    private Integer directorId;
    private String url;
    
	public DirectorImagen(Integer id, Integer directorId, String url) {
		super();
		this.id = id;
		this.directorId = directorId;
		this.url = url;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDirectorId() {
		return directorId;
	}
	public void setDirectorId(Integer directorId) {
		this.directorId = directorId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "DirectorImagen [id=" + id + ", directorId=" + directorId + ", url=" + url + "]";
	}
}