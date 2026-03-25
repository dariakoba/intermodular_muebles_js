package com.example.peliculas.security;

import jakarta.servlet.http.HttpSession;

public class CurrentUser {

	private final Integer id;
	private final String role;

	public CurrentUser(HttpSession session) {

		if (session == null) {
			this.id = null;
			this.role = null;
			return;
		}

		this.id = (Integer) session.getAttribute("userId");
		this.role = (String) session.getAttribute("role");
	}

	public boolean isAuthenticated() {
		return id != null;
	}

	public Integer getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

	public boolean hasRole(String role) {
		return this.role != null && this.role.equals(role);
	}
}
