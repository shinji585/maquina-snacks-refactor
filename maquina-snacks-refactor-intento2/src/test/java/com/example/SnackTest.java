package com.example;

import org.junit.jupiter.api.Test;

import com.example.Dominio.Snack;

import static org.junit.jupiter.api.Assertions.*;

public class SnackTest {
    

    @Test
    void testCreateSnack(){
         Snack snack = new Snack(25.8, "Helado de chocolate", 10, "Dulce");
         assertEquals("Helado de chocolate", snack.getNombre());
         assertEquals(25.8, snack.getPrecio());
    }
}
