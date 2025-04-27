package com.example.Presentacion;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.Dominio.Cliente;
import com.example.Dominio.Snack;
import com.example.Servicio.IservicioCliente;
import com.example.Servicio.IservicioSnakcs;
import com.example.Servicio.ServicioClienteArchivo;
import com.example.Servicio.ServicioSnacksArchivo;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando pruebas de ServicioClienteArchivo");
        
        // Crear servicio de clientes y snacks
        IservicioCliente servicioCliente = new ServicioClienteArchivo();
        IservicioSnakcs servicioSnacks = new ServicioSnacksArchivo();
        
        // Registramos algunos snacks en el sistema primero
        Snack snack1 = new Snack();
        snack1.setNombre("Papas fritas");
        snack1.setPrecio(1.50);
        snack1.setTipo("Salado");
        
        Snack snack2 = new Snack();
        snack2.setNombre("Chocolate");
        snack2.setPrecio(2.00);
        snack2.setTipo("Dulce");
        
        // Añadir snacks al servicio (asumiendo que hay un método para esto)
        servicioSnacks.agregarSnack(snack1);;
        servicioSnacks.agregarSnack(snack2);;
        
        // Crear y registrar un cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Pérez");
        try {
            cliente.recargarSaldo(new BigDecimal("10.00"));
        } catch (Exception e) {
            System.out.println("error de tipo: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        System.out.println("cliente:  " + cliente.getNombre());
        
        System.out.println("\n1. Registrando cliente...");
        boolean registroExitoso = servicioCliente.registrarCliente(cliente);
        System.out.println("Cliente registrado: " + registroExitoso);
        
        // Buscar cliente
        System.out.println("\n2. Buscando cliente...");
        Cliente clienteEncontrado = servicioCliente.buscarCliente(101);
        System.out.println("Cliente encontrado: " + (clienteEncontrado != null ? clienteEncontrado.getNombre() : "No encontrado"));
        
        // Recargar saldo
        System.out.println("\n3. Recargando saldo...");
        boolean recargaExitosa = servicioCliente.recargarSaldo(101, new BigDecimal("20.00"));
        System.out.println("Saldo recargado: " + recargaExitosa);
        clienteEncontrado = servicioCliente.buscarCliente(101);
        System.out.println("Nuevo saldo: " + (clienteEncontrado != null ? clienteEncontrado.verSaldo() : "Cliente no encontrado"));
        
        // Realizar compra
        System.out.println("\n4. Realizando compra...");
        boolean compraExitosa = servicioCliente.realizarCompra(101, 1);
        System.out.println("Compra realizada: " + compraExitosa);
        
        // Guardar historial
        System.out.println("\n5. Guardando historial...");
        boolean historialGuardado = servicioCliente.guardarHistorial(101);
        System.out.println("Historial guardado: " + historialGuardado);
        
        // Generar PDF de historial
        System.out.println("\n6. Generando PDF de historial...");
        java.io.File pdfGenerado = servicioCliente.generarPDFHistorial(101);
        System.out.println("PDF generado: " + (pdfGenerado != null ? pdfGenerado.getAbsolutePath() : "Error al generar PDF"));
        
        // Realizar otra compra
        System.out.println("\n7. Realizando otra compra...");
        compraExitosa = servicioCliente.realizarCompra(101, 2);
        System.out.println("Segunda compra realizada: " + compraExitosa);
        historialGuardado = servicioCliente.guardarHistorial(101);
        System.out.println("Historial actualizado: " + historialGuardado);
        
        // Generar PDF actualizado
        System.out.println("\n8. Generando PDF de historial actualizado...");
        pdfGenerado = servicioCliente.generarPDFHistorial(101);
        System.out.println("PDF actualizado generado: " + (pdfGenerado != null ? pdfGenerado.getAbsolutePath() : "Error al generar PDF"));
        
        System.out.println("\nPruebas completadas");
    }
}