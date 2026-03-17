package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.peliculas.entity.User;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

public class AuthController {
	private final DataSource ds;
	private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
	
	public AuthController (DataSource ds) {
		this.ds=ds;
	}
	
	@PostMapping("/api/login")
	public void login(@RequestBody LoginRequest req, HttpSession session) {
		try (Connection con = ds.getConnection()){
			UserRepository repo= new UserRepository(con);
			
			User user = repo.findByEmail(req.email());
			 //System.out.println(encoder.encode("123456"));
			 
			 //comprobacion
			if (user != null && encoder.matches(req.password(),user.getPasswordHash())) {
				 session.setAttribute("userId", user.getId());
				 session.setAttribute("role", user.getRol());
				 return;
			}
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	//registrar
	@PostMapping("/api/register")
	public void register(@RequestBody RegisterRequest req) {
		try (Connection con = ds.getConnection()){
			UserRepository repo = new UserRepository(con);
			
			User user = new User(null, req.name(), req.email(), encoder.encode(req.password));
			repo.insert(user);
			
		}catch(SQLException e) {
			if (e.getErrorCode() ==1062) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "el email ya existe");
			}
		}
	}
}
