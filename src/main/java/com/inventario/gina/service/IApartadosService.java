package com.inventario.gina.service;

import java.util.List;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;

public interface IApartadosService {
	public List<Apartados> listarApartados();
	
	public Apartados crearApartado(Apartados apartado);
	
	public Apartados buscarPorId(Integer id);
	
	public void actualizar(Apartados apartado);
	
	public List<Apartados> buscarPorPrenda(Prenda prenda);
	
	public List<Apartados> buscarPorNombre(String nombre);
	
	public List<Apartados> buscarTodosDesc();
}
