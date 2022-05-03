package com.inventario.gina.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.model.PrendaApartada;

public interface PrendaApartadaRepository extends JpaRepository<PrendaApartada, Integer> {
	
	List<PrendaApartada> findByApartado(Apartados apartado);
	
	PrendaApartada findByPrenda(Prenda prenda);
	
	@Query("select pa from PrendaApartada pa where prenda.id = :idP and apartado.id = :idA")
	PrendaApartada buscarIdApartadoAndIdPrenda(@Param("idA") Integer idA, @Param("idP") Integer idP);
	
}
