package com.dna.muebles.dto.auth;

public record LoginRequest(
	String email,
	String passwordHash
) {}
