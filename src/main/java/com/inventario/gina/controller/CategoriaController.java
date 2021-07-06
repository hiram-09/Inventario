package com.inventario.gina.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inventario.gina.model.Categorias;
import com.inventario.gina.service.ICategoriaService;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
	
	@Autowired
	ICategoriaService categoriaService;
	
	@GetMapping("/listar")
	public String listarCategorias(Model model) {
		model.addAttribute("categorias", categoriaService.buscarTodas());
		return "categoria/listaCategorias";
	}
	
	@GetMapping("/crear")
	public String crear(Model model) {
		model.addAttribute("categoria", new Categorias());
		return "categoria/formCategoria";
	}
	
	@PostMapping("guardar")
	public String guardar(Categorias categoria) {
		
		categoriaService.guardar(categoria);
		return "redirect:/categoria/listar";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id) {
		categoriaService.eliminar(id);
		return "redirect:/categoria/listar";
	}
	
	@GetMapping("/actualizar/{id}")
	public String actualizar(@PathVariable int id, Model model) {
		model.addAttribute("categoria", categoriaService.buscarPorId(id));
		return "categoria/formCategoria";
	}
}
