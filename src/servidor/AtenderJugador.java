package servidor;

import java.io.IOException;
import comun.Protocolo;

/** 
 * Hilo que se encarga de un jugador: lepide el nombre, lo mete enla partida y se queda escuchando
 * sus respuestas hasta que se va. 
 * */
public class AtenderJugador implements Runnable {

    private Jugador jugador;
    private Partida partida;

    public AtenderJugador(Jugador jugador, Partida partida) {
        this.jugador=jugador;
        this.partida=partida;
    }

    // saludo, unirse a la partida y escuchar respuestas y se las pasa a la partida (registrarRespuesta)
    @Override
    public void run() {
        try {
            String linea1 = jugador.leer();

            if(linea1==null) return;

            String [] campos = Protocolo.partir(linea1);

            if(campos[0].equals(Protocolo.NOMBRE) && campos.length==2 && !campos[1].isEmpty()){
                jugador.setNombre(campos[1]);
                jugador.enviar(Protocolo.OK);
            }else{
                jugador.enviar(Protocolo.construir(Protocolo.ERROR, "Error: no has introducido el nombre correctamente"));
                return;
            }

            if(!partida.unirse(jugador)){
                jugador.enviar(Protocolo.construir(Protocolo.ERROR, "error al entrar o partida llena"));
                return;
            }
            System.out.println("Se ha unido " + jugador.getNombre());
        

            String linea;
            while((linea=jugador.leer()) != null){ // leo las respuestas
                String[] campos2 = Protocolo.partir(linea);
                if(campos2.length == 3 && campos2[0].equals(Protocolo.RESPUESTA)){
                    int n = Integer.parseInt(campos2[1]);
                    partida.registrarRespuesta(jugador, n, campos2[2]);
                }


            }
            System.out.println(jugador.getNombre() + " se ha desconectado");
        

        } catch (IOException e) {
            System.out.println("Error atendiendo a un jugador: " + e.getMessage());
        } finally {
            jugador.cerrar();
        }
    }
}