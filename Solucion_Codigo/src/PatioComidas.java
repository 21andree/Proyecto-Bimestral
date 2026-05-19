
import java.util.ArrayList;

public class PatioComidas {

    private String nombre;
    private ArrayList<Restaurante> restaurantes;
    private ArrayList<Cliente> clientes;
    private ArrayList<Pedido> pedidos;
    private double iva;
    private double[] rangosKm;
    private double[] costosDelivery;

    public PatioComidas(String nombre, double iva, double[] rangosKm, double[] costosDelivery) {
        this.nombre = nombre;
        this.iva = iva;
        this.rangosKm = rangosKm;
        this.costosDelivery = costosDelivery;
        this.restaurantes = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public void agregarRestaurante(Restaurante restaurante) {
        restaurantes.add(restaurante);
    }

    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public Restaurante buscarRestaurante(String nombre) {
        for (int i = 0; i < restaurantes.size(); i++) {
            if (restaurantes.get(i).getNombre().equalsIgnoreCase(nombre)) {
                return restaurantes.get(i);
            }
        }
        return null;
    }

    public Cliente buscarCliente(String cedula) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCedula().equalsIgnoreCase(cedula)) {
                return clientes.get(i);
            }
        }
        return null;
    }

    public double calcularCostoDelivery(double km) {
        for (int i = 0; i < rangosKm.length; i++) {
            if (km <= rangosKm[i]) {
                return costosDelivery[i];
            }
        }
        return costosDelivery[costosDelivery.length - 1];
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Restaurante> getRestaurantes() {
        return restaurantes;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public double getIva() {
        return iva;
    }

    public double[] getRangosKm() {
        return rangosKm;
    }

    public double[] getCostosDelivery() {
        return costosDelivery;
    }
    
    

}
