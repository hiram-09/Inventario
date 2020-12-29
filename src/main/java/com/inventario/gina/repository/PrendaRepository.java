package com.inventario.gina.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.inventario.gina.model.Categorias;
import com.inventario.gina.model.Prenda;

public interface PrendaRepository extends CrudRepository<Prenda, Integer> {
	List<Prenda> findByEstatus(String disponible);
	List<Prenda> findByCodigoAndMarca(String codigo, String marca);
	List<Prenda> findByCodigo(String codigo);
	List<Prenda> findByMarca(String marca);
}
