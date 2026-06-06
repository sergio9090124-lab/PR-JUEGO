package es.Studium;

public class Principal
{
    public static void main(String[] args) 
    {
        Modelo miModelo = new Modelo();
        Vista miVista = new Vista();
       
        new Controlador(miModelo, miVista);
    }
}