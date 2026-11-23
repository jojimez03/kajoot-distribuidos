package servidor;

import java.util.TimerTask;

/**
 * Tarea programada para cerrar una ronda cuando se acaba el tiempo.
 */
public class FinRonda extends TimerTask {

    private Partida partida;
    private int n;   // número de la pregunta que debe cerrar

    public FinRonda(Partida partida, int n) {
        this.partida = partida;
        this.n = n;
    }

    @Override
    public void run() {
        partida.cerrarPorTiempo(n);
    }
}