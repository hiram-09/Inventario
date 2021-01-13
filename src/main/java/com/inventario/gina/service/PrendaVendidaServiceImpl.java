package com.inventario.gina.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.PrendaVendida;
import com.inventario.gina.repository.PrendaVendidaRepository;

@Service
public class PrendaVendidaServiceImpl implements IPrendaVendidaService {

	@Autowired
	PrendaVendidaRepository prendaVendidaRepo;
	
	@Override
	public void guardar(PrendaVendida prendaVendida) {
		prendaVendidaRepo.save(prendaVendida);
	}
	@Override
	public List<PrendaVendida> buscarPorFechaVenta(Date fecha) {
		return prendaVendidaRepo.getByFecha(fecha);
	}
	@Override
	public List<PrendaVendida> buscarPorFechas(Date desde, Date hasta) {
		return prendaVendidaRepo.findByVentaFechaBetween(desde, hasta);
	}

}
