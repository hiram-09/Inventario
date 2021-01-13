package com.inventario.gina.service;

import java.util.Date;
import java.util.List;

import com.inventario.gina.model.Venta;

public interface IVentaService {
	List<Venta> buscarPorFecha(Date fecha);
	Venta guardar(Venta venta);
}
