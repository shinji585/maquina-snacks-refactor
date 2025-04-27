package com.example.Servicio;

import java.time.LocalDateTime;

import com.example.Dominio.Compra;
import com.google.common.collect.Table;

public interface IservicioCompra {
    public Compra crearCompra(int idCliente);
    public boolean finalizarCompra(Compra compra);
    public boolean cancelarCompra(int idCompra);
    public Table<LocalDateTime,Integer,Compra> obtenerComprasporCliente(int idCliente);
    public String generarReciboCompra(int idCompra);

}
