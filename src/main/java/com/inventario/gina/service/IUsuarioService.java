package com.inventario.gina.service;

import com.inventario.gina.model.Usuario;

public interface IUsuarioService {
	Usuario buscarPorId(Integer id);
	
	public Usuario buscarPorUsername(String username);
	
	public void guardar(Usuario usuario);
}
