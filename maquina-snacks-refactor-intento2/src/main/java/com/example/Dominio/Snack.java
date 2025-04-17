package com.example.Dominio;

import java.io.Serializable;

public class Snack implements Serializable {
    private double precio;
    private String nombre;
    private static int contadorsnacks = 1;
    private int idSnack;

    public Snack() {
    };

    public Snack(double precio, String nombre) {
        this.idSnack = contadorsnacks++;
        this.precio = precio;
        this.nombre = nombre;
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

    // creamos los getters and setters

 

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(precio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + idSnack;
        return result;
    }

    @Override
    public String toString() {
        return "Snack [precio=" + precio + ", nombre=" + nombre + ", idSnack=" + idSnack + "]";
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
        return true;
    }
}
