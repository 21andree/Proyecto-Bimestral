
import java.util.ArrayList;
public class Plato {
    private String nombre;
    private double precio;
    private String categoria;
    private ArrayList<String> ingredientesReceta;
    private ArrayList<Double> cantidadesReceta;

    public Plato(String nombre, double precio, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.ingredientesReceta = new ArrayList<>();
        this.cantidadesReceta = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public ArrayList<String> getIngredientesReceta() {
        return ingredientesReceta;
    }

    public ArrayList<Double> getCantidadesReceta() {
        return cantidadesReceta;
    }




    public void agregarIngredientesReceta(String nombre,double cant){
        ingredientesReceta.add(nombre);
        cantidadesReceta.add(cant);
    }


}
