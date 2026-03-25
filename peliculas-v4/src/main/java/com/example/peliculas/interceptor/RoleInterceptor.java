package com.example.peliculas.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class RoleInterceptor implements HandlerInterceptor {
/*
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler)
			throws Exception {

		HttpSession session = request.getSession(false);

		String role = (String) session.getAttribute("rol");

		if (!"admin".equals(role)) {

			String path = request.getRequestURI();

			if (path.startsWith("/api/")) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else {
				response.sendRedirect("/error/403.html");
			}

			return false;
		}

		return true;
	}
	*/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
	                         Object handler) throws Exception {

	    String path = request.getRequestURI();

	    // 🔓 PERMITIR REGISTER Y LOGIN
	    if (path.startsWith("/api/login") || path.startsWith("/api/register")) {
	        return true;
	    }

	    HttpSession session = request.getSession(false);

	    if (session == null) {
	        return true; // 🔓 temporalmente dejar pasar
	    }

	    String role = (String) session.getAttribute("role"); // 👈 CORREGIDO

	    if (!"admin".equals(role)) {

	        if (path.startsWith("/api/")) {
	            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        } else {
	            response.sendRedirect("/error/403.html");
	        }

	        return false;
	    }

	    return true;
	}
}
