package com.example.Presentacion;

import java.math.BigDecimal;
import java.util.Scanner;
import java.io.File;
import java.time.LocalDateTime;

import com.example.Dominio.Cliente;
import com.example.Servicio.IservicioCliente;
import com.example.Servicio.ServicioClienteArchivo;
import com.example.Servicio.IservicioSnakcs;
import com.example.Servicio.ServicioSnacksArchivo;

public class MenuCliente {
    static Scanner scanner = new Scanner(System.in);
    static IservicioCliente servicioCliente = new ServicioClienteArchivo();
    static IservicioSnakcs servicioSnacks = new ServicioSnacksArchivo();

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Buscar cliente");
            System.out.println("3. Recargar saldo");
            System.out.println("4. Realizar compra");
            System.out.println("5. Guardar historial de compra");
            System.out.println("6. Generar PDF de historial");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        buscarCliente();
                        break;
                    case 3:
                        recargarSaldo();
                        break;
                    case 4:
                        realizarCompra();
                        break;
                    case 5:
                        guardarHistorial();
                        break;
                    case 6:
                        generarPDF();
                        break;
                    case 0:
                        salir = true;
                        System.out.println("¡Gracias por usar nuestro sistema!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void registrarCliente() {
        try {
            System.out.println("\n===== REGISTRO DE CLIENTE =====");
            
            System.out.print("Ingrese su nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Ingrese su edad: ");
            int edad = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Ingrese sus apellidos: ");
            String apellidos = scanner.nextLine();
            
            System.out.print("Ingrese su dirección: ");
            String direccion = scanner.nextLine();
            
            System.out.print("Ingrese su saldo inicial: ");
            BigDecimal saldo = new BigDecimal(scanner.nextLine());
            
            System.out.print("Ingrese su sexo (true para hombre o false para mujer): ");
            boolean sexo = Boolean.parseBoolean(scanner.nextLine());
            
            System.out.print("Ingrese su nacionalidad: ");
            String nacionalidad = scanner.nextLine();
            
            System.out.print("Ingrese su correo electrónico: ");
            String correoElectronico = scanner.nextLine();
            
            System.out.print("Ingrese su número de teléfono: ");
            long numeroTelefono = Long.parseLong(scanner.nextLine());
            
            Cliente nuevoCliente = new Cliente(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono, saldo);
            
            if (servicioCliente.registrarCliente(nuevoCliente)) {
                System.out.println("Cliente registrado con éxito. ID asignado: " + nuevoCliente.getID());
            } else {
                System.out.println("No se pudo registrar el cliente.");
            }
        } catch (Exception e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
    }

    private static void buscarCliente() {
        System.out.println("\n===== BUSCAR CLIENTE =====");
        System.out.print("Ingrese el ID del cliente: ");
        try {
            int idCliente = Integer.parseInt(scanner.nextLine());
            Cliente cliente = servicioCliente.buscarCliente(idCliente);
            
            if (cliente != null && cliente.getID() == idCliente) {
                System.out.println("\nInformación del cliente:");
                System.out.println("ID: " + cliente.getID());
                System.out.println("Nombre: " + cliente.getNombre() + " " + cliente.getApellidos());
                System.out.println("Edad: " + cliente.getEdad());
                System.out.println("Sexo: " + cliente.getSEXOString());
                System.out.println("Nacionalidad: " + cliente.getNacionalidad());
                System.out.println("Dirección: " + cliente.getDireccion());
                System.out.println("Correo: " + cliente.getCorreoElectronico());
                System.out.println("Teléfono: " + cliente.getNumeroTelefono());
                System.out.println("Saldo actual: " + cliente.verSaldo());
            } else {
                System.out.println("No se encontró un cliente con el ID: " + idCliente);
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un ID válido (número entero).");
        }
    }

    private static void recargarSaldo() {
        System.out.println("\n===== RECARGAR SALDO =====");
        try {
            System.out.print("Ingrese el ID del cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());
            
            Cliente cliente = servicioCliente.buscarCliente(idCliente);
            if (cliente != null && cliente.getID() == idCliente) {
                System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
                System.out.println("Saldo actual: " + cliente.verSaldo());
                
                System.out.print("Ingrese el monto a recargar: ");
                BigDecimal montoRecarga = new BigDecimal(scanner.nextLine());
                
                if (servicioCliente.recargarSaldo(idCliente, montoRecarga)) {
                    System.out.println("Recarga exitosa. Nuevo saldo: " + servicioCliente.buscarCliente(idCliente).verSaldo());
                } else {
                    System.out.println("No se pudo realizar la recarga.");
                }
            } else {
                System.out.println("No se encontró un cliente con el ID: " + idCliente);
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese valores numéricos válidos.");
        } catch (Exception e) {
            System.out.println("Error al recargar saldo: " + e.getMessage());
        }
    }

    private static void realizarCompra() {
        System.out.println("\n===== REALIZAR COMPRA =====");
        try {
            System.out.print("Ingrese el ID del cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());
            
            Cliente cliente = servicioCliente.buscarCliente(idCliente);
            if (cliente != null && cliente.getID() == idCliente) {
                System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
                System.out.println("Saldo disponible: " + cliente.verSaldo());
                
                // Mostrar lista de snacks disponibles
                System.out.println("\nSnacks disponibles:");
                servicioSnacks.getSnacks().rowMap().forEach((id, snackMap) -> {
                    System.out.println("ID: " + id + " - Producto: " + snackMap.values().iterator().next().getNombre() + 
                                      " - Precio: " + snackMap.values().iterator().next().getPrecio());
                });
                
                System.out.print("\nIngrese el ID del snack que desea comprar: ");
                int idSnack = Integer.parseInt(scanner.nextLine());
                
                if (servicioCliente.realizarCompra(idCliente, idSnack)) {
                    System.out.println("Compra realizada con éxito.");
                    System.out.println("Saldo restante: " + servicioCliente.buscarCliente(idCliente).verSaldo());
                } else {
                    System.out.println("No se pudo realizar la compra. Verifique el saldo o el ID del snack.");
                }
            } else {
                System.out.println("No se encontró un cliente con el ID: " + idCliente);
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese valores numéricos válidos.");
        } catch (Exception e) {
            System.out.println("Error al realizar compra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void guardarHistorial() {
        System.out.println("\n===== GUARDAR HISTORIAL DE COMPRA =====");
        try {
            System.out.print("Ingrese el ID del cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());
            
            if (servicioCliente.guardarHistorial(idCliente)) {
                System.out.println("Historial de compra guardado exitosamente.");
            } else {
                System.out.println("No se pudo guardar el historial. Verifique que el cliente exista y tenga compras pendientes.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un ID válido (número entero).");
        } catch (Exception e) {
            System.out.println("Error al guardar historial: " + e.getMessage());
        }
    }

    private static void generarPDF() {
        System.out.println("\n===== GENERAR PDF DE HISTORIAL =====");
        try {
            System.out.print("Ingrese el ID del cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());
            
            File pdfFile = servicioCliente.generarPDFHistorial(idCliente);
            
            if (pdfFile != null && pdfFile.exists()) {
                System.out.println("PDF generado exitosamente: " + pdfFile.getAbsolutePath());
            } else {
                System.out.println("No se pudo generar el PDF. Verifique que el cliente exista y tenga historial de compras.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un ID válido (número entero).");
        } catch (Exception e) {
            System.out.println("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}