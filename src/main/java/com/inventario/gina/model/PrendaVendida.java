package com.inventario.gina.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prenda_vendida")
public class PrendaVendida {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String codigo;
	private String modelo;
	private Double precio;
	@ManyToOne
	@JoinColumn(name = "idVenta")
	private Venta venta;
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;	
	
	public PrendaVendida(Integer id, String codigo, String modelo, Double precio, Venta venta, Usuario usuario) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.modelo = modelo;
		this.precio = precio;
		this.venta = venta;
		this.usuario = usuario;
	}
	public PrendaVendida(String codigo, String modelo, Double precio, Venta venta, Usuario usuario) {
		super();
		this.codigo = codigo;
		this.modelo = modelo;
		this.precio = precio;
		this.venta = venta;
		this.usuario = usuario;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}
