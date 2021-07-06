package com.inventario.gina.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.PrendaApartada;
import com.inventario.gina.repository.PrendaApartadaRepository;

@Service
public class PrendaApartadaServiceImpl implements IPrendaApartadaService {

	
	@Autowired
	private PrendaApartadaRepository prendaApartadaRepo;
	
	@Override
	public PrendaApartada guardar(PrendaApartada prendaApartada) {
		return prendaApartadaRepo.save(prendaApartada);
	}

	@Override
	public List<PrendaApartada> buscarTodas() {
		return prendaApartadaRepo.findAll();
	}

	@Override
	public List<PrendaApartada> buscarPorApartado(Apartados apartado) {
		return prendaApartadaRepo.findByApartado(apartado);
	}

}
