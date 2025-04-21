package com.example.Servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.example.Dominio.Snack;

public class ServicioSnacksTable{
   
    // implementamos metodos que estaban en presentacion para tener una separacion de logica 
      public  void comprarSnack(Scanner s, List<Snack> productos, IservicioSnakcs serviciosnacks) {
        System.out.print("¿Quieres comprar un snack? (si/no): ");
        String respuesta = s.nextLine().trim().toLowerCase();
    
        if (respuesta.equals("si")) {
            System.out.print("¿Qué snack quieres comprar (id)?: ");
            int idSnack = Integer.parseInt(s.nextLine());
    
            // Usar comprarSnack2 que devuelve el Snack comprado
            Snack snackComprado = serviciosnacks.comprarSnack2(idSnack);
    
            // Si la compra fue exitosa, agregamos el snack a la lista de productos
            if (snackComprado != null) {
                // Creamos un nuevo objeto Snack para agregarlo a la lista y evitar problemas de referencia
                Snack snackParaLista = new Snack(
                    snackComprado.getIdSnack(),
                    snackComprado.getPrecio(),
                    snackComprado.getNombre(),
                    1, // Cantidad 1 porque compramos uno
                    snackComprado.getTipo()
                );
                productos.add(snackParaLista);
                System.out.println("Compra realizada con éxito. Snack agregado al ticket.");
            } else {
                System.out.println("No se pudo realizar la compra.");
            }
        } else {
            System.out.println("No se ha realizado ninguna compra.");
        }
    }
    
     public  void mostrarTicket(List<Snack> productos) {
        String ticket = "*** Ticket de Venta ***\n";
        double total = 0;
        Map<String, Integer> cantidadSnacks = new HashMap<>();
    
        // Contabiliza las cantidades de cada snack
        for (Snack snack : productos) {
            cantidadSnacks.put(snack.getNombre(), cantidadSnacks.getOrDefault(snack.getNombre(), 0) + 1);
        }
    
        // Muestra los snacks y sus cantidades
        for (Map.Entry<String, Integer> entry : cantidadSnacks.entrySet()) {
            String nombreSnack = entry.getKey();
            int cantidad = entry.getValue();
            double precioSnack = productos.stream()
                    .filter(snack -> snack.getNombre().equals(nombreSnack))
                    .findFirst()
                    .get()
                    .getPrecio();
            ticket += "\n\t- " + nombreSnack + " x" + cantidad + " - $" + precioSnack * cantidad;
            total += precioSnack * cantidad;
        }
    
        ticket += "\n\tTotal -> $" + total;
        System.out.println(ticket);
    }

    public  void agregarSnack(Scanner s, IservicioSnakcs sIservicioSnakcs) {
        System.out.print("Ingrese el nombre del snack nuevo: ");
        String nombre = s.nextLine();
        
        double precio = 0;
        // Validación del precio
        while (true) {
            System.out.print("Ingrese el precio del snack nuevo: ");
            try {
                precio = Double.parseDouble(s.nextLine());
                if (precio <= 0) {
                    System.out.println("El precio debe ser mayor que cero. Intenta de nuevo.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un valor numérico válido.");
            }
        }
        
        int cantidad = 0;
        // Validación de la cantidad
        while (true) {
            System.out.print("Ingrese la cantidad del producto a agregar: ");
            try {
                cantidad = Integer.parseInt(s.nextLine());
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor que cero. Intenta de nuevo.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un valor numérico válido.");
            }
        }
        
        System.out.print("Ingrese el tipo de producto a agregar: ");
        String tipo = s.nextLine();
    
        // Agregar el snack
        sIservicioSnakcs.agregarSnack(new Snack(precio, nombre, cantidad, tipo));
        System.out.println("¡Tu snack se ha agregado correctamente!");
    
        // Mostrar snacks actualizados
        sIservicioSnakcs.mostrarSnacks();
    } 
}