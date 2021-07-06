package com.inventario.gina.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.model.PrendaApartada;

public interface PrendaApartadaRepository extends JpaRepository<PrendaApartada, Integer> {
	
	List<PrendaApartada> findByApartado(Apartados apartado);
	
	PrendaApartada findByPrenda(Prenda prenda);
	
}
