# kajoot-distribuidos

Juego de preguntas multijugador en red local, inspirado en Kahoot, desarrollado en Java
para la asignatura **Sistemas Distribuidos** (Grado en Ingeniería Informática, Universidad de La Rioja).

Un servidor dirige la partida y varios clientes se conectan por TCP para responder
las mismas preguntas a la vez, con tiempo límite y ranking en directo.

## Estructura

```
datos/
├── preguntas.xml             Banco de preguntas de la partida
└── preguntas.dtd             Estructura que debe cumplir el XML de preguntas
src/
├── comun/
│   └── Protocolo.java        Formato de los mensajes (compartido por cliente y servidor)
├── servidor/
│   ├── ServidorKajoot.java   Punto de entrada: acepta conexiones y crea la partida
│   ├── AtenderJugador.java   Hilo por jugador: saludo y lectura de respuestas
│   ├── Jugador.java          Envoltorio del socket de cada jugador (nombre, puntos)
│   ├── Partida.java          Lógica del juego: rondas, respuestas y ranking
│   ├── FinRonda.java         Tarea programada que cierra la ronda al acabar el tiempo
│   ├── Pregunta.java         Una pregunta con 4 opciones
│   └── BancoPreguntas.java   Carga las preguntas desde el XML (DOM)
└── cliente/
    ├── ClienteKajoot.java    Punto de entrada: lee el teclado y envía respuestas
    └── EscucharServidor.java  Hilo que muestra todo lo que envía el servidor
```

## Cómo funciona una partida

1. **Arranque.** El host inicia el servidor, indica cuántos jugadores participarán y se cargan
   las preguntas del XML. La partida queda esperando en su propio hilo.
2. **Conexión.** Cada jugador abre un cliente y escribe su nombre.
   El servidor le asigna un hilo propio y lo une a la partida.
3. **Inicio.** Cuando se han unido todos los jugadores esperados, la partida empieza automáticamente.
4. **Rondas.** Para cada pregunta:
   - el servidor la envía **a todos a la vez**;
   - cada jugador responde con una letra (A-D) y recibe al momento si ha acertado o fallado
     (10 puntos por acierto);
   - la ronda termina cuando han respondido todos **o** se agotan los 20 segundos
     (a quien no ha contestado se le notifica "tiempo agotado");
   - el servidor envía a todos el **ranking actualizado**.
5. **Fin.** Tras la última pregunta, todos reciben el ranking final y los clientes se cierran.


## Preguntas

Las preguntas se cargan al arrancar el servidor desde `datos/preguntas.xml`, que debe
cumplir `datos/preguntas.dtd`: al menos una pregunta, cada una con un enunciado,
exactamente 4 opciones (en orden A, B, C, D) y el atributo `correcta`.

```xml
<pregunta correcta="B">
    <enunciado>¿Cuál es la capital de Francia?</enunciado>
    <opcion>Madrid</opcion>
    <opcion>París</opcion>
    <opcion>Londres</opcion>
    <opcion>Roma</opcion>
</pregunta>
```

Para cambiar o añadir preguntas basta con editar el XML.

## Protocolo

Mensajes de texto, una línea por mensaje, con los campos separados por `|`.

| Dirección | Mensaje | Significado |
|---|---|---|
| C → S | `NOMBRE\|Jon` | Unirse a la partida |
| S → C | `OK` / `ERROR\|motivo` | Aceptado o rechazado |
| S → C | `PREGUNTA\|n\|total\|enunciado\|A\|B\|C\|D` | Nueva pregunta |
| C → S | `RESPUESTA\|n\|letra` | Respuesta del jugador (A-D) |
| S → C | `RESULTADO\|ACIERTO`, `FALLO` o `TIEMPO` | Corrección |
| S → C | `RANKING\|Jon:3\|Ana:2` | Clasificación tras cada pregunta |
| S → C | `FIN\|Jon:3\|Ana:2` | Ranking final |
