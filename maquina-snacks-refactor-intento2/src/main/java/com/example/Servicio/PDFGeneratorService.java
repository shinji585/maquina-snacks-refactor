package com.example.Servicio;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.example.Dominio.Snack;
import com.google.common.collect.Table;

/**
 * Servicio especializado para la generación de documentos PDF en la aplicación.
 * Esta clase centraliza todas las funcionalidades de generación de PDF para
 * mantener un código limpio y reutilizable.
 */
public class PDFGeneratorService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int MARGIN_TOP = 750;
    private static final int MARGIN_LEFT = 50;
    private static final int LINE_HEIGHT = 15;
    
    
    /**
     * Genera un PDF con el historial de compras de un cliente.
     * 
     * @param cliente El cliente cuyo historial se va a generar
     * @param historialCompras La tabla de compras históricas del cliente
     * @return El archivo PDF generado o null si hubo un error
     */
    public File generarHistorialClientePDF(Cliente cliente, Table<LocalDateTime, Integer, Compra> historialCompras) {
        if (cliente == null) {
            System.out.println("Error: Cliente no proporcionado para generar PDF.");
            return null;
        }
        
        String nombreArchivo = "historial_compra_cliente_" + cliente.getID() + ".pdf";
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Configuraciones iniciales
            int yPosition = MARGIN_TOP;
            
            // Título del documento
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Historial de Compras del Cliente");
            contentStream.endText();
            yPosition -= 25;
            
            // Información del cliente
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("ID Cliente: " + cliente.getID());
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Nombre: " + cliente.getNombre() + " " + cliente.getApellidos());
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Fecha de generación: " + LocalDateTime.now().format(DATE_FORMATTER));
            contentStream.endText();
            yPosition -= 30;
            
            // Sección de compras
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Detalle de Compras:");
            contentStream.endText();
            yPosition -= 20;
            
            // Verificar si hay compras para mostrar
            if (historialCompras == null || historialCompras.isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                contentStream.showText("No hay compras registradas para este cliente.");
                contentStream.endText();
            } else {
                // Recorrer las compras y mostrarlas
                BigDecimal totalGeneral = BigDecimal.ZERO;
                
                for (Map.Entry<LocalDateTime, Map<Integer, Compra>> entry : historialCompras.rowMap().entrySet()) {
                    LocalDateTime fecha = entry.getKey();
                    
                    for (Compra compra : entry.getValue().values()) {
                        // Verificar si necesitamos una nueva página por espacio
                        if (yPosition < 100) {
                            contentStream.close();
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            yPosition = MARGIN_TOP;
                        }
                        
                        // Encabezado de la compra
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                        contentStream.showText("Compra #" + compra.getIDCompra() + " - Fecha: " + fecha.format(DATE_FORMATTER));
                        contentStream.endText();
                        yPosition -= LINE_HEIGHT + 5;
                        
                        // Detalles de los productos
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.newLineAtOffset(MARGIN_LEFT + 20, yPosition);
                        contentStream.showText("Productos:");
                        contentStream.endText();
                        yPosition -= LINE_HEIGHT;
                        
                        // Mostrar cada producto
                        for (Snack snack : compra.getSnacksComprados().values()) {
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, 10);
                            contentStream.newLineAtOffset(MARGIN_LEFT + 30, yPosition);
                            String productoInfo = "- " + snack.getNombre() + " (" + snack.getTipo() + "): $" + 
                                                String.format("%.2f", snack.getPrecio());
                            contentStream.showText(productoInfo);
                            contentStream.endText();
                            yPosition -= LINE_HEIGHT;
                        }
                        
                        // Subtotal de la compra
                        BigDecimal total = compra.calcularTotal();
                        totalGeneral = totalGeneral.add(total);
                        
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                        contentStream.newLineAtOffset(MARGIN_LEFT + 20, yPosition);
                        contentStream.showText("Total de la compra: $" + String.format("%.2f", total));
                        contentStream.endText();
                        yPosition -= LINE_HEIGHT + 10;
                    }
                }
                
                // Total general
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                contentStream.showText("Total General: $" + String.format("%.2f", totalGeneral));
                contentStream.endText();
            }
            
            contentStream.close();
        } catch (IOException e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        // Guardar el documento PDF
        File pdfFile = new File(nombreArchivo);
        try {
            document.save(pdfFile);
            document.close();
            System.out.println("PDF generado exitosamente: " + pdfFile.getAbsolutePath());
            return pdfFile;
        } catch (IOException e) {
            System.out.println("Error al guardar el PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Genera un PDF con el recibo de una compra específica.
     * 
     * @param compra La compra para la cual se generará el recibo
     * @return El archivo PDF generado o null si hubo un error
     */
    public File generarReciboPDF(Compra compra) {
        if (compra == null || compra.getIDCompra() == null) {
            System.out.println("Error: Compra no válida para generar recibo PDF.");
            return null;
        }
        
        String nombreArchivo = "recibo_compra_" + compra.getIDCompra() + ".pdf";
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            int yPosition = MARGIN_TOP;
            
            // Título del recibo
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("RECIBO DE COMPRA");
            contentStream.endText();
            yPosition -= 30;
            
            // Información básica de la compra
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Número de Compra: " + compra.getIDCompra());
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Fecha: " + compra.getfecha().format(DATE_FORMATTER));
            contentStream.endText();
            yPosition -= LINE_HEIGHT;
            
            // Información del cliente
            Cliente cliente = compra.getCliente();
            if (cliente != null) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                contentStream.showText("Cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
                
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                contentStream.showText("ID Cliente: " + cliente.getID());
                contentStream.endText();
                yPosition -= 30;
            }
            
            // Línea separadora
            contentStream.setLineWidth(1f);
            contentStream.moveTo(MARGIN_LEFT, yPosition);
            contentStream.lineTo(MARGIN_LEFT + 500, yPosition);
            contentStream.stroke();
            yPosition -= 15;
            
            // Encabezados de productos
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Producto");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT + 200, yPosition);
            contentStream.showText("Tipo");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT + 350, yPosition);
            contentStream.showText("Precio");
            contentStream.endText();
            yPosition -= 15;
            
            // Línea separadora
            contentStream.setLineWidth(0.5f);
            contentStream.moveTo(MARGIN_LEFT, yPosition);
            contentStream.lineTo(MARGIN_LEFT + 500, yPosition);
            contentStream.stroke();
            yPosition -= 15;
            
            // Productos comprados
            for (Snack snack : compra.getSnacksComprados().values()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                contentStream.showText(snack.getNombre());
                contentStream.endText();
                
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT + 200, yPosition);
                contentStream.showText(snack.getTipo());
                contentStream.endText();
                
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT + 350, yPosition);
                contentStream.showText("$" + String.format("%.2f", snack.getPrecio()));
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
            }
            
            // Línea separadora
            yPosition -= 5;
            contentStream.setLineWidth(1f);
            contentStream.moveTo(MARGIN_LEFT, yPosition);
            contentStream.lineTo(MARGIN_LEFT + 500, yPosition);
            contentStream.stroke();
            yPosition -= 20;
            
            // Total
            BigDecimal total = compra.calcularTotal();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.newLineAtOffset(MARGIN_LEFT + 250, yPosition);
            contentStream.showText("TOTAL: $" + String.format("%.2f", total));
            contentStream.endText();
            yPosition -= 30;
            
            // Mensaje de agradecimiento
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("¡Gracias por su compra!");
            contentStream.endText();
            
            contentStream.close();
        } catch (IOException e) {
            System.out.println("Error al generar el recibo PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        // Guardar el documento PDF
        File pdfFile = new File(nombreArchivo);
        try {
            document.save(pdfFile);
            document.close();
            System.out.println("Recibo PDF generado exitosamente: " + pdfFile.getAbsolutePath());
            return pdfFile;
        } catch (IOException e) {
            System.out.println("Error al guardar el recibo PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Genera un PDF con el reporte de ventas de snacks.
     * 
     * @param snacks La tabla de snacks para generar el reporte
     * @return El archivo PDF generado o null si hubo un error
     */
    public File generarReporteInventarioPDF(Table<Integer, String, Snack> snacks) {
        if (snacks == null || snacks.isEmpty()) {
            System.out.println("Error: No hay snacks para generar el reporte de inventario.");
            return null;
        }
        
        String nombreArchivo = "reporte_inventario_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            int yPosition = MARGIN_TOP;
            
            // Título del reporte
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("REPORTE DE INVENTARIO DE SNACKS");
            contentStream.endText();
            yPosition -= 30;
            
            // Fecha del reporte
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Fecha de generación: " + LocalDateTime.now().format(DATE_FORMATTER));
            contentStream.endText();
            yPosition -= 30;
            
            // Encabezados de la tabla
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("ID");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT + 50, yPosition);
            contentStream.showText("Nombre");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT + 200, yPosition);
            contentStream.showText("Tipo");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT + 300, yPosition);
            contentStream.showText("Precio");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT + 400, yPosition);
            contentStream.showText("Stock");
            contentStream.endText();
            yPosition -= 15;
            
            // Línea separadora
            contentStream.setLineWidth(1f);
            contentStream.moveTo(MARGIN_LEFT, yPosition);
            contentStream.lineTo(MARGIN_LEFT + 500, yPosition);
            contentStream.stroke();
            yPosition -= 15;
            
            // Listar todos los snacks
            for (Table.Cell<Integer, String, Snack> cell : snacks.cellSet()) {
                Snack snack = cell.getValue();
                
                // Verificar si necesitamos una nueva página
                if (yPosition < 100) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = MARGIN_TOP;
                    
                    // Repetir encabezados en la nueva página
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                    contentStream.showText("ID");
                    contentStream.endText();
                    
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(MARGIN_LEFT + 50, yPosition);
                    contentStream.showText("Nombre");
                    contentStream.endText();
                    
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(MARGIN_LEFT + 200, yPosition);
                    contentStream.showText("Tipo");
                    contentStream.endText();
                    
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(MARGIN_LEFT + 300, yPosition);
                    contentStream.showText("Precio");
                    contentStream.endText();
                    
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(MARGIN_LEFT + 400, yPosition);
                    contentStream.showText("Stock");
                    contentStream.endText();
                    yPosition -= 15;
                    
                    // Línea separadora
                    contentStream.setLineWidth(1f);
                    contentStream.moveTo(MARGIN_LEFT, yPosition);
                    contentStream.lineTo(MARGIN_LEFT + 500, yPosition);
                    contentStream.stroke();
                    yPosition -= 15;
                }
                
                // ID
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
                contentStream.showText(String.valueOf(snack.getIdSnack()));
                contentStream.endText();
                
                // Nombre
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT + 50, yPosition);
                contentStream.showText(snack.getNombre());
                contentStream.endText();
                
                // Tipo
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT + 200, yPosition);
                contentStream.showText(snack.getTipo());
                contentStream.endText();
                
                // Precio
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT + 300, yPosition);
                contentStream.showText("$" + String.format("%.2f", snack.getPrecio()));
                contentStream.endText();
                
                // Stock
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(MARGIN_LEFT + 400, yPosition);
                contentStream.showText(String.valueOf(snack.getCantidad()));
                contentStream.endText();
                
                yPosition -= LINE_HEIGHT;
            }
            
            // Información final
            yPosition -= 20;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(MARGIN_LEFT, yPosition);
            contentStream.showText("Total de productos en inventario: " + snacks.size());
            contentStream.endText();
            
            contentStream.close();
        } catch (IOException e) {
            System.out.println("Error al generar el reporte de inventario PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        // Guardar el documento PDF
        File pdfFile = new File(nombreArchivo);
        try {
            document.save(pdfFile);
            document.close();
            System.out.println("Reporte de inventario PDF generado exitosamente: " + pdfFile.getAbsolutePath());
            return pdfFile;
        } catch (IOException e) {
            System.out.println("Error al guardar el reporte de inventario PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}