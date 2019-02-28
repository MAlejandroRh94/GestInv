package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Utiles;

import java.util.Calendar;

public class Util {
    public static String calendarToNumericString(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    public static String calendarToTextString(Calendar calendar) {
        return getDay(calendar.get(Calendar.DAY_OF_WEEK)) + "/" + getMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);
    }

    public static long numericStringToLong(String fecha) {
        Calendar calendar = Calendar.getInstance();
        String[] datos = fecha.split("/");
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datos[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(datos[1])-1);
        calendar.set(Calendar.YEAR, Integer.parseInt(datos[2]));
        return calendar.getTimeInMillis();
    }

    public static String getDay(int dia) {
        switch (dia) {
            case 1:
                return "Lunes";
            case 2:
                return "Martes";
            case 3:
                return "Miercoles";
            case 4:
                return "Jueves";
            case 5:
                return "Viernes";
            case 6:
                return "Sabado";
            case 0:
                return "Domingo";
            default:
                return "";
        }
    }

    public static String getMonth(int mes) {
        switch (mes) {
            case 0:
                return "Enero";
            case 1:
                return "Febrero";
            case 2:
                return "Marzo";
            case 3:
                return "Abril";
            case 4:
                return "Mayo";
            case 5:
                return "Junio";
            case 6:
                return "Julio";
            case 7:
                return "Agosto";
            case 8:
                return "Septiembre";
            case 9:
                return "Octubre";
            case 10:
                return "Noviembre";
            case 11:
                return "Diciembre";
            default:
                return "";
        }
    }
}
