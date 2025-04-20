package com.example.Servicio;

import com.example.Dominio.Snack;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class ServicioSnacksTable implements IservicioSnakcs {
    private static final Table<Integer, String, Snack> snacks;

    // Bloque de inicialización estática para cargar snacks iniciales
    static {
        snacks = TreeBasedTable.create();
        snacks.put(1, "Chocolatina", new Snack(2.50, "Chocolatina", 10, "Dulce"));
        snacks.put(2, "Galletas", new Snack(1.80, "Galletas", 15, "Dulce"));
        snacks.put(3, "Doritos", new Snack(3.00, "Doritos", 8, "Salado"));
        System.out.println("Snacks cargados: " + snacks.size());
    }

    @Override
    public void agregarSnack(Snack snack) {
        // Verificamos que no haya un snack con el mismo ID
        if (snacks.contains(snack.getIdSnack(), snack.getNombre())) {
            System.out.println("Ya existe un snack con este ID y nombre.");
        } else {
            snacks.put(snack.getIdSnack(), snack.getNombre(), snack);
            System.out.println("Snack agregado correctamente: " + snack.getNombre());
        }
    }

    @Override
    public void mostrarSnacks() {
        System.out.println("----- Snacks en el inventario -----");
        if (snacks.isEmpty()) {
            System.out.println("No hay snacks cargados.");
        } else {
            for (Table.Cell<Integer, String, Snack> celda : snacks.cellSet()) {
                Integer cID = celda.getRowKey();
                String nombre = celda.getColumnKey();
                Snack snack = celda.getValue();

                System.out.printf("ID: %d | Nombre: %s | Precio: %.2f | Cantidad: %d | Tipo: %s\n",
                        cID, nombre, snack.getPrecio(), snack.getCantidad(), snack.getTipo());
            }
        }
    }

    @Override
    public Table<Integer, String, Snack> getSnacks() {
        return snacks;
    }

    @Override
    public boolean comprarSnack(int id) {
        // Buscamos el snack por ID
        for (Table.Cell<Integer, String, Snack> celda : snacks.cellSet()) {
            if (celda.getRowKey() == id) {
                Snack snack = celda.getValue();

                // Verificamos si hay stock disponible
                if (snack.getCantidad() > 0) {
                    snack.setCantidad(snack.getCantidad() - 1); // Reducimos la cantidad
                    System.out.println("Compra realizada con éxito! Has comprado: " + snack.getNombre());
                    return true;
                } else {
                    System.out.println("Lo sentimos, no hay stock disponible de ese snack.");
                    return false;
                }
            }
        }
        System.out.println("Snack con ID " + id + " no encontrado.");
        return false;
    }

    @Override
    public Snack comprarSnack2(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'comprarSnack2'");
    }

    
}
