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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    private final PatioComidas patio;
    private final Gestor gestor;
    private final Repositorio repositorio;

    private JComboBox<Restaurante> cmbRestauranteAEditar;
    private JComboBox<Restaurante> cmbRestaurantesPedido;
    private JComboBox<Plato> cmbPlatosPedido;
    private JComboBox<Cliente> cmbClientesPedido;
    private DefaultTableModel modeloDetalles;
    private JTextField txtFechaPedido;
    private JLabel lblTotalPedido;
    private Pedido pedidoEnCurso;

    public VentanaPrincipal(Repositorio repositorio, PatioComidas patio, Gestor gestor) {
        this.repositorio = repositorio;
        this.patio = patio;
        this.gestor = gestor;

        setTitle("Patio de Comidas - " + patio.getNombre());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarYGuardar();
            }
        });

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Restaurantes", panelRestaurantes());
        tabs.addTab("Clientes", panelClientes());
        tabs.addTab("Pedidos", panelPedidos());
        tabs.addTab("Reportes", panelReportes());
        tabs.addTab("Listado", panelListado());
        tabs.addChangeListener(e -> refrescarCombos());

        setContentPane(tabs);
        refrescarCombos();
    }

    private void cerrarYGuardar() {
        repositorio.guardar(patio);
        JOptionPane.showMessageDialog(this, "Datos guardados. Hasta luego.");
        dispose();
        System.exit(0);
    }

    // ===================== Pestania Restaurantes =====================
    private JPanel panelRestaurantes() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pNuevo = new JPanel(new GridBagLayout());
        pNuevo.setBorder(BorderFactory.createTitledBorder("Nuevo restaurante"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 4, 2, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtNombre = new JTextField(20);
        JTextField txtTipo = new JTextField(20);
        JButton btnCrear = new JButton("Crear restaurante");

        g.gridx = 0;
        g.gridy = 0;
        pNuevo.add(new JLabel("Nombre:"), g);
        g.gridx = 1;
        pNuevo.add(txtNombre, g);
        g.gridx = 0;
        g.gridy = 1;
        pNuevo.add(new JLabel("Tipo:"), g);
        g.gridx = 1;
        pNuevo.add(txtTipo, g);
        g.gridx = 1;
        g.gridy = 2;
        pNuevo.add(btnCrear, g);

        btnCrear.addActionListener(e -> {
            String n = txtNombre.getText().trim();
            String t = txtTipo.getText().trim();
            if (n.isEmpty() || t.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete nombre y tipo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            gestor.crearRestaurante(n, t);
            txtNombre.setText("");
            txtTipo.setText("");
            JOptionPane.showMessageDialog(this, "Restaurante creado: " + n);
            refrescarCombos();
        });

        JPanel pEditar = new JPanel(new BorderLayout(6, 6));
        pEditar.setBorder(BorderFactory.createTitledBorder("Agregar ingredientes / platos a un restaurante"));

        cmbRestauranteAEditar = new JComboBox<>();
        cmbRestauranteAEditar.setRenderer(new RestauranteRenderer());

        JPanel pSel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSel.add(new JLabel("Restaurante:"));
        pSel.add(cmbRestauranteAEditar);
        pEditar.add(pSel, BorderLayout.NORTH);

        JPanel pAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnIng = new JButton("Agregar ingrediente...");
        JButton btnPlato = new JButton("Agregar plato (con receta)...");
        pAcciones.add(btnIng);
        pAcciones.add(btnPlato);
        pEditar.add(pAcciones, BorderLayout.CENTER);

        btnIng.addActionListener(e -> {
            Restaurante r = (Restaurante) cmbRestauranteAEditar.getSelectedItem();
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un restaurante.");
                return;
            }
            dialogAgregarIngrediente(r);
        });

        btnPlato.addActionListener(e -> {
            Restaurante r = (Restaurante) cmbRestauranteAEditar.getSelectedItem();
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un restaurante.");
                return;
            }
            dialogAgregarPlato(r);
        });

        p.add(pNuevo, BorderLayout.NORTH);
        p.add(pEditar, BorderLayout.CENTER);
        return p;
    }

    private void dialogAgregarIngrediente(Restaurante r) {
        JTextField txtN = new JTextField(15);
        JTextField txtC = new JTextField(8);
        JPanel form = new JPanel(new GridLayout(2, 2, 4, 4));
        form.add(new JLabel("Nombre:"));
        form.add(txtN);
        form.add(new JLabel("Cantidad disponible:"));
        form.add(txtC);
        int op = JOptionPane.showConfirmDialog(this, form,
                "Agregar ingrediente a " + r.getNombre(), JOptionPane.OK_CANCEL_OPTION);
        if (op != JOptionPane.OK_OPTION) {
            return;
        }
        String n = txtN.getText().trim();
        if (n.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre vacio.");
            return;
        }
        try {
            double c = Double.parseDouble(txtC.getText().trim());
            r.agregarIngrediente(new Ingrediente(n, c));
            JOptionPane.showMessageDialog(this, "Ingrediente agregado.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad invalida.");
        }
    }

    private void dialogAgregarPlato(Restaurante r) {
        JDialog dlg = new JDialog(this, "Agregar plato a " + r.getNombre(), true);
        dlg.setSize(520, 450);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(6, 6));

        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(BorderFactory.createTitledBorder("Datos del plato"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 4, 2, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtNom = new JTextField(15);
        JTextField txtPr = new JTextField(8);
        JTextField txtCat = new JTextField(15);
        g.gridx = 0;
        g.gridy = 0;
        pForm.add(new JLabel("Nombre:"), g);
        g.gridx = 1;
        pForm.add(txtNom, g);
        g.gridx = 0;
        g.gridy = 1;
        pForm.add(new JLabel("Precio:"), g);
        g.gridx = 1;
        pForm.add(txtPr, g);
        g.gridx = 0;
        g.gridy = 2;
        pForm.add(new JLabel("Categoria:"), g);
        g.gridx = 1;
        pForm.add(txtCat, g);

        DefaultTableModel modReceta = new DefaultTableModel(new String[]{"Ingrediente", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tblReceta = new JTable(modReceta);

        JPanel pReceta = new JPanel(new BorderLayout(4, 4));
        pReceta.setBorder(BorderFactory.createTitledBorder("Receta"));
        pReceta.add(new JScrollPane(tblReceta), BorderLayout.CENTER);

        JPanel pAddRec = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtIng = new JTextField(10);
        JTextField txtCant = new JTextField(5);
        JButton btnAddRec = new JButton("Agregar a receta");
        pAddRec.add(new JLabel("Ingrediente:"));
        pAddRec.add(txtIng);
        pAddRec.add(new JLabel("Cantidad:"));
        pAddRec.add(txtCant);
        pAddRec.add(btnAddRec);
        pReceta.add(pAddRec, BorderLayout.SOUTH);

        btnAddRec.addActionListener(e -> {
            String ni = txtIng.getText().trim();
            if (ni.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Nombre vacio.");
                return;
            }
            try {
                double c = Double.parseDouble(txtCant.getText().trim());
                modReceta.addRow(new Object[]{ni, c});
                txtIng.setText("");
                txtCant.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Cantidad invalida.");
            }
        });

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar plato");
        JButton btnCancelar = new JButton("Cancelar");
        pBotones.add(btnGuardar);
        pBotones.add(btnCancelar);

        btnCancelar.addActionListener(e -> dlg.dispose());
        btnGuardar.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String cat = txtCat.getText().trim();
            if (nom.isEmpty() || cat.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Complete nombre y categoria.");
                return;
            }
            try {
                double pr = Double.parseDouble(txtPr.getText().trim());
                Plato pl = new Plato(nom, pr, cat);
                for (int i = 0; i < modReceta.getRowCount(); i++) {
                    String ing = (String) modReceta.getValueAt(i, 0);
                    double q = (Double) modReceta.getValueAt(i, 1);
                    pl.agregarIngredientesReceta(ing, q);
                }
                r.agregarPlato(pl);
                JOptionPane.showMessageDialog(dlg, "Plato agregado: " + pl.getNombre());
                dlg.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Precio invalido.");
            }
        });

        dlg.add(pForm, BorderLayout.NORTH);
        dlg.add(pReceta, BorderLayout.CENTER);
        dlg.add(pBotones, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ===================== Pestania Clientes =====================
    private JPanel panelClientes() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Nuevo cliente"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 4, 2, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtCed = new JTextField(15);
        JTextField txtNom = new JTextField(20);
        JTextField txtDir = new JTextField(25);
        JTextField txtSec = new JTextField(15);
        JTextField txtKm = new JTextField(6);
        JButton btnCrear = new JButton("Crear cliente");

        int row = 0;
        g.gridx = 0;
        g.gridy = row;
        form.add(new JLabel("Cedula:"), g);
        g.gridx = 1;
        form.add(txtCed, g);
        row++;
        g.gridx = 0;
        g.gridy = row;
        form.add(new JLabel("Nombre:"), g);
        g.gridx = 1;
        form.add(txtNom, g);
        row++;
        g.gridx = 0;
        g.gridy = row;
        form.add(new JLabel("Direccion:"), g);
        g.gridx = 1;
        form.add(txtDir, g);
        row++;
        g.gridx = 0;
        g.gridy = row;
        form.add(new JLabel("Sector:"), g);
        g.gridx = 1;
        form.add(txtSec, g);
        row++;
        g.gridx = 0;
        g.gridy = row;
        form.add(new JLabel("Distancia (km):"), g);
        g.gridx = 1;
        form.add(txtKm, g);
        row++;
        g.gridx = 1;
        g.gridy = row;
        form.add(btnCrear, g);

        btnCrear.addActionListener(e -> {
            String c = txtCed.getText().trim();
            String n = txtNom.getText().trim();
            String d = txtDir.getText().trim();
            String s = txtSec.getText().trim();
            if (c.isEmpty() || n.isEmpty() || d.isEmpty() || s.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            try {
                double km = Double.parseDouble(txtKm.getText().trim());
                gestor.crearCliente(c, n, d, s, km);
                txtCed.setText("");
                txtNom.setText("");
                txtDir.setText("");
                txtSec.setText("");
                txtKm.setText("");
                JOptionPane.showMessageDialog(this, "Cliente creado.");
                refrescarCombos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Distancia invalida.");
            }
        });

        p.add(form, BorderLayout.NORTH);
        return p;
    }

    // ===================== Pestania Pedidos =====================
    private JPanel panelPedidos() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pTop = new JPanel(new GridBagLayout());
        pTop.setBorder(BorderFactory.createTitledBorder("Nuevo pedido"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 4, 2, 4);
        g.anchor = GridBagConstraints.WEST;

        cmbClientesPedido = new JComboBox<>();
        cmbClientesPedido.setRenderer(new ClienteRenderer());
        txtFechaPedido = new JTextField(10);
        JButton btnIniciar = new JButton("Iniciar pedido");

        g.gridx = 0;
        g.gridy = 0;
        pTop.add(new JLabel("Cliente:"), g);
        g.gridx = 1;
        pTop.add(cmbClientesPedido, g);
        g.gridx = 0;
        g.gridy = 1;
        pTop.add(new JLabel("Fecha (YYYY-MM-DD):"), g);
        g.gridx = 1;
        pTop.add(txtFechaPedido, g);
        g.gridx = 1;
        g.gridy = 2;
        pTop.add(btnIniciar, g);

        JPanel pMid = new JPanel(new GridBagLayout());
        pMid.setBorder(BorderFactory.createTitledBorder("Agregar plato al pedido"));
        GridBagConstraints gm = new GridBagConstraints();
        gm.insets = new Insets(2, 4, 2, 4);
        gm.anchor = GridBagConstraints.WEST;

        cmbRestaurantesPedido = new JComboBox<>();
        cmbRestaurantesPedido.setRenderer(new RestauranteRenderer());
        cmbPlatosPedido = new JComboBox<>();
        cmbPlatosPedido.setRenderer(new PlatoRenderer());
        JTextField txtCant = new JTextField(5);
        JButton btnAddDetalle = new JButton("Agregar al pedido");

        cmbRestaurantesPedido.addActionListener(e -> recargarPlatosDelRestauranteSeleccionado());

        gm.gridx = 0;
        gm.gridy = 0;
        pMid.add(new JLabel("Restaurante:"), gm);
        gm.gridx = 1;
        pMid.add(cmbRestaurantesPedido, gm);
        gm.gridx = 0;
        gm.gridy = 1;
        pMid.add(new JLabel("Plato:"), gm);
        gm.gridx = 1;
        pMid.add(cmbPlatosPedido, gm);
        gm.gridx = 0;
        gm.gridy = 2;
        pMid.add(new JLabel("Cantidad:"), gm);
        gm.gridx = 1;
        pMid.add(txtCant, gm);
        gm.gridx = 1;
        gm.gridy = 3;
        pMid.add(btnAddDetalle, gm);

        modeloDetalles = new DefaultTableModel(
                new String[]{"Restaurante", "Plato", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloDetalles);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 150));

        JPanel pBot = new JPanel(new BorderLayout());
        pBot.setBorder(BorderFactory.createTitledBorder("Detalles del pedido"));
        pBot.add(scroll, BorderLayout.CENTER);
        JPanel pAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalPedido = new JLabel("Total: -");
        JButton btnRegistrar = new JButton("Registrar pedido");
        pAcciones.add(lblTotalPedido);
        pAcciones.add(btnRegistrar);
        pBot.add(pAcciones, BorderLayout.SOUTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(pTop);
        center.add(pMid);
        center.add(pBot);

        p.add(center, BorderLayout.CENTER);

        btnIniciar.addActionListener(e -> {
            Cliente c = (Cliente) cmbClientesPedido.getSelectedItem();
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
                return;
            }
            String f = txtFechaPedido.getText().trim();
            if (f.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese la fecha.");
                return;
            }
            if (patio.getRestaurantes().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay restaurantes registrados.");
                return;
            }
            int numero = patio.getPedidos().size() + 1;
            double delivery = patio.calcularCostoDelivery(c.getDistanciaKm());
            pedidoEnCurso = new Pedido(numero, f, c, delivery);
            modeloDetalles.setRowCount(0);
            lblTotalPedido.setText(String.format("Subtotal: $0.00  Delivery: $%.2f", delivery));
            JOptionPane.showMessageDialog(this, "Pedido #" + numero + " iniciado. Agregue platos.");
        });

        btnAddDetalle.addActionListener(e -> {
            if (pedidoEnCurso == null) {
                JOptionPane.showMessageDialog(this, "Inicie un pedido primero.");
                return;
            }
            Restaurante r = (Restaurante) cmbRestaurantesPedido.getSelectedItem();
            Plato pl = (Plato) cmbPlatosPedido.getSelectedItem();
            if (r == null || pl == null) {
                JOptionPane.showMessageDialog(this, "Seleccione restaurante y plato.");
                return;
            }
            int cant;
            try {
                cant = Integer.parseInt(txtCant.getText().trim());
                if (cant <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad invalida.");
                return;
            }
            if (!r.hayStockSuficiente(pl, cant)) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente para este plato.",
                        "Stock", JOptionPane.WARNING_MESSAGE);
                return;
            }
            DetallePedido d = new DetallePedido(pl, r, cant);
            pedidoEnCurso.agregarDetalle(d);
            modeloDetalles.addRow(new Object[]{
                r.getNombre(), pl.getNombre(), cant,
                String.format("$%.2f", d.getSubtotal())});
            double subtotal = 0;
            for (int i = 0; i < pedidoEnCurso.getDetalles().size(); i++) {
                subtotal += pedidoEnCurso.getDetalles().get(i).getSubtotal();
            }
            lblTotalPedido.setText(String.format("Subtotal: $%.2f  Delivery: $%.2f",
                    subtotal, pedidoEnCurso.getCostoDelivery()));
            txtCant.setText("");
        });

        btnRegistrar.addActionListener(e -> {
            if (pedidoEnCurso == null) {
                JOptionPane.showMessageDialog(this, "No hay pedido iniciado.");
                return;
            }
            if (pedidoEnCurso.getDetalles().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pedido vacio.");
                return;
            }
            gestor.registrarPedido(pedidoEnCurso);
            JOptionPane.showMessageDialog(this, String.format(
                    "Pedido #%d registrado. Total: $%.2f",
                    pedidoEnCurso.getNumero(), pedidoEnCurso.getTotal()));
            pedidoEnCurso = null;
            modeloDetalles.setRowCount(0);
            lblTotalPedido.setText("Total: -");
            txtFechaPedido.setText("");
        });

        return p;
    }

    private void recargarPlatosDelRestauranteSeleccionado() {
        if (cmbPlatosPedido == null) {
            return;
        }
        cmbPlatosPedido.removeAllItems();
        Restaurante r = (Restaurante) cmbRestaurantesPedido.getSelectedItem();
        if (r == null) {
            return;
        }
        for (int i = 0; i < r.getPlatos().size(); i++) {
            cmbPlatosPedido.addItem(r.getPlatos().get(i));
        }
    }

    // ===================== Pestania Reportes =====================
    private JPanel panelReportes() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.add(new JLabel("Fecha (YYYY-MM-DD):"));
        JTextField txtFecha = new JTextField(10);
        JButton btn = new JButton("Generar reportes");
        pTop.add(txtFecha);
        pTop.add(btn);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        btn.addActionListener(e -> {
            String f = txtFecha.getText().trim();
            if (f.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese la fecha.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            Restaurante may = gestor.obtenerRestauranteMayorFacturacion(f);
            sb.append("Restaurante con mayor facturacion (").append(f).append("): ");
            sb.append(may == null ? "(sin ventas)" : may.getNombre()).append('\n');
            Plato es = gestor.obtenerPlatoEstrella(f);
            sb.append("Plato estrella (").append(f).append("): ");
            sb.append(es == null ? "(sin ventas)" : es.getNombre()).append('\n');
            sb.append('\n').append("== Inventario actual ==").append('\n');
            String inv = gestor.reporteInventarioActual();
            sb.append(inv.isEmpty() ? "(sin restaurantes)" : inv);
            area.setText(sb.toString());
        });

        p.add(pTop, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }

    // ===================== Pestania Listado =====================
    private JPanel panelListado() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JButton btn = new JButton("Actualizar");
        btn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            if (patio.getRestaurantes().isEmpty()) {
                sb.append("(sin restaurantes)");
            } else {
                for (int i = 0; i < patio.getRestaurantes().size(); i++) {
                    Restaurante r = patio.getRestaurantes().get(i);
                    sb.append("- ").append(r.getNombre()).append(" (")
                            .append(r.getTipoComida()).append(")").append('\n');
                    if (r.getPlatos().isEmpty()) {
                        sb.append("    (sin platos)\n");
                    } else {
                        for (int j = 0; j < r.getPlatos().size(); j++) {
                            Plato pl = r.getPlatos().get(j);
                            sb.append(String.format("    * %s  $%.2f  [%s]%n",
                                    pl.getNombre(), pl.getPrecio(), pl.getCategoria()));
                        }
                    }
                }
            }
            area.setText(sb.toString());
        });

        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(btn);
        p.add(north, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }

    // ===================== Refresh de combos =====================
    private void refrescarCombos() {
        if (cmbRestauranteAEditar != null) {
            Restaurante sel = (Restaurante) cmbRestauranteAEditar.getSelectedItem();
            cmbRestauranteAEditar.removeAllItems();
            for (int i = 0; i < patio.getRestaurantes().size(); i++) {
                cmbRestauranteAEditar.addItem(patio.getRestaurantes().get(i));
            }
            if (sel != null) {
                cmbRestauranteAEditar.setSelectedItem(sel);
            }
        }
        if (cmbRestaurantesPedido != null) {
            Restaurante sel = (Restaurante) cmbRestaurantesPedido.getSelectedItem();
            cmbRestaurantesPedido.removeAllItems();
            for (int i = 0; i < patio.getRestaurantes().size(); i++) {
                cmbRestaurantesPedido.addItem(patio.getRestaurantes().get(i));
            }
            if (sel != null) {
                cmbRestaurantesPedido.setSelectedItem(sel);
            }
            recargarPlatosDelRestauranteSeleccionado();
        }
        if (cmbClientesPedido != null) {
            Cliente sel = (Cliente) cmbClientesPedido.getSelectedItem();
            cmbClientesPedido.removeAllItems();
            for (int i = 0; i < patio.getClientes().size(); i++) {
                cmbClientesPedido.addItem(patio.getClientes().get(i));
            }
            if (sel != null) {
                cmbClientesPedido.setSelectedItem(sel);
            }
        }
    }

    // ===================== Renderers =====================
    private static class RestauranteRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Restaurante) {
                setText(((Restaurante) value).getNombre());
            }
            return c;
        }
    }

    private static class PlatoRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Plato) {
                Plato pl = (Plato) value;
                setText(String.format("%s ($%.2f)", pl.getNombre(), pl.getPrecio()));
            }
            return c;
        }
    }

    private static class ClienteRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Cliente) {
                Cliente cl = (Cliente) value;
                setText(cl.getNombre() + " (" + cl.getCedula() + ")");
            }
            return c;
        }
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Repositorio repositorio = new Repositorio();
            PatioComidas patio = repositorio.cargar();
            if (patio == null) {
                double[] rangos = {2.0, 5.0, 10.0};
                double[] costos = {1.50, 2.50, 4.00};
                patio = new PatioComidas("Patio de Comidas UTPL", 0.15, rangos, costos);
            }
            Gestor gestor = new Gestor(patio);
            VentanaPrincipal v = new VentanaPrincipal(repositorio, patio, gestor);
            v.setVisible(true);
        });
    }
}
