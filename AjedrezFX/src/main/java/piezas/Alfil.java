package piezas;

import enums.ColorPieza;
import enums.TipoPieza;
import model.Tablero;

public class Alfil extends Pieza {

    public Alfil(ColorPieza color) {

        super(color,
                TipoPieza.ALFIL,
                color == ColorPieza.BLANCO ? "♗" : "♝");

    }

    @Override
    public boolean movimientoValido(Tablero tablero,
            int filaOrigen,
            int columnaOrigen,
            int filaDestino,
            int columnaDestino) {

        // Debe moverse en diagonal
        if (Math.abs(filaDestino - filaOrigen) != Math.abs(columnaDestino - columnaOrigen)) {

            return false;

        }

        // Verificar que no haya piezas en el camino
        return tablero.caminoLibre(
                filaOrigen,
                columnaOrigen,
                filaDestino,
                columnaDestino);

    }

}