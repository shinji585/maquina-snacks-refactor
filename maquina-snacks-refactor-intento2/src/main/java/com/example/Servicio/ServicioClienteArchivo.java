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
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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

    public ServicioClienteArchivo() {
        boolean existe = false;
        clientes = new HashMap<>();
        compras = new HashMap<>();
        archivo = new File(ARCHIVO_CLIENTE_JSON);
        try {
            existe = archivo.exists();
            if (existe) {
                cargarClientes();
            } else {
                // se crea el archivo en caso de que no exista
                FileWriter fw = new FileWriter(archivo, true);
                fw.close();
                System.out.println("se ha creado con exito el archivo");
            }
        } catch (Exception e) {
            System.out.println("error de tipo: " + e.getLocalizedMessage());
        }
    }

    // creamos el metodo de cargarClientes
    public void cargarClientes() {
        File arFile = new File(ARCHIVO_CLIENTE_JSON);
        if (arFile.exists()) {
            try (FileReader fr = new FileReader(arFile)) {
                gson = new GsonBuilder().setPrettyPrinting().create();

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
           estado =  agregarClienteArchivo();
        }
        return estado;
    }

    private boolean agregarClienteArchivo() {
        boolean estado = false;
        archivo = new File(ARCHIVO_CLIENTE_JSON);
        try (FileWriter fWriter = new FileWriter(archivo)) {
            gson = new GsonBuilder().setPrettyPrinting().create();
            // serializamos los clientes
            String json = gson.toJson(clientes);

            // verificamos que el json generado sea correcto
            try {
                gson.fromJson(json, Object.class);
            } catch (Exception e) {
                System.out.println("Error: Se generó un JSON inválido. Usando formato alternativo.");
                // Crear un JSON alternativo más simple
                Map<Integer, Cliente> clientesMap = new HashMap<>();
                for (Map.Entry<Integer,Cliente> clientes : clientes.entrySet() ) {
                    clientesMap.put(clientes.getKey(), clientes.getValue());
                }
                json = gson.toJson(clientesMap);
                estado = true;
            }
        } catch (Exception e) {
            System.out.println("error de tipo: " + e.getLocalizedMessage());
        }
        return estado;
    }

    @Override
    public Cliente buscarCliente(int idCliente) {
        Cliente envioCliente = new Cliente();
        for (Map.Entry<Integer,Cliente> clienteEntry : clientes.entrySet()) {
            if (clienteEntry.getKey() == idCliente){
                envioCliente = clienteEntry.getValue();
            }
        }
        return envioCliente;
    }

    @Override
    public boolean realizarCompra(int idcliente, int idsnack) {
        IservicioSnakcs snakcs = new ServicioSnacksArchivo();
        boolean estado = false; 
        // buscamos los clientes almacenados en la tabla o el archivo 
        Optional<Cliente> clienteOptional = clientes.values()  // obtine los valores del map 
        .stream()  // comienza un stream (un clujo de datos)
        .filter(cliente -> cliente.getID() == idcliente) // busca el cliente con el cual tenga el mismo id que se ha ingresado 
        .findFirst(); // encuentra el primero 
    
        if (clienteOptional.isPresent()) {
            // creamos un objeto de tipo snack
            Snack snack = new Snack();
            // verificamos si el snack ingresado está presente en el servicio de archivos 
            for (Table.Cell<Integer, String, Snack> snakcsTable : snakcs.getSnacks().cellSet()) {
                if (snakcsTable.getRowKey() == idsnack) {
                    snack = snakcsTable.getValue();
                }
            }
            
            Cliente cliente = clienteOptional.get();  // obtenemos el cliente encontrado
            // ahora esperamos una respuesta de si el cliente tiene capital para realizar la compra 
            try {
                Compra compra = new Compra();
                compra.agregarSnack(snack);
                estado = cliente.realizarCompra(snack);
                // despues que se haya realizando la compra pasamos el objecto pero verificamos que esta haya existido
                if (estado == true){
                    this.compras.put(idcliente, compra);
                }
            } catch (Exception e) {
                System.out.println("error de tipo: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    
        return estado;
    }
    
    @Override
    public boolean recargarSaldo(int idcliente, BigDecimal saldo) {
        Optional<Cliente> clienteOptional = clientes.values()
            .stream()
            .filter(cliente -> cliente.getID() == idcliente)
            .findFirst();
    
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            // Recargamos el saldo
            try {
                cliente.recargarSaldo(saldo);
                return true;
            } catch (Exception e) {
                System.out.println("Error de tipo: " + e.getLocalizedMessage());
            }
        }
        return false;
    }
    

    @Override
    public boolean guardarHistorial(int idcliente) {
       boolean estado = false;

       // buscamos el cliente 
       Optional<Cliente> clienteOptional = clientes.values()
        .stream()
        .filter(cliente -> cliente.getID() == idcliente)
        .findFirst();

        if (clienteOptional.isPresent()){
            Cliente cliente = clienteOptional.get();

            // ahora agregamos la compra al historial del archivo y del cliente si es true 
            for (Map.Entry<Integer,Compra> historialComprasLista : compras.entrySet()) {
                if (historialComprasLista.getKey() == idcliente){
                    // creamos un estado de si es true se pueda crear un json de las ventas que se ha realizado para despues generar un pdf }
                    estado = cliente.agregarcomprahistorial(historialComprasLista.getValue());
                }
            }
        }
        return estado;
    }

    @Override
    public File generarPDFHistorial(int idcliente) {
        // Primero, buscamos el cliente y su historial de compras.
        Optional<Cliente> clienteOptional = clientes.values()
            .stream()
            .filter(cliente -> cliente.getID() == idcliente)
            .findFirst();
    
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            
            // Crear el documento PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(100, 750);  // Posición del texto
    
                // Título del PDF
                contentStream.showText("Historial de Compras del Cliente " + idcliente);
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                
                // Detalles del cliente
                contentStream.showText("Nombre: " + cliente.getNombre());
                contentStream.newLine();
                contentStream.showText("Fecha de generación: " + LocalDateTime.now());
                contentStream.newLine();
                
                // Detallar las compras realizadas por el cliente
                contentStream.showText("Detalles de las Compras:");
                contentStream.newLine();
    
                // Obtener el historial de compras del cliente
                Table<LocalDateTime, Integer, Compra> historialCompras  = HashBasedTable.create();
                try {
                    historialCompras = cliente.verhistorialCompras();
                } catch (Exception e) {
                    System.out.println("error de tipo: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
    
                // Iteramos sobre el historial de compras
                for (Map.Entry<LocalDateTime, Map<Integer, Compra>> entry : historialCompras.rowMap().entrySet()) {
                    LocalDateTime fecha = entry.getKey();
                    
                    // Para cada compra, obtenemos los detalles
                    for (Compra compra : entry.getValue().values()) {
                        Table<Integer, String, Object> detallesCompra = compra.getDetalles();
                        
                        // Escribir los detalles de la compra en el PDF
                        contentStream.showText("Fecha: " + fecha.toString());
                        contentStream.newLine();
                        contentStream.showText("Cliente: " + compra.getCliente().getNombre());
                        contentStream.newLine();
                        contentStream.showText("ID Compra: " + compra.getIDCompra());
                        contentStream.newLine();
    
                        // Detalles de los snacks de la compra
                        for (Map.Entry<Integer, Map<String, Object>> snackEntry : detallesCompra.rowMap().entrySet()) {
                            Map<String, Object> snackDetalles = snackEntry.getValue();
                            contentStream.showText("Producto: " + snackDetalles.get("Producto"));
                            contentStream.newLine();
                            contentStream.showText("Precio: " + snackDetalles.get("Precio"));
                            contentStream.newLine();
                            contentStream.showText("Tipo: " + snackDetalles.get("Tipo"));
                            contentStream.newLine();
                        }
    
                        // Espacio entre compras
                        contentStream.newLine();
                    }
                }
    
                // Detallar el total de la compra (última fila del historial)
                try {
                    contentStream.showText("Total: " + cliente.verhistorialCompras().values().stream()
                        .mapToDouble(compra -> compra.calcularTotal().doubleValue())
                        .sum());
                } catch (Exception e) {
                    System.out.println("error de tipo: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
                contentStream.newLine();
    
                contentStream.endText();  // Termina la escritura de texto
            } catch (IOException e) {
                e.printStackTrace();
                return null;  // Si hay un error, retornamos null
            }
    
            // Guardar el archivo PDF generado
            File pdfFile = new File("historial_compra_cliente_" + idcliente + ".pdf");
            try {
                document.save(pdfFile);
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
    
            // Retornar el archivo PDF generado
            return pdfFile;
        } else {
            System.out.println("Cliente no encontrado.");
            return null;
        }
    }
    

    }
    