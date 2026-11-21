package servidor;

import java.io.IOException;
import comun.Protocolo;

public class AtenderJugador implements Runnable {

    private Jugador jugador;

    public AtenderJugador(Jugador jugador) {
        this.jugador=jugador;
    }

    @Override
    public void run() {
        try {
            String linea1 = jugador.leer();
            // 1. Leer la primera línea. Si es null, return.

            String [] campos = Protocolo.partir(linea1);
            // 2. Partirla con Protocolo.partir

            if(campos[0].equals(Protocolo.NOMBRE)){
                jugador.setNombre(campos[1]);
                jugador.enviar(Protocolo.OK);
            }else{
                jugador.enviar(Protocolo.ERROR);
                return;
            }
            
            // 3. Si es válida (2 campos, campos[0] es NOMBRE, nombre no vacío):
            //      setNombre, enviar OK, imprimir "Se ha unido X"
            //    Si no:
            //      enviar ERROR|mensaje no valido, cerrar y return


            while(jugador.leer()!=null){

            }
            System.out.println(jugador.getNombre() + "se ha desconectado");
            // 4. (Solo para probar) esperar a que se desconecte:
            //      while (jugador.leer() != null) {}
            //      imprimir "X se ha desconectado"

        } catch (IOException e) {
            System.out.println("Error atendiendo a un jugador: " + e.getMessage());
        } finally {
            jugador.cerrar();
        }
    }
}