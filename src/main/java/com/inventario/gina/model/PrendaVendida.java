package com.inventario.gina.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prenda_vendida")
public class PrendaVendida {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Double precio;
	@OneToOne
	@JoinColumn(name="idPrenda")
	private Prenda prenda;
	@ManyToOne
	@JoinColumn(name = "idVenta")
	private Venta venta;
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;	
	
		
	public PrendaVendida(Integer id, Double precio, Prenda prenda, Venta venta, Usuario usuario) {
		super();
		this.id = id;
		this.precio = precio;
		this.prenda = prenda;
		this.venta = venta;
		this.usuario = usuario;
	}

	public PrendaVendida(Double precio, Prenda prenda, Venta venta, Usuario usuario) {
		super();
		this.precio = precio;
		this.prenda = prenda;
		this.venta = venta;
		this.usuario = usuario;
	}

	public PrendaVendida() {}
	
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

	public Prenda getPrenda() {
		return prenda;
	}

	public void setPrenda(Prenda prenda) {
		this.prenda = prenda;
	}

	@Override
	public String toString() {
		return "PrendaVendida [id=" + id + ", precio=" + precio + ", prenda=" + prenda + ", venta=" + venta
				+ ", usuario=" + usuario + "]";
	}
	
	
	
}
