package com.inventario.gina.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.inventario.gina.model.Abonos;
import com.inventario.gina.model.Apartados;

public interface AbonosRepository extends CrudRepository<Abonos, Integer> {
	
	List<Abonos> findByFecha(Date fecha);
	
	List<Abonos> findByApartado(Apartados apartado);
	
	
}
