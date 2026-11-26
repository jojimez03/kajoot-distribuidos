package cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import comun.Protocolo;

/**
 * Programa del jugador. 
 * - un hilo main que lee el teclado y envia respuestas
 * - un hilo EscucharServidor que lee y muetra todo lo que le llega al servidor
 * (Por si un jugador no responde no se quede esperando en teclado.nextLine como pasaba antes
 * Ahora, siguen llegando mensajes al cliente: tiempo agotado, ranking...)
 */
public class ClienteKajoot {
    
    public static void main(String[] args){
        Scanner teclado = new Scanner(System.in);

        try(Socket socket = new Socket("localhost", Protocolo.PUERTO)){
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

            // Se lee el nombre desde main para que no haya dos hilos leyendo la "entrada" a la vez
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

            
            // el hilo que lee
            // A partir de aqui, solo EscucharServidor va a leer "entrada"
            EscucharServidor escucha = new EscucharServidor(entrada);
            new Thread(escucha).start();


            // el main solo lee el teclado y envia respuestas
            // el programa acaba cuando EscucharServidor recibe FIN
            while (true) {
                // pedir la letra hasta que sea A, B, C o D
                String letra = teclado.nextLine().trim().toUpperCase();
                if (letra.matches("[ABCD]")) {
                    int n = escucha.getPreguntaActual();
                    salida.println(Protocolo.construir(Protocolo.RESPUESTA, String.valueOf(n), letra));
                    System.out.println("Esperando al resto de jugadores...");
                } else {
                    System.out.print("Escribe A, B, C o D: ");
                }
            }
            

        }catch(IOException e){
            System.out.println("No se pudo conectar al servidor");
            e.printStackTrace();
        }


    }


}
