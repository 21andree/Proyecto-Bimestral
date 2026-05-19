
import java.util.ArrayList;

public class Restaurante {
    private String nombre;
    private String tipoComida;
    private ArrayList<Plato> platos;
    private ArrayList<Ingrediente> ingredientes;

   public Restaurante(String nombre, String tipoComida) {
       this.nombre = nombre;
       this.tipoComida = tipoComida;
       this.platos = new ArrayList<>();
       this.ingredientes = new ArrayList<>();
    }


    public String getNombre() {
        return nombre;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public ArrayList<Plato> getPlatos() {
        return platos;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }


    public void agregarPlato(Plato p){
        platos.add(p);
    }
    public void agregarIngrediente(Ingrediente i){
        ingredientes.add(i);
    }
    public Plato buscarPlato(String nombre){
        for(int i=0;i<platos.size();i++){
            if (platos.get(i).getNombre().equalsIgnoreCase(nombre)){
                return platos.get(i);
            }
        }
        return null;
    }
    public Ingrediente buscarIngrediente(String nombre){
        for(int i=0;i<ingredientes.size();i++){
            if (ingredientes.get(i).getNombre().equalsIgnoreCase(nombre)){
                return ingredientes.get(i);
            }
        }
        return null;
    }
    public boolean hayStockSuficiente(Plato p,int cant){
        String nomI;
        Ingrediente ingInv;
        double canti, cantin;

        for (int i = 0; i < p.getIngredientesReceta().size(); i++) {
            nomI  = p.getIngredientesReceta().get(i);
            canti = p.getCantidadesReceta().get(i);
            cantin = canti * cant;

            ingInv = this.buscarIngrediente(nomI);

            if (ingInv == null) {
                return false;
            }

            if (ingInv.getCantidadDisponible() < cantin) {
                return false;
            }
        }
        return true;
    }
    public void descontarIngredientes(Plato p,int cant){
        String nomI;
        Ingrediente ingInv;
        double canti, cantin;
        for (int i = 0; i < p.getIngredientesReceta().size(); i++) {
            nomI  = p.getIngredientesReceta().get(i);
            canti = p.getCantidadesReceta().get(i);
            ingInv = this.buscarIngrediente(nomI);
            cantin = canti * cant;
            if (ingInv == null) {
                return;
            }
            ingInv.descontar(cantin);
        }
    }
    public void reponerIngrediente(String nombre,double cant){
        Ingrediente in;
        in=this.buscarIngrediente(nombre);
        if (in==null){
            return;
        }
        in.reponer(cant);
    }



}
