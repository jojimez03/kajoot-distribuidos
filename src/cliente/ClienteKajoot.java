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

            System.out.println("Pulsa Enter para salir...");
            teclado.nextLine();

        }catch(IOException e){
            System.out.println("No se pudo conectar al servidor");
            e.printStackTrace();
        }


    }


}
