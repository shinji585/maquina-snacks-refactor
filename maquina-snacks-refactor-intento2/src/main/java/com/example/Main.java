package com.example;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Usar Guava
        System.out.println(Lists.newArrayList("Uno", "Dos", "Tres"));

        // Usar Gson
        Gson gson = new Gson();
        String json = gson.toJson(new Person("Juan", 30));
        System.out.println(json);
        
        // Guardar el JSON en un archivo
        try (FileWriter writer = new FileWriter("person.json")) {
            gson.toJson(new Person("Juan", 30), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
