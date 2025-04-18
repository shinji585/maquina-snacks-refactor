package com.example.Presentacion;

import java.util.*;

import com.example.Dominio.*;
import com.example.Servicio.*;
import com.google.common.collect.Table.Cell;


public class MsquinaSnakcs {
    public static void main(String[] args) {
        maquinaSnaks(); // <-- Llama al método para ejecutarlo
    }

    public static void maquinaSnaks() {
        boolean salir = false;
        var consola = new Scanner(System.in);
        IservicioSnakcs sIservicioSnakcs = new ServicioSnacksTable();
        // creamos la lista de tipo snack

        List<Snack> produList = new ArrayList<>();
        System.out.println("*** Maquina de Snacks ***");
        sIservicioSnakcs.mostrarSnacks(); // Muestra el inventario
        while (!salir) {
            try {
                var option = mostrarMenu(consola);
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
                1. comprar snakc
                2. Mostrar ticket
                3. Agregar nuevo snakc
                4. Salir
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

    private static void comprarSnack(Scanner s, List<Snack> productos,IservicioSnakcs serviciosnacks) {
        System.out.print("que esnack quieres comprar (id)?: ");
        int idSnack = Integer.parseInt(s.nextLine());

        // validamos que el snack exista en la table de snacks
        boolean snackEncontrador = false;
       for (Cell<Integer, String, Snack> cell: serviciosnacks.getSnacks().cellSet()){
           if (cell.getRowKey() == idSnack){
            Snack snack = cell.getValue();
            System.out.println("Snack encontrado: " + snack);
            productos.add(snack);
            snackEncontrador = true;
            break;
           }
       }
        if (!snackEncontrador) {
            System.out.println("id de snakc no encontrado: " + idSnack);
        }
    }

    private static void mostrarTicket(List<Snack> productos) {
        String ticket = "*** ticket de venta ***";
        double total = 0;
        for (Snack snack : productos) {
            ticket += "\n\t- " + snack.getNombre() + " - $" + snack.getPrecio();
            total += snack.getPrecio();
        }
        ticket += "\n\tTotal -> $" + total;
        System.out.println(ticket);
    }

    private static void agregarSnack(Scanner s,IservicioSnakcs sIservicioSnakcs) {
        System.out.print("ingrese el nombre del snack nuevo: ");
        String nombre = s.nextLine();
        System.out.print("ingrese el precio del snakc nuevo: ");
        double precio = Double.parseDouble(s.nextLine());
        sIservicioSnakcs.agregarSnack(new Snack(precio, nombre, 0, nombre));
        System.out.println("tu snack se ha agregado correctamente");
        sIservicioSnakcs.mostrarSnacks();
    }
}
