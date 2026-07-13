package model;

public class Movimiento {

    private int filaOrigen;
    private int columnaOrigen;
    private int filaDestino;
    private int columnaDestino;

    public Movimiento(int filaOrigen, int columnaOrigen,
            int filaDestino, int columnaDestino) {

        this.filaOrigen = filaOrigen;
        this.columnaOrigen = columnaOrigen;
        this.filaDestino = filaDestino;
        this.columnaDestino = columnaDestino;

    }

    public int getFilaOrigen() {
        return filaOrigen;
    }

    public int getColumnaOrigen() {
        return columnaOrigen;
    }

    public int getFilaDestino() {
        return filaDestino;
    }

    public int getColumnaDestino() {
        return columnaDestino;
    }

}