package com.inventario.gina.service;

import java.util.Date;
import java.util.List;

import com.inventario.gina.model.PrendaVendida;

public interface IPrendaVendidaService {
	public void guardar(PrendaVendida prendaVendida);
	public List<PrendaVendida> buscarPorFechaVenta(Date fecha);
	public List<PrendaVendida> buscarPorFechas(Date desde, Date hasta);
}
