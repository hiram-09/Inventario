package com.inventario.gina.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.gina.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
	public Usuario findByUsername(String username);
	
}
