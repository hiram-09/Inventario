package com.inventario.gina.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inventario.gina.model.Perfiles;
import com.inventario.gina.model.Usuario;
import com.inventario.gina.service.IUsuarioService;

@Controller
public class HomeController {
	@Autowired
	IUsuarioService usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String home() {
		
		return "redirect:/index";
	}
	
	@GetMapping("/home")
	public String muestraHome() {
		
		return "home";
	}
	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {
		String username = auth.getName();
		
		if(session.getAttribute("usuario") == null) {
			Usuario usuario = usuarioService.buscarPorUsername(username);
			usuario.setPassword(null);
			session.setAttribute("usuario", usuario);
		}
		
		return "redirect:/home";		
	}
	
	@GetMapping("/signup")
	public String signup(Usuario usuario) {
		
		return "formRegistro";
	}
	
	@PostMapping("/signup")
	public String guardar(Usuario usuario) {
		String passPlano = usuario.getPassword();
		String passEncriptado = passwordEncoder.encode(passPlano);
		System.out.println(passEncriptado);
		usuario.setPassword(passEncriptado);
		usuario.setEstatus(1);
		usuario.setFechaRegistro(new Date());
		
		Perfiles perfil = new Perfiles();
		perfil.setId(2); // Perfil VENDEDOR
		usuario.agregar(perfil);
		
		usuarioService.guardar(usuario);
		return "/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "formLogin";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}
	
	@GetMapping("/bcrypt/{texto}")
    @ResponseBody
   	public String encriptar(@PathVariable("texto") String texto) {    	
   		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
   	}
}
