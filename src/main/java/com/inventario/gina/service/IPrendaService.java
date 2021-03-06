package com.inventario.gina.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inventario.gina.model.Prenda;

public interface IPrendaService {
	public List<Prenda> listarTodas();
	
	public Prenda guardar(Prenda prenda);
	
	public Prenda buscarPorId(Integer id);
	
	public Prenda buscarPorCodigo(String codigo);
	
	public void eliminar(Integer id);
	
	public Page<Prenda> buscarByExample(Example<Prenda> example, Pageable page);
	
	public List<Prenda> busquedaPorCodigoMarca(String codigo, String marca);
	
	public List<Prenda> busquedaPorCodigo(String codigo);
	
	public List<Prenda> busquedaPorMarca(String marca);
	
	public void actualizarPrendas(List<Prenda> prendas);
	
	Page<Prenda> buscarTodas(Pageable page);
}

