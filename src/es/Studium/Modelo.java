package es.Studium;

import java.util.ArrayList;
import java.util.Collections;
import java.sql.*;

public class Modelo 
{
    private ArrayList<String> baraja;
    private ArrayList<ArrayList<String>> manos;
    private String cartaMesa;
    private final String url = "jdbc:mysql://localhost:3306/uno_bd";
    private final String user = "usuario_uno";
    private final String pass = "1234";

    public Modelo() 
    {
        this.baraja = new ArrayList<>();
        this.manos = new ArrayList<>();
    }

    public void iniciarPartida(int numJugadores) 
    {
        baraja.clear(); manos.clear();
        String[] colores = {"Rojo", "Amarillo", "Verde", "Azul"};
        for (String col : colores)
        {
            for (int i = 0; i <= 9; i++) baraja.add(col + " " + i);
        }
        Collections.shuffle(baraja);
        for (int i = 0; i < numJugadores; i++) 
        {
            ArrayList<String> mano = new ArrayList<>();
            for (int j = 0; j < 7; j++) mano.add(baraja.remove(0));
            manos.add(mano);
        }
        cartaMesa = baraja.remove(0);
    }

    public String robarCarta(int jugador) 
    {
        if (baraja.isEmpty()) 
        {
            String[] colores = {"Rojo", "Amarillo", "Verde", "Azul"};
            for (String col : colores) 
            {
                for (int i = 0; i <= 9; i++) baraja.add(col + " " + i);
            }
            Collections.shuffle(baraja);
        }
        String c = baraja.remove(0);
        manos.get(jugador).add(c);
        return c;
    }

    public boolean jugarCarta(int jugador, int indice)
    {
        String elegida = manos.get(jugador).get(indice);
        String[] pMesa = cartaMesa.split(" ");
        String[] pElegida = elegida.split(" ");
        if (pElegida[0].equals(pMesa[0]) || pElegida[1].equals(pMesa[1])) 
        {
            cartaMesa = elegida;
            manos.get(jugador).remove(indice);
            return true;
        }
        return false;
    }

    public void guardarRanking(String nombre) 
    { 
        String sql = "INSERT INTO ranking (nombreJugador, puntuacionJugador) VALUES (?, 1) " +
                     "ON DUPLICATE KEY UPDATE puntuacionJugador = puntuacionJugador + 1";
        
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement st = con.prepareStatement(sql)) 
        {
            st.setString(1, nombre);
            st.executeUpdate();
        } 
        catch (Exception e) { e.printStackTrace(); }
    }

    public String obtenerRanking()
    {
        StringBuilder sb = new StringBuilder("TOP 10 JUGADORES:\n");
        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT nombreJugador, puntuacionJugador FROM ranking ORDER BY puntuacionJugador DESC LIMIT 10")) {
            while (rs.next())
            {
                sb.append(rs.getString("nombreJugador")).append(" -> ")
                  .append(rs.getInt("puntuacionJugador")).append(" victorias\n");
            }
        }
        catch (Exception e) { return "Error al cargar ranking."; }
        return sb.toString();
    }
    
    public String getCartaMesa() { return cartaMesa; }
    public ArrayList<String> getMano(int jugador) { return manos.get(jugador); }
}