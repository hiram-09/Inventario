package com.inventario.gina.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Venta;
import com.inventario.gina.repository.VentaRepository;

@Service
public class VentaServiceImpl implements IVentaService {

	@Autowired
	VentaRepository ventaRepository;
	
	@Override
	public List<Venta> buscarPorFecha(Date fecha) {
		return ventaRepository.findByFecha(fecha);
	}

	@Override
	public Venta guardar(Venta venta) {
		return ventaRepository.save(venta);
	}

}
