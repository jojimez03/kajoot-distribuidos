package cliente;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import comun.Protocolo;

public class ClienteKajoot {
    
    public static void main(String[] args){
        Scanner teclado = new Scanner(System.in);

        try(Socket socket = new Socket("localhost", Protocolo.PUERTO)){
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            PrintWriter salida  = new PrintWriter(socket.getOutputStream(),true);

            System.out.println("Tu nombre: ");
            String nombre = teclado.nextLine();
            salida.println(Protocolo.construir(Protocolo.NOMBRE, nombre));

            String leido = entrada.readLine();
            System.out.println("Mensaje del servidor " + leido);
            if(leido==null || !leido.equals(Protocolo.OK)){
                System.out.println("No se pudo entrar ");
                return;
            }
            System.out.println("Dentro! Esperando al resto de jugadores...");

            String linea;
            while((linea=entrada.readLine())!=null){
                String[] campos = Protocolo.partir(linea);

                switch (campos[0]) {
                    case Protocolo.PREGUNTA:
                        // PREGUNTA|n|total|enunciado|A|B|C|D
                        System.out.println();
                        System.out.println("Pregunta " + campos[1] + "/" + campos[2] + ": " + campos[3]);
                        System.out.println("  A) " + campos[4]);
                        System.out.println("  B) " + campos[5]);
                        System.out.println("  C) " + campos[6]);
                        System.out.println("  D) " + campos[7]);

                        // Pedir la letra hasta que sea A, B, C o D
                        String letra = "";
                        while (!letra.matches("[ABCD]")) {
                            System.out.print("Tu respuesta (A-D): ");
                            letra = teclado.nextLine().trim().toUpperCase();
                        }

                        salida.println(Protocolo.construir(Protocolo.RESPUESTA, campos[1], letra));
                        System.out.println("Esperando al resto de jugadores...");

                        break;
                    case Protocolo.RESULTADO:
                        // RESULTADO|ACIERTO  o  RESULTADO|FALLO
                        if (campos[1].equals("ACIERTO")) {
                            System.out.println("¡Correcto!");
                        } else {
                            System.out.println("Incorrecto");
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
                        return;

                    default:
                        break;
                }


            }



        }catch(IOException e){
            System.out.println("No se pudo conectar al servidor");
            e.printStackTrace();
        }


    }


}
