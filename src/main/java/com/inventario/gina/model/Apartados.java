package com.inventario.gina.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "apartados")
public class Apartados {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String telefono;
	private Date fechaApartado;
	private Date fechaLiquidado;
	private Date fechaCancelado;
	private String estatus;
	private Double precio;
	
	@OneToOne
	@JoinColumn(name="idPrenda")
	private Prenda prenda;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "apartados")
	private Set<Abonos> abonos;

	public Apartados(String nombre, String telefono, Double precio, Prenda prenda) {
		this.fechaApartado = new Date();
		this.estatus = "Apartada";
		this.nombre = nombre;
		this.telefono = telefono;
		this.precio = precio;
		this.prenda = prenda;
	}
    
	public Apartados() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Date getFechaApartado() {
		return fechaApartado;
	}

	public void setFechaApartado(Date fechaApartado) {
		this.fechaApartado = fechaApartado;
	}

	public Date getFechaLiquidado() {
		return fechaLiquidado;
	}

	public void setFechaLiquidado(Date fechaLiquidado) {
		this.fechaLiquidado = fechaLiquidado;
	}

	public Date getFechaCancelado() {
		return fechaCancelado;
	}

	public void setFechaCancelado(Date fechaCancelado) {
		this.fechaCancelado = fechaCancelado;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Prenda getPrenda() {
		return prenda;
	}

	public void setPrenda(Prenda prenda) {
		this.prenda = prenda;
	}

	public Set<Abonos> getAbonos() {
		return abonos;
	}

	public void setAbonos(Set<Abonos> abonos) {
		this.abonos = abonos;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getRestante() {
		Double precioApartado = this.getPrecio();
		for(Abonos abono : this.getAbonos()) {
			precioApartado -= abono.getImporte();
		}
		return precioApartado;
	}

	@Override
	public String toString() {
		return "Apartados [idApartado=" + id + ", nombre=" + nombre + ", telefono=" + telefono + ", fechaApartado="
				+ fechaApartado + ", fechaLiquidado=" + fechaLiquidado + ", fechaCancelado=" + fechaCancelado
				+ ", estatus=" + estatus + ", precio=" + precio + ", restante=" + getRestante() + ", prenda=" + prenda + ", abonos=" + abonos + "]";
	}
	
	
}
