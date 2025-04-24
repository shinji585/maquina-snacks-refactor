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
    private Table<LocalDateTime,Integer,Compra> historialCompras; 

    // creamos un metodo privado inicializador 
    private void inicializadorCliente(){
          // aumentamos el id del cliente en cada instanica
          this.clienteID = id++;
          this.historialCompras = HashBasedTable.create();
    }

    public Cliente(){
        inicializadorCliente();
    }

    // constructo para definir los datos del cliente 
    public Cliente(String nombre, int edad, String apellidos, boolean sexo, String nacionalidad, String direccion,String correoElectronico, int numeroTelefono,BigDecimal saldo) throws Exception {
        super(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono);
        inicializadorCliente();
        if ( saldo.compareTo(BigDecimal.ZERO) < 0){
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
           this.saldo =  this.saldo.add(monto);
        }
    }

    // creamos el metodo de versaldo 
    public BigDecimal verSaldo(){
         return this.saldo;
    }

    // creamos el metodo realizarcompra 
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
    public void agregarcomprahistorial(Compra compra){
        if (!compra.getDetalles().isEmpty() && compra != null){
             // si no esta vacio agregamos a la tabla 
             historialCompras.put(compra.getfecha(), compra.getIDCompra(), compra);
        }
    }

    // creamos la clase verhistorial 
    public Table<LocalDateTime,Integer,Compra> verhistorialCompras() throws Exception{
        // creamos una validacion que verifique que la tabla no esta vacia para no tener errores y que si esta esta vacia mande una exepcion 
        if (this.historialCompras.isEmpty()){
            throw new Exception("No tiene productos comprados hasta el momento...");
        }else{
            // ahora creamos una variable local para el caso y recorremos la tabla 
            Table<LocalDateTime,Integer,Compra> retonrarHistorial = HashBasedTable.create();
            historialCompras.cellSet().stream().forEach(celda -> {
                LocalDateTime fecha = celda.getRowKey();
                Integer idCompra = celda.getColumnKey();
                Compra valor = celda.getValue();
                // agregamos los datos a la tabla 
                retonrarHistorial.put(fecha, idCompra, valor);
            });
            // retornamos el valor 
            return retonrarHistorial;
        }
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

}
