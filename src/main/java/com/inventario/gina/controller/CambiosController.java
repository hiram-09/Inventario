package com.inventario.gina.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Prenda;
import com.inventario.gina.model.PrendaVendida;
import com.inventario.gina.model.Venta;
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
	private IVentaService ventaService;
	
	private PrendaVendida prendaDevuelta;
	private Prenda prendaCambio;
	
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
	
	@ModelAttribute
	public void setGenerico(Model model) {
		model.addAttribute("prendaDevuelta", prendaDevuelta);
		model.addAttribute("prendaCambio", prendaCambio);
	}
}
