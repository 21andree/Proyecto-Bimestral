package modelo;

import java.io.Serializable;

public class Ingrediente implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private double cantidadDisponible;

    public Ingrediente(String nombre, double cantidadDisponible) {
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
    }

    public void descontar(double cant) {
        cantidadDisponible -= cant;
    }

    public void reponer(double cant) {
        cantidadDisponible += cant;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(double cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    

}
