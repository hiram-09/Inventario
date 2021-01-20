package com.inventario.gina.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventario.gina.model.PrendaVendida;
import com.inventario.gina.service.IPrendaVendidaService;
import com.inventario.gina.util.ExcelExporterHistorial;
import com.inventario.gina.util.Utileria;

@Controller
@RequestMapping("/ventas")
public class VentasController {
	
	@Autowired
	IPrendaVendidaService prendaVendidaService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@GetMapping("/dia")
	public String ventasDelDia(Model model) {
		List<PrendaVendida> prendasVendidas = null;
		Double total = 0.0;
		try {
			Date fecha = dateFormat.parse(dateFormat.format(new Date()));
			prendasVendidas = prendaVendidaService.buscarPorFechaVenta(fecha);
			for(PrendaVendida pv : prendasVendidas) total+=pv.getPrecio();
		} catch (Exception e) {
			System.out.println("Error en el sistema: "+e);
		}
		model.addAttribute("total", total);
		model.addAttribute("ventas", prendasVendidas);
		return "ventas/ventasDelDia";
	}
	
	@GetMapping("/historial")
	public String historialVentas() {
		return "ventas/historialVentas";
	}
	
	@PostMapping("/buscar")
	public String busquedaPorFechas(@RequestParam Date desde, @RequestParam Date hasta, Model model) {
		Double total = 0.0;
		List<PrendaVendida> prendasVendidas = prendaVendidaService.buscarPorFechas(desde, hasta);
		for(PrendaVendida pv : prendasVendidas) total+=pv.getPrecio();
		model.addAttribute("ventas", prendasVendidas);
		model.addAttribute("totalPrendas", prendasVendidas.size());
		model.addAttribute("totalVentas", total);
		model.addAttribute("fechaDesde", Utileria.convertirFecha(desde));
		model.addAttribute("fechaHasta", Utileria.convertirFecha(hasta));
		return "ventas/historialVentas";
	}
	
	@GetMapping("/export/excel/{desde}/{hasta}")
    public void exportToExcel(HttpServletResponse response, @PathVariable Date desde, @PathVariable Date hasta) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PrendasVendidas_" + Utileria.convertirFecha(desde) + "_" + Utileria.convertirFecha(hasta) + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<PrendaVendida> prendasVendidas = prendaVendidaService.buscarPorFechas(desde, hasta);
         
        ExcelExporterHistorial excelExporter = new ExcelExporterHistorial(prendasVendidas);
         
        excelExporter.export(response);    
    }  
 
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
