package com.example.peliculas.entity;


public class ResenaImagen {

    private Integer id;
    private Integer resenaId;
    private String url;

    public ResenaImagen() {}

    public ResenaImagen(Integer id, Integer resenaId, String url) {
        this.id = id;
        this.resenaId = resenaId;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResenaId() {
        return resenaId;
    }

    public void setResenaId(Integer resenaId) {
        this.resenaId = resenaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}