package com.inventario.gina.service;

import java.util.Date;
import java.util.List;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.ApartadosAbonos;
import com.inventario.gina.model.Prenda;

public interface IApartadosService {
	public List<Apartados> listarApartados();
	
	public Apartados crearApartado(Apartados apartado);
	
	public Apartados buscarPorId(Integer id);
	
	public void actualizar(Apartados apartado);
	
	public List<Apartados> buscarPorPrenda(Prenda prenda);
	
	public List<Apartados> buscarPorNombre(String nombre);
	
	public List<Apartados> buscarTodosDesc();
	
	public List<Apartados> buscarPorFechaLiquidado(Date desde, Date hasta);
	
	public List<Apartados> buscarPorFechaLiquidado(Date fecha);
	
	public List<ApartadosAbonos> buscarPorFechaAbono(Date fecha);
	
	public List<ApartadosAbonos> buscarPorFechaAbono(Date desde, Date hasta);
}
