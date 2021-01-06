package com.inventario.gina.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.gina.model.Perfiles;
import com.inventario.gina.model.Prenda;
import com.inventario.gina.model.PrendaParaVender;
import com.inventario.gina.model.PrendaVendida;
import com.inventario.gina.model.Usuario;
import com.inventario.gina.model.Venta;
import com.inventario.gina.repository.PrendaVendidaRepository;
import com.inventario.gina.repository.UsuariosRepository;
import com.inventario.gina.repository.VentaRepository;
import com.inventario.gina.service.IPrendaService;

@Controller
@RequestMapping("/vender")
public class VenderController {
	
	@Autowired
	IPrendaService prendaService;
	
	@Autowired
	UsuariosRepository usuarioRepo;
	
	@Autowired
	PrendaVendidaRepository prendaVendidaRepo;
	
	@Autowired
	VentaRepository ventaRepo;
	
	@GetMapping("/")
	public String indexVender(Model model, HttpServletRequest request) {
		List<PrendaParaVender> carrito = getCarrito(request);
		Double total = 0.0;
		for(PrendaParaVender prenda : carrito)
			total += prenda.getPrecioVenta();
		
		model.addAttribute("numPrendas", carrito.size());
		model.addAttribute("total", total);
		model.addAttribute("prenda", new Prenda());
		return "/ventas/vender";
	}
	
	@PostMapping("/agregar")
	public String agregarPrendas(@ModelAttribute Prenda prenda, RedirectAttributes att, HttpServletRequest request) {
		Prenda prendaBuscadaPorCodigo = prendaService.buscarPorCodigo(prenda.getCodigo());
		ArrayList<PrendaParaVender> carrito = this.getCarrito(request);
		
		if(prendaBuscadaPorCodigo == null) {
			att.addFlashAttribute("mensaje", "No se encontró ninguna prenda con el código: "+prenda.getCodigo());
			return "redirect:/vender/";
		}
		if(prendaBuscadaPorCodigo.getEstatus().equals("Vendida")) {
			att.addFlashAttribute("mensaje", "La prenda con código: "+prenda.getCodigo() + " ya se vendió");
			return "redirect:/vender/";
		}
		for(PrendaParaVender prendaCarrito : carrito) {
			if(prendaCarrito.getCodigo().equals(prenda.getCodigo())) {
				att.addFlashAttribute("mensaje", "La prenda con código: "+prenda.getCodigo() + " ya se encuentra en el carrito");
				return "redirect:/vender/";
			}
		}
		carrito.add(new PrendaParaVender(prendaBuscadaPorCodigo.getId(), prendaBuscadaPorCodigo.getCodigo(), prendaBuscadaPorCodigo.getMarca(), prendaBuscadaPorCodigo.getTalla(), 
				prendaBuscadaPorCodigo.getModelo(), prendaBuscadaPorCodigo.getPrecioVenta(), prendaBuscadaPorCodigo.getCategoria()));

		this.addCarrito(carrito, request);
		return "redirect:/vender/";
	}
	@GetMapping("/cancelar")
	public String cancelarLimpiar(HttpServletRequest request) {
		this.cleanCarrito(request);
		
		return "redirect:/vender/";
	}
	@PostMapping("/quitar/{index}")
	public String quitarDelCarrito(@PathVariable int index, HttpServletRequest request) {
		ArrayList<PrendaParaVender> carrito = this.getCarrito(request);
		if (carrito != null && carrito.size() > 0 && carrito.get(index) != null) {
			carrito.remove(index);
			this.addCarrito(carrito, request);
		}
		
		return "redirect:/vender/";
	}
	
	@PostMapping("/terminar")
	public String terminarVenta(HttpServletRequest request, RedirectAttributes att) {
		ArrayList<PrendaParaVender> carrito = this.getCarrito(request);
		for(PrendaParaVender prendaCarrito : carrito) {
			Prenda prenda = prendaService.buscarPorCodigo(prendaCarrito.getCodigo());
			
			if(prenda == null || prenda.getEstatus().equals("Vendida")) continue;
			Usuario usuario = usuarioRepo.findById(1).orElse(null);
			Venta venta = ventaRepo.save(new Venta());
			prenda.setEstatus("Vendida");
			prendaService.guardar(prenda);
			prendaVendidaRepo.save(new PrendaVendida(prendaCarrito.getCodigo(), prendaCarrito.getModelo(), prendaCarrito.getPrecioVenta(), venta, usuario));
		}
		this.cleanCarrito(request);
		att.addFlashAttribute("mensaje","Venta realizada exitosamente!!!");
		return "redirect:/vender/";
	}
	
	private ArrayList<PrendaParaVender> getCarrito(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		ArrayList<PrendaParaVender> carrito = (ArrayList<PrendaParaVender>) request.getSession().getAttribute("carrito");
		if(carrito == null) {
			carrito = new ArrayList<>();
		}
		
		return carrito;
	}
	private void addCarrito(ArrayList<PrendaParaVender> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }
	private void cleanCarrito(HttpServletRequest request) {
        this.addCarrito(new ArrayList<>(), request);
    }
	private void crear() {
		System.out.println("SE VA A CREAR EL USUARIO");
		Usuario user = new Usuario();
		user.setNombre("Hiram Arellano");
		user.setEmail("harellano@mail.com");
		user.setFechaRegistro(new Date());
		user.setUsername("harellano");
		user.setPassword("12345");
		user.setEstatus(1);
		
		Perfiles p1 = new Perfiles();
		p1.setId(1);
		
		Perfiles p2 = new Perfiles();
		p2.setId(2);
		
		user.agregar(p1);
		user.agregar(p2);
		
		usuarioRepo.save(user);
	}
}
