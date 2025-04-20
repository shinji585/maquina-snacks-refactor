package com.example.Presentacion;

import java.util.*;

import com.example.Dominio.*;
import com.example.Servicio.*;


public class MsquinaSnakcs {
    public static void main(String[] args) {
        maquinaSnaks(); // <-- Llama al método para ejecutarlo
    }

    public static void maquinaSnaks() {
        boolean salir = false;
        var consola = new Scanner(System.in);
        IservicioSnakcs sIservicioSnakcs = new ServicioSnacksArchivo();
        // creamos la lista de tipo snack

        List<Snack> produList = new ArrayList<>();
        System.out.println("\t\t\t\t***  Maquina de Snacks   ***\n");
        sIservicioSnakcs.mostrarSnacks(); // Muestra el inventario
        while (!salir) {
            try {

                var option = mostrarMenu(consola);
                sIservicioSnakcs.mostrarSnacks();
                salir = ejecutarOpciones(option, consola, produList,sIservicioSnakcs);

            } catch (Exception e) {
                System.out.println("ocurrio un error: " + e.getMessage());
            } finally {
                System.out.println();
            }
        }
    }

    private static int mostrarMenu(Scanner consola) {
        System.out.print("""
                Menu:
                \t1. comprar snakc
                \t2. Mostrar ticket
                \t3. Agregar nuevo snakc
                \t4. Salir
                Elig una opcion: \s""");
        return Integer.parseInt(consola.nextLine());
    }

    private static boolean ejecutarOpciones(int option, Scanner s, List<Snack> produto, IservicioSnakcs serviciosnakcs) {
        boolean salir = false;
        switch (option) {
            case 1 -> {
                comprarSnack(s, produto,serviciosnakcs);
            }
            case 2 -> {
                mostrarTicket(produto);
            }
            case 3 -> {
                agregarSnack(s,serviciosnakcs);
            }
            case 4->{
                System.out.println("regresa pronto!");
                salir = true;
            }
            default ->{
                System.out.println("opcion invalidad: " + option);
            }
        }
        return salir;
    }

    private static void comprarSnack(Scanner s, List<Snack> productos, IservicioSnakcs serviciosnacks) {
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
    
    private static void mostrarTicket(List<Snack> productos) {
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
    

    private static void agregarSnack(Scanner s, IservicioSnakcs sIservicioSnakcs) {
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
