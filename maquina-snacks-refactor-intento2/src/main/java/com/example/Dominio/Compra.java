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

  // creamos los metdos
  public void agregarSnack(Snack snack) {
    if (snack != null && snack.getCantidad() > 0) {
      snacksComprados.put(snack.getIdSnack(), snack.getNombre(), snack);
    }
  }

  // creamos la clase calcular el total de la compra
  public BigDecimal calcularTotal() {
    BigDecimal total = BigDecimal.ZERO;
    for (Snack snack : snacksComprados.values()) {
      total = total.add(BigDecimal.valueOf(snack.getPrecio()));
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

}
