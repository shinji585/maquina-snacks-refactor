package com.example.Servicio;

import java.io.File;
import java.math.BigDecimal;

import com.example.Dominio.Cliente;

public  interface IservicioCliente {

    // creamos los metodos abstractos necesarips para el manejo de archivo del cliente
    public boolean registrarCliente(Cliente cliente);
    public Cliente buscarCliente(int idCliente);
    public boolean realizarCompra(int idcliente,int idsnack);
    public boolean recargarSaldo(int idcliente, BigDecimal slado);
    public boolean guardarHistorial(int idcliente);
    public File generarPDFHistorial(int idcliente);
}
