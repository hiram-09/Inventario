package com.inventario.gina.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Abonos;
import com.inventario.gina.model.Apartados;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.model.PrendaApartada;
import com.inventario.gina.model.PrendaParaVender;
import com.inventario.gina.model.PrendaVendida;
import com.inventario.gina.model.Usuario;
import com.inventario.gina.model.Venta;
import com.inventario.gina.service.IAbonosService;
import com.inventario.gina.service.IApartadosService;
import com.inventario.gina.service.IPrendaApartadaService;
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
	
	@Autowired
	IPrendaApartadaService prendaApartadaService;
	 
	@GetMapping("/consulta")
	public String consultaApartados(Model model) {
		List<Apartados> apartados = apartadoService.buscarTodosDesc();
		model.addAttribute("apartados", apartados);
		model.addAttribute("totalPrendas", apartados.size());
		return "apartados/consultaApartados";
	}
	
	@GetMapping("/consultaPrendas/{id}")
	public String consultaPrendas(Model model, @PathVariable("id") Integer idApartado) {
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
		return "apartados/consultaApartados :: resultsList";
	}
	
	@GetMapping("/crear")
	public String indexVender(Model model, HttpServletRequest request) {
		List<PrendaParaVender> carrito = getCarrito(request);
		Double total = 0.0;
		for(PrendaParaVender prenda : carrito) {
			total += prenda.getPrecioVenta();
		}
		
		model.addAttribute("numPrendas", carrito.size());
		model.addAttribute("total", total);
		model.addAttribute("prenda", new Prenda());
		
		return "apartados/formApartados";
	}
	/*
	 * Metodo para agregar varias prendas a una cesta para apartarlas
	 */
	@PostMapping("/agregar")
	public String agregarPrendas(@ModelAttribute Prenda prenda, RedirectAttributes att, HttpServletRequest request) {
		Prenda prendaBuscadaPorCodigo = prendaService.buscarPorCodigo(prenda.getCodigo());
		ArrayList<PrendaParaVender> carrito = this.getCarrito(request);
		
		if(prendaBuscadaPorCodigo == null) {
			att.addFlashAttribute("mensaje", "No se encontró ninguna prenda con el código: "+prenda.getCodigo()).addFlashAttribute("clase", "warning");
			return "redirect:/apartados/crear";
		}
		if(prendaBuscadaPorCodigo.getEstatus().equals("Vendida")) {
			att.addFlashAttribute("mensaje", "La prenda con código "+prenda.getCodigo() + " ya se vendió").addFlashAttribute("clase", "warning");
			return "redirect:/apartados/crear";
		}
		if(prendaBuscadaPorCodigo.getEstatus().equals("Apartada")) {
			att.addFlashAttribute("mensaje", "La prenda con código "+prenda.getCodigo() + " se encuentra apartada").addFlashAttribute("clase", "warning");
			return "redirect:/apartados/crear";
		}
		for(PrendaParaVender prendaCarrito : carrito) {
			if(prendaCarrito.getCodigo().equals(prenda.getCodigo())) {
				att.addFlashAttribute("mensaje", "La prenda con código: "+prenda.getCodigo() + " ya se encuentra en el carrito").addFlashAttribute("clase", "warning");
				return "redirect:/apartados/crear";
			}
		}
		carrito.add(new PrendaParaVender(prendaBuscadaPorCodigo.getId(), prendaBuscadaPorCodigo.getCodigo(), prendaBuscadaPorCodigo.getMarca(), prendaBuscadaPorCodigo.getTalla(), 
				prendaBuscadaPorCodigo.getModelo(), prendaBuscadaPorCodigo.getPrecioVenta(), prendaBuscadaPorCodigo.getCategoria(), prendaBuscadaPorCodigo.getEstatus()));

		this.addCarrito(carrito, request);
		return "redirect:/apartados/crear";
	}
	
	@PostMapping("/liquidar")
	public String liquidar(@RequestParam("idApartadoL") Integer idApartado, @RequestParam("restanteL") Double importe, @RequestParam("fechaL") Date fecha, RedirectAttributes att) {
		try {
			Apartados apartado = apartadoService.buscarPorId(idApartado);			
			List<PrendaApartada> prendasApartadas = prendaApartadaService.buscarPorApartado(apartado);
			//Se asigna el abono
			abonoService.crearAbono(new Abonos(importe, apartado, fecha));			
			//se pone en estatus liquidado el apartado y se pone la fecha en que se liquidó
			apartado.setEstatus("Liquidada");
			apartado.setFechaLiquidado(fecha);
			apartadoService.actualizar(apartado);
			//se pone estatus vendida en cada prenda apartada
			for(PrendaApartada pa : prendasApartadas) {
				pa.getPrenda().setEstatus("Vendida");
				prendaService.guardar(pa.getPrenda());
			}
			att.addFlashAttribute("mensaje","Se liquidó el apartado exitosamente!!!").addFlashAttribute("clase", "success");
			
		}catch(Exception e) {
			att.addFlashAttribute("mensaje","Ocurrió un error al realizar la liquidación").addFlashAttribute("clase", "warning");
		}
		return "redirect:/apartados/consulta";
	}
	
	@PostMapping("/abonar/{idApartado}")
	public String abonarApartado(@PathVariable Integer idApartado, @RequestParam("txtAbono") Double importe, @RequestParam("fecha") Date fecha, Model model, RedirectAttributes att) {
		try{
			Apartados apartado = apartadoService.buscarPorId(idApartado);
			abonoService.crearAbono(new Abonos(importe, apartado, fecha));
			att.addFlashAttribute("mensaje","Abono realizado exitosamente!!!").addFlashAttribute("clase", "success");
		}catch(Exception e) {
			att.addFlashAttribute("mensaje","Ocurrió un error al realizar el abono").addFlashAttribute("clase", "warning");
		}
		
		return "redirect:/apartados/consulta";
	}
	
	
	/*
	 * Metodo para cancelar la cesta que se guardó para apartados
	 */
	@GetMapping("/cancelar")
	public String cancelarLimpiar(HttpServletRequest request) {
		this.cleanCarrito(request);
		
		return "redirect:/apartados/crear";
	}
	
	/*
	 * Metodo para quitar prendas de la cesta de apartados
	 */
	
	@PostMapping("/quitar/{index}")
	public String quitarDelCarrito(@PathVariable int index, HttpServletRequest request) {
		ArrayList<PrendaParaVender> carrito = this.getCarrito(request);
		if (carrito != null && carrito.size() > 0 && carrito.get(index) != null) {
			carrito.remove(index);
			this.addCarrito(carrito, request);
		}
		
		return "redirect:/apartados/crear";
	}
	
	@PostMapping("/terminar")
	public String terminarVenta(Apartados apartado, @RequestParam("abono") Double abonoInicial, HttpServletRequest request, RedirectAttributes att, Authentication auth) {
		ArrayList<PrendaParaVender> carrito = this.getCarrito(request);
		boolean ventaRealizada = false;
		Apartados apartadoCreado = apartadoService.crearApartado(new Apartados(apartado.getNombreCliente(), apartado.getTelefonoCliente(), apartado.getFechaApartado()));
		abonoService.crearAbono(new Abonos(abonoInicial, apartadoCreado, apartado.getFechaApartado()));
		for(PrendaParaVender prendaCarrito : carrito) {
			Prenda prenda = prendaService.buscarPorCodigo(prendaCarrito.getCodigo());
			
			if(prenda == null || prenda.getEstatus().equals("Vendida") || prenda.getEstatus().equals("Apartada")) continue;
			
			prenda.setEstatus("Apartada");
			prendaService.guardar(prenda);
			prendaApartadaService.guardar(new PrendaApartada(prenda.getPrecioVenta(), prenda, apartadoCreado));
			ventaRealizada = true;
		}
		this.cleanCarrito(request);
		if(ventaRealizada)
			att.addFlashAttribute("mensaje","Apartado realizado exitosamente!!!").addFlashAttribute("clase", "success");
		else 
			att.addFlashAttribute("mensaje","No se vendieron productos").addFlashAttribute("clase", "warning");
		return "redirect:/apartados/consulta";
	}
	
	
	private ArrayList<PrendaParaVender> getCarrito(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		ArrayList<PrendaParaVender> carrito = (ArrayList<PrendaParaVender>) request.getSession().getAttribute("carritoApartados");
		if(carrito == null) {
			carrito = new ArrayList<>();
		}
		
		return carrito;
	}
	
	private void addCarrito(ArrayList<PrendaParaVender> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carritoApartados", carrito);
    }
	
	private void cleanCarrito(HttpServletRequest request) {
        this.addCarrito(new ArrayList<>(), request);
    }
	
	@GetMapping("/eliminar/{id}")
	public String eliminarApartado(@PathVariable("id") Integer id, Model model, RedirectAttributes att) {
		Apartados apartado = apartadoService.buscarPorId(id);
		apartado.setEstatus("Cancelada");
		apartado.setFechaCancelado(new Date());
		apartadoService.actualizar(apartado);
		List<PrendaApartada> prendasApartadas = prendaApartadaService.buscarPorApartado(new Apartados(id));
		Prenda prendaActualizar = null;
		for(PrendaApartada prendaApartada : prendasApartadas) {
			prendaActualizar = prendaService.buscarPorId(prendaApartada.getPrenda().getId());
			prendaActualizar.setEstatus("Disponible");
			prendaService.guardar(prendaActualizar);
		}
		
		att.addFlashAttribute("mensaje","Se canceló el apartado correctamente.").addFlashAttribute("clase", "success");
		return "redirect:/apartados/consulta";		
	}
	
	
	@GetMapping("/buscar")
	public String buscar(@RequestParam String nombre, RedirectAttributes att, Model model) {
		List<Apartados> apartados = null;
		if(!nombre.equals("")) {
			apartados = apartadoService.buscarPorNombre(nombre);
		}else {
			att.addFlashAttribute("mensaje", "No existen coincidencias con el nombre ingresado").addFlashAttribute("clase", "warning");
			return "apartados/consultaApartados";
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
	public String guardar(Apartados apartado, BindingResult result, @RequestParam String codigo, @RequestParam Double abono, @RequestParam Date fecha, RedirectAttributes att) {
		/*if(result.hasErrors()) {
			System.out.println("Existieron errores");
			return "apartados/formApartados"; 
		}
		Prenda prendaApartada = prendaService.buscarPorCodigo(codigo);
		if(prendaApartada== null || !prendaApartada.getEstatus().equals("Disponible")) {
			att.addFlashAttribute("mensaje", "la prenda con inventario "+codigo+" no está disponible");
			return "redirect:/apartados/consulta";
		}
		
		Apartados ap = apartadoService.crearApartado(new Apartados(apartado.getNombre(), apartado.getTelefono(), prendaApartada.getPrecioVenta(), prendaApartada, fecha));
		
		//se modifica el estatus de la prenda para que no quede disponible
		prendaApartada.setEstatus("Apartada");
		prendaService.guardar(prendaApartada);
		
		abonoService.crearAbono(new Abonos(abono, ap, fecha));
			
		att.addFlashAttribute("mensaje", "Apartado creado correctamente").addFlashAttribute("clase", "success");*/
		return "redirect:/apartados/consulta";
	}
	
	
	@GetMapping("/detalle/{id}")
	public String detalleApartado(@PathVariable Integer id, Model model) {
		Apartados apartado = apartadoService.buscarPorId(id);
		model.addAttribute("apartado", apartado);
		return "apartados/detalleApartado";
	}
	
	@PostMapping("/abonar")
	public String abonar(@RequestParam Integer id, @RequestParam Double abono, @RequestParam Date fecha, Model model) {
		/*Apartados apartado = apartadoService.buscarPorId(id);
		abonoService.crearAbono(new Abonos(abono, apartado, fecha));
		if(apartado.getRestante() == 0) {
			//se actualiza el estatus a liquidada
			apartado.setEstatus("Liquidada");
			apartado.setFechaLiquidado(fecha);
			apartadoService.actualizar(apartado);
		}
		model.addAttribute("apartado", apartado);*/
		return "redirect:/apartados/detalle/"+id;
	}
	
	@GetMapping("/cancelar/{id}")
	public String cancelar(@PathVariable Integer id, RedirectAttributes att) {
		/*Apartados apartado = apartadoService.buscarPorId(id);
		System.out.println(apartado);
		Prenda prenda = prendaService.buscarPorCodigo(apartado.getPrenda().getCodigo());
		
		apartado.setEstatus("Cancelada");
		apartado.setFechaCancelado(new Date());
		apartadoService.actualizar(apartado);
		prenda.setEstatus("Disponible");
		prendaService.guardar(prenda);
		
		att.addFlashAttribute("mensaje", "Se canceló el apartado de la prenda.");*/
		return "redirect:/apartados/consulta";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
