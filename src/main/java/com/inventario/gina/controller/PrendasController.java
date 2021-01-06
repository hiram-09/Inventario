package com.inventario.gina.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Prenda;
import com.inventario.gina.service.ICategoriaService;
import com.inventario.gina.service.IPrendaService;

@Controller
@RequestMapping("/inventario")
public class PrendasController {
	
	@Autowired
	ICategoriaService categoriaService;
	
	@Autowired
	IPrendaService prendaService;
	
	@GetMapping("/listado")
	public String listarInventario(Model model) {
		List<Prenda> prendas = prendaService.listarTodas();
		model.addAttribute("prendas", prendas);
		model.addAttribute("totalPrendas", prendas.size());
		model.addAttribute("prenda", new Prenda());
		return "prendas/inventarioPrendas";
	}
	
	@GetMapping("/registro")
	public String registroPrenda(Prenda prenda) {
		return "prendas/registroPrendas";
	}
	
	@GetMapping("/buscar")
	public String buscar(@ModelAttribute Prenda prenda, Model model) {
		List<Prenda> prendas =  null;
		if(!prenda.getCodigo().equals("") && !prenda.getMarca().equals("")) {
			prendas = prendaService.busquedaPorCodigoMarca(prenda.getCodigo(), prenda.getMarca());
		}else if(!prenda.getCodigo().equals("") && prenda.getMarca().equals("")) {
			prendas = prendaService.busquedaPorCodigo(prenda.getCodigo());
		}else if(prenda.getCodigo().equals("") && !prenda.getMarca().equals("")) {
			prendas = prendaService.busquedaPorMarca(prenda.getMarca());
		}else {
			prendas = prendaService.listarTodas();
		}
		
		model.addAttribute("prenda", prenda);
		model.addAttribute("prendas", prendas);
		model.addAttribute("totalPrendas", prendas.size());
		return "prendas/inventarioPrendas";
	}
	
	@PostMapping("/guardar")
	public String guardarPrenda(Prenda prenda, BindingResult result, RedirectAttributes attributes) {
		System.out.println(prenda);
		if(result.hasErrors()) {
			return "prendas/registroPrendas";
		}
		
		Prenda prendaEncontrada = prendaService.buscarPorCodigo(prenda.getCodigo());
		if(prendaEncontrada != null) {
			attributes.addFlashAttribute("mensaje", "Ya existe una prenda con este código de barras").addFlashAttribute("clase", "alert-warning").addFlashAttribute("prenda", prenda);
			return "redirect:/inventario/registro";
		}
		prenda.setFechaCreacion(new Date());
		prendaService.guardar(prenda);
		attributes.addFlashAttribute("clase", "alert-success").addFlashAttribute("mensaje", "Prenda guardada correctamente");
		return "redirect:/inventario/listado";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id, RedirectAttributes attributes) {
		Prenda prenda = prendaService.buscarPorId(id);
		if(prenda == null) {
			attributes.addFlashAttribute("mensaje", "No se encontró la prenda con el id");
			return "redirect:prendas/inventarioPrendas";
		}
		model.addAttribute("prenda", prenda);
		return "prendas/detallePrenda";
	}
	
	@GetMapping("/editar/{id}")
	public String editarPrenda(@PathVariable Integer id, Model model) {
		model.addAttribute("prenda", prendaService.buscarPorId(id));
		return "prendas/editarPrenda";
	}
	
	@PostMapping("/editar")
	public String actualizar(Prenda prenda, RedirectAttributes att) {
		System.out.println(prenda);
		Prenda prendaBD = prendaService.buscarPorId(prenda.getId());
		if(prendaBD != null) {
			prendaBD.setMarca(prenda.getMarca());
			prendaBD.setModelo(prenda.getModelo());
			prendaBD.setTalla(prenda.getTalla());
			prendaBD.setCategoria(prenda.getCategoria());
			prendaBD.setPrecioCompra(prenda.getPrecioCompra());
			prendaBD.setPrecioVenta(prenda.getPrecioVenta());
			prendaBD.setEstatus(prenda.getEstatus());
			prendaBD.setCaracteristicas(prenda.getCaracteristicas());
			
			prendaService.guardar(prendaBD);
			att.addFlashAttribute("mensaje", "Prenda Actualizada correctamente");
		}
		
		return "redirect:/inventario/listado";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes att) {
		prendaService.eliminar(id);
		att.addFlashAttribute("mensaje", "Prenda eliminada correctamente");
		return "redirect:/inventario/listado";
	}
	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("categorias", categoriaService.buscarTodas());
	}
}
