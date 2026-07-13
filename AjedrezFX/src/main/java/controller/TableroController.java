package controller;

import enums.ColorPieza;
import enums.Turno;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.Casilla;
import model.Tablero;
import piezas.*;

import java.util.List;
import java.util.Optional;

public class TableroController {

    @FXML
    private GridPane tableroGrid;

    @FXML
    private Label lblTurno;

    @FXML
    private Button btnNueva;

    @FXML
    private Button btnReiniciar;

    @FXML
    private Button btnSalir;

    private Button[][] botones;

    private Tablero tablero;

    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;

    @FXML
    public void initialize() {
        botones = new Button[8][8];
        tablero = new Tablero();
        crearTablero();
        actualizarTablero();
    }

    private void crearTablero() {
        tableroGrid.getChildren().clear();
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Button boton = new Button();
                boton.setPrefSize(80, 80);
                boton.setStyle(colorCasilla(fila, columna));

                final int f = fila;
                final int c = columna;

                boton.setOnAction(e -> clicCasilla(f, c));

                botones[fila][columna] = boton;
                tableroGrid.add(boton, columna, fila);
            }
        }
    }

    private void clicCasilla(int fila, int columna) {
        if (filaSeleccionada == -1) {
            seleccionarPieza(fila, columna);
        } else {
            moverPieza(fila, columna);
        }
    }

    private void seleccionarPieza(int fila, int columna) {
        Casilla casilla = tablero.getCasilla(fila, columna);
        if (casilla.estaVacia()) {
            return;
        }

        Pieza pieza = casilla.getPieza();

        if (tablero.getTurno().toString().equals("BLANCAS")
                && pieza.getColor().toString().equals("NEGRO")) {
            mostrarMensaje("Es el turno de las piezas blancas.");
            return;
        }

        if (tablero.getTurno().toString().equals("NEGRAS")
                && pieza.getColor().toString().equals("BLANCO")) {
            mostrarMensaje("Es el turno de las piezas negras.");
            return;
        }

        filaSeleccionada = fila;
        columnaSeleccionada = columna;

        actualizarTablero();

        botones[fila][columna].setStyle(
                "-fx-background-color:yellow;"
                + "-fx-font-size:30px;"
                + "-fx-font-weight:bold;"
        );
    }

    private void moverPieza(int filaDestino, int columnaDestino) {
        Pieza piezaAMover = tablero.getPieza(filaSeleccionada, columnaSeleccionada);

        boolean movimiento = tablero.moverPieza(
                filaSeleccionada,
                columnaSeleccionada,
                filaDestino,
                columnaDestino);

        if (!movimiento) {
            mostrarMensaje("Movimiento inválido.");
        } else {
            if (piezaAMover != null) {
                piezaAMover.setHaMoverse(true);
            }

            // --- PROCESAR PROMOCIÓN ---
            Pieza piezaEnDestino = tablero.getPieza(filaDestino, columnaDestino);
            if (piezaEnDestino instanceof Peon && (filaDestino == 0 || filaDestino == 7)) {
                String eleccion = mostrarDialogoPromocion();
                ColorPieza colorPeon = piezaEnDestino.getColor();
                Pieza nuevaPieza;

                switch (eleccion) {
                    case "Torre": nuevaPieza = new Torre(colorPeon); break;
                    case "Alfil": nuevaPieza = new Alfil(colorPeon); break;
                    case "Caballo": nuevaPieza = new Caballo(colorPeon); break;
                    default: nuevaPieza = new Reina(colorPeon); break;
                }
                nuevaPieza.setHaMoverse(true);
                tablero.getCasilla(filaDestino, columnaDestino).setPieza(nuevaPieza);
            }

            // --- EVALUAR JAQUES Y JAQUE MATE ---
            ColorPieza colorTurnoActual = (tablero.getTurno() == Turno.BLANCAS) ? ColorPieza.BLANCO : ColorPieza.NEGRO;

            if (tablero.esJaqueMate(colorTurnoActual)) {
                actualizarTablero();
                
                // Si el turno actual al que le dieron mate es blanco, ganan las negras
                String ganador = (colorTurnoActual == ColorPieza.BLANCO) ? "Las Piezas Negras" : "Las Piezas Blancas";
                
                mostrarMensaje("¡JAQUE MATE!\nFin de la partida.\n¡Ganador: " + ganador + "!");
                return;
            } else if (tablero.estaEnJaque(colorTurnoActual)) {
                mostrarMensaje("¡Jaque al Rey!");
            }
        }

        filaSeleccionada = -1;
        columnaSeleccionada = -1;

        actualizarTablero();
    }

    private String mostrarDialogoPromocion() {
        List<String> opciones = List.of("Reina", "Torre", "Alfil", "Caballo");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Reina", opciones);
        dialog.setTitle("Promoción de Peón");
        dialog.setHeaderText("¡Tu peón ha alcanzado el final del tablero!");
        dialog.setContentText("Selecciona la pieza para la promoción:");
        
        Optional<String> resultado = dialog.showAndWait();
        return resultado.orElse("Reina");
    }

    private void actualizarTablero() {
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                
                Pieza pieza = tablero.getPieza(fila, columna);
                
                // Determinamos el color del texto (Blanco o Negro) basado en el bando de la pieza
                String colorTexto = "-fx-text-fill: transparent;"; 
                if (pieza != null) {
                    if (pieza.getColor() == ColorPieza.BLANCO) {
                        // Blanco puro con un sombreado sutil para que resalte excelentemente sobre el café claro
                        colorTexto = "-fx-text-fill: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 2, 0.0, 0, 1);";
                    } else {
                        // Negro absoluto que contrasta de maravilla con el café oscuro
                        colorTexto = "-fx-text-fill: #000000;";
                    }
                }

                // Ajustamos fuente a 32px y forzamos padding 0 para que no salgan los "..."
                botones[fila][columna].setStyle(
                        colorCasilla(fila, columna)
                        + "-fx-font-size: 32px;" 
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0;"
                        + colorTexto
                );

                if (pieza == null) {
                    botones[fila][columna].setText("");
                } else {
                    botones[fila][columna].setText(pieza.getSimbolo());
                }
            }
        }

        if (tablero.getTurno().toString().equals("BLANCAS")) {
            lblTurno.setText("Turno: Blancas");
        } else {
            lblTurno.setText("Turno: Negras");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ajedrez");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private String colorCasilla(int fila, int columna) {
        // Restaurado a tus colores café originales
        if ((fila + columna) % 2 == 0) {
            return "-fx-background-color:#F0D9B5;";
        }
        return "-fx-background-color:#B58863;";
    }

    @FXML
    private void nuevaPartida() {
        tablero = new Tablero();
        filaSeleccionada = -1;
        columnaSeleccionada = -1;
        actualizarTablero();
    }

    @FXML
    private void reiniciar() {
        nuevaPartida();
    }

    @FXML
    private void salir() {
        Platform.exit();
    }
}