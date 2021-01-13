package com.inventario.gina.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Usuario;
import com.inventario.gina.repository.UsuariosRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	UsuariosRepository usuariosRepository;
	
	@Override
	public Usuario buscarPorId(Integer id) {
		return usuariosRepository.findById(id).orElse(null);				
	}

}
