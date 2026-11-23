package servidor;

import comun.Protocolo;

/**
 * Pregunta: un enunciado con 4 opciones y respuesta correcta
 */

public class Pregunta {
    
    private String enunciado;
    private String[] opciones;
    private String correcta;

    public Pregunta(String enunciado, String[] opciones, String correcta){
        this.enunciado=enunciado;
        this.opciones=opciones;
        this.correcta=correcta;
    }

    // Devuelve true si la respuesta es correcta y false en caso contrario
    public boolean esCorrecta(String respuesta){
        return this.correcta.equalsIgnoreCase(respuesta);
    }

    // Construye el mensaje con la pregunta
    public String toMensaje (int n, int total){
        return Protocolo.construir(Protocolo.PREGUNTA, String.valueOf(n), String.valueOf(total), this.enunciado, this.opciones[0],this.opciones[1],this.opciones[2],this.opciones[3]);
    }


}
