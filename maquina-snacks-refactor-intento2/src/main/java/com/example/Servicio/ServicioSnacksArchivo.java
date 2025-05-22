package com.example.Servicio;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.example.Dominio.Snack;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServicioSnacksArchivo implements IservicioSnakcs {
  private final String ARCHIVO_JSON = "snacks.json";
  // creamos la tabla donde se guardaran los datos
  private Table<Integer, String, Snack> snacks = HashBasedTable.create();

  // creamos el constructor
  public ServicioSnacksArchivo() {
    // creamos el file del archivo json
    File file = new File(ARCHIVO_JSON);
    boolean existe = false;
    try {
      existe = file.exists();
      if (existe) {
        ObtenerSnacks();
      } else {
        // en caso de que no exista creamos el archivo
        FileWriter fw = new FileWriter(ARCHIVO_JSON, true);
        fw.close();
        System.out.println("Se ha creado el archivo correctamente.");
      }
    } catch (Exception e) {
      System.out.println("Error al cargar el archivo: " + e.getMessage());
    }

    if (!existe) {
      cargarSnacksIniciales();
    }
  }

  private void cargarSnacksIniciales() {
    this.agregarSnack(new Snack(70, "Papas fritas", 4, "Barbacoa"));
    this.agregarSnack(new Snack(50, "Postobon", 20, "Manzana"));
    this.agregarSnack(new Snack(120, "Sandwich", 12, "Jamon y Queso"));
  }

  @Override
  public void agregarSnack(Snack snack) {
    // agregamos el snack a la tabla
    snacks.put(snack.getIdSnack(), snack.getNombre(), snack);
    // luego lo agregamos al archivo
    this.agregarSnackArchivo();
  }

  public Map<Integer, Snack> obtenerTodosLosSnacksComoMapa() {
    Map<Integer, Snack> mapa = new HashMap<>();
    for (Cell<Integer, String, Snack> cell : snacks.cellSet()) {
        mapa.put(cell.getRowKey(), cell.getValue());
    }
    return mapa;
}

  public void ObtenerSnacks() {
    File arFile = new File(ARCHIVO_JSON);
    if (arFile.exists()) {
        try (FileReader fr = new FileReader(arFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            
            // Intenta leer primero el contenido como String para depuración
            StringBuilder jsonContent = new StringBuilder();
            char[] buffer = new char[1024];
            int read;
            while ((read = fr.read(buffer)) != -1) {
                jsonContent.append(buffer, 0, read);
            }
            
            // Si el archivo está vacío, no intentes deserializar
            if (jsonContent.length() == 0) {
                System.out.println("El archivo de snacks está vacío.");
                return;
            }
            
            // Reiniciar el FileReader
            fr.close();
            try (FileReader fr2 = new FileReader(arFile)) {
                Type tipo = new TypeToken<Map<Integer, Map<String, Snack>>>(){}.getType();
                Map<Integer, Map<String, Snack>> mapa = gson.fromJson(fr2, tipo);
                
                // Verificar si el mapa es nulo
                if (mapa == null) {
                    System.out.println("Error al deserializar el JSON: El mapa resultante es nulo.");
                    return;
                }
                
                // Limpiar la tabla existente
                snacks.clear();
                
                // Cargar los snacks en la tabla desde el mapa deserializado
                mapa.forEach((id, snacksMap) -> {
                    snacksMap.forEach((nombre, snack) -> {
                        // Asegurar que el ID del snack coincida con el ID del mapa
                        if (snack.getIdSnack() != id) {
                            snack = new Snack(id, snack.getPrecio(), snack.getNombre(), 
                                             snack.getCantidad(), snack.getTipo());
                        }
                        snacks.put(id, nombre, snack);
                    });
                });
                
                System.out.println("Snacks cargados desde el archivo exitosamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar los archivos desde el archivo: " + e.getMessage());
            e.printStackTrace();  // Esto mostrará el stack trace completo para mejor diagnóstico
            
            // Si hay error, intentamos eliminar el archivo corrupto y crear uno nuevo
            arFile.delete();
            try (FileWriter fw = new FileWriter(ARCHIVO_JSON)) {
                fw.write("{}");  // Escribir un JSON vacío válido
                fw.flush();
            } catch (Exception ex) {
                System.out.println("Error al recrear el archivo: " + ex.getMessage());
            }
        }
    } else {
        System.out.println("El archivo de snacks no existe.");
    }
}

private void agregarSnackArchivo() {
    File archivo = new File(ARCHIVO_JSON);
    try (FileWriter fw = new FileWriter(archivo)) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // Convertir la tabla a un mapa para serializarlo
        Map<Integer, Map<String, Snack>> mapa = snacks.rowMap();
        String json = gson.toJson(mapa);
        
        // Verificar que el JSON generado sea válido
        try {
            gson.fromJson(json, Object.class);
        } catch (Exception e) {
            System.out.println("Error: Se generó un JSON inválido. Usando formato alternativo.");
            // Crear un JSON alternativo más simple
            Map<String, Snack> snacksMap = new HashMap<>();
            for (Cell<Integer, String, Snack> cell : snacks.cellSet()) {
                snacksMap.put(cell.getRowKey() + "_" + cell.getColumnKey(), cell.getValue());
            }
            json = gson.toJson(snacksMap);
        }
        
        fw.write(json);
        fw.flush();
    } catch (Exception e) {
        System.out.println("Error al escribir en el archivo: " + e.getMessage());
        e.printStackTrace();
    }
}


@Override
public void mostrarSnacks() {
  File archivo = new File(ARCHIVO_JSON);

  // Verificamos si el archivo existe y no está vacío
  if (!archivo.exists() || archivo.length() == 0) {
    System.out.println("No hay snacks registrados aún.");
    return;
  }

  Table<Integer, String, Snack> mostrarTabla = HashBasedTable.create();

  try (FileReader fr = new FileReader(archivo)) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Type tipo = new TypeToken<Map<Integer, Map<String, Snack>>>(){}.getType();
    Map<Integer, Map<String, Snack>> mapa = gson.fromJson(fr, tipo);

    // Verificamos si el mapa es null (archivo mal formado o sin datos válidos)
    if (mapa == null || mapa.isEmpty()) {
      System.out.println("No hay snacks que mostrar (archivo vacío o inválido).");
      return;
    }

    // Llenamos la tabla
    mapa.forEach((k, v) -> v.forEach((col, snack) -> mostrarTabla.put(k, col, snack)));

    // Mostramos los snacks
    System.out.println("\t\t\t\t===== Lista de Snacks =====");
    mostrarTabla.cellSet().forEach(cell -> {
      Snack snack = cell.getValue();
      System.out.printf("\tID: %d | Nombre: %s | Precio: %.2f | Cantidad: %d | Tipo: %s\n",
          snack.getIdSnack(), snack.getNombre(), snack.getPrecio(), snack.getCantidad(), snack.getTipo());
    });

  } catch (Exception e) {
    System.out.println("Error al mostrar los snacks: " + e.getMessage());
  }
}


  @Override
  public Table<Integer, String, Snack> getSnacks() {
    return snacks;
  }

  // creamos el metodo actualizarArchivo
  public void actualizarArchivo() {
    try (Writer wr = new FileWriter(this.ARCHIVO_JSON)) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Convertir la tabla a un mapa para serializarlo correctamente
        Map<Integer, Map<String, Snack>> mapa = snacks.rowMap();
        
        gson.toJson(mapa, wr);
    } catch (Exception e) {
        System.out.println("Error de tipo: " + e.getLocalizedMessage());
    }
}

  @Override
  public boolean comprarSnack(int id) {
    // Buscar el snack por ID
    boolean snackEncontrado = false;
    for (Cell<Integer, String, Snack> cell : getSnacks().cellSet()) {
      if (cell.getRowKey() == id) {
        Snack snack = cell.getValue();
        snackEncontrado = true;

        // Verificar stock
        if (snack.getCantidad() > 0) {
          snack.setCantidad(snack.getCantidad() - 1);
          actualizarArchivo(); 
          return true;
        } else {
          return false;
        }
      }
    }
    if (!snackEncontrado) {
      System.out.println("Snack con ID " + id + " no encontrado.");
    }
    return false;
  }

  @Override
  public Snack comprarSnack2(int id) {
      for (Cell<Integer, String, Snack> cell : getSnacks().cellSet()) {
          if (cell.getRowKey() == id) {
              Snack snack = cell.getValue();
              if (snack.getCantidad() > 0) {
                  snack.setCantidad(snack.getCantidad() - 1);
                  actualizarArchivo(); // Actualizar el archivo después de la compra
                  return snack; // Devuelve el snack comprado
              } else {
                  return null;
              }
          }
      }
      return null;
  }

  @Override
  public Snack comprarSnackJSON(int idSnack) throws Exception {
    // 1. Buscar el snack en tu estructura de datos (ej: tabla hash)
    Snack snack = snacks.get(idSnack, "default"); // Ajusta según tu implementación
    
    // 2. Validar existencia y stock
    if (snack == null) {
        throw new Exception("Snack no encontrado");
    }
    if (snack.getCantidad() <= 0) {
        throw new Exception("Stock agotado");
    }
    
    // 3. Reducir cantidad y guardar en JSON
    snack.setCantidad(snack.getCantidad() - 1);
    guardarSnacksEnJSON(); // Método para persistir en JSON
    
    return snack;
}

private void guardarSnacksEnJSON() {
    try (FileWriter writer = new FileWriter("snacks.json")) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(obtenerTodosLosSnacksComoMapa(), writer);
    } catch (IOException e) {
        System.err.println("Error al guardar en JSON: " + e.getMessage());
    }
}
  
}