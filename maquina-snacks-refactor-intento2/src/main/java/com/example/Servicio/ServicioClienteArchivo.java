package com.example.Servicio;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.common.reflect.TypeToken;

public class ServicioClienteArchivo implements IservicioCliente {
    private static final String ARCHIVO_CLIENTE_JSON = "clientes.json";
    private final File archivo;
    private final Gson gson;
    private final Map<Integer, Cliente> clientes;
    private final PDFGeneratorService pdfService;
    
    public ServicioClienteArchivo() {
        this.clientes = new HashMap<>();
        this.archivo = new File(ARCHIVO_CLIENTE_JSON);
        this.pdfService = new PDFGeneratorService();
        this.gson = crearGsonConfigurado();
        
        inicializarArchivoClientes();
    }
    
    private Gson crearGsonConfigurado() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        
        Type tableType = new TypeToken<Table<LocalDateTime, Integer, Compra>>(){}.getType();
        gsonBuilder.registerTypeAdapter(tableType, 
            new TableTypeAdapter<>(LocalDateTime.class, Integer.class, Compra.class));
        
        return gsonBuilder.create();
    }
    
    private void inicializarArchivoClientes() {
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                guardarClientesEnArchivo(); // Guarda un JSON vacío
                System.out.println("Archivo de clientes creado exitosamente.");
            } else {
                cargarClientes();
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar archivo de clientes: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de clientes", e);
        }
    }
    
    private void cargarClientes() {
        try (FileReader reader = new FileReader(archivo)) {
            if (archivo.length() == 0) {
                System.out.println("El archivo de clientes está vacío.");
                return;
            }

            Type tipoMapa = new TypeToken<Map<Integer, Cliente>>(){}.getType();
            Map<Integer, Cliente> clientesCargados = gson.fromJson(reader, tipoMapa);

            if (clientesCargados == null) {
                System.out.println("El archivo no contiene datos válidos de clientes.");
                return;
            }

            clientes.clear();
            clientes.putAll(clientesCargados);
            System.out.println("Clientes cargados exitosamente: " + clientes.size() + " registros.");
        } catch (Exception e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
            manejarErrorCargaClientes(e);
        }
    }

    private void manejarErrorCargaClientes(Exception e) {
        System.err.println("Intentando recuperar archivo corrupto...");
        if (archivo.delete()) {
            try {
                archivo.createNewFile();
                guardarClientesEnArchivo();
                System.out.println("Archivo corrupto reemplazado por uno nuevo vacío.");
            } catch (IOException ioEx) {
                System.err.println("No se pudo recrear el archivo: " + ioEx.getMessage());
            }
        } else {
            System.err.println("No se pudo eliminar el archivo corrupto.");
        }
    }
    
    private void guardarClientesEnArchivo() {
        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(clientes, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
            throw new RuntimeException("Error crítico al guardar datos de clientes", e);
        }
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || existeCliente(cliente.getID())) {
            return false;
        }
        
        clientes.put(cliente.getID(), cliente);
        guardarClientesEnArchivo();
        return true;
    }

    @Override
    public Cliente obtenerClientePorID(Integer id) {
        return clientes.get(id);
    }

    @Override
    public Map<Integer, Cliente> obtenerTodosLosClientes() {
        return new HashMap<>(clientes);
    }

    @Override
    public boolean actualizarCliente(Cliente cliente) {
        if (cliente == null || !existeCliente(cliente.getID())) {
            return false;
        }
        
        clientes.put(cliente.getID(), cliente);
        guardarClientesEnArchivo();
        return true;
    }

    @Override
    public boolean eliminarCliente(Integer id) {
        if (id == null || !clientes.containsKey(id)) {
            return false;
        }
        
        clientes.remove(id);
        guardarClientesEnArchivo();
        return true;
    }

    @Override
    public boolean registrarCompra(Cliente cliente, Compra compra) {
        if (cliente == null || compra == null) {
            return false;
        }
        
        Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
        if (historial == null) {
            cliente.setHistorialCompras(HashBasedTable.create());
            historial = cliente.verhistorialCompras();
        }
        
        historial.put(compra.getfecha(), compra.getIDCompra(), compra);
        
        if (!actualizarCliente(cliente)) {
            System.err.println("No se pudo actualizar el cliente después de registrar la compra");
            return false;
        }
        
        generarReciboCompra(compra);
        return true;
    }

    private void generarReciboCompra(Compra compra) {
        File reciboPDF = pdfService.generarReciboPDF(compra);
        if (reciboPDF != null) {
            System.out.println("Recibo generado: " + reciboPDF.getAbsolutePath());
        } else {
            System.err.println("No se pudo generar el recibo PDF para la compra " + compra.getIDCompra());
        }
    }

    @Override
    public File generarHistorialClientePDF(Integer idCliente) {
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente == null) {
            System.err.println("Cliente no encontrado: " + idCliente);
            return null;
        }
        
        Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
        if (historial == null || historial.isEmpty()) {
            System.out.println("El cliente no tiene compras registradas.");
            historial = HashBasedTable.create(); // PDF vacío
        }
        
        return pdfService.generarHistorialClientePDF(cliente, historial);
    }

    @Override
    public BigDecimal calcularGastoTotalCliente(Integer idCliente) {
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente == null) {
            return BigDecimal.ZERO;
        }
        
        Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
        if (historial == null || historial.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return historial.values().stream()
                .map(Compra::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int contarComprasCliente(Integer idCliente) {
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente == null || cliente.verhistorialCompras() == null) {
            return 0;
        }
        return cliente.verhistorialCompras().size();
    }

    @Override
    public boolean existeCliente(Integer id) {
        return clientes.containsKey(id);
    }

    @Override
    public File generarFacturaCompra(Integer idCompra) {
        for (Cliente cliente : clientes.values()) {
            Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
            if (historial != null) {
                for (Compra compra : historial.values()) {
                    if (compra.getIDCompra().equals(idCompra)) {
                        return pdfService.generarReciboPDF(compra);
                    }
                }
            }
        }
        
        System.err.println("Compra no encontrada: " + idCompra);
        return null;
    }
    
    @Override
    public Cliente buscarCliente(int idCliente) {
        return obtenerClientePorID(idCliente);
    }
    
    @Override
    public void guardarHistorial(int idCliente) {
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente != null) {
            actualizarCliente(cliente);
        }
    }
}