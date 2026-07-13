package model;

import enums.ColorPieza;
import enums.Turno;
import piezas.*;

public class Tablero {

    private Casilla[][] casillas;
    private Turno turno;

    public Tablero() {
        casillas = new Casilla[8][8];
        turno = Turno.BLANCAS;

        inicializarCasillas();
        colocarPiezas();
    }

    private void inicializarCasillas() {
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                casillas[fila][columna] = new Casilla(fila, columna);
            }
        }
    }

    private void colocarPiezas() {
        // Negras
        casillas[0][0].setPieza(new Torre(ColorPieza.NEGRO));
        casillas[0][1].setPieza(new Caballo(ColorPieza.NEGRO));
        casillas[0][2].setPieza(new Alfil(ColorPieza.NEGRO));
        casillas[0][3].setPieza(new Reina(ColorPieza.NEGRO));
        casillas[0][4].setPieza(new Rey(ColorPieza.NEGRO));
        casillas[0][5].setPieza(new Alfil(ColorPieza.NEGRO));
        casillas[0][6].setPieza(new Caballo(ColorPieza.NEGRO));
        casillas[0][7].setPieza(new Torre(ColorPieza.NEGRO));

        for (int i = 0; i < 8; i++) {
            casillas[1][i].setPieza(new Peon(ColorPieza.NEGRO));
        }

        // Blancas
        casillas[7][0].setPieza(new Torre(ColorPieza.BLANCO));
        casillas[7][1].setPieza(new Caballo(ColorPieza.BLANCO));
        casillas[7][2].setPieza(new Alfil(ColorPieza.BLANCO));
        casillas[7][3].setPieza(new Reina(ColorPieza.BLANCO));
        casillas[7][4].setPieza(new Rey(ColorPieza.BLANCO));
        casillas[7][5].setPieza(new Alfil(ColorPieza.BLANCO));
        casillas[7][6].setPieza(new Caballo(ColorPieza.BLANCO));
        casillas[7][7].setPieza(new Torre(ColorPieza.BLANCO));

        for (int i = 0; i < 8; i++) {
            casillas[6][i].setPieza(new Peon(ColorPieza.BLANCO));
        }
    }

    public Casilla getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    public Casilla[][] getCasillas() {
        return casillas;
    }

    public Turno getTurno() {
        return turno;
    }

    private void cambiarTurno() {
        if (turno == Turno.BLANCAS) {
            turno = Turno.NEGRAS;
        } else {
            turno = Turno.BLANCAS;
        }
    }

    public boolean posicionValida(int fila, int columna) {
        return fila >= 0 && fila < 8 && columna >= 0 && columna < 8;
    }

    public Pieza getPieza(int fila, int columna) {
        if (!posicionValida(fila, columna)) {
            return null;
        }
        return casillas[fila][columna].getPieza();
    }

    public boolean estaVacia(int fila, int columna) {
        if (!posicionValida(fila, columna)) {
            return false;
        }
        return casillas[fila][columna].estaVacia();
    }

    public boolean caminoLibre(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        int incrementoFila = Integer.compare(filaDestino, filaOrigen);
        int incrementoColumna = Integer.compare(columnaDestino, columnaOrigen);

        int fila = filaOrigen + incrementoFila;
        int columna = columnaOrigen + incrementoColumna;

        while (fila != filaDestino || columna != columnaDestino) {
            if (!estaVacia(fila, columna)) {
                return false;
            }
            fila += incrementoFila;
            columna += incrementoColumna;
        }
        return true;
    }

    // ==========================================
    // NUEVO: DETECTAR JAQUE
    // ==========================================
    public boolean estaEnJaque(ColorPieza colorRey) {
        int filaRey = -1;
        int colRey = -1;

        // 1. Buscar la posición del Rey del color indicado
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                Pieza p = getPieza(f, c);
                if (p instanceof Rey && p.getColor() == colorRey) {
                    filaRey = f;
                    colRey = c;
                    break;
                }
            }
        }

        if (filaRey == -1)
            return false; // Por seguridad

        // 2. Comprobar si alguna pieza enemiga puede moverse a la casilla del rey
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                Pieza enemiga = getPieza(f, c);
                if (enemiga != null && enemiga.getColor() != colorRey) {
                    if (enemiga.movimientoValido(this, f, c, filaRey, colRey)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ==========================================
    // NUEVO: COMPROBAR SI UNA CASILLA ESTÁ AMENAZADA (Para el Enroque)
    // ==========================================
    public boolean casillaAmenazada(int fila, int columna, ColorPieza colorDefensor) {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                Pieza enemiga = getPieza(f, c);
                if (enemiga != null && enemiga.getColor() != colorDefensor) {
                    if (enemiga.movimientoValido(this, f, c, fila, columna)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ==========================================
    // NUEVO: DETECTAR JAQUE MATE
    // ==========================================
    public boolean esJaqueMate(ColorPieza colorTurno) {
        if (!estaEnJaque(colorTurno)) {
            return false; // Si no está en jaque, no puede ser jaque mate
        }

        // Iterar sobre todas las casillas buscando piezas del jugador en turno
        for (int fo = 0; fo < 8; fo++) {
            for (int co = 0; co < 8; co++) {
                Pieza pieza = getPieza(fo, co);
                if (pieza != null && pieza.getColor() == colorTurno) {

                    // Intentar mover la pieza a cualquier casilla del tablero
                    for (int fd = 0; fd < 8; fd++) {
                        for (int cd = 0; cd < 8; cd++) {

                            // Verificar restricciones básicas de movimiento
                            if (pieza.movimientoValido(this, fo, co, fd, cd)) {
                                Pieza destinoPieza = getPieza(fd, cd);
                                if (destinoPieza == null || destinoPieza.getColor() != colorTurno) {

                                    // SIMULAR MOVIMIENTO
                                    casillas[fd][cd].setPieza(pieza);
                                    casillas[fo][co].setPieza(null);

                                    boolean sigueEnJaque = estaEnJaque(colorTurno);

                                    // DESHACER SIMULACIÓN
                                    casillas[fo][co].setPieza(pieza);
                                    casillas[fd][cd].setPieza(destinoPieza);

                                    if (!sigueEnJaque) {
                                        return false; // ¡Hay al menos una jugada legal que salva al rey!
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true; // No hay movimientos posibles para salir del jaque
    }

    // ==========================================
    // ACTUALIZADO: MOVER PIEZA CON NUEVAS REGLAS
    // ==========================================
    public boolean moverPieza(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {

        if (!posicionValida(filaOrigen, columnaOrigen) || !posicionValida(filaDestino, columnaDestino)) {
            return false;
        }

        if (filaOrigen == filaDestino && columnaOrigen == columnaColumnaDestino(columnaDestino)) { // Simplificación de
                                                                                                   // tu condición
            return false;
        }

        Pieza pieza = getPieza(filaOrigen, columnaOrigen);
        if (pieza == null) {
            return false;
        }

        // Turnos
        ColorPieza colorTurnoActual = (turno == Turno.BLANCAS) ? ColorPieza.BLANCO : ColorPieza.NEGRO;
        if (pieza.getColor() != colorTurnoActual) {
            return false;
        }

        // --- DETECCIÓN DE ENROQUE ---
        boolean esMovimientoEnroque = false;
        int torreFilaOrig = -1, torreColOrig = -1, torreFilaDest = -1, torreColDest = -1;

        if (pieza instanceof Rey && Math.abs(columnaDestino - columnaOrigen) == 2) {
            // Verificar condiciones específicas del enroque
            if (estaEnJaque(colorTurnoActual))
                return false; // No se puede enrocar en jaque

            int dir = Integer.compare(columnaDestino, columnaOrigen);
            int pasoCol = columnaOrigen + dir;

            // Verificar que la casilla de paso no esté amenazada
            if (casillaAmenazada(filaOrigen, pasoCol, colorTurnoActual))
                return false;

            // Identificar qué torre debe moverse
            torreFilaOrig = filaOrigen;
            torreColOrig = (dir > 0) ? 7 : 0;
            torreFilaDest = filaOrigen;
            torreColDest = (dir > 0) ? 5 : 3;

            Pieza torre = getPieza(torreFilaOrig, torreColOrig);
            // Comprobación de que la torre existe y el camino esté libre
            if (torre instanceof Torre && caminoLibre(filaOrigen, columnaOrigen, torreFilaOrig, torreColOrig)) {
                esMovimientoEnroque = true;
            } else {
                return false;
            }
        }
        // Movimiento regular si no es enroque
        else if (!pieza.movimientoValido(this, filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            return false;
        }

        Pieza destino = getPieza(filaDestino, columnaDestino);
        if (destino != null && destino.getColor() == pieza.getColor()) {
            return false;
        }

        // --- SIMULACIÓN RESTRICCIÓN DE JAQUE PROPIO ---
        // Una pieza no puede moverse si expone o deja a su propio rey en jaque
        casillas[filaDestino][columnaDestino].setPieza(pieza);
        casillas[filaOrigen][columnaOrigen].setPieza(null);

        if (estaEnJaque(colorTurnoActual)) {
            // Deshacer movimiento ilegal porque el rey queda en jaque
            casillas[filaOrigen][columnaOrigen].setPieza(pieza);
            casillas[filaDestino][columnaDestino].setPieza(destino);
            return false;
        }

        // Si el enroque fue exitoso en la simulación, movemos físicamente la torre
        if (esMovimientoEnroque) {
            Pieza torre = getPieza(torreFilaOrig, torreColOrig);
            casillas[torreFilaDest][torreColDest].setPieza(torre);
            casillas[torreFilaOrig][torreColOrig].setPieza(null);
        }

        // --- GESTIÓN DE NOTIFICACIÓN DE PRIMER MOVIMIENTO ---
        // Si tus piezas tienen un atributo como `haMoverse`, actívalo aquí:
        // pieza.setSeHaMovido(true);

        // --- DETECCIÓN DE PROMOCIÓN DEL PEÓN ---
        if (pieza instanceof Peon && (filaDestino == 0 || filaDestino == 7)) {
            // Nota: Aquí lo ideal es abrir un cuadro de diálogo en tu controlador/UI.
            // Para mantener la lógica del Tablero pura, por defecto lo convertimos en
            // Reina.
            // (Puedes cambiar esto para recibir el tipo de pieza deseado desde el
            // controlador)
            casillas[filaDestino][columnaDestino].setPieza(new Reina(colorTurnoActual));
        }

        cambiarTurno();

        // Aquí puedes comprobar si tras este movimiento el rival cayó en Jaque Mate
        // ColorPieza colorRival = (colorTurnoActual == ColorPieza.BLANCO) ?
        // ColorPieza.NEGRO : ColorPieza.BLANCO;
        // if (esJaqueMate(colorRival)) { System.out.println("¡FIN DEL JUEGO! Jaque
        // Mate."); }

        return true;
    }

    // Método auxiliar rápido para corregir la redundancia de variables
    private int columnaColumnaDestino(int col) {
        return col;
    }
}