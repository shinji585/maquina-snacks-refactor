/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Servicio;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.google.common.collect.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Flia Seplveda Mendoz
 */
public class ServicioAdministradorArchivo implements IservicioAdministrador {

    @Override
    public void verProductosVendidos(IservicioCliente servicioCliente) {
        Map<String, Integer> productosVendidos = new HashMap<>();

        for (int id : obtenerIDsClientes(servicioCliente)) {
            Cliente cliente = servicioCliente.buscarCliente(id);
            Table<LocalDateTime, Integer, Compra> historial;
            try {
                historial = cliente.verhistorialCompras();
            } catch (Exception e) {
                continue;
            }

            for (Compra compra : historial.values()) {
                Table<Integer, String, Object> detalles = compra.getDetalles();

                for (Map<String, Object> snackDetalles : detalles.rowMap().values()) {
                    String nombre = snackDetalles.get("Producto").toString();
                    productosVendidos.put(nombre, productosVendidos.getOrDefault(nombre, 0) + 1);
                }
            }
        }

        System.out.println("\n=== Productos Vendidos ===");
        if (productosVendidos.isEmpty()) {
            System.out.println("No hay productos vendidos registrados.");
        } else {
            productosVendidos.forEach((nombre, cantidad)
                    -> System.out.println("- " + nombre + ": " + cantidad + " vendidos"));
        }
    }

    @Override
    public void verGanancias(IservicioCliente servicioCliente) {

        double total = 0;

        for (int id : obtenerIDsClientes(servicioCliente)) {
            Cliente cliente = servicioCliente.buscarCliente(id);
            Table<LocalDateTime, Integer, Compra> historial;
            try {
                historial = cliente.verhistorialCompras();
            } catch (Exception e) {
                continue; // Si no tiene historial o falla, pasamos al siguiente cliente
            }

            for (Compra compra : historial.values()) {
                total += compra.calcularTotal().doubleValue();
            }
        }

        System.out.println("\n=== Ganancias Totales ===");
        System.out.printf("Total de ingresos por ventas: $%.2f%n", total);
    }

    @Override
    public void verReportesOrdenados(IservicioCliente servicioCliente, String criterio) {

        List<Compra> todasLasCompras = new ArrayList<>();

        // Reunimos todas las compras de todos los clientes
        for (int id : obtenerIDsClientes(servicioCliente)) {
            Cliente cliente = servicioCliente.buscarCliente(id);
            Table<LocalDateTime, Integer, Compra> historial;
            try {
                historial = cliente.verhistorialCompras();
            } catch (Exception e) {
                continue;
            }

            todasLasCompras.addAll(historial.values());
        }

        // Ordenamos según el criterio indicado
        switch (criterio.toLowerCase()) {
            case "fecha":
                todasLasCompras.sort(Comparator.comparing(Compra::getfecha));
                break;
            case "cantidad":
                todasLasCompras.sort(Comparator.comparingInt(c -> c.getDetalles().rowKeySet().size()));
                break;
            case "total":
                todasLasCompras.sort(Comparator.comparingDouble(c -> c.calcularTotal().doubleValue()));
                break;
            default:
                System.out.println("❌ Criterio inválido. Usa: fecha, cantidad o total.");
                return;
        }

        // Mostramos los resultados ordenados
        System.out.println("\n=== Reportes Ordenados por " + criterio.toUpperCase() + " ===");
        for (Compra compra : todasLasCompras) {
            System.out.printf("ID Compra: %d | Cliente: %s | Total: $%.2f | Fecha: %s | Items: %d%n",
                    compra.getIDCompra(),
                    compra.getCliente().getNombre(),
                    compra.calcularTotal(),
                    compra.getfecha(),
                    compra.getDetalles().rowKeySet().size()
            );
        }
    }

    private List<Integer> obtenerIDsClientes(IservicioCliente servicioCliente) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {  // 👈 Rango provisional; mejor si tienes una lista real
            Cliente c = servicioCliente.buscarCliente(i);
            if (c != null && c.getNombre() != null) {
                ids.add(i);
            }
        }
        return ids;
    }

}
