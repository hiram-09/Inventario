package com.inventario.gina.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="prendas")
public class Prenda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String codigo;
	private String marca;
	private String talla;
	private String modelo;
	private String estatus;
	private Double precioCompra;
	private Double precioVenta;
	private Date fechaCreacion;
	private String caracteristicas;
	
	@OneToOne
	@JoinColumn(name = "idCategoria")
	private Categorias categoria;
	
	
	public Prenda(){}

	public Prenda(Integer id, String codigo, String marca, String talla, String modelo, Double precioVenta,
			Categorias categoria, String estatus, String caracteristicas) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.marca = marca;
		this.talla = talla;
		this.modelo = modelo;
		this.precioVenta = precioVenta;
		this.categoria = categoria;
		this.estatus = estatus;
		this.caracteristicas = caracteristicas;
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

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getTalla() {
		return talla;
	}

	public void setTalla(String talla) {
		this.talla = talla;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public Double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public String getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public Categorias getCategoria() {
		return categoria;
	}

	public void setCategoria(Categorias categoria) {
		this.categoria = categoria;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public String toString() {
		return "Prenda [id=" + id + ", codigo=" + codigo + ", marca=" + marca + ", talla=" + talla + ", modelo="
				+ modelo + ", estatus=" + estatus + ", precioCompra=" + precioCompra + ", precioVenta=" + precioVenta
				+ ", fechaCreacion=" + fechaCreacion + ", caracteristicas=" + caracteristicas + ", categoria="
				+ categoria + "]";
	}

	
}
