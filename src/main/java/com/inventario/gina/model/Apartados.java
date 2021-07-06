package com.inventario.gina.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "apartados")
public class Apartados {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombreCliente;
	private String telefonoCliente;
	private Date fechaApartado;
	private Date fechaLiquidado;
	private Date fechaCancelado;
	private String estatus;
	
	
	@OneToMany(mappedBy = "apartado", cascade = CascadeType.ALL)
	private Set<Abonos> abonos;
	
	
	public Apartados() {
		
	}
	
	public Apartados(Integer id) {
		this.id = id;
	}
	public Apartados(String nombreCliente, String telefonoCliente, Date fechaApartado) {
		this.nombreCliente = nombreCliente;
		this.telefonoCliente = telefonoCliente;
		this.fechaApartado = fechaApartado;
		this.estatus = "Apartada";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getTelefonoCliente() {
		return telefonoCliente;
	}

	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
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

	public Set<Abonos> getAbonos() {
		return abonos;
	}

	public void setAbonos(Set<Abonos> abonos) {
		this.abonos = abonos;
	}

	

	@Override
	public String toString() {
		return "Apartados [id=" + id + ", nombreCliente=" + nombreCliente + ", telefonoCliente=" + telefonoCliente
				+ ", fechaApartado=" + fechaApartado + ", fechaLiquidado=" + fechaLiquidado + ", fechaCancelado="
				+ fechaCancelado + ", estatus=" + estatus + ", abonos=" + abonos + "]";
	}
	
}
