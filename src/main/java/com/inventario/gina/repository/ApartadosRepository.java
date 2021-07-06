package com.inventario.gina.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.ApartadosAbonos;
import com.inventario.gina.model.Prenda;

public interface ApartadosRepository extends CrudRepository<Apartados, Integer> {
	Apartados findFirstById(Integer id);
	
	//List<Apartados> findByPrendaOrderByIdDesc(Prenda prenda);
	
	//List<Apartados> findByNombreContainsOrderByIdDesc(String nombre);
	
	List<Apartados> findAllByOrderByIdDesc();
	
	List<Apartados> findByFechaLiquidadoBetween(Date desde, Date hasta);
	
	List<Apartados> findByFechaLiquidado(Date fecha);
	
	
	@Query("select new com.inventario.gina.model.ApartadosAbonos(ap.id, ap.nombreCliente, ap.fechaApartado, ab.fecha, ab.importe) from Apartados ap inner join Abonos ab on ap.id = ab.apartado.id where ab.fecha = :fecha order by ab.fecha desc")
	List<ApartadosAbonos> getByFechaAbono(@Param("fecha") Date fecha);
	
	@Query("select new com.inventario.gina.model.ApartadosAbonos(ap.id, ap.nombreCliente, ap.fechaApartado, ab.fecha, ab.importe) from Apartados ap inner join Abonos ab on ap.id = ab.apartado.id where ab.fecha between :desde and :hasta  order by ab.fecha desc")
	List<ApartadosAbonos> getByFechasAbono(@Param("desde") Date desde, @Param("hasta") Date hasta);

}
