package com.inventario.gina.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Prenda;
import com.inventario.gina.service.ICategoriaService;
import com.inventario.gina.service.IPrendaService;
import com.inventario.gina.util.ExcelExporterInventario;

@Controller
@RequestMapping("/inventario")
public class PrendasController {
	
	@Autowired
	ICategoriaService categoriaService;
	
	@Autowired
	IPrendaService prendaService;
	
	@Autowired
	static Page<Prenda> prendas;
	
	@GetMapping("/listado")
	public String listarInventario(Model model) {
		List<Prenda> prendas = prendaService.listarTodas();
		model.addAttribute("prendas", prendas);
		model.addAttribute("totalPrendas", prendas.size());
		model.addAttribute("prenda", new Prenda());
		return "prendas/inventarioPrendas";
	}
	
	@SuppressWarnings("static-access")
	@GetMapping("/listadoPaginado")
	public String listarInventarioPaginado(Model model, Pageable page) {
		Page<Prenda> prendas = prendaService.buscarTodas(page);
		this.prendas = prendas;
		model.addAttribute("prendas", prendas);
		model.addAttribute("totalPrendas", prendas.getTotalElements());
		model.addAttribute("prenda", new Prenda());
		return "prendas/inventarioPrendas";
	}
	
	@GetMapping("/registro")
	public String registroPrenda(Prenda prenda) {
		return "prendas/registroPrendas";
	}
	
	@SuppressWarnings("static-access")
	@GetMapping("/buscar")
	public String buscar(Prenda prenda, Model model, Pageable page) {
		Page<Prenda> prendas =  null;
		
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("marca", ExampleMatcher.GenericPropertyMatchers.contains());
		Example<Prenda> example = Example.of(prenda, matcher);
		
		prendas = prendaService.buscarByExample(example, page);
		this.prendas = prendas;
		
		model.addAttribute("prenda", prenda);
		model.addAttribute("prendas", prendas);
		model.addAttribute("totalPrendas", prendas.getTotalElements());
		return "prendas/inventarioPrendas";
	}
	
	@PostMapping("/guardar")
	public String guardarPrenda(@ModelAttribute Prenda prenda, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return "prendas/registroPrendas";
		}
		
		Prenda prendaEncontrada = prendaService.buscarPorCodigo(prenda.getCodigo());
		if(prendaEncontrada != null) {
			attributes.addFlashAttribute("mensaje", "Ya existe una prenda con este código de barras").addFlashAttribute("clase", "warning");
			return "redirect:/inventario/registro";
		}
		prenda.setFechaCreacion(new Date());
		prendaService.guardar(prenda);
		attributes.addFlashAttribute("clase", "success").addFlashAttribute("mensaje", "Prenda guardada correctamente");
		return "redirect:/inventario/listadoPaginado";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id, RedirectAttributes attributes) {
		Prenda prenda = prendaService.buscarPorId(id);
		if(prenda == null) {
			attributes.addFlashAttribute("mensaje", "No se encontró la prenda con el id " + id).addFlashAttribute("clase", "warning");
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
		Prenda prendaBD = prendaService.buscarPorId(prenda.getId());
		try {
			if(prendaBD != null) {
				prendaBD.setCodigo(prenda.getCodigo());
				prendaBD.setMarca(prenda.getMarca());
				prendaBD.setModelo(prenda.getModelo());
				prendaBD.setTalla(prenda.getTalla());
				prendaBD.setCategoria(prenda.getCategoria());
				prendaBD.setPrecioCompra(prenda.getPrecioCompra());
				prendaBD.setPrecioVenta(prenda.getPrecioVenta());
				prendaBD.setEstatus(prenda.getEstatus());
				prendaBD.setCaracteristicas(prenda.getCaracteristicas());
				
				prendaService.guardar(prendaBD);
				att.addFlashAttribute("mensaje", "Prenda Actualizada correctamente").addFlashAttribute("clase", "success");
			}
			
			return "redirect:/inventario/listadoPaginado";
		} catch (Exception e) {
			att.addFlashAttribute("mensaje", "Ocurrió un error al actualizar la prenda... Contacte a su administrador").addFlashAttribute("clase", "danger");
			return "prendas/editar";
		}
	}
	
	@GetMapping("/export/excel/inventario")
    public void exportToExcel(HttpServletResponse response, Pageable page) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Inventario_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        //Page<Prenda> prendas = prendaService.buscarTodas(page);
         
        ExcelExporterInventario excelExporter = new ExcelExporterInventario(prendas);
         
        excelExporter.export(response);
    }
	@GetMapping("/export/excel/inventario_completo")
    public void exportToExcelAll(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Inventario_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Prenda> prendas = prendaService.listarTodas();
         
        ExcelExporterInventario excelExporter = new ExcelExporterInventario(prendas);
         
        excelExporter.exportAll(response); 

    }
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes att) {
		try {
			prendaService.eliminar(id);
			att.addFlashAttribute("mensaje", "Prenda eliminada correctamente").addFlashAttribute("clase", "success");
		} catch (Exception e) {
			System.out.println(e);
			att.addFlashAttribute("mensaje", "Error al eliminar la prenda... Contacte a su administrador").addFlashAttribute("clase", "danger");
		}
		return "redirect:/inventario/listadoPaginado";
	}
	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("categorias", categoriaService.buscarTodas());
	}
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
