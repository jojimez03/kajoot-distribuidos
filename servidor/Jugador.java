package servidor;

import java.io.*;
import java.net.Socket;

public class Jugador {

    private Socket socket;
    private DataInputStream entrada;   // para leer líneas del cliente
    private PrintWriter salida;       // para enviar líneas al cliente
    private String nombre;
    private int puntos;

    public Jugador(Socket socket) throws IOException {
        this.socket=socket;
        this.entrada = new DataInputStream(socket.getInputStream());
        this.salida = new PrintWriter(socket.getOutputStream(), true);
        this.puntos=0;
        // el nombre no lo se al crear el Jugador
    }

    public void enviar(String mensaje) {
        salida.println(mensaje);
    }

    public String leer() throws IOException {
        return entrada.readLine();
    }

    public void sumarPuntos(int p) { this.puntos+=p;}

    public String getNombre() { return this.nombre; }
    public void setNombre(String nombre) { this.nombre=nombre; }
    public int getPuntos() { return this.puntos; }

    public void cerrar() {
        sokcet.close();
    }
}