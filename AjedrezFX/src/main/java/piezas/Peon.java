package piezas;

import enums.ColorPieza;
import enums.TipoPieza;
import model.Tablero;

public class Peon extends Pieza {

    public Peon(ColorPieza color) {
        super(color,
                TipoPieza.PEON,
                color == ColorPieza.BLANCO ? "♙" : "♟");
    }

    @Override
    public boolean movimientoValido(Tablero tablero,
            int filaOrigen,
            int columnaOrigen,
            int filaDestino,
            int columnaDestino) {

        int direccion = (color == ColorPieza.BLANCO) ? -1 : 1;

        // ==========================
        // Avanzar una casilla
        // ==========================
        if (columnaOrigen == columnaDestino &&
                filaDestino == filaOrigen + direccion &&
                tablero.estaVacia(filaDestino, columnaDestino)) {
            return true;
        }

        // ==========================
        // Primer movimiento (Avance Doble)
        // ==========================
        if (columnaOrigen == columnaDestino && !this.isHaMoverse()) {
            int filaIntermedia = filaOrigen + direccion;
            if (filaDestino == filaOrigen + (2 * direccion) &&
                    tablero.estaVacia(filaIntermedia, columnaOrigen) &&
                    tablero.estaVacia(filaDestino, columnaOrigen)) {
                return true;
            }
        }

        // ==========================
        // Captura diagonal
        // ==========================
        if (filaDestino == filaOrigen + direccion &&
                Math.abs(columnaDestino - columnaOrigen) == 1) {

            Pieza destino = tablero.getPieza(filaDestino, columnaDestino);

            if (destino != null &&
                    destino.getColor() != color) {
                return true;
            }
        }

        return false;
    }
}