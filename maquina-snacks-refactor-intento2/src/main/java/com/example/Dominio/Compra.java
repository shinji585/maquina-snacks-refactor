 package com.example.Dominio;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
public class Compra {
  private Integer Idcompra;
  private LocalDateTime fecha;
  private Table<Integer, String, Snack> snacksComprados;
  private static int contadorCompras = 1;
  private Cliente cliente;
 
  // creamos un constructo vacio
  public Compra() {
    this.Idcompra = contadorCompras++;
    snacksComprados = HashBasedTable.create();
    this.fecha = LocalDateTime.now(); // fecha actual
  }
 
  // inicializamos el cliente
  public Compra(Cliente cliente) {
    this(); // se llama el constructor vacio
    this.cliente = cliente;
  }
 
  // Método corregido para agregar snacks
  public void agregarSnack(Snack snack) {
    // Usar el ID del snack como rowKey y el nombre como columnKey
    Integer idSnack = snack.getIdSnack();
    String nombreSnack = snack.getNombre();
    
    // Verificar si ya existe el snack en la tabla
    if (snacksComprados.contains(idSnack, nombreSnack)) {
        // Si ya existe, aumentar la cantidad en uno
        Snack existingSnack = snacksComprados.get(idSnack, nombreSnack);
        existingSnack.setCantidad(existingSnack.getCantidad() + 1);
    } else {
        // Si es un nuevo snack, agregarlo con cantidad 1
        // Nota: No modificamos la cantidad original del snack, solo para esta compra
        Snack snackParaCompra = new Snack(
            snack.getIdSnack(),
            snack.getPrecio(),
            snack.getNombre(),
            1, // Cantidad inicial para esta compra es 1
            snack.getTipo()
        );
        snacksComprados.put(idSnack, nombreSnack, snackParaCompra);
    }
  }
 
  // creamos la clase calcular el total de la compra
  public BigDecimal calcularTotal() {
    BigDecimal total = BigDecimal.ZERO;
    for (Snack snack : snacksComprados.values()) {
      // Multiplicar el precio por la cantidad del snack en esta compra
      BigDecimal snackPrice = BigDecimal.valueOf(snack.getPrecio());
      BigDecimal quantity = BigDecimal.valueOf(snack.getCantidad());
      total = total.add(snackPrice.multiply(quantity));
    }
    return total;
  }
 
  // Generamos la estructura para el pdf
  public Table<Integer, String, Object> getDetalles() {
    Table<Integer, String, Object> detalles = HashBasedTable.create();
    int fila = 0;
    // informacion de el head
    detalles.put(fila, "Fecha", fecha.toString());
    detalles.put(fila, "Cliente", this.cliente != null ? cliente.getNombre() : "N/A");
    detalles.put(fila, "ID Compra", this.Idcompra);
    fila++;
    // detalles del producto
    for (Snack snack : snacksComprados.values()) {
      detalles.put(fila, "Producto", snack.getNombre());
      detalles.put(fila, "Precio", snack.getPrecio());
      detalles.put(fila, "Cantidad", snack.getCantidad());
      detalles.put(fila, "Subtotal", snack.getPrecio() * snack.getCantidad());
      detalles.put(fila, "Tipo", snack.getTipo());
      fila++;
    }
    // total al final del pdf
    detalles.put(fila, "Total", calcularTotal());
    return detalles;
  }
 
  // creamos los getters and setters
  public Integer getIDCompra() {
    return this.Idcompra;
  }
 
  public void setIdcompra(Integer idcompra) {
    Idcompra = idcompra;
  }

  public void setFecha(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  public void setSnacksComprados(Table<Integer, String, Snack> snacksComprados) {
    this.snacksComprados = snacksComprados;
  }

  public static void setContadorCompras(int contadorCompras) {
    Compra.contadorCompras = contadorCompras;
  }

  public LocalDateTime getfecha() {
    return this.fecha;
  }
 
  public Table<Integer, String, Snack> getSnacksComprados() {
    return this.snacksComprados;
  }
 
  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }
 
  public Cliente getCliente() {
    return this.cliente;
  }
 
  // metodo para darle un valor especifico a la compra en las clases de servicios 
  public void setIdCompra(Integer idCompra) {
    this.Idcompra = idCompra;
  }
}