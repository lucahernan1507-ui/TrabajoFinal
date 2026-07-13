package piezas;

import enums.ColorPieza;
import enums.TipoPieza;
import model.Tablero;

public class Caballo extends Pieza {

    public Caballo(ColorPieza color) {

        super(color,
                TipoPieza.CABALLO,
                color == ColorPieza.BLANCO ? "♘" : "♞");

    }

    @Override
    public boolean movimientoValido(Tablero tablero,
            int filaOrigen,
            int columnaOrigen,
            int filaDestino,
            int columnaDestino) {

        int fila = Math.abs(filaDestino - filaOrigen);
        int columna = Math.abs(columnaDestino - columnaOrigen);

        return (fila == 2 && columna == 1)
                || (fila == 1 && columna == 2);

    }

}