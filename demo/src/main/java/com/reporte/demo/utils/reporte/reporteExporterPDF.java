package com.reporte.demo.utils.reporte;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.reporte.demo.entidades.reportes;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class reporteExporterPDF {

	private List<reportes> listarreportes;

	public reporteExporterPDF(List<reportes> listarreportes) {
		super();
		this.listarreportes = listarreportes;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.BLUE);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("ID", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("CANTIDAD", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("DESCRIPCION", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("FECHA", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("TIPO", fuente));
		tabla.addCell(celda);

	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		double totalCantidad = 0.0; // Inicializa el total en 0.0

		for (reportes REPORTE : listarreportes) {
			tabla.addCell(String.valueOf(REPORTE.getId()));
			tabla.addCell(String.valueOf(REPORTE.getCantidad()));
			tabla.addCell(REPORTE.getDescripcion());
			tabla.addCell(REPORTE.getFecha().toString());
			tabla.addCell(REPORTE.getTipo());

			totalCantidad += REPORTE.getCantidad(); // Suma la cantidad actual al total
		}

		PdfPCell totalCell = new PdfPCell(new Phrase("Total Cantidad: " + totalCantidad));
		totalCell.setColspan(5); // Establece que ocupe todas las columnas
		tabla.addCell(totalCell);
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.BLUE);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("Reportes Actuales", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 2.3f, 2.3f, 3f, 2f });
		tabla.setWidthPercentage(110);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}
}
