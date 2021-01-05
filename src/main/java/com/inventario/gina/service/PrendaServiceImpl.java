package com.inventario.gina.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.inventario.gina.model.Prenda;
import com.inventario.gina.repository.PrendaRepository;

@Service
public class PrendaServiceImpl implements IPrendaService {

	@Autowired
	PrendaRepository prendaRepository;
	
	@Override
	public List<Prenda> listarTodas() {
		return (List<Prenda>) prendaRepository.findByEstatus("Disponible");
	}

	@Override
	public Prenda guardar(Prenda prenda) {
		return prendaRepository.save(prenda);
	}

	@Override
	public List<Prenda> busquedaPorCodigoMarca(String codigo, String marca) {
		return prendaRepository.findByCodigoAndMarca(codigo, marca);
	}

	@Override
	public List<Prenda> busquedaPorCodigo(String codigo) {
		return prendaRepository.findByCodigo(codigo);
	}

	@Override
	public List<Prenda> busquedaPorMarca(String marca) {
		return prendaRepository.findByMarca(marca);
	}

	@Override
	public Prenda buscarPorId(Integer id) {
		return prendaRepository.findById(id).orElse(null);
	}

	@Override
	public Prenda buscarPorCodigo(String codigo) {
		return prendaRepository.findFirstByCodigo(codigo);
	}

	@Override
	public void eliminar(Integer id) {
		prendaRepository.deleteById(id);
	}

	@Override
	public List<Prenda> buscarByExample(Example<Prenda> example) {
		return prendaRepository.findAll(example); 
	}

}
