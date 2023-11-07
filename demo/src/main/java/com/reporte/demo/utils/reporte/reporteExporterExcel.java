package com.reporte.demo.utils.reporte;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.reporte.demo.entidades.reportes;

public class reporteExporterExcel {
	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<reportes> listareportes;

	public reporteExporterExcel(List<reportes> listareportes) {
		this.listareportes = listareportes;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("reportes");
	}

	private void escribirCabeceraDeTabla() {
		Row fila = hoja.createRow(0);

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setBold(true);
		fuente.setFontHeight(16);
		estilo.setFont(fuente);

		Cell celda = fila.createCell(0);
		celda.setCellValue("ID");
		celda.setCellStyle(estilo);

		celda = fila.createCell(1);
		celda.setCellValue("Cantidad");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("Descripcion");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("Fecha");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("Tipo");
		celda.setCellStyle(estilo);

	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;
		double totalCantidad = 0.0; // Inicializa el total en 0.0

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (reportes reporte : listareportes) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(reporte.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(reporte.getCantidad());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(reporte.getDescripcion());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(reporte.getFecha().toString());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(reporte.getTipo());
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);

			totalCantidad += reporte.getCantidad(); // Suma la cantidad actual al total
		}

		// Crear una fila adicional para mostrar el total fuera de la tabla
		Row totalRow = hoja.createRow(numeroFilas);
		Cell totalCell = totalRow.createCell(1);
		totalCell.setCellValue("Total Cantidad: " + totalCantidad);
		totalCell.setCellStyle(estilo);
	}

	public void exportar(HttpServletResponse response) throws IOException {
		escribirCabeceraDeTabla();
		escribirDatosDeLaTabla();

		ServletOutputStream outputStream = response.getOutputStream();
		libro.write(outputStream);

		libro.close();
		outputStream.close();
	}
}
