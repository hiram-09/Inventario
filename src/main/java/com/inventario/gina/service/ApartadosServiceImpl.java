package com.inventario.gina.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.ApartadosAbonos;
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
		return null;//apartadosRepo.findByPrendaOrderByIdDesc(prenda); 
	}

	@Override
	public List<Apartados> buscarPorNombre(String nombre) {
		return null;//apartadosRepo.findByNombreContainsOrderByIdDesc(nombre);
	}

	@Override
	public List<Apartados> buscarTodosDesc() {
		return apartadosRepo.findAllByOrderByIdDesc();
	}

	@Override
	public List<Apartados> buscarPorFechaLiquidado(Date desde, Date hasta) {
		return apartadosRepo.findByFechaLiquidadoBetween(desde, hasta);
	}

	@Override
	public List<Apartados> buscarPorFechaLiquidado(Date fecha) {
		return apartadosRepo.findByFechaLiquidado(fecha);
	}

	@Override
	public List<ApartadosAbonos> buscarPorFechaAbono(Date fecha) {
		return apartadosRepo.getByFechaAbono(fecha);
	}

	@Override
	public List<ApartadosAbonos> buscarPorFechaAbono(Date desde, Date hasta) {
		return apartadosRepo.getByFechasAbono(desde, hasta);
	}

}
