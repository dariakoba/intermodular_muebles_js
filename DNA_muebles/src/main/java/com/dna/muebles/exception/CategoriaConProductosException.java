package com.dna.muebles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoriaConProductosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CategoriaConProductosException() {
        super("No se puede eliminar una categoría con productos asociados");
    }
}