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

import com.inventario.gina.model.PrendaVendida;
 
public class ExcelExporterHistorial{
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<PrendaVendida> prendasVendidas;
     
    public ExcelExporterHistorial(List<PrendaVendida> prendasVendidas) {
        this.prendasVendidas = prendasVendidas;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("PrendaVendida");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
       createCell(row, 0, "INVENTARIO", style);      
        createCell(row, 1, "MARCA", style);       
        createCell(row, 2, "MODELO", style);    
        createCell(row, 3, "CATEGORIA", style);
        createCell(row, 4, "VENDEDOR", style);
        createCell(row, 5, "PRECIO", style);
        createCell(row, 6, "FECHA DE VENTA", style);
         
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
        CellStyle styleCodigo = workbook.createCellStyle();
        XSSFFont codigo = workbook.createFont();
        codigo.setFontHeight(25);
        codigo.setFontName("Bar-Code 39");
        styleCodigo.setFont(codigo);
                 
        for (PrendaVendida pv : prendasVendidas) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, pv.getPrenda().getCodigo(), style);
            createCell(row, columnCount++, pv.getPrenda().getMarca(), style);
            createCell(row, columnCount++, pv.getPrenda().getModelo(), style);
            createCell(row, columnCount++, pv.getPrenda().getCategoria().getNombre(), style);
            createCell(row, columnCount++, pv.getUsuario().getNombre(), style);
            createCell(row, columnCount++, pv.getPrecio(), style);
            createCell(row, columnCount++, pv.getVenta().getFecha(), style);
             
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
