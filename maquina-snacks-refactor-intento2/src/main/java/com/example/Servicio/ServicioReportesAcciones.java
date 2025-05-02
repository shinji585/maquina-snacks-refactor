package com.example.Servicio;

import java.io.File;
import java.util.Scanner;

import com.example.Dominio.Snack;
import com.google.common.collect.Table;

/**
 * Clase que encapsula las acciones relacionadas con los reportes
 * que anteriormente estaban en la clase Main
 */
public class ServicioReportesAcciones {
    
    private IservicioSnakcs servicioSnacks;
    private IservicioCliente servicioCliente;
    private IservicioCompra servicioCompra;
    private PDFGeneratorService pdfService;
    private ServicioClienteAcciones servicioClienteAcciones;
    private ServicioCompraAcciones servicioCompraAcciones;
    private Scanner scanner;
    
    public ServicioReportesAcciones(IservicioSnakcs servicioSnacks, 
                                  IservicioCliente servicioCliente, 
                                  IservicioCompra servicioCompra,
                                  PDFGeneratorService pdfService,
                                  ServicioClienteAcciones servicioClienteAcciones,
                                  ServicioCompraAcciones servicioCompraAcciones) {
        this.servicioSnacks = servicioSnacks;
        this.servicioCliente = servicioCliente;
        this.servicioCompra = servicioCompra;
        this.pdfService = pdfService;
        this.servicioClienteAcciones = servicioClienteAcciones;
        this.servicioCompraAcciones = servicioCompraAcciones;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Muestra el inventario de snacks disponibles
     */
    public void verInventario() {
        System.out.println("\n===== INVENTARIO DE SNACKS =====");
        servicioSnacks.mostrarSnacks();
    }
    
    /**
     * Genera un reporte de inventario en formato PDF
     */
    public void generarReporteInventarioPDF() {
        System.out.println("\nGenerando reporte de inventario...");
        Table<Integer, String, Snack> snacks = servicioSnacks.getSnacks();
        File pdfFile = pdfService.generarReporteInventarioPDF(snacks);
        
        if (pdfFile != null) {
            System.out.println("PDF generado exitosamente: " + pdfFile.getAbsolutePath());
        }
    }
    
    /**
     * Muestra un menú para generar diferentes tipos de reportes en PDF
     */
    public void mostrarMenuReportesPDF() {
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
                        servicioClienteAcciones.generarHistorialClientePDF();
                        break;
                    case 2:
                        servicioCompraAcciones.generarReciboPDF();
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
}