package com.inventario.gina.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Abonos;
import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.service.IAbonosService;
import com.inventario.gina.service.IApartadosService;
import com.inventario.gina.service.IPrendaService;

@Controller
@RequestMapping("/apartados")
public class ApartadosController {
	
	@Autowired
	IApartadosService apartadoService;
	
	@Autowired
	IPrendaService prendaService;
	
	@Autowired
	IAbonosService abonoService;
	 
	@GetMapping("/consulta")
	public String consultaApartados(Model model) {
		List<Apartados> apartados = apartadoService.buscarTodosDesc();
		model.addAttribute("apartados", apartados);
		model.addAttribute("totalPrendas", apartados.size());
		return "apartados/consultaApartados";
	}
	
	@GetMapping("/buscar")
	public String buscar(@RequestParam String nombre, @RequestParam String codigo, RedirectAttributes att, Model model) {
		List<Apartados> apartados = null;
		if(!nombre.equals("")) {
			apartados = apartadoService.buscarPorNombre(nombre);
		}else {
			Prenda prenda = prendaService.buscarPorCodigo(codigo);
			System.out.println(prenda);
			if(prenda != null)
				apartados = apartadoService.buscarPorPrenda(prenda);
			else {
				att.addFlashAttribute("mensaje", "El código " + codigo + " no existe").addFlashAttribute("clase", "warning");
				return "apartados/consultaApartados";
			}
		}
		model.addAttribute("apartados", apartados);
		model.addAttribute("totalPrendas", apartados.size());
		return "apartados/consultaApartados";
	}
	
	@PostMapping("/crear")
	public String crear(@RequestParam String codigo, Model model, RedirectAttributes att) {
		Prenda prenda = prendaService.buscarPorCodigo(codigo);
		if(!prenda.getEstatus().equals("Disponible")) {
			att.addFlashAttribute("mensaje", "La prenda con código " + codigo + " no está disponible").addFlashAttribute("clase", "warning");
			return "redirect:/apartados/consulta";
		}
		model.addAttribute("apartados", new Apartados());
		model.addAttribute("prenda", prenda);
		return "apartados/formApartados";
	}
	
	@PostMapping("/guardar")
	public String guardar(Apartados apartado, BindingResult result, @RequestParam String codigo, @RequestParam Double abono, RedirectAttributes att) {
		if(result.hasErrors()) {
			System.out.println("Existieron errores");
			return "apartados/formApartados"; 
		}
		Prenda prendaApartada = prendaService.buscarPorCodigo(codigo);
		if(prendaApartada== null || !prendaApartada.getEstatus().equals("Disponible")) {
			att.addFlashAttribute("mensaje", "la prenda con inventario "+codigo+" no está disponible");
			return "redirect:/apartados/consulta";
		}
		
		Apartados ap = apartadoService.crearApartado(new Apartados(apartado.getNombre(), apartado.getTelefono(), prendaApartada.getPrecioVenta(), prendaApartada));
		
		//se modifica el estatus de la prenda para que no quede disponible
		prendaApartada.setEstatus("Apartada");
		prendaService.guardar(prendaApartada);
		
		abonoService.crearAbono(new Abonos(abono, ap));
			
		att.addFlashAttribute("mensaje", "Apartado creado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/apartados/consulta";
	}
	
	@GetMapping("/abonar/{id}")
	public String abonarApartado(@PathVariable Integer id, Model model) {
		Apartados apartado = apartadoService.buscarPorId(id);
		model.addAttribute("apartado", apartado);
		return "apartados/abonar";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleApartado(@PathVariable Integer id, Model model) {
		Apartados apartado = apartadoService.buscarPorId(id);
		model.addAttribute("apartado", apartado);
		return "apartados/detalleApartado";
	}
	
	@PostMapping("/abonar")
	public String abonar(@RequestParam Integer id, @RequestParam Double abono, Model model) {
		Apartados apartado = apartadoService.buscarPorId(id);
		abonoService.crearAbono(new Abonos(abono, apartado));
		if(apartado.getRestante() == 0) {
			//se actualiza el estatus a liquidada
			apartado.setEstatus("Liquidada");
			apartado.setFechaLiquidado(new Date());
			apartadoService.actualizar(apartado);
		}
		model.addAttribute("apartado", apartado);
		return "apartados/abonar";
	}
	
	@GetMapping("/cancelar/{id}")
	public String cancelar(@PathVariable Integer id, RedirectAttributes att) {
		Apartados apartado = apartadoService.buscarPorId(id);
		System.out.println(apartado);
		Prenda prenda = prendaService.buscarPorCodigo(apartado.getPrenda().getCodigo());
		
		apartado.setEstatus("Cancelada");
		apartado.setFechaCancelado(new Date());
		apartadoService.actualizar(apartado);
		prenda.setEstatus("Disponible");
		prendaService.guardar(prenda);
		
		att.addFlashAttribute("mensaje", "Se canceló el apartado de la prenda.");
		return "redirect:/apartados/consulta";
	}
}
