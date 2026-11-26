package servidor;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import comun.Protocolo;

public class Partida implements Runnable{

    private static final int SEGUNDOS = 20;

    private List<Pregunta> preguntas;
    private List<Jugador> jugadores;

    // cuantos jugadores se esperan.
    private int numJugadores;

    private CountDownLatch jugadoresListos; // llega a 0 cuando se han unido todos
    private CountDownLatch respuestasRonda; // llega a 0 cuando han respondido todos

    // numero de la pregunta actual
    private int preguntaActual;

    // llevar el control de los jugadores que si han respondido
    private Set<Jugador> hanRespondido;

    private Timer timer;


    public Partida(List<Pregunta> preguntas, int numJugadores){
        this.preguntas=preguntas;
        this.numJugadores=numJugadores;
        this.jugadoresListos=new CountDownLatch(numJugadores);

        this.jugadores = new ArrayList<>();

        this.hanRespondido= new HashSet<>();
        this.timer = new Timer();
    }

    public boolean unirse(Jugador j){
        if(this.jugadores.size()!=numJugadores){
            this.jugadores.add(j);
            this.jugadoresListos.countDown();
            return true;
        }else{
            return false;
        }
    }

    public void run(){
        try{
            // espero a que esten todos. En un futuro en vez de esperar a tener todos los jugadores esperados
            // se leera desde teclado mediante otro hilo para empezar.
            jugadoresListos.await();

            int numPreguntas=preguntas.size();
            this.preguntaActual=1;

            for(Pregunta p: preguntas){
                respuestasRonda = new CountDownLatch(numJugadores);
                

                String mensajePreg = p.toMensaje(preguntaActual, numPreguntas);
                enviarATodos(mensajePreg);

                FinRonda tarea = new FinRonda(this, preguntaActual);
                timer.schedule(tarea, SEGUNDOS * 1000);  

                respuestasRonda.await();
                tarea.cancel();

                enviarATodos(Protocolo.construir(Protocolo.RANKING, ranking()));


                preguntaActual++;
            }

            String rankingFinal = ranking();
            enviarATodos(Protocolo.construir(Protocolo.FIN, rankingFinal));
            System.out.println("Partida terminada: " + rankingFinal);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void enviarATodos(String mensaje){
        for(Jugador j:jugadores){
            j.enviar(mensaje);
        }
    }

    public void registrarRespuesta(Jugador j, int n, String letra){
         if (n != preguntaActual || !hanRespondido.add(j)) {
            return; // respuesta tardia o repetida
        }

        if (preguntas.get(n - 1).esCorrecta(letra)) {
            j.sumarPuntos(10);
            j.enviar(Protocolo.construir(Protocolo.RESULTADO, "ACIERTO"));
        } else {
            j.enviar(Protocolo.construir(Protocolo.RESULTADO, "FALLO"));
        }
         // después de enviar el resultado
        respuestasRonda.countDown();  
    }

    private String ranking(){
        List<Jugador> quedan = new ArrayList<>(jugadores); // copia
        String[] campos = new String[quedan.size()];

        for(int i=0; i< campos.length; i++){
            Jugador mejor = quedan.get(0);
            for(Jugador j: quedan){
                if(j.getPuntos() > mejor.getPuntos()){
                    mejor=j;
                }
            }
            campos[i] = mejor.getNombre() + ":" + mejor.getPuntos();
            quedan.remove(mejor);
        }

        return String.join("|", campos);
    }


    public void cerrarPorTiempo(int n) {
        if (n != preguntaActual) {
            return;   // esa ronda ya terminó: la tarea ha llegado tarde
        }

        System.out.println("Fin del tiempo para la pregunta " + n);

        for (Jugador j : jugadores) {
            if (!hanRespondido.contains(j)) {
                hanRespondido.add(j);
                j.enviar(Protocolo.construir(Protocolo.RESULTADO, "TIEMPO"));
                respuestasRonda.countDown();
            }
        }
    }


}