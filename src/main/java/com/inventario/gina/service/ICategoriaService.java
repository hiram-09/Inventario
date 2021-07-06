package com.inventario.gina.service;

import java.util.List;

import com.inventario.gina.model.Categorias;

public interface ICategoriaService {
	
	public Categorias buscarPorId(Integer id);
	
	public List<Categorias> buscarTodas();
	
	public void guardar(Categorias categoria);
	
	public void eliminar(Integer id);
}
