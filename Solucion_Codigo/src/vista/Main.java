package vista;

import controlador.Gestor;
import controlador.Repositorio;
import modelo.Cliente;
import modelo.DetallePedido;
import modelo.Ingrediente;
import modelo.PatioComidas;
import modelo.Pedido;
import modelo.Plato;
import modelo.Restaurante;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Repositorio repositorio = new Repositorio();
        PatioComidas patio = repositorio.cargar();
        if (patio == null) {
            double[] rangos = {2.0, 5.0, 10.0};
            double[] costos = {1.50, 2.50, 4.00};
            patio = new PatioComidas("Patio de Comidas UTPL", 0.15, rangos, costos);
            System.out.println("No se encontro archivo previo. Se creo un patio nuevo.");
        } else {
            System.out.println("Datos cargados desde archivo.");
        }

        Gestor gestor = new Gestor(patio);

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    crearRestaurante(gestor);
                    break;
                case 2:
                    crearCliente(gestor);
                    break;
                case 3:
                    registrarPedido(gestor, patio);
                    break;
                case 4:
                    verReportes(gestor);
                    break;
                case 5:
                    listarRestaurantes(patio);
                    break;
                case 0:
                    repositorio.guardar(patio);
                    System.out.println("Datos guardados. Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== PATIO DE COMIDAS =====");
        System.out.println("1. Crear restaurante");
        System.out.println("2. Crear cliente");
        System.out.println("3. Registrar pedido");
        System.out.println("4. Ver reportes");
        System.out.println("5. Listar restaurantes y sus platos");
        System.out.println("0. Salir");
    }

    private static void crearRestaurante(Gestor gestor) {
        System.out.print("Nombre del restaurante: ");
        String nombre = sc.nextLine();
        System.out.print("Tipo de comida: ");
        String tipo = sc.nextLine();
        Restaurante r = gestor.crearRestaurante(nombre, tipo);
        System.out.println("Restaurante creado: " + r.getNombre());

        int sub;
        do {
            System.out.println();
            System.out.println("-- " + r.getNombre() + " --");
            System.out.println("1. Agregar ingrediente al inventario");
            System.out.println("2. Agregar plato (con receta)");
            System.out.println("0. Terminar");
            sub = leerEntero("Opcion: ");
            switch (sub) {
                case 1:
                    agregarIngrediente(r);
                    break;
                case 2:
                    agregarPlato(r);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (sub != 0);
    }

    private static void agregarIngrediente(Restaurante r) {
        System.out.print("Nombre del ingrediente: ");
        String nombre = sc.nextLine();
        double cant = leerDouble("Cantidad disponible: ");
        r.agregarIngrediente(new Ingrediente(nombre, cant));
        System.out.println("Ingrediente agregado.");
    }

    private static void agregarPlato(Restaurante r) {
        System.out.print("Nombre del plato: ");
        String nombre = sc.nextLine();
        double precio = leerDouble("Precio: ");
        System.out.print("Categoria: ");
        String cat = sc.nextLine();
        Plato p = new Plato(nombre, precio, cat);

        System.out.println("Ingrese la receta (deje el nombre vacio para terminar):");
        while (true) {
            System.out.print("  Nombre del ingrediente: ");
            String ing = sc.nextLine();
            if (ing.isEmpty()) {
                break;
            }
            double c = leerDouble("  Cantidad por porcion: ");
            p.agregarIngredientesReceta(ing, c);
        }
        r.agregarPlato(p);
        System.out.println("Plato agregado: " + p.getNombre());
    }

    private static void crearCliente(Gestor gestor) {
        System.out.print("Cedula: ");
        String ced = sc.nextLine();
        System.out.print("Nombre: ");
        String nom = sc.nextLine();
        System.out.print("Direccion: ");
        String dir = sc.nextLine();
        System.out.print("Sector: ");
        String sec = sc.nextLine();
        double km = leerDouble("Distancia (km): ");
        gestor.crearCliente(ced, nom, dir, sec, km);
        System.out.println("Cliente creado.");
    }

    private static void registrarPedido(Gestor gestor, PatioComidas patio) {
        if (patio.getClientes().isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        if (patio.getRestaurantes().isEmpty()) {
            System.out.println("No hay restaurantes registrados.");
            return;
        }

        System.out.print("Cedula del cliente: ");
        String ced = sc.nextLine();
        Cliente cliente = patio.buscarCliente(ced);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Fecha del pedido (YYYY-MM-DD): ");
        String fecha = sc.nextLine();

        int numero = patio.getPedidos().size() + 1;
        double delivery = patio.calcularCostoDelivery(cliente.getDistanciaKm());
        Pedido pedido = new Pedido(numero, fecha, cliente, delivery);

        int sub;
        do {
            System.out.println();
            System.out.println("1. Agregar plato al pedido");
            System.out.println("0. Terminar y registrar");
            sub = leerEntero("Opcion: ");
            if (sub == 1) {
                agregarDetalleAlPedido(pedido, patio);
            }
        } while (sub != 0);

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("Pedido vacio. Cancelado.");
            return;
        }
        gestor.registrarPedido(pedido);
        System.out.printf("Pedido #%d registrado. Total: $%.2f%n", pedido.getNumero(), pedido.getTotal());
    }

    private static void agregarDetalleAlPedido(Pedido pedido, PatioComidas patio) {
        System.out.print("Nombre del restaurante: ");
        String nomR = sc.nextLine();
        Restaurante r = patio.buscarRestaurante(nomR);
        if (r == null) {
            System.out.println("Restaurante no encontrado.");
            return;
        }
        if (r.getPlatos().isEmpty()) {
            System.out.println("Este restaurante no tiene platos.");
            return;
        }
        System.out.print("Nombre del plato: ");
        String nomP = sc.nextLine();
        Plato p = r.buscarPlato(nomP);
        if (p == null) {
            System.out.println("Plato no encontrado.");
            return;
        }
        int cant = leerEntero("Cantidad: ");
        if (cant <= 0) {
            System.out.println("Cantidad invalida.");
            return;
        }
        if (!r.hayStockSuficiente(p, cant)) {
            System.out.println("Stock insuficiente para este plato.");
            return;
        }
        pedido.agregarDetalle(new DetallePedido(p, r, cant));
        System.out.println("Plato agregado al pedido.");
    }

    private static void verReportes(Gestor gestor) {
        System.out.print("Fecha del reporte (YYYY-MM-DD): ");
        String fecha = sc.nextLine();

        Restaurante mayor = gestor.obtenerRestauranteMayorFacturacion(fecha);
        if (mayor == null) {
            System.out.println("No hay ventas en esa fecha.");
        } else {
            System.out.println("Restaurante con mayor facturacion: " + mayor.getNombre());
        }

        Plato estrella = gestor.obtenerPlatoEstrella(fecha);
        if (estrella == null) {
            System.out.println("No hay plato estrella en esa fecha.");
        } else {
            System.out.println("Plato estrella: " + estrella.getNombre());
        }

        System.out.println();
        System.out.println("== Inventario actual ==");
        String inv = gestor.reporteInventarioActual();
        if (inv.isEmpty()) {
            System.out.println("(sin restaurantes)");
        } else {
            System.out.print(inv);
        }
    }

    private static void listarRestaurantes(PatioComidas patio) {
        if (patio.getRestaurantes().isEmpty()) {
            System.out.println("No hay restaurantes registrados.");
            return;
        }
        for (int i = 0; i < patio.getRestaurantes().size(); i++) {
            Restaurante r = patio.getRestaurantes().get(i);
            System.out.println();
            System.out.println("- " + r.getNombre() + " (" + r.getTipoComida() + ")");
            if (r.getPlatos().isEmpty()) {
                System.out.println("    (sin platos)");
            } else {
                for (int j = 0; j < r.getPlatos().size(); j++) {
                    Plato p = r.getPlatos().get(j);
                    System.out.printf("    * %s  $%.2f  [%s]%n", p.getNombre(), p.getPrecio(), p.getCategoria());
                }
            }
        }
    }

    private static int leerEntero(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Ingrese un numero entero.");
            }
        }
    }

    private static double leerDouble(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine();
            try {
                return Double.parseDouble(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Ingrese un numero.");
            }
        }
    }
}
