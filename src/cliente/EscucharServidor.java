package cliente;

import java.io.*;

import comun.Protocolo;

public class EscucharServidor implements Runnable {


    private DataInputStream entrada;
    private int preguntaActual = 0;

    public EscucharServidor(DataInputStream entrada){
        this.entrada=entrada;
    }

    public int getPreguntaActual() { return preguntaActual; }
    private void setPreguntaActual(int n) { preguntaActual = n; }


    public void run(){
        try {
            String linea;
            while((linea=entrada.readLine())!=null){
                String[] campos = Protocolo.partir(linea);

                switch (campos[0]) {
                    case Protocolo.PREGUNTA:
                        // PREGUNTA|n|total|enunciado|A|B|C|D
                        setPreguntaActual(Integer.parseInt(campos[1]));
                        System.out.println();
                        System.out.println("Pregunta " + campos[1] + "/" + campos[2] + ": " + campos[3]);
                        System.out.println("  A) " + campos[4]);
                        System.out.println("  B) " + campos[5]);
                        System.out.println("  C) " + campos[6]);
                        System.out.println("  D) " + campos[7]);

                        break;
                    case Protocolo.RESULTADO:
                        // RESULTADO|ACIERTO  o  RESULTADO|FALLO
                        if (campos[1].equals("ACIERTO")) {
                            System.out.println("¡Correcto!");
                        } else if (campos[1].equals("FALLO")) {
                            System.out.println("Incorrecto");
                        } else {
                            System.out.println("Se acabó el tiempo");
                        }
                        break;

                    case Protocolo.RANKING:
                        // RANKING|Jon:3|Ana:2
                        System.out.println("--- Ranking ---");
                        for (int i = 1; i < campos.length; i++) {
                            System.out.println("  " + i + ". " + campos[i]);
                        }
                        break;

                    case Protocolo.ERROR:
                        // ERROR|motivo
                        System.out.println("Error: " + campos[1]);
                        break;

                    case Protocolo.FIN:
                        // FIN|Jon:3|Ana:2
                        System.out.println();
                        System.out.println("=== FIN DE LA PARTIDA ===");
                        for (int i = 1; i < campos.length; i++) {
                            System.out.println("  " + i + ". " + campos[i]);
                        }
                        System.exit(0);

                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Conexión perdida con el servidor");
        }
        System.exit(0);
    
    }
    
}
