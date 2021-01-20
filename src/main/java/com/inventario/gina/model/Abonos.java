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
	private Apartados apartados;
	
	public Abonos(Double importe, Apartados apartados) {
		super();
		this.fecha = new Date();
		this.importe = importe;
		this.apartados = apartados;
	}
	
	public Apartados getApartados() {
		return apartados;
	}

	public void setApartados(Apartados apartados) {
		this.apartados = apartados;
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
