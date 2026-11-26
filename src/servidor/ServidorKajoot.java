package servidor;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;


import comun.Protocolo;

/** 
 * Punto de entrada del servidor. Carga las preguntas, crea la partida y acepta conexiones.
 * Le da a cada jugador su propio hilo AtenderJugador.
 */
public class ServidorKajoot {
    
    public static void main(String[] args){

        try(ServerSocket ss = new ServerSocket(Protocolo.PUERTO)){
            ExecutorService pool = Executors.newCachedThreadPool();
            System.out.println("Servidor escuchando en el puerto " + Protocolo.PUERTO);

            Scanner teclado = new Scanner(System.in);
            System.out.print("¿Cuántos jugadores? ");
            int numJugadores = teclado.nextInt();


            List<Pregunta> preguntas;
            try {
                preguntas = new BancoPreguntas("datos/preguntas.xml").cargar();
            } catch (Exception e) {
                System.out.println("No se pudieron cargar las preguntas: " + e.getMessage());
                return;
            }
            System.out.println("Cargadas " + preguntas.size() + " preguntas");

            // la partida arranca en su hilo y se queda en el await esperando
            Partida partida = new Partida(preguntas, numJugadores);
            pool.execute(partida);
            
            while(true){
                // los jugadores entran aqui y se unen a la partida
                try{
                    Socket cliente = ss.accept();
                    Jugador jugador = new Jugador(cliente);
                    
                    pool.execute(new AtenderJugador(jugador, partida));

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
