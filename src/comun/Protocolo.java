package comun;

/**
 * Formato de los mensajes entre cliente y servidor.
 * Cada mensaje es una línea de texto con los campos separados por '|'.
 * El primer campo indica el tipo de mensaje (NOMBRE, PREGUNTA, ...) y los 
 * siguientes el valor del mensaje
 */

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


    // Une los campos con |
    public static String construir(String... campos) {
        // String... = cualquier numero de String de 0 en adelante. tengo mensajes de longitud variable.
        // Los utilizo para no tener que crear array cada vez que quiera invocar el cosntruir
        return String.join("|",campos);
    }

    // Devuelve la linea de mensaje en un array
    public static String[] partir(String linea) {
        String[] partido = linea.split("\\|",-1); // pongo el -1 por si el ultimo campo esta vacío despues de |
        return partido;
    }

}
