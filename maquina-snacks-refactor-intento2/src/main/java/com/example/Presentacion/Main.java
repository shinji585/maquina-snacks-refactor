package com.example.Presentacion;
import java.util.Scanner;
import com.example.Servicio.IservicioCliente;
import com.example.Servicio.IservicioCompra;
import com.example.Servicio.IservicioSnakcs;
import com.example.Servicio.PDFGeneratorService;
import com.example.Servicio.ServicioClienteAcciones;
import com.example.Servicio.ServicioClienteArchivo;
import com.example.Servicio.ServicioCompraAcciones;
import com.example.Servicio.ServicioCompraArchivo;
import com.example.Servicio.ServicioReportesAcciones;
import com.example.Servicio.ServicioSnacksArchivo;
/**
 * Clase principal de la aplicación con el menú principal
 * Refactorizada para utilizar servicios de acciones separados
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static IservicioSnakcs servicioSnacks;
    private static IservicioCliente servicioCliente;
    private static IservicioCompra servicioCompra;
    private static PDFGeneratorService pdfService;
    
    // Nuevos servicios de acciones
    private static ServicioClienteAcciones servicioClienteAcciones;
    private static ServicioCompraAcciones servicioCompraAcciones;
    private static ServicioReportesAcciones servicioReportesAcciones;
    
    public static void main(String[] args) {
        // Inicializar servicios de datos
        inicializarServicios();
        
        // Mostrar menú principal
        mostrarMenuPrincipal();
        
        // Cerrar el scanner al finalizar
        scanner.close();
        System.out.println("Gracias por usar el Sistema de Gestión de Snacks. ¡Hasta pronto!");
    }
    
    /**
     * Inicializa todos los servicios necesarios para la aplicación
     */
    private static void inicializarServicios() {
        // Servicios de datos
        servicioSnacks = new ServicioSnacksArchivo();
        servicioCliente = new ServicioClienteArchivo();
        servicioCompra = new ServicioCompraArchivo();
        pdfService = new PDFGeneratorService();
        
        // Servicios de acciones
        servicioClienteAcciones = new ServicioClienteAcciones(servicioCliente, pdfService);
        servicioCompraAcciones = new ServicioCompraAcciones(servicioCliente, servicioCompra, servicioSnacks, pdfService);
        servicioReportesAcciones = new ServicioReportesAcciones(servicioSnacks, servicioCliente, servicioCompra, 
                                                              pdfService, servicioClienteAcciones, servicioCompraAcciones);
    }
    
    /**
     * Muestra el menú principal de la aplicación
     */
    private static void mostrarMenuPrincipal() {
        int opcion = 0;
        
        do {
            System.out.println("\n===== SISTEMA DE GESTIÓN DE SNACKS =====");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Realizar Compra");
            System.out.println("3. Ver Inventario");
            System.out.println("4. Generar Reportes");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcion) {
                    case 1:
                        // Gestionar clientes
                        servicioClienteAcciones.mostrarMenuGestionClientes();
                        break;
                    case 2:
                        // Realizar compra
                        servicioCompraAcciones.mostrarMenuCompras();
                        break;
                    case 3:
                        // Ver inventario
                        servicioReportesAcciones.verInventario();
                        break;
                    case 4:
                        // Generar reportes
                        servicioReportesAcciones.mostrarMenuReportesPDF();
                        break;
                    case 5:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                opcion = 0; // Resetear la opción para continuar el bucle
            }
        } while (opcion != 5);
    }
}