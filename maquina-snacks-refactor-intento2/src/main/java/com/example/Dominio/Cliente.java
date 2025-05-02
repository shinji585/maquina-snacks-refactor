package com.example.Dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Cliente extends Persona {

    private int clienteID;
    private static int id = 1;
    private BigDecimal saldo;
    private Table<LocalDateTime, Integer, Compra> historialCompras; 

    // creamos un metodo privado inicializador 
    private void inicializadorCliente(){
          // aumentamos el id del cliente en cada instanica
          this.clienteID = id++;
          this.historialCompras = HashBasedTable.create();
    }

    public Cliente(){
        inicializadorCliente();
        this.saldo = BigDecimal.ZERO;
    }

    // constructo para definir los datos del cliente 
    public Cliente(String nombre, int edad, String apellidos, boolean sexo, String nacionalidad, String direccion,String correoElectronico, long numeroTelefono,BigDecimal saldo) throws Exception {
        super(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono);
        inicializadorCliente();
        if (saldo.compareTo(BigDecimal.ZERO) < 0){
            throw new Exception("no puede ingresar un saldo menor a cero");
        }else{
            this.saldo = saldo;
        }
    }

    // creamos los metodos para nuestra clase 
    public void recargarSaldo(BigDecimal monto) throws Exception{
        if (monto.compareTo(BigDecimal.ZERO) < 0){
            throw new Exception("no se puede agregar al saldo un valor negativo");
        }else{
           if (this.saldo == null){
            this.saldo = BigDecimal.ZERO;
           }
           this.saldo = this.saldo.add(monto);
        }
    }

    // creamos el metodo de versaldo 
    public BigDecimal verSaldo(){
         return this.saldo;
    }

    // creamos el metodo realizarCompra 
    public boolean realizarCompra(Snack snack) throws Exception{
       if (snack != null && snack.getCantidad() > 0){
        if (this.saldo.compareTo(new BigDecimal(snack.getPrecio())) == -1){
            throw new Exception("No tiene saldo para realizar la compra");
        }else if (this.saldo.compareTo(new BigDecimal(snack.getPrecio())) == 0){
            return true;
        }else{
            return true;
        }
       }
       return false;
    }

    // creamos el metodo para agregarcomprahistorial
    public boolean agregarcomprahistorial(Compra compra){
        if (!compra.getDetalles().isEmpty() && compra != null){
             // si no esta vacio agregamos a la tabla 
             historialCompras.put(compra.getfecha(), compra.getIDCompra(), compra);
             return true;
        }
        return false;
    }

    // creamos la clase verhistorial - MODIFIED to not throw Exception
    public Table<LocalDateTime,Integer,Compra> verhistorialCompras(){
        // Return the historial even if empty
        return this.historialCompras;
    }

    // Add setter method for historialCompras
    public void setHistorialCompras(Table<LocalDateTime, Integer, Compra> historialCompras) {
        this.historialCompras = historialCompras;
    }

    // creamo el metodo verHistorialComprasPorFecah
    public List<Compra> verhistorialComprasPorFecha(LocalDateTime fecha){
        // iteramos sobre el historial de compras para ver si las fechas coiciden 
        List<Compra> lista = new ArrayList<>();
        for (Table.Cell<LocalDateTime,Integer,Compra> j : historialCompras.cellSet()) {
            if (j.getRowKey().equals(fecha)){
                // creamos la lista 
                lista.add(j.getValue());
            }
        }
        return lista;
    }

    public int getID(){
        return this.clienteID;
    }

    public void setSaldo(BigDecimal nuevoSaldo) {
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }
        this.saldo = nuevoSaldo;
    }
}