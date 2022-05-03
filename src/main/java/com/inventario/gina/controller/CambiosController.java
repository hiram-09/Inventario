package com.inventario.gina.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.model.PrendaApartada;
import com.inventario.gina.model.PrendaVendida;
import com.inventario.gina.model.Venta;
import com.inventario.gina.service.IApartadosService;
import com.inventario.gina.service.IPrendaApartadaService;
import com.inventario.gina.service.IPrendaService;
import com.inventario.gina.service.IPrendaVendidaService;
import com.inventario.gina.service.IVentaService;

@Controller
@RequestMapping("/cambios")
public class CambiosController {
	
	@Autowired
	private IPrendaVendidaService prendaVendidaService;
	
	@Autowired
	private IPrendaService prendaService;
	
	@Autowired
	private IApartadosService apartadoService;
	
	@Autowired
	private IPrendaApartadaService prendaApartadaService;
	
	private PrendaVendida prendaDevuelta;
	private Prenda prendaCambio;
	private static PrendaApartada prendaApartadaDevuelta;
	private static Prenda prendaApartadaCambio;
	
	@GetMapping("/")
	public String cambiosIndex() {
		prendaDevuelta = null;
		prendaCambio = null;
		return "cambios/cambio";
	}
	
	@GetMapping("/cambio")
	public String cambios() {
		return "cambios/cambio";
	}
	
	@GetMapping("/apartado/cambio")
	public String cambiosApartado() {
		return "cambios/cambioApartado";
	}
	
	@GetMapping("/buscar/devuelta")
	public String buscarPrendaDevuelta(@RequestParam("codigoDevuelto") String codigo, Model model, RedirectAttributes att) {
		this.prendaDevuelta = prendaVendidaService.buscarPorCodigo(codigo);	
		if(prendaDevuelta == null) {
			att.addFlashAttribute("mensaje", "La prenda con código " + codigo + " no tiene registro de que se haya vendido.").
			    addFlashAttribute("clase", "warning");
			return "redirect:/cambios/cambio";
		}
		model.addAttribute("prendaDevuelta", this.prendaDevuelta);
		return "cambios/cambio";
	}
	
	@SuppressWarnings("static-access")
	@PostMapping("/buscar/apartada/devuelta")
	public String buscarPrendaApartadaDevuelta(@RequestParam Integer idApartado, @RequestParam Integer idPrenda, Model model, RedirectAttributes att) {
		this.prendaApartadaDevuelta = prendaApartadaService.buscarPorIdApartadoAndIdPrenda(idApartado, idPrenda);
		if(this.prendaApartadaDevuelta == null) {
			att.addFlashAttribute("mensaje", "La prenda con código " + this.prendaApartadaDevuelta.getPrenda().getCodigo() + " no tiene registro de que se haya apartado.").
			    addFlashAttribute("clase", "warning");
			return "redirect:/cambios/cambioApartado";
		}
		model.addAttribute("prendaApartadaDevuelta", this.prendaApartadaDevuelta);
		return "cambios/cambioApartado";
	}
	
	@GetMapping("/buscar/cambio")
	public String buscarCambio(@RequestParam("codigoCambio") String codigo, Model model, @ModelAttribute("prendaDevuelta") PrendaVendida prendaDevuelta, RedirectAttributes att) {
		this.prendaCambio = prendaService.buscarPorCodigo(codigo);
		if(prendaCambio == null) {
			att.addFlashAttribute("mensaje", "La prenda con código " + codigo + " no se encuentra en el inventario de la tienda.").
			    addFlashAttribute("clase", "warning");
			return "redirect:/cambios/cambio";
		}
		if(!prendaCambio.getEstatus().equals("Disponible")) {
			att.addFlashAttribute("mensaje", "La prenda con código " + codigo + " se encuentra apartada o ya se vendió y no está disponible para cambiar.").
			    addFlashAttribute("clase", "warning");
			prendaCambio = null;
			return "redirect:/cambios/cambio";
		}
		model.addAttribute("prendaCambio", prendaService.buscarPorCodigo(codigo));
		return "cambios/cambio";
	}
	
	@SuppressWarnings("static-access")
	@GetMapping("/buscar/apartado/cambio")
	public String buscarApartadoCambio(@RequestParam("codigoCambio") String codigo, Model model, RedirectAttributes att) {
		this.prendaApartadaCambio = prendaService.buscarPorCodigo(codigo);
		if(this.prendaApartadaCambio == null) {
			att.addFlashAttribute("mensaje", "La prenda con código " + codigo + " no se encuentra en el inventario de la tienda.").
			    addFlashAttribute("clase", "warning");
			return "redirect:/cambios/apartado/cambio";
		}
		if(!this.prendaApartadaCambio.getEstatus().equals("Disponible")) {
			att.addFlashAttribute("mensaje", "La prenda con código " + codigo + " se encuentra apartada o ya se vendió y no está disponible para cambiar.").
			    addFlashAttribute("clase", "warning");
			this.prendaApartadaCambio = null;
			return "redirect:/cambios/apartado/cambio";
		}
		if(this.prendaApartadaCambio.getPrecioVenta() > this.prendaApartadaDevuelta.getPrecio()) {
			model.addAttribute("mensaje", "El precio de la prenda con código " + codigo + " es mayor a la prenda que devuelve. En caso de haber liquidado el apartado anterior, este se abrirá con la nueva deuda.");
			model.addAttribute("clase", "warning");
		}
		model.addAttribute("prendaApartadaCambio", this.prendaApartadaCambio);
		model.addAttribute("prendaApartadaDevuelta", this.prendaApartadaDevuelta);
		return "cambios/cambioApartado";
	}
	
	@GetMapping("/guardar")
	public String guardar(@RequestParam("codigoDevuelto") String codigoDevuelto, @RequestParam("codigoCambio") String codigoCambio, RedirectAttributes att) {
		//Buscamos la prenda que se vendió y se va a regresar
		PrendaVendida prendaQueDevuelve = prendaVendidaService.buscarPorCodigo(codigoDevuelto);
		//buscamos la prenda que se va a llevar a cambio
		Prenda prendaQueCambia = prendaService.buscarPorCodigo(codigoCambio);
		
		//actualizamos precio de la prenda que devuelve
		prendaQueDevuelve.setPrecio(prendaQueCambia.getPrecioVenta());
		
		prendaQueDevuelve.setPrenda(prendaQueCambia);
		/*Venta actualizaFecha = prendaQueDevuelve.getVenta();
		actualizaFecha.setFecha(new Date());
		ventaService.guardar(actualizaFecha);*/
		
		prendaVendidaService.guardar(prendaQueDevuelve);
		
		prendaQueCambia.setEstatus("Vendida");
		prendaService.guardar(prendaQueCambia);
		
		Prenda prendaDevuelta = prendaService.buscarPorCodigo(codigoDevuelto);
		prendaDevuelta.setEstatus("Disponible");
		prendaService.guardar(prendaDevuelta);
		
		att.addFlashAttribute("mensaje", "El cambio se realizó correctamente.").
	    addFlashAttribute("clase", "success");
		
		return "redirect:/cambios/";
	}
	
	@SuppressWarnings("static-access")
	@GetMapping("/apartado/guardar")
	public String apartadoGuardar(RedirectAttributes att) {
		Apartados apartado = null;
		PrendaApartada prendaQueDevuelve = prendaApartadaService
				.buscarPorIdApartadoAndIdPrenda(this.prendaApartadaDevuelta.getApartado().getId(), this.prendaApartadaDevuelta.getPrenda().getId());
		Prenda prendaQueCambia = prendaService.buscarPorCodigo(this.prendaApartadaCambio.getCodigo());
		
		if(prendaQueDevuelve.getPrenda().getEstatus().equals("Vendida") && (prendaQueCambia.getPrecioVenta() > prendaQueDevuelve.getPrecio())) {
			prendaQueCambia.setEstatus("Apartada");
			apartado = prendaQueDevuelve.getApartado();
			apartado.setEstatus("Apartada");
		}else
			prendaQueCambia.setEstatus(prendaQueDevuelve.getPrenda().getEstatus());
		
		prendaQueDevuelve.setPrecio(prendaQueCambia.getPrecioVenta());
		prendaQueDevuelve.setPrenda(prendaQueCambia);
		prendaApartadaService.guardar(prendaQueDevuelve);
		
		Prenda prendaDevuelta = prendaService.buscarPorCodigo(this.prendaApartadaDevuelta.getPrenda().getCodigo());
		prendaDevuelta.setEstatus("Disponible");
		prendaService.guardar(prendaDevuelta);
		
		if(apartado != null)
			apartadoService.actualizar(apartado);
		
		att.addFlashAttribute("mensaje", "El cambio se realizó correctamente.").
	    addFlashAttribute("clase", "success");
		
		return "redirect:/apartados/consulta";
	}
	
	@ModelAttribute
	public void setGenerico(Model model) {
		model.addAttribute("prendaDevuelta", prendaDevuelta);
		model.addAttribute("prendaCambio", prendaCambio);
	}
}
