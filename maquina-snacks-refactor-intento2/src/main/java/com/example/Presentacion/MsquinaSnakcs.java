package com.example.Presentacion;

import java.util.*;

import com.example.Dominio.*;
import com.example.Servicio.*;


public class MsquinaSnakcs {
    // creamos un objecto de ServicioSnacksTable que se encargara de la logica de negocio 
    static ServicioSnacksTable servicio = new ServicioSnacksTable();
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
                servicio.comprarSnack(s, produto, serviciosnakcs);
            }
            case 2 -> {
                servicio.mostrarTicket(produto);
            }
            case 3 -> {
                servicio.agregarSnack(s,serviciosnakcs);
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
}
