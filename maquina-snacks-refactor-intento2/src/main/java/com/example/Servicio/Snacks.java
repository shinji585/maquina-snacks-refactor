package com.example.Servicio;

import java.util.ArrayList;
import java.util.List;

import com.example.Dominio.Snack;
public class Snacks {
    private static  final List<Snack> snacks;

    // bloque de tipo statico inicializador 
    static{
        snacks = new ArrayList<>();
        snacks.add(new Snack(70, "papas"));
        snacks.add(new Snack(50, "refresco"));
        snacks.add(new Snack(120, "sandwich"));
        System.out.println("Snacks cargados: " + snacks.size()); 
    }

    public static void agregarSnack(Snack snack){
        snacks.add(snack);
    }

    public static void mostrarSnacks(){
        System.out.println("----- Snacks en el inventario -----");
        if (snacks.isEmpty()) {
            System.out.println("No hay snacks cargados.");
        } else {
            for (var snack : snacks) {
                System.out.println(snack); 
            }
        }
    }
    
    public static List<Snack> getSnacks(){
        return snacks;
    }
}
