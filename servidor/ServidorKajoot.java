package servidor;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


import comun.Protocolo;

public class ServidorKajoot {
    
    public static void main(String[] args){

        try(ServerSocket ss = new ServerSocket(Protocolo.PUERTO)){
            ExecutorService pool = Executors.newCachedThreadPool();
            System.out.println("Servidor escuchando en el puerto " + Protocolo.PUERTO);
            
            while(true){
                
                try{
                    Socket cliente = ss.accept();
                    Jugador jugador = new Jugador(cliente);
                    
                    pool.execute(new AtenderJugador(jugador));

                    System.out.println("Nueva conexion desde " + cliente.getInetAddress());

                }catch(IOException e){
                    System.out.println("Error con un cliente ");
                    e.printStackTrace();
                }
            }



        }catch(IOException e){
            System.out.println("No se puede abrir el ServerSocket");
            e.printStackTrace();
        }

    }

}
