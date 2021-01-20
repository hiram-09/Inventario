package com.inventario.gina.service;

import java.util.List;

import com.inventario.gina.model.Abonos;

public interface IAbonosService {
	List<Abonos> listarAbonos();
	Abonos crearAbono(Abonos abono);
}
