package com.example.peliculas.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {

			String path = request.getRequestURI();

			if (path.startsWith("/api/")) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				response.sendRedirect("/login.html");
			}

			return false;
		}

		return true;
	}
}
