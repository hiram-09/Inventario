package com.inventario.gina.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.inventario.gina.model.Prenda;

public interface PrendaRepository extends JpaRepository<Prenda, Integer> {
	
	List<Prenda> findByEstatusOrderByFechaCreacionDesc(String disponible);
	
	List<Prenda> findByCodigoAndMarca(String codigo, String marca);
	
	List<Prenda> findByCodigo(String codigo);
	
	List<Prenda> findByMarca(String marca);
	
	Prenda findFirstByCodigo(String codigo);
	
	Page<Prenda> findByEstatusOrderByFechaCreacionDesc(String estatus, Pageable page);
}
