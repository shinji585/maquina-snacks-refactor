package com.example.Servicio;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Dominio.Cliente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raven.model.ModelUser;

/**
 * Clase utilitaria para manejar la coherencia entre diferentes estructuras JSON
 */
public class JSONStructureHelper {
    
    private static final String ARCHIVO_CLIENTE_JSON = "clientes.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * Inicializa el archivo JSON si no existe o está vacío
     */
    public static void inicializarArchivoJSON() {
        File archivo = new File(ARCHIVO_CLIENTE_JSON);
        try {
            if (!archivo.exists() || archivo.length() == 0) {
                // Crea el archivo con una estructura consistente (un Map vacío)
                try (FileWriter writer = new FileWriter(archivo)) {
                    Map<Integer, Cliente> clientesVacios = new HashMap<>();
                    gson.toJson(clientesVacios, writer);
                    System.out.println("Archivo JSON inicializado correctamente");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar archivo JSON: " + e.getMessage());
        }
    }
    
    /**
     * Convierte la estructura JSON existente a Map si es necesario
     */
    public static Map<Integer, Cliente> asegurarMapaClientes() {
        File archivo = new File(ARCHIVO_CLIENTE_JSON);
        Map<Integer, Cliente> mapa = new HashMap<>();
        
        try {
            if (!archivo.exists() || archivo.length() == 0) {
                inicializarArchivoJSON();
                return mapa;
            }
            
            try (FileReader reader = new FileReader(archivo)) {
                // Intenta leer como mapa primero
                Type tipoMapa = new TypeToken<Map<Integer, Cliente>>() {}.getType();
                Object resultado = gson.fromJson(reader, tipoMapa);
                
                if (resultado != null) {
                    // Si es un mapa, úsalo directamente
                    mapa = (Map<Integer, Cliente>) resultado;
                } else {
                    // Si es null, podría ser un formato incorrecto o vacío
                    inicializarArchivoJSON();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer JSON como mapa: " + e.getMessage());
            
            // Intenta leer como lista y convertir a mapa
            try (FileReader reader = new FileReader(archivo)) {
                Type tipoLista = new TypeToken<List<Cliente>>() {}.getType();
                List<Cliente> lista = gson.fromJson(reader, tipoLista);
                
                if (lista != null) {
                    for (Cliente cliente : lista) {
                        mapa.put(cliente.getID(), cliente);
                    }
                    
                    // Guarda como mapa para futuras lecturas
                    guardarMapaClientes(mapa);
                }
            } catch (Exception ex) {
                System.err.println("Error al convertir lista a mapa: " + ex.getMessage());
                inicializarArchivoJSON();
            }
        }
        
        return mapa;
    }
    
    /**
     * Guarda el mapa de clientes en el archivo JSON
     */
    public static void guardarMapaClientes(Map<Integer, Cliente> clientes) {
        try (FileWriter writer = new FileWriter(ARCHIVO_CLIENTE_JSON)) {
            gson.toJson(clientes, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar mapa de clientes: " + e.getMessage());
        }
    }
    
    /**
     * Convierte ModelUser a Cliente
     */
    public static Cliente convertirModelUserACliente(ModelUser user) {
        try {
            return new Cliente(
                user.getUserID(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword()
            );
        } catch (Exception e) {
            System.err.println("Error al convertir ModelUser a Cliente: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convierte Cliente a ModelUser
     */
    public static ModelUser convertirClienteAModelUser(Cliente cliente) {
        if (cliente == null) return null;
        
        return new ModelUser(
            cliente.getID(),
            cliente.getNombre(),
            cliente.getCorreoElectronico(),
            cliente.getContraseña()
        );
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     */
    public static boolean registrarNuevoUsuario(ModelUser user) {
        Map<Integer, Cliente> clientes = asegurarMapaClientes();
        
        // Verifica si ya existe el correo
        for (Cliente cliente : clientes.values()) {
            if (cliente.getCorreoElectronico().equals(user.getEmail())) {
                return false;
            }
        }
        
        // Genera un nuevo ID si es necesario
        int nuevoID = user.getUserID();
        if (nuevoID <= 0) {
            nuevoID = clientes.keySet().stream().mapToInt(id -> id).max().orElse(0) + 1;
        }
        
        Cliente nuevoCliente = convertirModelUserACliente(user);
        if (nuevoCliente != null) {
            nuevoCliente.setID(nuevoID);
            clientes.put(nuevoID, nuevoCliente);
            guardarMapaClientes(clientes);
            return true;
        }
        
        return false;
    }
}
