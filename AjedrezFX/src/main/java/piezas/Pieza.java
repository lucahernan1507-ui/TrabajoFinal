package piezas;

import enums.ColorPieza;
import enums.TipoPieza;
import model.Tablero;

public abstract class Pieza {

    protected ColorPieza color;
    protected TipoPieza tipo;
    protected String simbolo;
    // NUEVO: Atributo indispensable para controlar el enroque
    protected boolean haMoverse;

    public Pieza(ColorPieza color, TipoPieza tipo, String simbolo) {
        this.color = color;
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.haMoverse = false; // Al iniciar el juego, ninguna pieza se ha movido
    }

    public ColorPieza getColor() {
        return color;
    }

    public TipoPieza getTipo() {
        return tipo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    // NUEVO: Getter para comprobar el enroque
    public boolean isHaMoverse() {
        return haMoverse;
    }

    // NUEVO: Setter para actualizar el estado tras un movimiento exitoso
    public void setHaMoverse(boolean haMoverse) {
        this.haMoverse = haMoverse;
    }

    public abstract boolean movimientoValido(
            Tablero tablero,
            int filaOrigen,
            int columnaOrigen,
            int filaDestino,
            int columnaDestino);

    @Override
    public String toString() {
        return simbolo;
    }
}