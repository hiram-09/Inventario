package com.inventario.gina.service;

import java.util.Date;
import java.util.List;

import com.inventario.gina.model.Venta;

public interface IVentaService {
	
	public List<Venta> buscarPorFecha(Date fecha);
	
	public Venta guardar(Venta venta);
	
	public void actualizaFechaVenta(Venta venta);
	
	public void eliminarVenta(Venta venta);
}
