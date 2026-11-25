package servidor;

import java.util.*;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Origen de las preguntas: las lee de un fichero XML (validado con preguntas.dtd)
 * usando DOM y las devuelve como lista de objetos Pregunta.
 */

public class BancoPreguntas {

    private String ruta;

    public BancoPreguntas(String ruta){
        this.ruta=ruta;
    }
    
    public List<Pregunta> cargar() throws Exception{

        List<Pregunta> preguntas = new ArrayList<>();

        // parsear y obtener el árbol DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(ruta));

        // elementos <pregunta>
        NodeList nodos = doc.getElementsByTagName("pregunta");

        // recorrer nodos y crear obetos pregunta
        for (int i = 0; i < nodos.getLength(); i++) {
            Element e = (Element) nodos.item(i);
            String correcta = e.getAttribute("correcta");
            String enunciado = e.getElementsByTagName("enunciado").item(0).getTextContent();

            NodeList ops = e.getElementsByTagName("opcion");
            String[] opciones = new String[4];
            for (int j = 0; j < 4; j++) {
                opciones[j] = ops.item(j).getTextContent();
            }

            preguntas.add(new Pregunta(enunciado, opciones, correcta));
        }

        return preguntas;
    

        /**preguntas.add(new Pregunta("Cual es la capital de Francia?", new String[]{"Madrid", "Paris", "Londres", "Todas son correctas"}, "B"));
        preguntas.add(new Pregunta("Cuanto es 2+2?", new String[]{"3.14", "3", "0", "4"}, "D"));

        // más preguntas...
        return preguntas;
        */
    }

}
