package com.inventario.gina.service;

import java.util.List;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.PrendaApartada;

public interface IPrendaApartadaService {
	PrendaApartada guardar(PrendaApartada prendaApartada); 
	
	List<PrendaApartada> buscarTodas();
	
	List<PrendaApartada> buscarPorApartado(Apartados apartado);
	
	PrendaApartada buscarPorIdApartadoAndIdPrenda(Integer idApartado, Integer idPrenda);
}
