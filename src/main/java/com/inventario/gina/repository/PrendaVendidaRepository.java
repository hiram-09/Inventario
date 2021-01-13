package com.inventario.gina.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventario.gina.model.PrendaVendida;

public interface PrendaVendidaRepository extends JpaRepository<PrendaVendida, Integer> {
	@Query("select p from PrendaVendida p where venta.fecha= :fecha group by venta.id")
	List<PrendaVendida> getByFecha(@Param("fecha") Date fecha);
	List<PrendaVendida> findByVentaFechaBetween(Date desde, Date hasta);
}