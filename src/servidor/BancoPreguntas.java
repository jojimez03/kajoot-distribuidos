package servidor;

import java.util.*;

public class BancoPreguntas {
    
    public List<Pregunta> cargar() {
        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(new Pregunta("Cual es la capital de Francia?", new String[]{"Madrid", "Paris", "Londres", "Todas son correctas"}, "B"));
        preguntas.add(new Pregunta("Cuanto es 2+2?", new String[]{"3.14", "3", "0", "4"}, "D"));

        // más preguntas...
        return preguntas;
    }

}
