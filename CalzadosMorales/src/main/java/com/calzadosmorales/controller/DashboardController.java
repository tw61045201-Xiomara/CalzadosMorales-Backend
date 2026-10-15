package com.calzadosmorales.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.calzadosmorales.entity.Usuario;
import com.calzadosmorales.repository.UsuarioRepository;
import com.calzadosmorales.service.DashboardVendedorService;
import com.calzadosmorales.service.DashboardAdminService;
import java.util.Map;

@Controller
public class DashboardController {

	@Autowired
	private DashboardVendedorService vendedorService;

	@Autowired
	private DashboardAdminService adminService;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@GetMapping("/index")
	public String dashboard(Model model, Authentication auth) {

		if (auth == null || !auth.isAuthenticated()) {
			return "redirect:/login";
		}

		String username = auth.getName();
		Usuario u = usuarioRepo.findByUsuario(username);

		// Verificacion de existencia del usuario
		if (u == null) {
			return "redirect:/login?error=user_not_found";
		}

		if (u.getRol() == null) {
			model.addAttribute("error", "El usuario no tiene un rol asignado en la base de datos.");
			return "redirect:/login?error=no_role";
		}

		int rolId = u.getRol().getId_rol();

		model.addAttribute("userNombreCompleto", u.getNombre());
		model.addAttribute("userRol", u.getRol().getNombre());
		model.addAttribute("rolId", rolId);

		try {
			if (rolId == 1) {
				Map<String, Object> statsAdmin = adminService.cargarPanelAdministrativo();
				if (statsAdmin != null)
					model.addAllAttributes(statsAdmin);

			} else if (rolId == 2) {
				Map<String, Object> datosVendedor = vendedorService.obtenerDatosDashboardVendedor(u.getId_usuario());
				if (datosVendedor != null)
					model.addAllAttributes(datosVendedor);

			}
		} catch (Exception e) {
			System.err.println("Error al cargar datos de estadísticas en Dashboard: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("errorDashboard", "No se pudieron cargar algunas métricas del panel.");
		}

		return "index";
	}
}