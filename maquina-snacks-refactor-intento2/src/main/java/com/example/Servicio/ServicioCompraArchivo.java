package com.example.Servicio;


import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.example.Dominio.Snack;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ServicioCompraArchivo implements IservicioCompra {
    private final String ARCHIVO_COMPRAS = "compras.json";
    private Table<Integer, Integer, Compra> compras; // Tabla <idCliente, idCompra, Compra>
    private AtomicInteger contadorId;
    private Gson gson;
    private IservicioCliente servicioCliente;
    private IservicioSnakcs servicioSnacks;
    static Scanner scanner = new Scanner(System.in);

    public ServicioCompraArchivo() {
        this.compras = HashBasedTable.create();
        this.contadorId = new AtomicInteger(1);
        this.servicioCliente = new ServicioClienteArchivo();
        this.servicioSnacks = new ServicioSnacksArchivo();
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        this.gson = gsonBuilder.create();
        
        cargarCompras();
    }

    private void cargarCompras() {
        try (FileReader reader = new FileReader(ARCHIVO_COMPRAS)) {
            Type type = new TypeToken<Map<Integer, Map<Integer, Compra>>>(){}.getType();
            Map<Integer, Map<Integer, Compra>> datos = gson.fromJson(reader, type);
            
            if (datos != null) {
                datos.forEach((idCliente, mapCompras) -> 
                    mapCompras.forEach((idCompra, compra) -> {
                        compras.put(idCliente, idCompra, compra);
                        if (idCompra >= contadorId.get()) {
                            contadorId.set(idCompra + 1);
                        }
                    })
                );
            }
        } catch (Exception e) {
            System.out.println("Error al cargar compras: " + e.getMessage());
        }
    }

    private void guardarCompras() {
        try (FileWriter writer = new FileWriter(ARCHIVO_COMPRAS)) {
            Map<Integer, Map<Integer, Compra>> datos = new HashMap<>();
            
            for (Integer idCliente : compras.rowKeySet()) {
                datos.put(idCliente, new HashMap<>(compras.row(idCliente)));
            }
            
            gson.toJson(datos, writer);
        } catch (Exception e) {
            System.out.println("Error al guardar compras: " + e.getMessage());
        }
    }

    @Override
    public Compra crearCompra(int idCliente) {
        Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado");
            return null;
        }
        
        int idCompra = contadorId.getAndIncrement();
        Compra nuevaCompra = new Compra(cliente);
        nuevaCompra.setIdCompra(idCompra);
        
        compras.put(idCliente, idCompra, nuevaCompra);
        guardarCompras();
        
        return nuevaCompra;
    }

    @Override
    public boolean finalizarCompra(Compra compra) {
        if (compra == null || compra.getCliente() == null) {
            return false;
        }
        
        int idCliente = compra.getCliente().getID();
        int idCompra = compra.getIDCompra();
        
        // Verificar saldo y stock
        BigDecimal total = compra.calcularTotal();
        Cliente cliente = compra.getCliente();
        
        if (cliente.verSaldo().compareTo(total) < 0) {
            System.out.println("Saldo insuficiente");
            return false;
        }
        
        for (Snack snack : compra.getSnacksComprados().values()) {
            if (!servicioSnacks.comprarSnack(snack.getIdSnack())) {
                System.out.println("No hay stock de: " + snack.getNombre());
                return false;
            }
        }
        
        // Actualizar saldo del cliente
        cliente.setSaldo(cliente.verSaldo().subtract(total));
        servicioCliente.actualizarCliente(cliente);
        
        // Actualizar compra en la tabla
        compras.put(idCliente, idCompra, compra);
        guardarCompras();
        
        return true;
    }

    @Override
    public boolean cancelarCompra(int idCompra) {
        for (Table.Cell<Integer, Integer, Compra> cell : compras.cellSet()) {
            if (cell.getColumnKey() == idCompra) {
                compras.remove(cell.getRowKey(), cell.getColumnKey());
                guardarCompras();
                return true;
            }
        }
        return false;
    }

    @Override
    public Table<LocalDateTime, Integer, Compra> obtenerComprasporCliente(int idCliente) {
        Table<LocalDateTime, Integer, Compra> resultado = HashBasedTable.create();
        
        Map<Integer, Compra> comprasCliente = compras.row(idCliente);
        if (comprasCliente != null) {
            comprasCliente.values().forEach(compra -> 
                resultado.put(compra.getfecha(), compra.getIDCompra(), compra)
            );
        }
        
        return resultado;
    }

    @Override
    public String generarReciboCompra(int idCompra) {
        for (Compra compra : compras.values()) {
            if (compra.getIDCompra() == idCompra) {
                StringBuilder recibo = new StringBuilder();
                recibo.append("RECIBO DE COMPRA #").append(idCompra).append("\n");
                recibo.append("Cliente: ").append(compra.getCliente().getNombre()).append("\n");
                recibo.append("Fecha: ").append(compra.getfecha()).append("\n\n");
                recibo.append("Productos:\n");
                
                compra.getSnacksComprados().values().forEach(snack -> 
                    recibo.append("- ").append(snack.getNombre())
                         .append(" ($").append(snack.getPrecio()).append(")\n")
                );
                
                recibo.append("\nTOTAL: $").append(compra.calcularTotal());
                return recibo.toString();
            }
        }
        return "Compra no encontrada";
    }

    @Override
    public boolean agregarSnackACompra(Integer idCompra, int idSnack) {
        for (Compra compra : compras.values()) {
            if (compra.getIDCompra() == idCompra) {
                Snack snack = servicioSnacks.comprarSnack2(idSnack);
                if (snack != null) {
                    compra.agregarSnack(snack);
                    guardarCompras();
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    // creamos el metodo comprar por dropset 

    @Override
    public Compra comprarDropset(Integer idCliente) throws Exception {
        System.out.println("\tUsted ha decidido comprar por dropset. A continuación se generará su dropset");
        
        // Primero verificamos que el cliente exista
        Cliente cliente = servicioCliente.obtenerClientePorID(idCliente);
        if (cliente == null) {
            throw new Exception("Cliente no encontrado");
        }
    
        // Creamos la compra primero
        Compra nuevaCompra = crearCompra(idCliente);
        if (nuevaCompra == null) {
            throw new Exception("No se pudo crear la compra");
        }
        
        // Obtenemos la cantidad de tipos diferentes de snacks a comprar
        System.out.print("Ingrese la cantidad de tipos de snacks que tendrá su dropset: ");
        int cantidadTipos = scanner.nextInt();
    
        if (cantidadTipos <= 0) {
            throw new Exception("La cantidad de tipos de productos a comprar no debe ser menor o igual a cero");
        } else {
            // Mostramos los snacks disponibles
            ServicioSnacksArchivo servicioSnacksArchivo = new ServicioSnacksArchivo();
            servicioSnacksArchivo.mostrarSnacks();
            
            for (int i = 0; i < cantidadTipos; i++) {
                // Solicitamos el id del snack junto con la cantidad
                System.out.print("Ingrese el ID del snack #" + (i+1) + " que quiere comprar: ");
                int idSnack = scanner.nextInt();
    
                System.out.print("Ingrese la cantidad de unidades que quiere comprar de este snack: ");
                int cantidadSnacks = scanner.nextInt();
    
                if (cantidadSnacks <= 0) {
                    System.out.println("La cantidad debe ser mayor que cero. No se agregará este snack.");
                    continue;
                }
    
                // Verificamos si hay suficiente stock antes de intentar agregar
                boolean hayStock = true;
                for (int j = 0; j < cantidadSnacks; j++) {
                    Snack snack = servicioSnacks.comprarSnack2(idSnack);
                    if (snack == null) {
                        hayStock = false;
                        System.out.println("No hay suficiente stock para " + cantidadSnacks + " unidades del snack ID " + idSnack);
                        break;
                    }
                    nuevaCompra.agregarSnack(snack);
                }
                
                if (hayStock) {
                    System.out.println("Se agregaron " + cantidadSnacks + " unidades del snack ID " + idSnack + " a su compra");
                }
            }
            
            // Guardamos los cambios en la compra
            guardarCompras();
            
            // Mostramos el total de la compra
            System.out.println("Total de la compra: $" + nuevaCompra.calcularTotal());
            System.out.println("Para finalizar la compra, use la opción 'Finalizar compra' con el ID: " + nuevaCompra.getIDCompra());
            
            return nuevaCompra;
        }
    }
}