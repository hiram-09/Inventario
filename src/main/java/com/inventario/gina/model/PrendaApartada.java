package com.inventario.gina.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prenda_apartada")
public class PrendaApartada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Double precio;
	
	@ManyToOne
	@JoinColumn(name = "idPrenda")
	private Prenda prenda;
	
	@ManyToOne
	@JoinColumn(name = "idApartado")
	private Apartados apartado;

	
	
	public PrendaApartada() {
	}

	public PrendaApartada(Double precio, Prenda prenda, Apartados apartado) {
		this.precio = precio;
		this.prenda = prenda;
		this.apartado = apartado;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Prenda getPrenda() {
		return prenda;
	}

	public void setPrenda(Prenda prenda) {
		this.prenda = prenda;
	}

	public Apartados getApartado() {
		return apartado;
	}

	public void setApartado(Apartados apartado) {
		this.apartado = apartado;
	}
	
	public Double getTotalAbonos() {
		return this.apartado.getAbonos().stream().mapToDouble(abono -> abono.getImporte()).sum();
	}
	
	@Override
	public String toString() {
		return "PrendaApartada [id=" + id + ", precio=" + precio + ", prenda=" + prenda + ", apartado=" + apartado + ", Total Importe=" + getTotalAbonos() +"]";
	}
	
}
