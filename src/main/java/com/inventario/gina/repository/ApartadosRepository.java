package com.inventario.gina.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;

public interface ApartadosRepository extends CrudRepository<Apartados, Integer> {
	Apartados findFirstById(Integer id);
	
	List<Apartados> findByPrendaOrderByIdDesc(Prenda prenda);
	
	List<Apartados> findByNombreContainsOrderByIdDesc(String nombre);
	
	List<Apartados> findAllByOrderByIdDesc();
}
