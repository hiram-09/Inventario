package com.inventario.gina.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.gina.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
	List<Venta> findByFecha(Date fecha);
}
