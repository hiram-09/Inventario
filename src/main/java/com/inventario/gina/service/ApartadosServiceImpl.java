package com.inventario.gina.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.repository.ApartadosRepository;

@Service
public class ApartadosServiceImpl implements IApartadosService {

	@Autowired
	ApartadosRepository apartadosRepo;
	
	@Override
	public List<Apartados> listarApartados() {
		return (List<Apartados>) apartadosRepo.findAll();
	}

	@Override
	public Apartados crearApartado(Apartados apartado) {
		return apartadosRepo.save(apartado);		
	}

	@Override
	public Apartados buscarPorId(Integer id) {
		return apartadosRepo.findFirstById(id);
	}

	@Override
	public void actualizar(Apartados apartado) {
		apartadosRepo.save(apartado);		
	}

	@Override
	public List<Apartados> buscarPorPrenda(Prenda prenda) {
		return apartadosRepo.findByPrendaOrderByIdDesc(prenda);
	}

	@Override
	public List<Apartados> buscarPorNombre(String nombre) {
		return apartadosRepo.findByNombreContainsOrderByIdDesc(nombre);
	}

	@Override
	public List<Apartados> buscarTodosDesc() {
		return apartadosRepo.findAllByOrderByIdDesc();
	}

}
