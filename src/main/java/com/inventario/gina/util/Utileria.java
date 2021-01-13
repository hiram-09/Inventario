package com.inventario.gina.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utileria {
	public static String convertirFecha(Date fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dFecha = null;
        try {
			dFecha = dateFormat.format(fecha);
		} catch (Exception e) {
			System.out.println("Error al convertir la fecha");
		}
        return dFecha;
    }
	public static String obtenerHoraActual() {
        String formato = "HH:mm:ss";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        LocalDateTime ahora = LocalDateTime.now();
        return formateador.format(ahora);
    }
}
