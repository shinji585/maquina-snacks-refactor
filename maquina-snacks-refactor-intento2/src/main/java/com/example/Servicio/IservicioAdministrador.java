package com.example.Servicio;

import java.util.List;

import com.example.Dominio.Cliente;
import com.example.Dominio.Snack;

public interface IservicioAdministrador {
    // Métodos para snacks
    void crearSnack(Snack snack);
    List<Snack> listarSnacks();
    boolean actualizarSnack(Snack snack);
    boolean eliminarSnack(int idSnack);
    Snack buscarSnackPorId(int idSnack);
    List<Snack> buscarSnackPorNombre(String nombre);
    void restaurarStock(int idSnack, int cantidad);
    void verReporteInventario();

    // Métodos para clientes
    void crearCliente(Cliente cliente);
    List<Cliente> listarClientes();
    boolean actualizarCliente(Cliente cliente);
    boolean eliminarCliente(int idCliente);
    Cliente buscarClientePorId(int idCliente);
    List<Cliente> buscarClientePorNombre(String nombre);
}