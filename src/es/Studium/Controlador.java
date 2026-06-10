package es.Studium;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Controlador implements ActionListener {
    private Modelo m;
    private Vista v;
    private int numJugadores;
    private int turno = 0;
    private String[] nombres;
    private int jugadorActualInput = 0;

    public Controlador(Modelo m, Vista v) {
        this.m = m;
        this.v = v;

        v.ventanaMenu.setVisible(true);

        v.btnNueva.addActionListener(this);
        v.btnRobar.addActionListener(this);
        v.btnRanking.addActionListener(this);
        v.btnAyuda.addActionListener(this); 
        v.btnSalir.addActionListener(e -> System.exit(0));
        
        v.btn2.addActionListener(this);
        v.btn3.addActionListener(this);
        v.btn4.addActionListener(this);
        
        v.btnAceptarNombre.addActionListener(e -> {
            nombres[jugadorActualInput] = v.txtNombre.getText();
            v.txtNombre.setText(""); 
            v.dlgNombre.setVisible(false);
        });
    }

    private Color getColor(String textoCarta) {
        if (textoCarta.contains("Rojo")) return Color.RED;
        if (textoCarta.contains("Amarillo")) return Color.ORANGE; 
        if (textoCarta.contains("Verde")) return Color.GREEN;
        if (textoCarta.contains("Azul")) return Color.CYAN; 
        return Color.BLACK;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == v.btnNueva) {
            v.ventanaSeleccion.setVisible(true);
        } else if (e.getSource() == v.btn2 || e.getSource() == v.btn3 || e.getSource() == v.btn4) {
            if (e.getSource() == v.btn2) numJugadores = 2;
            else if (e.getSource() == v.btn3) numJugadores = 3;
            else numJugadores = 4;
            
            nombres = new String[numJugadores];
            jugadorActualInput = 0;
            pedirNombre(); 
        } else if (e.getSource() == v.btnRobar) {
            m.robarCarta(turno);
            
            ArrayList<String> manoActual = m.getMano(turno);
            String cartaRobada = manoActual.get(manoActual.size() - 1);
            v.lblCartaRobada.setText("Has robado: " + cartaRobada);
            v.lblCartaRobada.setForeground(getColor(cartaRobada));
            v.dlgRobar.setVisible(true); // Bloquea hasta que aceptan
            
            turno = (turno + 1) % numJugadores;
            actualizarPantallaJuego();
        } else if (e.getSource() == v.btnRanking) {
            v.txtRanking.setText(m.obtenerRanking());
            v.dlgRanking.setVisible(true);
        } else if (e.getSource() == v.btnAyuda) {
            try {
                Runtime.getRuntime().exec("hh.exe manual_uno\\AyudaUno.chm");
            } catch (Exception ex) {
                System.out.println("Error al abrir el archivo de ayuda: " + ex.getMessage());
            }
        }
    }

    private void pedirNombre() {
        if (jugadorActualInput < numJugadores) {
            v.dlgNombre.setTitle("Jugador " + (jugadorActualInput + 1));
            v.dlgNombre.setVisible(true); 
            jugadorActualInput++;
            pedirNombre(); 
        } else {
            v.ventanaSeleccion.setVisible(false);
            m.iniciarPartida(numJugadores);
            v.ventanaJuego.setVisible(true);
            actualizarPantallaJuego();
        }
    }

    private void actualizarPantallaJuego() {
        v.lblTurno.setText("Turno: " + nombres[turno]);
        
        String cartaMesa = m.getCartaMesa();
        v.lblMesa.setText("Mesa: " + cartaMesa);
        v.lblMesa.setForeground(getColor(cartaMesa)); 

        v.panelCartas.removeAll();

        ArrayList<String> manoActual = m.getMano(turno);
        for (int i = 0; i < manoActual.size(); i++) {
            final int index = i;
            String textoCarta = manoActual.get(i);
            Button btn = new Button(textoCarta);
            
            btn.setBackground(getColor(textoCarta));

            btn.addActionListener(ev -> {
                if (m.jugarCarta(turno, index)) {
                    if (m.getMano(turno).isEmpty()) {
                        m.guardarRanking(nombres[turno]);
                        
                        v.lblGanadorMensaje.setText("¡" + nombres[turno] + " ha ganado la partida!");
                        v.dlgGanador.setVisible(true); 
                        
                        v.ventanaJuego.setVisible(false);
                    } else {
                        turno = (turno + 1) % numJugadores;
                        actualizarPantallaJuego();
                    }
                } else {
                    v.dlgError.setVisible(true);
                }
            });
            v.panelCartas.add(btn);
        }
        v.panelCartas.validate();
        v.panelCartas.repaint();
    }
}