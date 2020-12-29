package com.inventario.gina.service;

import java.util.List;

import com.inventario.gina.model.Prenda;

public interface IPrendaService {
	public List<Prenda> listarTodas();
	public Prenda guardar(Prenda prenda);
	public List<Prenda> busquedaPorCodigoMarca(String codigo, String marca);
	public List<Prenda> busquedaPorCodigo(String codigo);
	public List<Prenda> busquedaPorMarca(String marca);
}
