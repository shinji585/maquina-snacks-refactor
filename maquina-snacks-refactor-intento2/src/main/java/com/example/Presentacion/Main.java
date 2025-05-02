package com.example.Presentacion;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.example.Dominio.Snack;
import com.example.Servicio.IservicioCliente;
import com.example.Servicio.IservicioCompra;
import com.example.Servicio.IservicioSnakcs;
import com.example.Servicio.PDFGeneratorService;
import com.example.Servicio.ServicioClienteArchivo;
import com.example.Servicio.ServicioCompraArchivo;
import com.example.Servicio.ServicioSnacksArchivo;
import com.google.common.collect.Table;

public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static IservicioSnakcs servicioSnacks;
    private static IservicioCliente servicioCliente;
    private static IservicioCompra servicioCompra;
    private static PDFGeneratorService pdfService;
    
    public static void main(String[] args) {
        // Inicializar servicios
        servicioSnacks = new ServicioSnacksArchivo();
        servicioCliente = new ServicioClienteArchivo();
        servicioCompra = new ServicioCompraArchivo();
        pdfService = new PDFGeneratorService();
        
        // Mostrar menú principal
        mostrarMenuPrincipal();
    }
    
    private static void mostrarMenuPrincipal() {
        int opcion = 0;
        
        do {
            System.out.println("\n===== SISTEMA DE GESTIÓN DE SNACKS =====");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Realizar Compra");
            System.out.println("3. Ver Inventario");
            System.out.println("4. Generar Reportes PDF");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        gestionarClientes();
                        break;
                    case 2:
                        realizarCompra();
                        break;
                    case 3:
                        verInventario();
                        break;
                    case 4:
                        generarReportesPDF();
                        break;
                    case 5:
                        System.out.println("¡Gracias por usar el sistema!");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        } while (opcion != 5);
    }
    
    private static void gestionarClientes() {
        int opcion = 0;
        
        do {
            System.out.println("\n===== GESTIÓN DE CLIENTES =====");
            System.out.println("1. Ver todos los clientes");
            System.out.println("2. Buscar cliente por ID");
            System.out.println("3. Añadir nuevo cliente");
            System.out.println("4. Actualizar saldo de cliente");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        mostrarClientes();
                        break;
                    case 2:
                        buscarCliente();
                        break;
                    case 3:
                        crearCliente();
                        break;
                    case 4:
                        actualizarSaldoCliente();
                        break;
                    case 5:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        } while (opcion != 5);
    }
    
    private static void mostrarClientes() {
        System.out.println("\n===== LISTA DE CLIENTES =====");
        Map<Integer, Cliente> clientes = servicioCliente.obtenerTodosLosClientes();
        
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        
        for (Cliente cliente : clientes.values()) {
            System.out.println("ID: " + cliente.getID() +
                    " | Nombre: " + cliente.getNombre() + " " + cliente.getApellidos() +
                    " | Saldo: $" + cliente.verSaldo());
            
            Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
            int comprasRealizadas = historial != null ? historial.size() : 0;
            
            System.out.println("   Compras realizadas: " + comprasRealizadas);
            System.out.println("----------------------------------------");
        }
    }
    
    private static void buscarCliente() {
        System.out.print("\nIngrese el ID del cliente: ");
        try {
            int idCliente = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
            
            if (cliente != null) {
                System.out.println("\n===== DATOS DEL CLIENTE =====");
                System.out.println("ID: " + cliente.getID());
                System.out.println("Nombre: " + cliente.getNombre() + " " + cliente.getApellidos());
                System.out.println("Saldo actual: $" + cliente.verSaldo());
                
                Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
                int comprasRealizadas = historial != null ? historial.size() : 0;
                System.out.println("Número de compras: " + comprasRealizadas);
                
                if (comprasRealizadas > 0) {
                    System.out.print("¿Desea generar un PDF con el historial de compras? (S/N): ");
                    String respuesta = scanner.nextLine().trim().toUpperCase();
                    
                    if (respuesta.equals("S")) {
                        File historialPDF = pdfService.generarHistorialClientePDF(cliente, historial);
                        if (historialPDF != null) {
                            System.out.println("PDF generado exitosamente: " + historialPDF.getAbsolutePath());
                        }
                    }
                }
            } else {
                System.out.println("No se encontró un cliente con el ID proporcionado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un ID válido.");
        }
    }
    
    private static void crearCliente() {
        System.out.println("\n===== CREAR NUEVO CLIENTE =====");
        
        System.out.print("Ingrese el ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        
        if (servicioCliente.existeCliente(id)) {
            System.out.println("Ya existe un cliente con ese ID.");
            return;
        }
        
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Ingrese los apellidos: ");
        String apellidos = scanner.nextLine().trim();
        
        System.out.print("Ingrese la edad: ");
        int edad = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Ingrese el sexo (M/F): ");
        boolean sexo = scanner.nextLine().trim().equalsIgnoreCase("M");
        
        System.out.print("Ingrese la nacionalidad: ");
        String nacionalidad = scanner.nextLine().trim();
        
        System.out.print("Ingrese la dirección: ");
        String direccion = scanner.nextLine().trim();
        
        System.out.print("Ingrese el correo electrónico: ");
        String correo = scanner.nextLine().trim();
        
        System.out.print("Ingrese el número de teléfono: ");
        long telefono = Long.parseLong(scanner.nextLine().trim());
        
        System.out.print("Ingrese el saldo inicial: $");
        BigDecimal saldo = new BigDecimal(scanner.nextLine().trim());
        
        try {
            Cliente nuevoCliente = new Cliente(nombre, edad, apellidos, sexo, nacionalidad, 
                                             direccion, correo, telefono, saldo);
            
            boolean resultado = servicioCliente.registrarCliente(nuevoCliente);
            
            if (resultado) {
                System.out.println("Cliente registrado exitosamente.");
            } else {
                System.out.println("Error al registrar el cliente.");
            }
        } catch (Exception e) {
            System.out.println("Error al crear el cliente: " + e.getMessage());
        }
    }
    
    private static void actualizarSaldoCliente() {
        System.out.print("\nIngrese el ID del cliente: ");
        try {
            int idCliente = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
            
            if (cliente != null) {
                System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
                System.out.println("Saldo actual: $" + cliente.verSaldo());
                
                System.out.print("Ingrese el monto a recargar: $");
                BigDecimal monto = new BigDecimal(scanner.nextLine().trim());
                
                try {
                    cliente.recargarSaldo(monto);
                    boolean resultado = servicioCliente.actualizarCliente(cliente);
                    
                    if (resultado) {
                        System.out.println("Saldo actualizado exitosamente. Nuevo saldo: $" + cliente.verSaldo());
                    } else {
                        System.out.println("Error al actualizar el saldo.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("No se encontró un cliente con el ID proporcionado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese valores numéricos válidos.");
        }
    }
    
    private static void realizarCompra() {
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
    
    private static void verInventario() {
        System.out.println("\n===== INVENTARIO DE SNACKS =====");
        servicioSnacks.mostrarSnacks();
    }
    
    private static void generarReportesPDF() {
        int opcion = 0;
        
        do {
            System.out.println("\n===== GENERAR REPORTES PDF =====");
            System.out.println("1. Generar historial de cliente");
            System.out.println("2. Generar recibo de compra");
            System.out.println("3. Generar reporte de inventario");
            System.out.println("4. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        generarHistorialClientePDF();
                        break;
                    case 2:
                        generarReciboPDF();
                        break;
                    case 3:
                        generarReporteInventarioPDF();
                        break;
                    case 4:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        } while (opcion != 4);
    }
    
    private static void generarHistorialClientePDF() {
        System.out.print("\nIngrese el ID del cliente: ");
        try {
            int idCliente = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
            
            if (cliente != null) {
                Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
                File pdfFile = pdfService.generarHistorialClientePDF(cliente, historial);
                
                if (pdfFile != null) {
                    System.out.println("PDF generado exitosamente: " + pdfFile.getAbsolutePath());
                }
            } else {
                System.out.println("No se encontró un cliente con el ID proporcionado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un ID válido.");
        }
    }
    
    private static void generarReciboPDF() {
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
    
    private static void generarReporteInventarioPDF() {
        System.out.println("\nGenerando reporte de inventario...");
        Table<Integer, String, Snack> snacks = servicioSnacks.getSnacks();
        File pdfFile = pdfService.generarReporteInventarioPDF(snacks);
        
        if (pdfFile != null) {
            System.out.println("PDF generado exitosamente: " + pdfFile.getAbsolutePath());
        }
    }
}