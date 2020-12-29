package com.inventario.gina.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Categorias;
import com.inventario.gina.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Override
	public List<Categorias> buscarTodas() {
		return (List<Categorias>) categoriaRepository.findAll();
	}

}
