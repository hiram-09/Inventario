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

	@Override
	public void guardar(Categorias categoria) {
		categoriaRepository.save(categoria);		
	}

	@Override
	public void eliminar(Integer id) {
		categoriaRepository.deleteById(id);
	}

	@Override
	public Categorias buscarPorId(Integer id) {
		return categoriaRepository.findById(id).orElse(null);
	}

}
