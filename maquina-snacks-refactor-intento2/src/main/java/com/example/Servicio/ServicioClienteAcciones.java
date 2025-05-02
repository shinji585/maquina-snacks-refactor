package com.example.Servicio;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.google.common.collect.Table;

/**
 * Clase que encapsula las acciones relacionadas con los clientes
 * que anteriormente estaban en la clase Main
 */
public class ServicioClienteAcciones {
    
    private IservicioCliente servicioCliente;
    private PDFGeneratorService pdfService;
    private Scanner scanner;
    
    public ServicioClienteAcciones(IservicioCliente servicioCliente, PDFGeneratorService pdfService) {
        this.servicioCliente = servicioCliente;
        this.pdfService = pdfService;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Muestra la lista de todos los clientes registrados
     */
    public void mostrarClientes() {
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
    
    /**
     * Busca un cliente por su ID y muestra sus datos
     */
    public void buscarCliente() {
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
    
    /**
     * Crea un nuevo cliente
     */
    public void crearCliente() {
        System.out.println("\n===== CREAR NUEVO CLIENTE =====");
        
        try {
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
            
            Cliente nuevoCliente = new Cliente(nombre, edad, apellidos, sexo, nacionalidad, 
                                            direccion, correo, telefono, saldo);
            
            boolean resultado = servicioCliente.registrarCliente(nuevoCliente);
            
            if (resultado) {
                System.out.println("Cliente registrado exitosamente.");
            } else {
                System.out.println("Error al registrar el cliente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese valores numéricos válidos.");
        } catch (Exception e) {
            System.out.println("Error al crear el cliente: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza el saldo de un cliente
     */
    public void actualizarSaldoCliente() {
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
    
    /**
     * Genera un historial de cliente en formato PDF
     */
    public void generarHistorialClientePDF() {
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
    
    /**
     * Muestra un menú para gestionar los clientes
     */
    public void mostrarMenuGestionClientes() {
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
}