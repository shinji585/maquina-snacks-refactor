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
        
        for (Table.Cell<R, C, V> cell : table.cellSet()) {
            String rowKey = context.serialize(cell.getRowKey()).toString();
            String colKey = context.serialize(cell.getColumnKey()).toString();
            String compositeKey = rowKey + "|||" + colKey; // Usamos un separador poco común
            
            JsonElement valueElement = context.serialize(cell.getValue(), valueType);
            json.add(compositeKey, valueElement);
        }
        
        return json;
    }

    @Override
    public Table<R, C, V> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        Table<R, C, V> table = HashBasedTable.create();
        
        JsonObject jsonObject = json.getAsJsonObject();
        for (java.util.Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String[] keys = entry.getKey().split("\\|\\|\\|");
            if (keys.length != 2) {
                throw new JsonParseException("Invalid key format: " + entry.getKey());
            }
            
            R rowKey = context.deserialize(JsonParser.parseString(keys[0]), rowType);
            C colKey = context.deserialize(JsonParser.parseString(keys[1]), columnType);
            V value = context.deserialize(entry.getValue(), valueType);
            
            table.put(rowKey, colKey, value);
        }
        
        return table;
    }
}