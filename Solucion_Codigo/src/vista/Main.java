package vista;

import modelo.*;

public class Main {

    public static void main(String[] args) {

        double[] rangos = {2.0, 5.0, 10.0};
        double[] costos = {1.50, 2.50, 4.00};
        PatioComidas patio = new PatioComidas("Patio UTPL", 0.15, rangos, costos);


        Restaurante r1 = new Restaurante("La Casa del Sabor", "Comida tipica");

        Ingrediente papa   = new Ingrediente("Papa", 50);
        Ingrediente queso  = new Ingrediente("Queso", 20);
        Ingrediente pollo  = new Ingrediente("Pollo", 30);
        Ingrediente arroz  = new Ingrediente("Arroz", 40);

        r1.agregarIngrediente(papa);
        r1.agregarIngrediente(queso);
        r1.agregarIngrediente(pollo);
        r1.agregarIngrediente(arroz);

        Plato llapingachos = new Plato("Llapingachos", 4.50, "Plato fuerte");
        llapingachos.agregarIngredientesReceta("Papa", 0.3);
        llapingachos.agregarIngredientesReceta("Queso", 0.1);

        Plato secoPollo = new Plato("Seco de pollo", 5.00, "Plato fuerte");
        secoPollo.agregarIngredientesReceta("Pollo", 0.25);
        secoPollo.agregarIngredientesReceta("Arroz", 0.2);

        r1.agregarPlato(llapingachos);
        r1.agregarPlato(secoPollo);

        patio.agregarRestaurante(r1);


        Cliente c1 = new Cliente("1104567890", "Andre Macas", "Av. Pio Jaramillo", "Norte", 3.5);
        patio.agregarCliente(c1);


        double costoDelivery = patio.calcularCostoDelivery(c1.getDistanciaKm());
        Pedido pedido = new Pedido(1, "2026-05-19", c1, costoDelivery);


        if (r1.hayStockSuficiente(llapingachos, 2)) {
            DetallePedido d1 = new DetallePedido(llapingachos, r1, 2);
            pedido.agregarDetalle(d1);
            r1.descontarIngredientes(llapingachos, 2);
        }

        if (r1.hayStockSuficiente(secoPollo, 1)) {
            DetallePedido d2 = new DetallePedido(secoPollo, r1, 1);
            pedido.agregarDetalle(d2);
            r1.descontarIngredientes(secoPollo, 1);
        }


        pedido.calcularSubtotal();
        pedido.calcularIva(patio.getIva());
        pedido.calcularTotal();

        patio.agregarPedido(pedido);


        System.out.println("=========================================");
        System.out.println("   " + patio.getNombre());
        System.out.println("=========================================");
        System.out.println("Pedido #" + pedido.getNumero() + "   Fecha: " + pedido.getFecha());
        System.out.println("Cliente: " + pedido.getCliente().getNombre() + " (" + pedido.getCliente().getCedula() + ")");
        System.out.println("Sector: " + pedido.getCliente().getSector() + "   Distancia: " + pedido.getCliente().getDistanciaKm() + " km");
        System.out.println("-----------------------------------------");
        System.out.println("Detalles:");
        for (int i = 0; i < pedido.getDetalles().size(); i++) {
            DetallePedido d = (DetallePedido) pedido.getDetalles().get(i);
            System.out.println("  - " + d.getCantidad() + " x " + d.getPlato().getNombre()
                    + " ($" + d.getPlato().getPrecio() + ")  =  $" + d.getSubtotal()
                    + "   [" + d.getRestaurante().getNombre() + "]");
        }
        System.out.println("-----------------------------------------");
        System.out.printf("Subtotal:        $%.2f%n", pedido.getSubtotal());
        System.out.printf("IVA (%.0f%%):       $%.2f%n", patio.getIva() * 100, pedido.getMontoIva());
        System.out.printf("Delivery:        $%.2f%n", pedido.getCostoDelivery());
        System.out.printf("TOTAL:           $%.2f%n", pedido.getTotal());
        System.out.println("Estado: " + pedido.getEstado());
        System.out.println("=========================================");


        System.out.println("\nStock restante en " + r1.getNombre() + ":");
        for (int i = 0; i < r1.getIngredientes().size(); i++) {
            Ingrediente ing = r1.getIngredientes().get(i);
            System.out.println("  " + ing.getNombre() + ": " + ing.getCantidadDisponible());
        }
    }
}
