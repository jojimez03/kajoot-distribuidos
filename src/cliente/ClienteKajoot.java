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

public class ClienteKajoot {
    
    public static void main(String[] args){
        Scanner teclado = new Scanner(System.in);

        try(Socket socket = new Socket("localhost", Protocolo.PUERTO)){
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

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
            EscucharServidor escucha = new EscucharServidor(entrada);
            new Thread(escucha).start();


            // solo lee el teclado y envia respuestas
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
