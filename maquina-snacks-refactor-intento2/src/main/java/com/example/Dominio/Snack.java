package com.example.Dominio;

import java.io.Serializable;


public class Snack implements Serializable {
    private double precio;
    private String nombre;
    private static int contadorsnacks = 1;
    private int idSnack;
    private int cantidad; 
    private String tipo;

    public Snack() {
    };

    public Snack(double precio, String nombre,int cantidad,String tipo) {
        this.idSnack = contadorsnacks++;
        this.precio = precio;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public Snack(int id,double precio, String nombre,int cantidad,String tipo) {
        this.idSnack = contadorsnacks++;
        this.precio = precio;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipo = tipo;
        if (id >= contadorsnacks){
           contadorsnacks = id + 1; 
        }
    }

    // metodos getters and setters 
    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public int getCantidad(){
        return this.cantidad;
    }

    public String getTipo(){
        return this.tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdSnack() {
        return idSnack;
    }

    // creamos y sobre escribimos metodos importantes 

    public static Snack fromString(String line){
     String[] partes = line.split(",");
     int id = Integer.parseInt(partes[0]);
     String tipo = partes[1];
     String nombre = partes[2];
     int cantidad = Integer.parseInt(partes[3]);
     double precio = Double.parseDouble(partes[4]);

     return new Snack(id, precio, nombre, cantidad, tipo);
    }


    @Override
    public String toString() {
        return getIdSnack() + "," + getTipo() + "," + getNombre() + "," + getCantidad() + "," + getPrecio();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(precio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + idSnack;
        result = prime * result + cantidad;
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Snack other = (Snack) obj;
        if (Double.doubleToLongBits(precio) != Double.doubleToLongBits(other.precio))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (idSnack != other.idSnack)
            return false;
        if (cantidad != other.cantidad)
            return false;
        if (tipo == null) {
            if (other.tipo != null)
                return false;
        } else if (!tipo.equals(other.tipo))
            return false;
        return true;
    }
   
}
