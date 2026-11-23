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
                        System.out.println("Pregunta activado");
                        break;
                    case Protocolo.RESULTADO:
                        System.out.println("Resutlado activado");
                        break;
                    case Protocolo.RANKING:
                        System.out.println("Ranking activado");
                        break;
                    case Protocolo.ERROR:
                        System.out.println("Error activado");
                        break;
                    case Protocolo.FIN:
                        System.out.println("Fin activado");
                        break;

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
