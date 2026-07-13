package piezas;

import enums.ColorPieza;
import enums.TipoPieza;
import model.Tablero;

public class Reina extends Pieza {

    public Reina(ColorPieza color) {

        super(color,
                TipoPieza.REINA,
                color == ColorPieza.BLANCO ? "♕" : "♛");

    }

    @Override
    public boolean movimientoValido(Tablero tablero,
            int filaOrigen,
            int columnaOrigen,
            int filaDestino,
            int columnaDestino) {

        boolean movimientoRecto = filaOrigen == filaDestino ||
                columnaOrigen == columnaDestino;

        boolean movimientoDiagonal = Math.abs(filaDestino - filaOrigen) == Math.abs(columnaDestino - columnaOrigen);

        if (!movimientoRecto && !movimientoDiagonal) {

            return false;

        }

        return tablero.caminoLibre(
                filaOrigen,
                columnaOrigen,
                filaDestino,
                columnaDestino);

    }

}