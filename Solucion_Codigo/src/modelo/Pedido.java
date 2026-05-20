package modelo;

import java.util.ArrayList;

public class Pedido {

    private int numero;
    private String fecha;
    private Cliente cliente;
    private ArrayList<DetallePedido> detalles;
    private double subtotal;
    private double montoIva;
    private double costoDelivery;
    private double total;
    private String estado;

    public Pedido(int numero, String fecha, Cliente cliente, double costoDelivery) {
        this.numero = numero;
        this.fecha = fecha;
        this.cliente = cliente;
        this.costoDelivery = costoDelivery;
        this.detalles = new ArrayList<>();
        this.subtotal = 0;
        this.montoIva = 0;
        this.total = 0;
        this.estado = "pendiente";
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    public void calcularSubtotal() {
        for (int i = 0; i < detalles.size(); i++) {
            subtotal += detalles.get(i).getSubtotal();
        }
    }

    public void calcularIva(double porcentaje) {
        montoIva = subtotal * porcentaje;
    }
    
    public void calcularTotal() {
        total = subtotal + montoIva + costoDelivery;
    }

    public int getNumero() {
        return numero;
    }

    public String getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList getDetalles() {
        return detalles;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getMontoIva() {
        return montoIva;
    }

    public double getCostoDelivery() {
        return costoDelivery;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    

}
