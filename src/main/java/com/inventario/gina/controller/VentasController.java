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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Abonos;
import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.ApartadosAbonos;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.model.PrendaApartada;
import com.inventario.gina.model.PrendaVendida;
import com.inventario.gina.model.Venta;
import com.inventario.gina.service.IAbonosService;
import com.inventario.gina.service.IApartadosService;
import com.inventario.gina.service.IPrendaApartadaService;
import com.inventario.gina.service.IPrendaService;
import com.inventario.gina.service.IPrendaVendidaService;
import com.inventario.gina.service.IVentaService;
import com.inventario.gina.util.ExcelExporterHistorial;
import com.inventario.gina.util.Utileria;

@Controller
@RequestMapping("/ventas")
public class VentasController {
	
	@Autowired
	IPrendaVendidaService prendaVendidaService;
	
	@Autowired
	IVentaService ventaService;
	
	@Autowired
	IApartadosService apartadoService;
	
	@Autowired
	IPrendaApartadaService prendaApartadaService;
	
	@Autowired
	IAbonosService abonoService;
	
	@Autowired
	IPrendaService prendaService;
	
	private static Date fechaDesde;
	private static Date fechaHasta;
		
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@GetMapping("/dia")
	public String ventasDelDia(Model model) {
		List<PrendaVendida> prendasVendidas = null;
		List<Apartados> prendasApartadas = null;
		List<ApartadosAbonos> apartadosAbonos = null;
		Double total = 0.0;
		try {
			Date fecha = dateFormat.parse(dateFormat.format(new Date()));
			prendasVendidas = prendaVendidaService.buscarPorFechaVenta(fecha);
			for(PrendaVendida pv : prendasVendidas) total+=pv.getPrecio();
			
			apartadosAbonos = apartadoService.buscarPorFechaAbono(fecha);
			for(ApartadosAbonos apartadoAbono : apartadosAbonos) total += apartadoAbono.getImporte();
			
		} catch (Exception e) {
			System.out.println("Error en el sistema: "+e);
		}
		model.addAttribute("total", total);
		model.addAttribute("ventas", prendasVendidas);
		model.addAttribute("apartados", prendasApartadas);
		model.addAttribute("abonos", apartadosAbonos);
		return "ventas/ventasDelDia";
	}
	
	@GetMapping("/historial")
	public String historialVentas() {
		return "ventas/historialVentas";
	}
	
	@RequestMapping(value="/buscar", method = {RequestMethod.GET, RequestMethod.POST})
	public String busquedaPorFechas(@RequestParam Date desde, @RequestParam Date hasta, @RequestParam(required = false) String mensaje, Model model, RedirectAttributes att) {
		fechaDesde = desde;
		fechaHasta = hasta;
		try {
			Double total = 0.0;
			
			List<PrendaVendida> prendasVendidas = prendaVendidaService.buscarPorFechas(fechaDesde, fechaHasta);
			for(PrendaVendida pv : prendasVendidas) total+=pv.getPrecio();
			
			List<ApartadosAbonos> apartados = apartadoService.buscarPorFechaAbono(fechaDesde, fechaHasta);
			for(ApartadosAbonos apartado : apartados) total += apartado.getImporte();
			
			model.addAttribute("ventas", prendasVendidas);
			model.addAttribute("apartados", apartados);
			model.addAttribute("totalPrendas", prendasVendidas.size());
			model.addAttribute("totalApartados", apartados.size());
			model.addAttribute("totalVentas", total);
			model.addAttribute("fechaDesde", Utileria.convertirFecha(fechaDesde));
			model.addAttribute("fechaHasta", Utileria.convertirFecha(fechaHasta));
			model.addAttribute("mensaje",mensaje);
		} catch (Exception e) {
			System.out.println(e);
		}
		return "ventas/historialVentas";
	}
	
	@PostMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response, @RequestParam("hdDesde") Date desde, @RequestParam("hdHasta") Date hasta) throws IOException {
		response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PrendasVendidas_" + Utileria.convertirFecha(desde) + "_" + Utileria.convertirFecha(hasta) + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<PrendaVendida> prendasVendidas = prendaVendidaService.buscarPorFechas(desde, hasta);
        List<ApartadosAbonos> apartadosAbonos = apartadoService.buscarPorFechaAbono(desde, hasta);
        Apartados apartadoAux = new Apartados();
        for(ApartadosAbonos apartado : apartadosAbonos) {
        	apartadoAux.setId(apartado.getIdApartado());
        	apartado.setPrendasApartadas(prendaApartadaService.buscarPorApartado(apartadoAux));
        }
        
        ExcelExporterHistorial excelExporter = new ExcelExporterHistorial(prendasVendidas, apartadosAbonos);
         
        excelExporter.export(response);    
    } 
	
	@PostMapping("/actualiza_fecha")
	public ModelAndView actualizaFecha(@RequestParam("idPrendaVendida") Integer id, @RequestParam("fVenta") Date fecha, @RequestParam("desde") Date desde, @RequestParam("hasta") Date hasta, RedirectAttributes att) {	
		ModelAndView model = new ModelAndView("redirect:/ventas/buscar");
		try{
			PrendaVendida pv = prendaVendidaService.buscarPorId(id);
			Venta venta = pv.getVenta();
			venta.setFecha(fecha);
			ventaService.actualizaFechaVenta(venta);		
			model.addObject("desde", Utileria.convertirFecha(desde));
			model.addObject("hasta", Utileria.convertirFecha(hasta));
			model.addObject("mensaje", "La fecha se actualizó correctamente");
		}catch(Exception e) {
			model.addObject("mensaje", "Ocurrió un error al actualizar la fecha");
		}
		
		return model;
	}
	
	@GetMapping("/consultaPrendas/{id}")
	public String consultaPrendas(Model model, @PathVariable("id") Integer idApartado, @RequestParam("fVenta") Date fecha, @RequestParam("desde") Date desde) {
		Double importeTotal = 0.0;
		Double totalAbonos = 0.0;
		Apartados apartado = apartadoService.buscarPorId(idApartado);
		List<PrendaApartada> prendasApartadas = prendaApartadaService.buscarPorApartado(apartado);
		List<Abonos> abonos = abonoService.buscarPorApartado(apartado);
		
		for(PrendaApartada pa : prendasApartadas) importeTotal += pa.getPrecio();
		for(Abonos abono : abonos) totalAbonos += abono.getImporte();
		
		model.addAttribute("prendasApartadas", prendasApartadas);	
		model.addAttribute("abonos", abonos);
		model.addAttribute("idApartado", idApartado);
		model.addAttribute("importeTotal", importeTotal);
		model.addAttribute("restante", importeTotal - totalAbonos);
		model.addAttribute("apartado", apartado);
		return "ventas/historialVentas :: resultsList";
	}
	
	@GetMapping("/cancelar/{id}")
	public ModelAndView cancelarVenta(@PathVariable("id") Integer idVenta) {
		ModelAndView model = new ModelAndView("redirect:/ventas/buscar");
		try{
			PrendaVendida pv = prendaVendidaService.buscarPorId(idVenta);
			Venta venta = pv.getVenta();
			prendaVendidaService.eliminaPrendaVendida(pv);
			ventaService.eliminarVenta(venta);		
			Prenda prendaActualizada = pv.getPrenda();
			prendaActualizada.setEstatus("Disponible");
			prendaService.guardar(prendaActualizada);
			
			model.addObject("desde", Utileria.convertirFecha(fechaDesde));
			model.addObject("hasta", Utileria.convertirFecha(fechaHasta));
			model.addObject("mensaje", "Se canceló correctamente la venta. La prenda está disponible para su venta");
		}catch(Exception e) {
			model.addObject("mensaje", "Ocurrió un error al actualizar la fecha");
		}
		
		return model;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
