package com.example.peliculas.controller;

import javax.sql.DataSource;

public class BaseController {
	
	protected final DataSource ds;

	public BaseController(DataSource ds) {
		this.ds = ds;
	}
}
