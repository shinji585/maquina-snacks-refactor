package com.example.Servicio;



import com.example.Dominio.Snack;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class ServicioSnacksTable  implements IservicioSnakcs{
    private static final Table<Integer,String,Snack> snacks;

    // bloque de tipo statico inicializador 
    static{
        snacks = TreeBasedTable.create();
        snacks.put(1, "Chocolatina", new Snack(2.50, "Chocolatina", 10, "Dulce"));
        snacks.put(2, "Galletas", new Snack(1.80, "Galletas", 15, "Dulce"));
        snacks.put(3, "Doritos", new Snack(3.00, "Doritos", 8, "Salado"));
        System.out.println("Snacks cargados: " + snacks.size()); 
    }

    @Override
    public  void agregarSnack(Snack snack){
        snacks.put(snack.getIdSnack(), snack.getNombre(), snack);
    }
    @Override
    public  void mostrarSnacks(){
        System.out.println("----- Snacks en el inventario -----");
        if (snacks.isEmpty()) {
            System.out.println("No hay snacks cargados.");
        } else {
            for (Table.Cell<Integer,String,Snack> celda: snacks.cellSet()){
                Integer cID = celda.getRowKey();
                String nombre = celda.getColumnKey();
                Snack snackt = celda.getValue();

                System.out.println("ID: " + cID + " | Nombre: " + nombre + " | Snack: " + snackt);
            }
        }
    }
    
    @Override
    public  Table<Integer,String,Snack> getSnacks(){
        return snacks;
    }
}
