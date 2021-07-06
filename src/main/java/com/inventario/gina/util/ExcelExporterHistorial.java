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

import com.inventario.gina.model.ApartadosAbonos;
import com.inventario.gina.model.PrendaVendida;
 
public class ExcelExporterHistorial{
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<PrendaVendida> prendasVendidas;
    private List<ApartadosAbonos> apartadosAbonos;
    private int rowCount = 1;
     
    public ExcelExporterHistorial(List<PrendaVendida> prendasVendidas, List<ApartadosAbonos> apartadosAbonos) {
        this.prendasVendidas = prendasVendidas;
        this.apartadosAbonos = apartadosAbonos;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Vendidos");
         
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
        createCell(row, 7, "CARACTERISTICAS", style);
         
    }
    
    private void writeHeaderLineAbonos() {
    	sheet = workbook.createSheet("Apartados"); 
        Row row = sheet.createRow(0);
        
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "FECHA APARTADO", style);      
        createCell(row, 1, "NOMBRE DEL CLIENTE", style);       
        createCell(row, 2, "FECHA ABONO", style); 
        createCell(row, 3, "IMPORTE", style); 
         
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
        rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        int columnCount;
                 
        for (PrendaVendida pv : prendasVendidas) {
            Row row = sheet.createRow(rowCount++);
            columnCount = 0;
             
            createCell(row, columnCount++, pv.getPrenda().getCodigo(), style);
            createCell(row, columnCount++, pv.getPrenda().getMarca(), style);
            createCell(row, columnCount++, pv.getPrenda().getModelo(), style);
            createCell(row, columnCount++, pv.getPrenda().getCategoria().getNombre(), style);
            createCell(row, columnCount++, pv.getUsuario().getNombre(), style);
            createCell(row, columnCount++, pv.getPrecio(), style);
            createCell(row, columnCount++, pv.getVenta().getFecha(), style);
            createCell(row, columnCount++, pv.getPrenda().getCaracteristicas(), style);
             
        }
    }
    private void writeDataLinesAbonos() {
    	rowCount = 1;;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        
        int columnCount; 
        for (ApartadosAbonos aa : apartadosAbonos) {
        	System.out.println(aa);
            Row row = sheet.createRow(rowCount++);
            columnCount = 0;
             
            createCell(row, columnCount++, aa.getFechaApartado(), style);
            createCell(row, columnCount++, aa.getNombreCliente(), style);
            createCell(row, columnCount++, aa.getFechaAbono(), style);
            createCell(row, columnCount++, aa.getImporte(), style);             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        writeHeaderLineAbonos();
        writeDataLinesAbonos();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}
