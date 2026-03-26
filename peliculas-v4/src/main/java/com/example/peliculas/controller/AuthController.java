package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.peliculas.dto.UserResponse;
import com.example.peliculas.dto.auth.LoginRequest;
 
import com.example.peliculas.dto.auth.RegisterRequest;
import com.example.peliculas.entity.User;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.UserRepository;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@RestController
public class AuthController {

	private final DataSource ds;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public AuthController(DataSource ds) {
		this.ds = ds;
	}

	@PostMapping("/api/login")
	public void login(@RequestBody LoginRequest req, HttpSession session) {

		try (Connection con = ds.getConnection()) {

			UserRepository repo = new UserRepository(con);

			User user = repo.findByEmail(req.email());
			
			System.out.println(req);
			System.out.println(user);
			//System.out.println(encoder.encode("123456"));

			if (user != null && encoder.matches(req.passwordHash(), user.getPasswordHash())) {
				session.setAttribute("userId", user.getId());
				session.setAttribute("role", user.getRol());
				return;
				
				
			}

			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	/*
	// REGISTER
	@PostMapping("/api/register")
	public void register(@RequestBody RegisterRequest req) {

		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(true);
			UserRepository repo = new UserRepository(con);

			User user = new User(
				    encoder.encode(req.passwordHash()),
				    "cliente",
				    req.telefono(),
				    req.nombre(),
				    req.apellidos(),
				    req.email()
				);
				
			repo.insert(user);

		} catch (SQLException e) {

			// email duplicado
			if (e.getErrorCode() == 1062) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
			}

			throw new DataAccessException(e);
		}
	}*/
	@PostMapping("/api/register")
	public void register(@RequestBody RegisterRequest req) {
	    System.out.println("RegisterRequest recibida: " + req);
	    try (Connection con = ds.getConnection()) {
	        con.setAutoCommit(true);

	        UserRepository repo = new UserRepository(con);

	        // Aquí es donde se crea el usuario
	        User user = new User(
	            encoder.encode(req.passwordHash()), // 🔹 passwordHash debe tener valor
	            "cliente",
	            req.telefono(),
	            req.nombre(),
	            req.apellidos(),
	            req.email()
	        );

	        // Inicializar campos opcionales
	        user.setEstado("activo");
	        user.setDireccion("");
	        user.setPuntos(0);
	        user.setSalario(0f);
	        System.out.println("Usuario insertado: " + user);
	        
	        repo.insert(user);

	        System.out.println("Usuario insertado: " + user);

	    } catch (SQLException e) {
	        if (e.getErrorCode() == 1062) {
	            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
	        }
	        throw new DataAccessException(e);
	    }
	}

	// LOGOUT
	@PostMapping("/api/logout")
	public void logout(HttpSession session) {

		session.invalidate();
	}

	// CURRENT USER
	@GetMapping("/api/me")
	public UserResponse me(HttpSession session) {

		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		try (Connection con = ds.getConnection()) {

			UserRepository repo = new UserRepository(con);
			
			return repo.findResponseById(userId);

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	
}
