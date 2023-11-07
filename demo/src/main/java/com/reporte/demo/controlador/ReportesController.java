package com.reporte.demo.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import  com.reporte.demo.entidades.reportes;
import  com.reporte.demo.servicio.reporteServicios;
import com.reporte.demo.utils.paginacion.PageRender;
import  com.reporte.demo.utils.reporte.reporteExporterExcel;
import  com.reporte.demo.utils.reporte.reporteExporterPDF;
import com.lowagie.text.DocumentException;

@Controller
public class ReportesController {

	@Autowired
	private reporteServicios reporteServicios;
	
	@GetMapping("/ver/{id}")
	public String verDetallesDelEmpleado(@PathVariable(value = "id") Long id,Map<String,Object> modelo,RedirectAttributes flash) {
		reportes reportes = reporteServicios.findOne(id);
		if(reportes == null) {
			flash.addFlashAttribute("error", "El reporte no existe en la base de datos");
			return "redirect:/listar";
		}
		
		modelo.put("reportes",reportes);
		modelo.put("titulo", "Detalle de reporte " + reportes.getTipo());
		return "ver";
	}
	



	@GetMapping({"/","/listar",""})
	public String listarreportes(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<reportes> reportes = reporteServicios.findAll(pageRequest);
		PageRender<reportes> pageRender = new PageRender<>("/listar", reportes);
		
		modelo.addAttribute("titulo","Listado de reportes");
		modelo.addAttribute("reportes",reportes);
		modelo.addAttribute("page", pageRender);
		
		return "listar";
	}

	@GetMapping("/form")
	public String mostrarFormularioDeRegistrarCliente(Map<String,Object> modelo) {
		reportes reportes = new reportes();
		modelo.put("reportes", reportes);
		modelo.put("titulo", "Registro");
		return "form";
	}
	
	@PostMapping("/form")
	public String guardarEmpleado(@Valid reportes reportes,BindingResult result,Model modelo,RedirectAttributes flash,SessionStatus status) {
		if(result.hasErrors()) {
			modelo.addAttribute("titulo", "Registro de reportes");
			return "form";
		}
		
		String mensaje = (reportes.getId() != null) ? "Se ha editado con exito" : "Se ha registrado con exito";
		
		reporteServicios.save(reportes);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}
	
	@GetMapping("/form/{id}")
	public String editarreporte(@PathVariable(value = "id") Long id,Map<String, Object> modelo,RedirectAttributes flash) {
		reportes reportes = null;
		if(id > 0) {
			reportes = reporteServicios.findOne(id);
			if(reportes == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/listar";
			}
		}
		else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser cero");
			return "redirect:/listar";
		}
		
		modelo.put("reportes",reportes);
		modelo.put("titulo", "Edición de reportes");
		return "form";
	}
	@GetMapping("/eliminar/{id}")
	public String eliminarCliente(@PathVariable(value = "id") Long id,RedirectAttributes flash) {
		if(id > 0) {
			reporteServicios.delete(id);
			flash.addFlashAttribute("success", "Eliminación exitosa");
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/exportarPDF")
	public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=INFORME_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<reportes> reportes = reporteServicios.findAll();
		
		reporteExporterPDF exporter = new reporteExporterPDF(reportes);
		exporter.exportar(response);
	}

	@GetMapping("/exportarExcel")
	public void exportarListadoDeEmpleadosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=INFORME_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<reportes> reportes = reporteServicios.findAll();
		
		reporteExporterExcel exporter = new reporteExporterExcel(reportes);
		exporter.exportar(response);
	}

}
