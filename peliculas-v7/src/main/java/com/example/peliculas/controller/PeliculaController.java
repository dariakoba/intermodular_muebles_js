package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.peliculas.dto.PeliculaDetalleDTO;
import com.example.peliculas.dto.PeliculaResumen;
import com.example.peliculas.dto.VotoDTO;
import com.example.peliculas.dto.VotoRequest;
import com.example.peliculas.entity.Voto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.PeliculaRepository;
import com.example.peliculas.repository.VotoRepository;
import com.example.peliculas.security.CurrentUser;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {
	private final DataSource ds;

	public PeliculaController(DataSource ds) {
		this.ds = ds;
	}

	@GetMapping
	public List<PeliculaResumen> index() {
		try (Connection con = ds.getConnection()) {
			PeliculaRepository repo = new PeliculaRepository(con);
			return repo.findResumen();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	@GetMapping("/{id}")
	public PeliculaDetalleDTO show(HttpSession session, @PathVariable int id) {
		try (Connection con = ds.getConnection()) {
			CurrentUser user = new CurrentUser(session);
			PeliculaRepository repo = new PeliculaRepository(con);
			return repo.findDetalle(id, user.getId());
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	@PostMapping("/{id}/voto")
	public Voto votar(HttpSession session, @PathVariable int id, @RequestBody VotoRequest req) {
		try (Connection con = ds.getConnection()) {
			CurrentUser user = new CurrentUser(session);
			int userId = user.getId();

			VotoRepository repo = new VotoRepository(con);
			System.out.println("id: " + id);
			System.out.println("userId: " + userId);
			if (repo.exists(id, userId)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya has votado esta película");
			}

			Voto voto = new Voto(null, id, userId, req.puntuacion(), req.critica(), LocalDate.now());
			repo.insert(voto);

			return voto;
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	@GetMapping("/{id}/votos")
	public List<VotoDTO> votar(@PathVariable int id) {
		try (Connection con = ds.getConnection()) {
			VotoRepository repo = new VotoRepository(con);
			return repo.findByPeliculaId(id);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
