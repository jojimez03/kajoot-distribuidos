package servidor;

import java.util.TimerTask;

/**
 * Tarea programada para cerrar una ronda cuando se acaba el tiempo.
 * Partida la programa al enviar cada pregunta.
 */
public class FinRonda extends TimerTask {

    private Partida partida;
    private int n;   // número de la pregunta que debe cerrar

    public FinRonda(Partida partida, int n) {
        this.partida = partida;
        this.n = n;
    }

    // se ejecuta el hilo del Timer cuando pasan los segundos
    @Override
    public void run() {
        partida.cerrarPorTiempo(n);
    }
}