package es.Studium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class Modelo {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    // --- LÍNEA CORREGIDA CON EL PERMISO DE LLAVE PÚBLICA ---
    private static final String URL = "jdbc:mysql://localhost:3306/ranking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "usuario_uno";
    private static final String CLAVE = "1234";

    private ArrayList<ArrayList<String>> manos;
    private ArrayList<String> mazo;
    private String cartaMesa;

    public Modelo() {
        manos = new ArrayList<>();
        mazo = new ArrayList<>();
    }

    private Connection conectar() {
        Connection con = null;
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión a MySQL: " + e.getMessage());
        }
        return con;
    }

    public String obtenerRanking() {
        StringBuilder sb = new StringBuilder();
        sb.append("RANKING DE JUGADORES:\n");
        sb.append("====================\n\n");
        
        Connection con = conectar();
        if (con == null) {
            return sb.append("Error: No se pudo conectar a la base de datos. Revisa la consola.").toString();
        }
        
        String sql = "SELECT nombreJugador, victoriasJugador FROM jugadores ORDER BY victoriasJugador DESC";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int puesto = 1;
            while (rs.next()) {
                sb.append(puesto).append(". ")
                  .append(rs.getString("nombreJugador"))
                  .append(" - ")
                  .append(rs.getInt("victoriasJugador"))
                  .append(" victorias\n");
                puesto++;
            }
        } catch (SQLException e) {
            sb.append("Error al cargar el ranking: ").append(e.getMessage());
        } finally {
            try { con.close(); } catch (Exception e) {}
        }
        return sb.toString();
    }

    public void guardarRanking(String nombre) {
        Connection con = conectar();
        if (con == null) return;

        String sqlExiste = "SELECT victoriasJugador FROM jugadores WHERE nombreJugador = ?";
        String sqlInsertar = "INSERT INTO jugadores (nombreJugador, victoriasJugador) VALUES (?, 1)";
        String sqlActualizar = "UPDATE jugadores SET victoriasJugador = victoriasJugador + 1 WHERE nombreJugador = ?";

        try (PreparedStatement psExiste = con.prepareStatement(sqlExiste)) {
            psExiste.setString(1, nombre);
            try (ResultSet rs = psExiste.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement psActualizar = con.prepareStatement(sqlActualizar)) {
                        psActualizar.setString(1, nombre);
                        psActualizar.executeUpdate();
                    }
                } else {
                    try (PreparedStatement psInsertar = con.prepareStatement(sqlInsertar)) {
                        psInsertar.setString(1, nombre);
                        psInsertar.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar en la base de datos: " + e.getMessage());
        } finally {
            try { con.close(); } catch (Exception e) {}
        }
    }

    // --- El resto de métodos del juego (iniciarPartida, robarCarta, jugarCarta, etc.) se quedan exactamente igual ---
    public void iniciarPartida(int numJugadores) {
        mazo.clear();
        manos.clear();
        String[] colores = {"Rojo", "Amarillo", "Verde", "Azul"};
        for (String c : colores) {
            for (int i = 0; i <= 9; i++) {
                mazo.add(c + " " + i);
                if (i != 0) mazo.add(c + " " + i);
            }
        }
        Collections.shuffle(mazo);
        for (int i = 0; i < numJugadores; i++) {
            ArrayList<String> manoJugador = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                manoJugador.add(mazo.remove(0));
            }
            manos.add(manoJugador);
        }
        cartaMesa = mazo.remove(0);
    }

    public void robarCarta(int jugador) {
        if (mazo.isEmpty()) {
            iniciarPartida(manos.size()); 
            return;
        }
        manos.get(jugador).add(mazo.remove(0));
    }

    public boolean jugarCarta(int jugador, int indiceCarta) {
        String cartaA_Jugar = manos.get(jugador).get(indiceCarta);
        String[] partesMesa = cartaMesa.split(" ");
        String[] partesJugar = cartaA_Jugar.split(" ");
        if (partesMesa[0].equals(partesJugar[0]) || partesMesa[1].equals(partesJugar[1])) {
            cartaMesa = cartaA_Jugar;
            manos.get(jugador).remove(indiceCarta);
            return true;
        }
        return false;
    }

    public String getCartaMesa() { return cartaMesa; }
    public ArrayList<String> getMano(int jugador) { return manos.get(jugador); }
}