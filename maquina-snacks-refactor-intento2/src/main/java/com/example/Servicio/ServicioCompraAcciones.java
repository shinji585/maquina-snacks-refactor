package com.example.Servicio;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Scanner;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.example.Dominio.Snack;
import com.google.common.collect.Table;

/**
 * Clase que encapsula las acciones relacionadas con las compras
 * que anteriormente estaban en la clase Main
 */
public class ServicioCompraAcciones {
    
    private IservicioCliente servicioCliente;
    private IservicioCompra servicioCompra;
    private IservicioSnakcs servicioSnacks;
    private PDFGeneratorService pdfService;
    private Scanner scanner;
    
    public ServicioCompraAcciones(IservicioCliente servicioCliente, 
                                IservicioCompra servicioCompra, 
                                IservicioSnakcs servicioSnacks,
                                PDFGeneratorService pdfService) {
        this.servicioCliente = servicioCliente;
        this.servicioCompra = servicioCompra;
        this.servicioSnacks = servicioSnacks;
        this.pdfService = pdfService;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Realiza una compra de productos
     */
    public void realizarCompra() {
        System.out.println("\n===== REALIZAR COMPRA =====");
        
        System.out.print("Ingrese el ID del cliente: ");
        try {
            int idCliente = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
            
            if (cliente == null) {
                System.out.println("No se encontró un cliente con el ID proporcionado.");
                return;
            }
            
            System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
            System.out.println("Saldo disponible: $" + cliente.verSaldo());
            
            Compra compra = servicioCompra.crearCompra(idCliente);
            
            if (compra == null) {
                System.out.println("Error al crear la compra.");
                return;
            }
            
            boolean seguirComprando = true;
            while (seguirComprando) {
                servicioSnacks.mostrarSnacks();
                
                System.out.print("\nIngrese el ID del producto que desea comprar (0 para finalizar): ");
                int idSnack = Integer.parseInt(scanner.nextLine().trim());
                
                if (idSnack == 0) {
                    seguirComprando = false;
                    continue;
                }
                
                Snack snack = servicioSnacks.comprarSnack2(idSnack);
                if (snack != null) {
                    try {
                        if (cliente.realizarCompra(snack)) {
                            boolean agregado = servicioCompra.agregarSnackACompra(compra.getIDCompra(), idSnack);
                            
                            if (agregado) {
                                System.out.println("Producto agregado: " + snack.getNombre() + " - $" + snack.getPrecio());
                                System.out.println("Subtotal actual: $" + compra.calcularTotal());
                                
                                if (cliente.verSaldo().compareTo(compra.calcularTotal()) < 0) {
                                    System.out.println("¡Advertencia! El saldo disponible es insuficiente para esta compra.");
                                }
                                
                                System.out.print("¿Desea agregar otro producto? (S/N): ");
                                String respuesta = scanner.nextLine().trim().toUpperCase();
                                seguirComprando = respuesta.equals("S");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("No se pudo agregar el producto. Verifique el ID o la disponibilidad.");
                }
            }
            
            if (compra.getSnacksComprados().isEmpty()) {
                System.out.println("No se agregaron productos a la compra. La compra ha sido cancelada.");
                servicioCompra.cancelarCompra(compra.getIDCompra());
                return;
            }
            
            System.out.println("\n===== RESUMEN DE LA COMPRA =====");
            System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
            System.out.println("Total a pagar: $" + compra.calcularTotal());
            System.out.println("Saldo disponible: $" + cliente.verSaldo());
            
            System.out.print("¿Desea confirmar la compra? (S/N): ");
            String confirmar = scanner.nextLine().trim().toUpperCase();
            
            if (confirmar.equals("S")) {
                boolean resultado = servicioCompra.finalizarCompra(compra);
                
                if (resultado) {
                    System.out.println("¡Compra realizada exitosamente!");
                    servicioCliente.registrarCompra(cliente, compra);
                    
                    File reciboPDF = pdfService.generarReciboPDF(compra);
                    if (reciboPDF != null) {
                        System.out.println("Recibo PDF generado: " + reciboPDF.getAbsolutePath());
                    }
                } else {
                    System.out.println("Error al finalizar la compra.");
                }
            } else {
                System.out.println("Compra cancelada por el usuario.");
                servicioCompra.cancelarCompra(compra.getIDCompra());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese valores numéricos válidos.");
        }
    }
    
    /**
     * Realiza una compra por dropset
     */
    public void realizarCompraDropset() {
        System.out.println("\n===== REALIZAR COMPRA POR DROPSET =====");
        
        System.out.print("Ingrese el ID del cliente: ");
        try {
            int idCliente = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
            
            if (cliente == null) {
                System.out.println("No se encontró un cliente con el ID proporcionado.");
                return;
            }
            
            System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
            System.out.println("Saldo disponible: $" + cliente.verSaldo());
            
            try {
                Compra nuevaCompra = servicioCompra.comprarDropset(idCliente);
                
                if (nuevaCompra != null && !nuevaCompra.getSnacksComprados().isEmpty()) {
                    System.out.println("\n===== RESUMEN DE LA COMPRA DROPSET =====");
                    System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
                    System.out.println("Total a pagar: $" + nuevaCompra.calcularTotal());
                    System.out.println("Saldo disponible: $" + cliente.verSaldo());
                    
                    System.out.print("¿Desea confirmar la compra? (S/N): ");
                    String confirmar = scanner.nextLine().trim().toUpperCase();
                    
                    if (confirmar.equals("S")) {
                        boolean resultado = servicioCompra.finalizarCompra(nuevaCompra);
                        
                        if (resultado) {
                            System.out.println("¡Compra dropset realizada exitosamente!");
                            servicioCliente.registrarCompra(cliente, nuevaCompra);
                            
                            File reciboPDF = pdfService.generarReciboPDF(nuevaCompra);
                            if (reciboPDF != null) {
                                System.out.println("Recibo PDF generado: " + reciboPDF.getAbsolutePath());
                            }
                        } else {
                            System.out.println("Error al finalizar la compra dropset.");
                        }
                    } else {
                        System.out.println("Compra dropset cancelada por el usuario.");
                        servicioCompra.cancelarCompra(nuevaCompra.getIDCompra());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al realizar la compra dropset: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un ID válido.");
        }
    }
    
    /**
     * Genera un recibo de compra en formato PDF
     */
    public void generarReciboPDF() {
        System.out.print("\nIngrese el ID de la compra: ");
        try {
            int idCompra = Integer.parseInt(scanner.nextLine().trim());
            
            // Buscar la compra en los clientes
            for (Cliente cliente : servicioCliente.obtenerTodosLosClientes().values()) {
                Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
                if (historial != null) {
                    for (Compra compra : historial.values()) {
                        if (compra.getIDCompra() == idCompra) {
                            File pdfFile = pdfService.generarReciboPDF(compra);
                            if (pdfFile != null) {
                                System.out.println("PDF generado exitosamente: " + pdfFile.getAbsolutePath());
                            }
                            return;
                        }
                    }
                }
            }
            
            System.out.println("No se encontró la compra con el ID proporcionado.");
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un ID válido.");
        }
    }
    
    /**
     * Muestra un menú con opciones para realizar distintos tipos de compras
     */
    public void mostrarMenuCompras() {
        int opcion = 0;
        
        do {
            System.out.println("\n===== MENÚ DE COMPRAS =====");
            System.out.println("1. Realizar compra estándar");
            System.out.println("2. Realizar compra por dropset");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        realizarCompra();
                        break;
                    case 2:
                        realizarCompraDropset();
                        break;
                    case 3:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        } while (opcion != 3);
    }
}