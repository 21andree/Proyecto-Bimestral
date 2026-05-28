package controlador;

import modelo.*;
import java.util.ArrayList;

public class Gestor {

    private PatioComidas Patio;

    public Gestor(PatioComidas Patio) {
        this.Patio = Patio;
    }

    public Restaurante obtenerRestauranteMayorFacturacion(String fecha) {
        Restaurante res;
        double subtotal;
        ArrayList<Restaurante> listaRestaurantes = new ArrayList<>();
        ArrayList<Double> listaTotales = new ArrayList<>();

        for (int i = 0; i < Patio.getPedidos().size(); i++) {
            Pedido pedido = Patio.getPedidos().get(i);

            if (!pedido.getFecha().equals(fecha)) {
                continue;
            }
            for (int j = 0; j < pedido.getDetalles().size(); j++) {
                DetallePedido detalle = pedido.getDetalles().get(j);
                res = detalle.getRestaurante();
                subtotal = detalle.getSubtotal();
                boolean encontrado = false;
                for (int k = 0; k < listaRestaurantes.size(); k++) {
                    if (listaRestaurantes.get(k).getNombre().equals(res.getNombre())) {
                        listaTotales.set(k, listaTotales.get(k) + subtotal);
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    listaRestaurantes.add(res);
                    listaTotales.add(subtotal);
                }
            }
        }

        if (listaTotales.isEmpty()) {
            return null;
        }
        double mayor = listaTotales.get(0);
        int in = 0;
        for (int l = 0; l < listaTotales.size(); l++) {
            if (listaTotales.get(l) > mayor) {
                mayor = listaTotales.get(l);
                in = l;
            }
        }
        return listaRestaurantes.get(in);
    }

    public Plato obtenerPlatoEstrella(String fecha) {
        Plato plato;
        int cant;
        ArrayList<Plato> listaPlatos = new ArrayList<>();
        ArrayList<Integer> listacant = new ArrayList<>();
        for (int i = 0; i < Patio.getPedidos().size(); i++) {
            Pedido pedido = Patio.getPedidos().get(i);
            if (!pedido.getFecha().equals(fecha)) {
                continue;
            }
            for (int j = 0; j < pedido.getDetalles().size(); j++) {
                DetallePedido detalle = pedido.getDetalles().get(j);
                plato = detalle.getPlato();
                cant = detalle.getCantidad();
                boolean encontrado = false;
                for (int k = 0; k < listaPlatos.size(); k++) {
                    if (listaPlatos.get(k).getNombre().equals(plato.getNombre())) {
                        listacant.set(k, listacant.get(k) + cant);
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    listaPlatos.add(plato);
                    listacant.add(cant);
                }
            }
        }
        if (listaPlatos.isEmpty()) {
            return null;
        }
        int mayor = listacant.get(0);
        int in = 0;
        for (int l = 0; l < listacant.size(); l++) {
            if (listacant.get(l) > mayor) {
                mayor = listacant.get(l);
                in = l;

            }
        }
        return listaPlatos.get(in);
    }

    public String reporteInventarioActual() {
        String inventariodisp = "";
        String nombreres;
        for (int i = 0; i < Patio.getRestaurantes().size(); i++) {
            Restaurante restaurante = Patio.getRestaurantes().get(i);
            nombreres = restaurante.getNombre();
            inventariodisp = inventariodisp + "=== " + nombreres + " ===\n";
            for (int j = 0; j < restaurante.getIngredientes().size(); j++) {
                Ingrediente ingrediente = restaurante.getIngredientes().get(j);
                String ing = "Nombre :" + ingrediente.getNombre() + ", Cantidad :" + String.valueOf(ingrediente.getCantidadDisponible());
                inventariodisp = inventariodisp + ing + "\n";
            }
            inventariodisp = inventariodisp + "\n";
        }
        return inventariodisp;
    }

    public void registrarPedido(Pedido p) {
        for (int i = 0; i < p.getDetalles().size(); i++) {
            Restaurante res = p.getDetalles().get(i).getRestaurante();
            res.descontarIngredientes(p.getDetalles().get(i).getPlato(), p.getDetalles().get(i).getCantidad());
        }
        double km = p.getCliente().getDistanciaKm();
        double delivery = Patio.calcularCostoDelivery(km);
        p.setCostoDelivery(delivery);
        p.calcularSubtotal();
        p.calcularIva(Patio.getIva());
        p.calcularTotal();
        Patio.agregarPedido(p);
    }

    public Restaurante crearRestaurante(String nombre, String tipo) {
        Restaurante r = new Restaurante(nombre, tipo);
        Patio.agregarRestaurante(r);
        return r;
    }

    public void crearCliente(String cedula, String nombre, String direccion, String sector, double km) {
        Cliente c = new Cliente(cedula, nombre, direccion, sector, km);
        Patio.agregarCliente(c);
    }
}
