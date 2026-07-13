package piezas;

import enums.ColorPieza;
import enums.TipoPieza;
import model.Tablero;

public class Torre extends Pieza {

    public Torre(ColorPieza color) {
        super(color,
                TipoPieza.TORRE,
                color == ColorPieza.BLANCO ? "♖" : "♜");
    }

    @Override
    public boolean movimientoValido(Tablero tablero,
            int filaOrigen,
            int columnaOrigen,
            int filaDestino,
            int columnaDestino) {

        // Movimiento rectilíneo obligatorio
        if (filaOrigen != filaDestino && columnaOrigen != columnaDestino) {
            return false; //
        }

        // El camino debe estar libre
        return tablero.caminoLibre(
                filaOrigen,
                columnaOrigen,
                filaDestino,
                columnaDestino); // [cite: 1]
    }
}