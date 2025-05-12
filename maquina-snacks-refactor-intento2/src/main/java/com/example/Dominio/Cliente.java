package com.example.Dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Cliente extends Persona {

    private int clienteID;
    private static int id = 1; // Variable estática para asignar IDs incrementales
    private BigDecimal saldo;
    private Table<LocalDateTime, Integer, Compra> historialCompras; 

    // creamos un metodo privado inicializador 
    private void inicializadorCliente(){
          this.historialCompras = HashBasedTable.create();
    }

    public Cliente(){
        inicializadorCliente();
        this.clienteID = id++; // Solo incrementamos id automáticamente en el constructor vacío
        this.saldo = BigDecimal.ZERO;
    }

    // Constructor para definir los datos del cliente
    public Cliente(String nombre, String edad, String apellidos, boolean sexo, String nacionalidad, String direccion, String correoElectronico, String numeroTelefono, BigDecimal saldo) throws Exception {
        super(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono);
        inicializadorCliente();
        this.clienteID = id++; // Asignamos el ID automáticamente y lo incrementamos
        if (saldo.compareTo(BigDecimal.ZERO) < 0){
            throw new Exception("no puede ingresar un saldo menor a cero");
        }else{
            this.saldo = saldo;
        }
    }

    // Constructor adicional que permite especificar un ID
    public Cliente(int clienteID, String nombre, String edad, String apellidos, boolean sexo, String nacionalidad, String direccion, String correoElectronico, String numeroTelefono, BigDecimal saldo) throws Exception {
        super(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono);
        inicializadorCliente();
        this.clienteID = clienteID;
        if (clienteID >= id) {
            id = clienteID + 1; // Actualizamos el contador estático si es necesario
        }
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
    public boolean realizarCompra(Snack snack) throws Exception {
        if (snack != null && snack.getCantidad() > 0) {
            // Convertimos el precio del snack a BigDecimal para comparar con el saldo
            BigDecimal precioSnack = new BigDecimal(snack.getPrecio());
            
            // Verificamos si el saldo es suficiente
            if (this.saldo.compareTo(precioSnack) < 0) {
                throw new Exception("No tiene saldo suficiente para realizar la compra");
            } else {
                // Actualizamos el saldo del cliente
                this.saldo = this.saldo.subtract(precioSnack);
                
                // Actualizamos la cantidad del snack
                snack.setCantidad(snack.getCantidad() - 1);
                
                System.out.println("Compra realizada con éxito. Saldo restante: $" + this.saldo);
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

    // Método para establecer el ID manualmente
    public void setID(int id) {
        this.clienteID = id;
    }

    public void setSaldo(BigDecimal nuevoSaldo) {
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }
        this.saldo = nuevoSaldo;
    }
    
    // Método estático para reiniciar el contador de ID
    public static void reiniciarContador() {
        id = 1;
    }
    
    // Método estático para establecer el contador de ID a un valor específico
    public static void establecerContador(int nuevoValor) {
        if (nuevoValor > 0) {
            id = nuevoValor;
        }
    }
    
    // Método estático para obtener el valor actual del contador
    public static int obtenerContadorActual() {
        return id;
    }
}