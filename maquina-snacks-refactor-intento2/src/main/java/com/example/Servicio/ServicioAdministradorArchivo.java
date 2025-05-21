package com.example.Servicio;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Dominio.Cliente;
import com.example.Dominio.Snack;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServicioAdministradorArchivo implements IservicioAdministrador {
    private static final String ARCHIVO_SNACKS_JSON = "snacks.json";
    private static final String ARCHIVO_CLIENTES_JSON = "clientes.json";

    private final File archivoSnacks;
    private final File archivoClientes;
    private final Gson gson;

    private final Map<Integer, Snack> snacks;
    private final Map<Integer, Cliente> clientes;

    public ServicioAdministradorArchivo() {
        this.snacks = new HashMap<>();
        this.clientes = new HashMap<>();
        this.archivoSnacks = new File(ARCHIVO_SNACKS_JSON);
        this.archivoClientes = new File(ARCHIVO_CLIENTES_JSON);
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        inicializarArchivoSnacks();
        inicializarArchivoClientes();
    }

    // Métodos para Snacks
    private void inicializarArchivoSnacks() {
        try {
            if (!archivoSnacks.exists()) {
                archivoSnacks.createNewFile();
                guardarSnacksEnArchivo();
            } else {
                cargarSnacks();
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo de snacks", e);
        }
    }

    private void cargarSnacks() {
        try (FileReader reader = new FileReader(archivoSnacks)) {
            if (archivoSnacks.length() == 0) return;
            Type tipoMapa = new TypeToken<Map<Integer, Snack>>(){}.getType();
            Map<Integer, Snack> snacksCargados = gson.fromJson(reader, tipoMapa);
            if (snacksCargados != null) {
                snacks.clear();
                snacks.putAll(snacksCargados);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar snacks: " + e.getMessage());
        }
    }

    private void guardarSnacksEnArchivo() {
        try (FileWriter writer = new FileWriter(archivoSnacks)) {
            gson.toJson(snacks, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar snacks", e);
        }
    }

    @Override
    public void crearSnack(Snack snack) {
        snacks.put(snack.getIdSnack(), snack);
        guardarSnacksEnArchivo();
    }

    @Override
    public List<Snack> listarSnacks() {
        return new ArrayList<>(snacks.values());
    }

    @Override
    public boolean actualizarSnack(Snack snack) {
        if (!snacks.containsKey(snack.getIdSnack())) return false;
        snacks.put(snack.getIdSnack(), snack);
        guardarSnacksEnArchivo();
        return true;
    }

    @Override
    public boolean eliminarSnack(int idSnack) {
        if (snacks.remove(idSnack) != null) {
            guardarSnacksEnArchivo();
            return true;
        }
        return false;
    }

    @Override
    public Snack buscarSnackPorId(int idSnack) {
        return snacks.get(idSnack);
    }

    @Override
    public List<Snack> buscarSnackPorNombre(String nombre) {
        List<Snack> resultado = new ArrayList<>();
        for (Snack s : snacks.values()) {
            if (s.getNombre().equalsIgnoreCase(nombre)) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    @Override
    public void restaurarStock(int idSnack, int cantidad) {
        Snack snack = snacks.get(idSnack);
        if (snack != null) {
            snack.setCantidad(snack.getCantidad() + cantidad);
            guardarSnacksEnArchivo();
        }
    }

    @Override
    public void verReporteInventario() {
        for (Snack s : snacks.values()) {
            System.out.println(s);
        }
    }

    // Métodos para Clientes
    private void inicializarArchivoClientes() {
        try {
            if (!archivoClientes.exists()) {
                archivoClientes.createNewFile();
                guardarClientesEnArchivo();
            } else {
                cargarClientes();
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo de clientes", e);
        }
    }

    private void cargarClientes() {
        try (FileReader reader = new FileReader(archivoClientes)) {
            if (archivoClientes.length() == 0) return;
            Type tipoMapa = new TypeToken<Map<Integer, Cliente>>(){}.getType();
            Map<Integer, Cliente> clientesCargados = gson.fromJson(reader, tipoMapa);
            if (clientesCargados != null) {
                clientes.clear();
                clientes.putAll(clientesCargados);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        }
    }

    private void guardarClientesEnArchivo() {
        try (FileWriter writer = new FileWriter(archivoClientes)) {
            gson.toJson(clientes, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar clientes", e);
        }
    }

    @Override
    public void crearCliente(Cliente cliente) {
        clientes.put(cliente.getID(), cliente);
        guardarClientesEnArchivo();
    }

    @Override
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    @Override
    public boolean actualizarCliente(Cliente cliente) {
        if (!clientes.containsKey(cliente.getID())) return false;
        clientes.put(cliente.getID(), cliente);
        guardarClientesEnArchivo();
        return true;
    }

    @Override
    public boolean eliminarCliente(int idCliente) {
        if (clientes.remove(idCliente) != null) {
            guardarClientesEnArchivo();
            return true;
        }
        return false;
    }

    @Override
    public Cliente buscarClientePorId(int idCliente) {
        return clientes.get(idCliente);
    }

    @Override
    public List<Cliente> buscarClientePorNombre(String nombre) {
        List<Cliente> resultado = new ArrayList<>();
        for (Cliente c : clientes.values()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}