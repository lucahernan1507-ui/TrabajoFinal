package piezas;

import enums.ColorPieza;
import enums.TipoPieza;
import model.Tablero;

public class Rey extends Pieza {

    public Rey(ColorPieza color) {
        super(color,
                TipoPieza.REY,
                color == ColorPieza.BLANCO ? "♔" : "♚");
    }

    @Override
    public boolean movimientoValido(Tablero tablero,
            int filaOrigen,
            int columnaOrigen,
            int filaDestino,
            int columnaDestino) {

        int difFila = Math.abs(filaDestino - filaOrigen);
        int difCol = Math.abs(columnaDestino - columnaOrigen);

        // Movimiento regular de una casilla a la redonda
        if (difFila <= 1 && difCol <= 1) {
            return true;
        }

        // CONTROL DEL ENROQUE: El rey intenta moverse 2 casillas horizontalmente
        if (difFila == 0 && difCol == 2 && !this.isHaMoverse()) {
            // No se puede enrocar si el rey está actualmente en jaque
            if (tablero.estaEnJaque(this.color)) {
                return false;
            }

            int direccion = Integer.compare(columnaDestino, columnaOrigen);
            int colTorre = (direccion > 0) ? 7 : 0; // Columna 7 enroque corto, Columna 0 largo

            Pieza torre = tablero.getPieza(filaOrigen, colTorre);

            // Validar que la torre exista, sea del mismo color y no se haya movido
            if (torre instanceof Torre && !torre.isHaMoverse()) {
                // El camino entre el rey y la torre debe estar completamente libre
                if (tablero.caminoLibre(filaOrigen, columnaOrigen, filaOrigen, colTorre)) {
                    // El rey no puede pasar por una casilla que esté bajo ataque enemigo
                    int columnaPaso = columnaOrigen + direccion;
                    if (!tablero.casillaAmenazada(filaOrigen, columnaPaso, this.color)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}