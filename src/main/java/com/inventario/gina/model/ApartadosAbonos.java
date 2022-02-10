package com.inventario.gina.model;

import java.util.Date;
import java.util.List;

public class ApartadosAbonos{
	
	private Integer idApartado;
	private String nombreCliente;
	private Date fechaApartado;
	private Date fechaAbono;
	private Double importe;
	private String estatus;
	private List<PrendaApartada> prendasApartadas;
	public ApartadosAbonos() {}

	public ApartadosAbonos(Integer idApartado, String nombreCliente, Date fechaApartado, Date fechaAbono, Double importe, String estatus) {
		this.idApartado = idApartado;
		this.nombreCliente = nombreCliente;
		this.fechaApartado = fechaApartado;
		this.fechaAbono = fechaAbono;
		this.importe = importe;
		this.estatus = estatus;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Integer getIdApartado() {
		return idApartado;
	}

	public void setIdApartado(Integer idApartado) {
		this.idApartado = idApartado;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public Date getFechaApartado() {
		return fechaApartado;
	}

	public void setFechaApartado(Date fechaApartado) {
		this.fechaApartado = fechaApartado;
	}

	public Date getFechaAbono() {
		return fechaAbono;
	}

	public void setFechaAbono(Date fechaAbono) {
		this.fechaAbono = fechaAbono;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public List<PrendaApartada> getPrendasApartadas() {
		return prendasApartadas;
	}

	public void setPrendasApartadas(List<PrendaApartada> prendasApartadas) {
		this.prendasApartadas = prendasApartadas;
	}

	@Override
	public String toString() {
		return "ApartadosAbonos [idApartado=" + idApartado + ", nombreCliente=" + nombreCliente + ", fechaApartado="
				+ fechaApartado + ", fechaAbono=" + fechaAbono + ", importe=" + importe + ", estatus=" + estatus
				+ ", prendasApartadas=" + prendasApartadas + "]";
	}
	
}
