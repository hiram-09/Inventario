package com.inventario.gina.service;

import java.util.List;

import com.inventario.gina.model.Abonos;
import com.inventario.gina.model.Apartados;

public interface IAbonosService {
	
	List<Abonos> listarAbonos();
	
	Abonos crearAbono(Abonos abono);
	
	List<Abonos> buscarPorApartado(Apartados apartado);
}
