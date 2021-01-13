package com.inventario.gina.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.inventario.gina.model.Prenda;

public class ExcelExporterInventario {
	 private XSSFWorkbook workbook;
	    private XSSFSheet sheet;
	    private List<Prenda> prendas;
	     
	    public ExcelExporterInventario(List<Prenda> prendas) {
	        this.prendas = prendas;
	        workbook = new XSSFWorkbook();
	    }
	 
	 
	    private void writeHeaderLine() {
	        sheet = workbook.createSheet("Prenda");
	         
	        Row row = sheet.createRow(0);
	         
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        font.setFontHeight(16);
	        style.setFont(font);
	         
	        createCell(row, 0, "INVENTARIO", style);      
	        createCell(row, 1, "MARCA", style);       
	        createCell(row, 2, "TALLA", style);      
	        createCell(row, 3, "MODELO", style);    
	        createCell(row, 4, "CATEGORIA", style);
	        createCell(row, 5, "PRECIO DE COMPRA", style);
	        createCell(row, 6, "PRECIO DE VENTA", style);
	         
	    }
	     
	    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
	        sheet.autoSizeColumn(columnCount);
	        Cell cell = row.createCell(columnCount);
	        if (value instanceof Integer) {
	            cell.setCellValue((Integer) value);
	        } else if (value instanceof Boolean) {
	            cell.setCellValue((Boolean) value);
	        } else if(value instanceof Double) {
	        	cell.setCellValue((Double) value);
	        } else if(value instanceof Date) {
	        	cell.setCellValue((String) Utileria.convertirFecha((Date) value));
	        } else {
	            cell.setCellValue((String) value);
	        }
	        cell.setCellStyle(style);
	    }
	     
	    private void writeDataLines() {
	        int rowCount = 1;
	 
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setFontHeight(14);
	        style.setFont(font);
	                 
	        for (Prenda p : prendas) {
	            Row row = sheet.createRow(rowCount++);
	            int columnCount = 0;
	             
	            createCell(row, columnCount++, p.getCodigo(), style);
	            createCell(row, columnCount++, p.getMarca(), style);
	            createCell(row, columnCount++, p.getTalla(), style);
	            createCell(row, columnCount++, p.getModelo(), style);
	            createCell(row, columnCount++, p.getCategoria().getNombre(), style);
	            createCell(row, columnCount++, p.getPrecioCompra(), style);
	            createCell(row, columnCount++, p.getPrecioVenta(), style);
	             
	        }
	    }
	     
	    public void export(HttpServletResponse response) throws IOException {
	        writeHeaderLine();
	        writeDataLines();
	         
	        ServletOutputStream outputStream = response.getOutputStream();
	        workbook.write(outputStream);
	        workbook.close();
	         
	        outputStream.close();
	         
	    }
	}

