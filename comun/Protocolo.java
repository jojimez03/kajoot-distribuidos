package comun;

public class Protocolo {
    
    public static final int PUERTO = 6666;
    // --------------------------------------------------------------------
    // Cliente envia a Servidor
    // --------------------------------------------------------------------
    // NOMBRE|Jon
    public static final String NOMBRE = "NOMBRE";

    // RESPUESTA|n|A
    public static final String RESPUESTA = "RESPUESTA";

    // --------------------------------------------------------------------
    // Servidor envia a Cliente
    // --------------------------------------------------------------------
    // OK
    public static final String OK = "OK";

    // ERROR|Motivo
    public static final String ERROR = "ERROR";

    // PREGUNTA|n|total|enunciado|A|B|C|D
    public static final String PREGUNTA = "PREGUNTA";

    // RESULTADO|acierto/fallo
    public static final String RESULTADO = "RESULTADO";
    
    // RANKING|Anne:30|Jon:20
    public static final String RANKING = "RANKING";

    // FIN|Anne:30|Jon:20
    public static final String FIN = "FIN";



    public static String construir(String[] campos) {
        return String.join("|",campos);
    }

    public static String[] partir(String linea) {
        String[] partido = linea.split("\\|",-1); // pongo el -1 por si el ultimo campo esta vacío despues de |
        return partido;
    }

}
