package com.inventario.gina.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "abonos")
public class Abonos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Date fecha;
	private Double importe;
	
	@ManyToOne
	@JoinColumn(name = "idApartado")
	private Apartados apartado;
	
	public Abonos(Double importe, Apartados apartado) {
		super();
		this.fecha = new Date();
		this.importe = importe;
		this.apartado = apartado;
	}
	public Abonos(Double importe, Apartados apartado, Date fecha) {
		super();
		this.fecha = fecha;
		this.importe = importe;
		this.apartado = apartado;
	}
	public Apartados getApartado() {
		return apartado;
	}

	public void setApartado(Apartados apartado) {
		this.apartado = apartado;
	}

	public Abonos() {
		
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	@Override
	public String toString() {
		return "Abonos [id=" + id + ", fecha=" + fecha + ", importe=" + importe + "]";
	}
	
	
}
