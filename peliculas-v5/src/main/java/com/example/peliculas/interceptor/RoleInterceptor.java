package com.example.peliculas.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class RoleInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler)
			throws Exception {

		HttpSession session = request.getSession(false);

		String role = (String) session.getAttribute("role");

		if (!"ADMIN".equals(role)) {

			String path = request.getRequestURI();

			if (path.startsWith("/api/")) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else {
				response.sendRedirect("/");
			}

			return false;
		}

		return true;
	}
}
