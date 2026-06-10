package es.Studium;

import java.awt.*;
import java.awt.event.*;

public class Vista {
    public Frame ventanaMenu, ventanaJuego, ventanaSeleccion;
    public Dialog dlgRanking, dlgNombre, dlgError, dlgRobar, dlgGanador;
    public Button btnNueva, btnRanking, btnAyuda, btnSalir, btnRobar;
    public Button btn2, btn3, btn4;
    public Panel panelCartas;
    public Label lblTurno, lblMesa, lblCartaRobada, lblGanadorMensaje;
    public TextArea txtRanking;
    public TextField txtNombre;
    public Button btnAceptarNombre;

    public Vista() {
        // MENÚ 
        ventanaMenu = new Frame("UNO");
        ventanaMenu.setLayout(new GridLayout(4, 1));
        btnNueva = new Button("Nueva Partida");
        btnRanking = new Button("Ver Ranking");
        btnAyuda = new Button("Ayuda");
        btnSalir = new Button("Salir");
        
        ventanaMenu.add(btnNueva); ventanaMenu.add(btnRanking);
        ventanaMenu.add(btnAyuda); ventanaMenu.add(btnSalir);
        ventanaMenu.setSize(300, 300);
        ventanaMenu.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });

        // SELECCIÓN JUGADORES
        ventanaSeleccion = new Frame("Jugadores");
        ventanaSeleccion.setLayout(new GridLayout(3, 1));
        btn2 = new Button("2 Jugadores");
        btn3 = new Button("3 Jugadores");
        btn4 = new Button("4 Jugadores");
        ventanaSeleccion.add(btn2); ventanaSeleccion.add(btn3); ventanaSeleccion.add(btn4);
        ventanaSeleccion.setSize(200, 200);
        ventanaSeleccion.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { ventanaSeleccion.setVisible(false); } });

        // DIALOGS
        dlgRanking = new Dialog(ventanaMenu, "Ranking", true);
        dlgRanking.setLayout(new BorderLayout());
        txtRanking = new TextArea("", 10, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
        txtRanking.setEditable(false);
        Button btnCerrarRanking = new Button("Cerrar");
        btnCerrarRanking.addActionListener(e -> dlgRanking.setVisible(false));
        dlgRanking.add(txtRanking, BorderLayout.CENTER);
        dlgRanking.add(btnCerrarRanking, BorderLayout.SOUTH);
        dlgRanking.setSize(300, 300);
        dlgRanking.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dlgRanking.setVisible(false); } });

        dlgNombre = new Dialog(ventanaMenu, "Nombre", true);
        dlgNombre.setLayout(new FlowLayout());
        txtNombre = new TextField(20);
        btnAceptarNombre = new Button("Aceptar");
        dlgNombre.add(new Label("Introduce nombre:"));
        dlgNombre.add(txtNombre);
        dlgNombre.add(btnAceptarNombre);
        dlgNombre.setSize(300, 150);
        dlgNombre.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dlgNombre.setVisible(false); } });

        dlgError = new Dialog(ventanaJuego, "Error", true);
        dlgError.setLayout(new FlowLayout());
        dlgError.add(new Label("Movimiento inválido"));
        Button btnOk = new Button("Aceptar");
        btnOk.addActionListener(e -> dlgError.setVisible(false));
        dlgError.add(btnOk);
        dlgError.setSize(200, 120);
        dlgError.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dlgError.setVisible(false); } });

        dlgRobar = new Dialog(ventanaJuego, "Carta Robada", true);
        dlgRobar.setLayout(new FlowLayout());
        lblCartaRobada = new Label("Has robado: ");
        Button btnOkRobar = new Button("Aceptar");
        btnOkRobar.addActionListener(e -> dlgRobar.setVisible(false));
        dlgRobar.add(lblCartaRobada);
        dlgRobar.add(btnOkRobar);
        dlgRobar.setSize(250, 120);
        dlgRobar.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dlgRobar.setVisible(false); } });

        dlgGanador = new Dialog(ventanaJuego, "¡Fin de la Partida!", true);
        dlgGanador.setLayout(new FlowLayout());
        lblGanadorMensaje = new Label("");
        Button btnOkGanador = new Button("Aceptar");
        btnOkGanador.addActionListener(e -> dlgGanador.setVisible(false));
        dlgGanador.add(lblGanadorMensaje);
        dlgGanador.add(btnOkGanador);
        dlgGanador.setSize(300, 120);
        dlgGanador.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dlgGanador.setVisible(false); } });

        // JUEGO 
        ventanaJuego = new Frame("Partida en curso");
        ventanaJuego.setLayout(new BorderLayout());
        Panel pN = new Panel(new GridLayout(2,1));
        lblTurno = new Label("Turno..."); lblMesa = new Label("Mesa...");
        pN.add(lblTurno); pN.add(lblMesa);
        ventanaJuego.add(pN, BorderLayout.NORTH);
        
        panelCartas = new Panel(new FlowLayout());
        ventanaJuego.add(panelCartas, BorderLayout.CENTER);
        
        btnRobar = new Button("Robar Carta");
        ventanaJuego.add(btnRobar, BorderLayout.SOUTH);
        ventanaJuego.setSize(600, 400);
        ventanaJuego.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { ventanaJuego.setVisible(false); } });
    }
}