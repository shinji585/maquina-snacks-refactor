package com.example.Servicio;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.*;
import java.lang.reflect.Type;

public class TableTypeAdapter<R, C, V> implements JsonSerializer<Table<R, C, V>>, JsonDeserializer<Table<R, C, V>> {
    private final Type rowType;
    private final Type columnType;
    private final Type valueType;

    public TableTypeAdapter(Type rowType, Type columnType, Type valueType) {
        this.rowType = rowType;
        this.columnType = columnType;
        this.valueType = valueType;
    }

    @Override
    public JsonElement serialize(Table<R, C, V> table, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        JsonArray cells = new JsonArray();
        
        // Almacenamos cada celda como un objeto con tres propiedades: row, col, y value
        for (Table.Cell<R, C, V> cell : table.cellSet()) {
            JsonObject cellObj = new JsonObject();
            cellObj.add("row", context.serialize(cell.getRowKey(), rowType));
            cellObj.add("col", context.serialize(cell.getColumnKey(), columnType));
            cellObj.add("value", context.serialize(cell.getValue(), valueType));
            cells.add(cellObj);
        }
        
        json.add("cells", cells);
        return json;
    }

    @Override
    public Table<R, C, V> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Table<R, C, V> table = HashBasedTable.create();
        
        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected JsonObject for Table");
        }
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Si estamos usando el nuevo formato con "cells"
        if (jsonObject.has("cells") && jsonObject.get("cells").isJsonArray()) {
            JsonArray cells = jsonObject.getAsJsonArray("cells");
            
            for (int i = 0; i < cells.size(); i++) {
                JsonObject cellObj = cells.get(i).getAsJsonObject();
                
                if (!cellObj.has("row") || !cellObj.has("col") || !cellObj.has("value")) {
                    throw new JsonParseException("Cell is missing properties");
                }
                
                R rowKey = context.deserialize(cellObj.get("row"), rowType);
                C colKey = context.deserialize(cellObj.get("col"), columnType);
                V value = context.deserialize(cellObj.get("value"), valueType);
                
                table.put(rowKey, colKey, value);
            }
        } 
        // Si estamos usando el formato antiguo (compatibilidad)
        else {
            for (java.util.Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                
                // Ignorar propiedades especiales como "rowKeySet"
                if (key.equals("rowKeySet") || key.equals("columnKeySet") || key.equals("values")) {
                    continue;
                }
                
                try {
                    // Intentar procesar como formato anterior con "|||"
                    String[] keys = key.split("\\|\\|\\|");
                    if (keys.length == 2) {
                        try {
                            R rowKey = context.deserialize(JsonParser.parseString(keys[0]), rowType);
                            C colKey = context.deserialize(JsonParser.parseString(keys[1]), columnType);
                            V value = context.deserialize(entry.getValue(), valueType);
                            
                            table.put(rowKey, colKey, value);
                        } catch (Exception e) {
                            System.out.println("Error procesando clave: " + key + " - " + e.getMessage());
                            // Continuar con la siguiente entrada
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error al dividir clave: " + key + " - " + e.getMessage());
                    // Continuar con la siguiente entrada
                }
            }
        }
        
        return table;
    }
}