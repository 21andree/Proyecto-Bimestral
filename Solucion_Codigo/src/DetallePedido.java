

public class DetallePedido {
    private Plato plato;
    private Restaurante restaurante;
    private int cantidad;
    private double subtotal;

    public DetallePedido(Plato plato, Restaurante restaurante, int cantidad) {
        this.plato = plato;
        this.restaurante = restaurante;
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public Plato getPlato() {
        return plato;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }



    public void calcularSubtotal(){
        this.subtotal=(plato.getPrecio()*cantidad);
    }

}
