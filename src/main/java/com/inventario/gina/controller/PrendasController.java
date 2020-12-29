package com.inventario.gina.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String registroPrenda(Model model) {
		model.addAttribute("categorias", categoriaService.buscarTodas());
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
	public String guardarPrenda(@ModelAttribute Prenda prenda) {
		prendaService.guardar(prenda);
		return "redirect:/inventario/listado";
	}

}
