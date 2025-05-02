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
import com.example.Dominio.Snack;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServicioClienteArchivo implements IservicioCliente {
    // cremos los atributos de nuestra clase
    private final String ARCHIVO_CLIENTE_JSON = "clientes.json";
    private File archivo;
    private Gson gson;
    private Map<Integer, Cliente> clientes;
    private Map<Integer,Compra> compras; 
    private PDFGeneratorService pdfService;
    
    public ServicioClienteArchivo() {
        boolean existe = false;
        clientes = new HashMap<>();
        compras = new HashMap<>();
        archivo = new File(ARCHIVO_CLIENTE_JSON);
        pdfService = new PDFGeneratorService();
       
        // creamos un gson personalizado con los adaptadores necesarios
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    
        // registrar el adaptador para localdatetime 
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
    
        // registrar el adaptador para Table
        Type tableType = new TypeToken<Table<LocalDateTime, Integer, Compra>>(){}.getType();
        gsonBuilder.registerTypeAdapter(tableType, 
            new TableTypeAdapter<>(LocalDateTime.class, Integer.class, Compra.class));
    
        // establecemos el gson
        this.gson = gsonBuilder.create();
    
        try {
            existe = archivo.exists();
            if (existe) {
                cargarClientes();
            } else {
                // se crea el archivo en caso de que no exista
                FileWriter fw = new FileWriter(archivo);
                fw.write("{}"); // Inicializar con un JSON válido vacío
                fw.close();
                System.out.println("se ha creado con exito el archivo");
            }
        } catch (Exception e) {
            System.out.println("error de tipo: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    // creamos el metodo de cargarClientes
    public void cargarClientes() {
        File arFile = new File(ARCHIVO_CLIENTE_JSON);
        if (arFile.exists()) {
            try (FileReader fr = new FileReader(arFile)) {
                // usamos el gson que ya tiene adaptadores registrados
                // no creamos uno nuevo 

                // Intenta leer primero el contenido como String
                StringBuilder jsonContent = new StringBuilder();
                char[] buffer = new char[1024];
                int read;
                while ((read = fr.read(buffer)) != -1) {
                    jsonContent.append(buffer, 0, read);
                }

                // Si el archivo está vacío, no intentes deserializar
                if (jsonContent.length() == 0) {
                    System.out.println("El archivo de clientes está vacío.");
                    return;
                }

                // Reiniciar el FileReader
                fr.close();
                try (FileReader fr2 = new FileReader(arFile)) {
                    Type tipo = new TypeToken<Map<Integer, Cliente>>() {
                    }.getType();
                    Map<Integer, Cliente> mapa = gson.fromJson(fr2, tipo);

                    // Verificar si el mapa es nulo
                    if (mapa == null) {
                        System.out.println("Error al deserializar el JSON: El mapa resultante es nulo.");
                        return;
                    }

                    // Limpiar la tabla existente
                    clientes.clear();

                    // Cargar los snacks en la tabla desde el mapa deserializado
                    mapa.forEach((id, cliente) -> {
                        clientes.put(id, cliente);
                    });

                    System.out.println("Clientes cargados desde el archivo exitosamente.");
                }
            } catch (Exception e) {
                System.out.println("Error al cargar los archivos desde el archivo: " + e.getMessage());
                e.printStackTrace(); // Esto mostrará el stack trace completo para mejor diagnóstico

                // Si hay error, intentamos eliminar el archivo corrupto y crear uno nuevo
                arFile.delete();
                try (FileWriter fw = new FileWriter(ARCHIVO_CLIENTE_JSON)) {
                    fw.write("{}"); // Escribir un JSON vacío válido
                    fw.flush();
                } catch (Exception ex) {
                    System.out.println("Error al recrear el archivo: " + ex.getMessage());
                }
            }
        } else {
            System.out.println("El archivo de clientes no existe.");
        }
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        boolean estado = false;
        if (cliente != null){
            clientes.put(cliente.getID(), cliente);
            this.agregarClienteArchivo();
            estado = true;
        }
        return estado;
    }
    
  
    private void agregarClienteArchivo() {
        this.archivo = new File(ARCHIVO_CLIENTE_JSON);
        try {
            // Primero cargar los clientes existentes (si los hay)
            Map<Integer, Cliente> clientesExistentes = new HashMap<>();
            if (archivo.exists() && archivo.length() > 0) {
                try (FileReader fileReader = new FileReader(archivo)) {
                    Type tipoMapa = new TypeToken<Map<Integer, Cliente>>() {}.getType();
                    Map<Integer, Cliente> mapaCargado = gson.fromJson(fileReader, tipoMapa);
                    if (mapaCargado != null) {
                        clientesExistentes = mapaCargado;
                    }
                } catch (Exception e) {
                    System.out.println("Error al cargar clientes existentes: " + e.getMessage());
                }
            }
            
            // Actualizar el mapa con los nuevos clientes
            clientesExistentes.putAll(clientes);
            
            // Guardar el mapa actualizado
            try (FileWriter writer = new FileWriter(archivo)) {
                gson.toJson(clientesExistentes, writer);
                System.out.println("Cliente(s) agregado(s) al archivo exitosamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al agregar cliente al archivo: " + e.getMessage());
            e.printStackTrace();
        }
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
        boolean resultado = false;
        if (cliente != null && cliente.getID() > 0) {  // Cambiado de getID() != null a getID() > 0
            // Verificar si el cliente existe
            if (clientes.containsKey(cliente.getID())) {
                clientes.put(cliente.getID(), cliente);
                agregarClienteArchivo();
                resultado = true;
            }
        }
        return resultado;
    }

    @Override
    public boolean eliminarCliente(Integer id) {
        boolean resultado = false;
        if (id != null && clientes.containsKey(id)) {
            clientes.remove(id);
            agregarClienteArchivo();
            resultado = true;
        }
        return resultado;
    }

    @Override
    public boolean registrarCompra(Cliente cliente, Compra compra) {
        boolean resultado = false;
        if (cliente != null && compra != null) {
            // Asignar la compra al cliente
            // Usamos el método modificado verhistorialCompras() que no lanza excepciones
            Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
            if (historial == null) {
                cliente.setHistorialCompras(HashBasedTable.create());
            }
            
            cliente.verhistorialCompras().put(compra.getfecha(), compra.getIDCompra(), compra);
            
            // Actualizar el cliente en la base de datos
            actualizarCliente(cliente);
            
            // Generar recibo PDF
            File reciboPDF = pdfService.generarReciboPDF(compra);
            if (reciboPDF != null) {
                System.out.println("Recibo generado exitosamente: " + reciboPDF.getAbsolutePath());
            }
            
            resultado = true;
        }
        return resultado;
    }

    @Override
    public File generarHistorialClientePDF(Integer idCliente) {
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente == null) {
            System.out.println("Error: No se encontró el cliente con ID " + idCliente);
            return null;
        }
        
        Table<LocalDateTime, Integer, Compra> historial = cliente.verhistorialCompras();
        if (historial == null || historial.isEmpty()) {
            System.out.println("El cliente no tiene historial de compras.");
            // Aún así generamos un PDF vacío
            historial = HashBasedTable.create();
        }
        
        return pdfService.generarHistorialClientePDF(cliente, historial);
    }

    @Override
    public BigDecimal calcularGastoTotalCliente(Integer idCliente) {
        Cliente cliente = obtenerClientePorID(idCliente);
        BigDecimal total = BigDecimal.ZERO;
        
        if (cliente != null && cliente.verhistorialCompras() != null) {
            for (Compra compra : cliente.verhistorialCompras().values()) {
                total = total.add(compra.calcularTotal());
            }
        }
        
        return total;
    }

    @Override
    public int contarComprasCliente(Integer idCliente) {
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente != null && cliente.verhistorialCompras() != null) {
            return cliente.verhistorialCompras().size();
        }
        return 0;
    }

    @Override
    public boolean existeCliente(Integer id) {
        return clientes.containsKey(id);
    }

    @Override
    public File generarFacturaCompra(Integer idCompra) {
        // Buscar la compra en los historiales de todos los clientes
        for (Cliente cliente : clientes.values()) {
            if (cliente.verhistorialCompras() != null) {
                for (Compra compra : cliente.verhistorialCompras().values()) {
                    if (compra.getIDCompra().equals(idCompra)) {
                        return pdfService.generarReciboPDF(compra);
                    }
                }
            }
        }
        
        System.out.println("No se encontró la compra con ID " + idCompra);
        return null;
    }
    
    // Implementación de los métodos adicionales de la interfaz
    
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