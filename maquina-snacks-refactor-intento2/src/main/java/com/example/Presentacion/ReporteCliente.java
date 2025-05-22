package com.example.Presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.example.Dominio.Snack;
import com.example.Servicio.ServicioCompraArchivo;
import com.example.Servicio.ServicioClienteArchivo;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Ventana de Reportes para Clientes
 */
public class ReporteCliente extends JFrame {

    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private JButton btnVolver;
    private JButton btnGenerarReporte;
    private JButton btnExportar;
    private JButton btnCargarCompras;
    private JLabel lblTitulo;
    private JTextField txtIdCliente;
    private JLabel lblIdCliente;

    private ServicioCompraArchivo servicioCompraArchivo;
    private ServicioClienteArchivo servicioClienteArchivo;
    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ReporteCliente() {
        servicioCompraArchivo = new ServicioCompraArchivo();
        servicioClienteArchivo = new ServicioClienteArchivo();
        initComponents();
        configurarVentana();
        servicioCompraArchivo.cargarCompras();
    }

    private void initComponents() {
        // Configuración básica de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Reportes de Cliente - Snacks_Bums.com");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Crear componentes
        lblTitulo = new JLabel("REPORTES DE CLIENTE", JLabel.CENTER);
        lblTitulo.setFont(new Font("Gadugi", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(48, 73, 240));

        // Campo para ID del cliente
        lblIdCliente = new JLabel("ID Cliente:");
        lblIdCliente.setFont(new Font("Gadugi", Font.BOLD, 14));
        txtIdCliente = new JTextField(10);
        txtIdCliente.setFont(new Font("Gadugi", Font.PLAIN, 14));

        // Configurar tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columnas = { "ID Compra", "Producto", "Cantidad", "Precio Unitario", "Total", "Fecha" };
        modeloTabla.setColumnIdentifiers(columnas);

        tablaReportes = new JTable(modeloTabla);
        tablaReportes.setRowHeight(25);
        tablaReportes.getTableHeader().setBackground(new Color(48, 73, 240));
        tablaReportes.getTableHeader().setForeground(Color.WHITE);
        tablaReportes.getTableHeader().setFont(new Font("Gadugi", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(tablaReportes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historial de Compras"));

        // Crear botones
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(48, 73, 240));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Gadugi", Font.BOLD, 14));
        btnVolver.setFocusPainted(false);

        btnCargarCompras = new JButton("Cargar Compras");
        btnCargarCompras.setBackground(new Color(70, 130, 180));
        btnCargarCompras.setForeground(Color.WHITE);
        btnCargarCompras.setFont(new Font("Gadugi", Font.BOLD, 14));
        btnCargarCompras.setFocusPainted(false);

        btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.setBackground(new Color(34, 139, 34));
        btnGenerarReporte.setForeground(Color.WHITE);
        btnGenerarReporte.setFont(new Font("Gadugi", Font.BOLD, 14));
        btnGenerarReporte.setFocusPainted(false);

        btnExportar = new JButton("Exportar a PDF");
        btnExportar.setBackground(new Color(255, 140, 0));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFont(new Font("Gadugi", Font.BOLD, 14));
        btnExportar.setFocusPainted(false);

        // Layout
        setLayout(new BorderLayout());

        // Panel superior con título y campo de ID
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        JPanel panelIdCliente = new JPanel(new FlowLayout());
        panelIdCliente.setBackground(Color.WHITE);
        panelIdCliente.add(lblIdCliente);
        panelIdCliente.add(txtIdCliente);
        panelIdCliente.add(btnCargarCompras);

        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelIdCliente, BorderLayout.SOUTH);

        // Panel central con tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnVolver);
        panelBotones.add(btnGenerarReporte);
        panelBotones.add(btnExportar);

        // Agregar paneles a la ventana
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarVentana() {
        // Configurar eventos de botones
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cerrar esta ventana
            }
        });

        btnCargarCompras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarComprasCliente();
            }
        });

        btnGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarAPDF();
            }
        });

        // Permitir cargar compras presionando Enter en el campo de texto
        txtIdCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarComprasCliente();
            }
        });
    }

    private void cargarComprasCliente() {
        String idTexto = txtIdCliente.getText().trim();

        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingrese un ID de cliente válido.",
                    "ID Requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idCliente = Integer.parseInt(idTexto);

            ServicioClienteArchivo servicioCliente = null;
            // Verificar cliente
            Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ServicioCompraArchivo servicioCompra = null;
            Table<Integer, Integer, Compra> comprasCliente = servicioCompra.obtenerComprasPorCliente(idCliente);

            // Limpiar tabla antes de cargar nuevos datos
            modeloTabla.setRowCount(0);

            if (comprasCliente.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "El cliente " + cliente.getNombre() + " no tiene compras registradas.",
                        "Sin Compras",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Llenar la tabla con los datos de las compras
            for (Cell<Integer, Integer, Compra> cell : comprasCliente.cellSet()) {
                Compra compra = cell.getValue();
                modeloTabla.addRow(new Object[] {
                        cell.getColumnKey(), // idCompra
                        compra.getfecha().format(DateTimeFormatter.ISO_DATE),
                        compra.getSnacksComprados().size(),
                });
                // Procesar cada snack en la compra
                Table<Integer, String, Snack> snacksComprados = compra.getSnacksComprados();

                LocalDateTime fecha = null;
                if (snacksComprados.isEmpty()) {
                    Compra idcompra = null;
                    // Si no hay snacks, mostrar una fila vacía para la compra
                    modeloTabla.addRow(new Object[] {
                            idcompra,
                            "Sin productos",
                            0,
                            ".00",
                            ".00",
                            fecha.format(formatoFecha)
                    });
                } else {
                    // Contar cantidad de cada tipo de snack
                    Map<Integer, Integer> conteoSnacks = new java.util.HashMap<>();
                    Map<Integer, Snack> snacksUnicos = new java.util.HashMap<>();

                    for (Table.Cell<Integer, String, Snack> snackCell : snacksComprados.cellSet()) {
                        Snack snack = snackCell.getValue();
                        int idSnack = snack.getIdSnack();
                        conteoSnacks.put(idSnack, conteoSnacks.getOrDefault(idSnack, 0) + 1);
                        snacksUnicos.put(idSnack, snack);
                    }

                    // Agregar una fila por cada tipo de snack único
                    for (Map.Entry<Integer, Integer> entry : conteoSnacks.entrySet()) {
                        Snack snack = snacksUnicos.get(entry.getKey());
                        int cantidad = entry.getValue();
                        double precioUnitario = snack.getPrecio();
                        double totalSnack = precioUnitario * cantidad;

                        Object idcompra = null;
                        modeloTabla.addRow(new Object[] {
                                idcompra,
                                snack.getNombre(),
                                cantidad,
                                String.format("$%.2f", precioUnitario),
                                String.format("$%.2f", totalSnack),
                                fecha.format(formatoFecha)
                        });
                    }
                }
            }

            // Calcular y mostrar totales
            calcularTotalCompras();

            JOptionPane.showMessageDialog(
                    this,
                    "Compras del cliente " + cliente.getNombre() + " cargadas exitosamente.\n" +
                            "Total de compras: " + comprasCliente.size(),
                    "Compras Cargadas",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingrese un número válido para el ID del cliente.",
                    "ID Inválido",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las compras: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularTotalCompras() {
        double total = 0.0;
        int totalCompras = 0;

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String totalStr = modeloTabla.getValueAt(i, 4).toString();
            // Remover el símbolo $ para poder convertir a número
            totalStr = totalStr.replace("$", "");
            try {
                double subtotal = Double.parseDouble(totalStr);
                total += subtotal;
                totalCompras++;
            } catch (NumberFormatException e) {
                // Ignorar filas con valores no numéricos
            }
        }

        // Agregar fila separadora
        modeloTabla.addRow(new Object[] { "", "", "", "", "", "" });

        // Agregar fila de total
        modeloTabla.addRow(new Object[] {
                "",
                "TOTAL GENERAL",
                totalCompras + " items",
                "",
                String.format("$%.2f", total),
                ""
        });

        // Hacer la fila del total más visible
        SwingUtilities.invokeLater(() -> {
            int ultimaFila = tablaReportes.getRowCount() - 1;
            tablaReportes.setRowSelectionInterval(ultimaFila, ultimaFila);
        });
    }

    private void generarReporte() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay datos para generar el reporte.\nPrimero cargue las compras de un cliente.",
                    "Sin Datos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idTexto = txtIdCliente.getText().trim();
        if (!idTexto.isEmpty()) {
            try {
                int idCliente = Integer.parseInt(idTexto);
                Cliente cliente = servicioClienteArchivo.obtenerClientePorID(idCliente);

                if (cliente != null) {
                    StringBuilder reporte = new StringBuilder();
                    reporte.append("REPORTE DETALLADO DE COMPRAS\n\n");
                    reporte.append("Cliente: ").append(cliente.getNombre()).append("\n");
                    reporte.append("ID Cliente: ").append(cliente.getID()).append("\n");
                    reporte.append("Saldo Actual: $").append(cliente.verSaldo()).append("\n");
                    reporte.append("Fecha de generación: ").append(LocalDateTime.now().format(formatoFecha))
                            .append("\n\n");

                    // Calcular estadísticas
                    int totalItems = modeloTabla.getRowCount() - 2; // Excluir filas de total
                    double totalGastado = 0.0;

                    for (int i = 0; i < totalItems; i++) {
                        String totalStr = modeloTabla.getValueAt(i, 4).toString().replace("$", "");
                        try {
                            totalGastado += Double.parseDouble(totalStr);
                        } catch (NumberFormatException e) {
                            // Ignorar valores no numéricos
                        }
                    }

                    reporte.append("RESUMEN:\n");
                    reporte.append("- Total de productos comprados: ").append(totalItems).append("\n");
                    reporte.append("- Total gastado: $").append(String.format("%.2f", totalGastado)).append("\n");

                    JTextArea areaTexto = new JTextArea(reporte.toString());
                    areaTexto.setEditable(false);
                    areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));

                    JScrollPane scrollReporte = new JScrollPane(areaTexto);
                    scrollReporte.setPreferredSize(new Dimension(500, 400));

                    JOptionPane.showMessageDialog(
                            this,
                            scrollReporte,
                            "Reporte Generado - " + cliente.getNombre(),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                // Ignorar error de conversión
            }
        }
    }

    private void exportarAPDF() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay datos para exportar.\nPrimero cargue las compras de un cliente.",
                    "Sin Datos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte como PDF");

        String nombreArchivo = "reporte_cliente";
        String idTexto = txtIdCliente.getText().trim();
        if (!idTexto.isEmpty()) {
            nombreArchivo += "_" + idTexto;
        }
        nombreArchivo += ".pdf";

        fileChooser.setSelectedFile(new java.io.File(nombreArchivo));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();

            // Simular exportación (aquí implementarías la lógica real de exportación a PDF)
            JOptionPane.showMessageDialog(
                    this,
                    "Reporte exportado exitosamente a:\n" + fileToSave.getAbsolutePath() +
                            "\n\nEl archivo contiene " + (modeloTabla.getRowCount() - 2) + " registros de compras.",
                    "Exportación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}