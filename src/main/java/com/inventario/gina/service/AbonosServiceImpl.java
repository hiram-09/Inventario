package com.inventario.gina.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Abonos;
import com.inventario.gina.repository.AbonosRepository;

@Service
public class AbonosServiceImpl implements IAbonosService {

	@Autowired
	AbonosRepository abonosRepo;
	
	@Override
	public List<Abonos> listarAbonos() {
		return (List<Abonos>) abonosRepo.findAll();
	}

	@Override
	public Abonos crearAbono(Abonos abono) {
		return abonosRepo.save(abono);
	}

}
