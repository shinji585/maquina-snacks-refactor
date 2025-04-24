package com.example;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
import com.example.Dominio.Snack;
import com.google.common.collect.Table;

public class CompraTest {
    
    // Para las pruebas, necesitamos crear stubs para Snack
    private class SnackStub extends Snack {
        private int idSnack;
        private String nombre;
        private double precio;
        private int cantidad;
        private String tipo;
        
        public SnackStub(int idSnack, String nombre, double precio, int cantidad, String tipo) {
            this.idSnack = idSnack;
            this.nombre = nombre;
            this.precio = precio;
            this.cantidad = cantidad;
            this.tipo = tipo;
        }
        
        @Override
        public int getIdSnack() {
            return idSnack;
        }
        
        @Override
        public String getNombre() {
            return nombre;
        }
        
        @Override
        public double getPrecio() {
            return precio;
        }
        
        @Override
        public int getCantidad() {
            return cantidad;
        }
        
        @Override
        public String getTipo() {
            return tipo;
        }
    }
    
    private Cliente clienteStub;
    private Compra compra;
    private SnackStub snack1;
    private SnackStub snack2;
    
    @Before
    public void setUp() throws Exception {
        // Inicializar el cliente stub
        BigDecimal saldoInicial = new BigDecimal("100.00");
        clienteStub = new Cliente("Cliente Test", 30, "Apellido Test", true, 
                           "Nacionalidad Test", "Dirección Test", 
                           "cliente@test.com", 123456789, saldoInicial);
        
        // Inicializar la compra
        compra = new Compra(clienteStub);
        
        // Inicializar los snacks stub
        snack1 = new SnackStub(1, "Papas", 15.50, 10, "Salado");
        snack2 = new SnackStub(2, "Chocolate", 12.75, 5, "Dulce");
    }
    
    @Test
    public void testConstructorCompra() {
        // Arrange & Act
        Compra compraVacia = new Compra();
        
        // Assert
        assertNotNull("El ID de compra no debería ser nulo", compraVacia.getIDCompra());
        assertNotNull("La fecha no debería ser nula", compraVacia.getfecha());
        assertNotNull("La tabla de snacks no debería ser nula", compraVacia.getSnacksComprados());
        assertTrue("La tabla de snacks debería estar vacía", compraVacia.getSnacksComprados().isEmpty());
    }
    
    @Test
    public void testConstructorCompraConCliente() {
        // Arrange & Act ya realizado en setUp()
        
        // Assert
        assertNotNull("El ID de compra no debería ser nulo", compra.getIDCompra());
        assertNotNull("La fecha no debería ser nula", compra.getfecha());
        assertNotNull("La tabla de snacks no debería ser nula", compra.getSnacksComprados());
        assertTrue("La tabla de snacks debería estar vacía", compra.getSnacksComprados().isEmpty());
        assertEquals("El cliente debería ser el mismo que se pasó al constructor", 
                   clienteStub, compra.getCliente());
    }
    
    @Test
    public void testAgregarSnack() {
        // Act
        boolean resultado1 = compra.agregarSnack(snack1);
        boolean resultado2 = compra.agregarSnack(snack2);
        boolean resultadoNull = compra.agregarSnack(null);
        
        // Assert
        assertTrue("Debería regresar true al agregar un snack válido", resultado1);
        assertTrue("Debería regresar true al agregar otro snack válido", resultado2);
        assertFalse("Debería regresar false al agregar un snack nulo", resultadoNull);
        assertEquals("Deberían haber 2 snacks en la tabla", 
                   2, compra.getSnacksComprados().size());
        assertSame("El snack1 debería estar en la tabla", 
                 snack1, compra.getSnacksComprados().get(snack1.getIdSnack(), snack1.getNombre()));
        assertSame("El snack2 debería estar en la tabla", 
                 snack2, compra.getSnacksComprados().get(snack2.getIdSnack(), snack2.getNombre()));
    }
    
    @Test
    public void testAgregarSnackConCantidadCero() {
        // Arrange
        SnackStub snackCeroCantidad = new SnackStub(3, "Galletas", 8.25, 0, "Dulce");
        
        // Act
        boolean resultado = compra.agregarSnack(snackCeroCantidad);
        
        // Assert
        assertFalse("Debería regresar false al agregar un snack con cantidad cero", resultado);
        assertTrue("La tabla de snacks debería seguir vacía", compra.getSnacksComprados().isEmpty());
    }
    
    @Test
    public void testCalcularTotal() {
        // Arrange
        compra.agregarSnack(snack1);
        compra.agregarSnack(snack2);
        BigDecimal expectedTotal = new BigDecimal("28.25"); // 15.50 + 12.75
        
        // Act
        BigDecimal actualTotal = compra.calcularTotal();
        
        // Assert
        assertEquals("El total calculado debería ser la suma de los precios de los snacks", 
                   expectedTotal, actualTotal);
    }
    
    @Test
    public void testCalcularTotalSinSnacks() {
        // Act
        BigDecimal actualTotal = compra.calcularTotal();
        
        // Assert
        assertEquals("El total calculado debería ser cero sin snacks", 
                   BigDecimal.ZERO, actualTotal);
    }
    
    @Test
    public void testGetDetalles() {
        // Arrange
        compra.agregarSnack(snack1);
        compra.agregarSnack(snack2);
        
        // Act
        Table<Integer, String, Object> detalles = compra.getDetalles();
        
        // Assert
        assertNotNull("Los detalles no deberían ser nulos", detalles);
        assertFalse("Los detalles no deberían estar vacíos", detalles.isEmpty());
        
        // Verificar información del encabezado (fila 0)
        assertTrue("Los detalles deberían contener la fecha", 
                 detalles.contains(0, "Fecha"));
        assertTrue("Los detalles deberían contener el nombre del cliente", 
                 detalles.contains(0, "Cliente"));
        assertTrue("Los detalles deberían contener el ID de compra", 
                 detalles.contains(0, "ID Compra"));
        assertEquals("El nombre del cliente debería ser el correcto", 
                   clienteStub.getNombre(), detalles.get(0, "Cliente"));
        assertEquals("El ID de compra debería ser el correcto", 
                   compra.getIDCompra(), detalles.get(0, "ID Compra"));
        
        // Verificar información de los productos (filas 1 y 2)
        assertTrue("Los detalles deberían contener información del producto", 
                 detalles.contains(1, "Producto"));
        assertTrue("Los detalles deberían contener información del precio", 
                 detalles.contains(1, "Precio"));
        assertTrue("Los detalles deberían contener información del tipo", 
                 detalles.contains(1, "Tipo"));
        
        // Verificar el total (última fila)
        assertTrue("Los detalles deberían contener el total", 
                 detalles.contains(3, "Total"));
        assertEquals("El total debería ser correcto", 
                   compra.calcularTotal(), detalles.get(3, "Total"));
    }
    
    @Test
    public void testSetterAndGetterCliente() {
        // Arrange
        Cliente nuevoCliente = null;
        try {
            nuevoCliente = new Cliente("Nuevo Cliente", 25, "Nuevo Apellido", false, 
                                "Nueva Nacionalidad", "Nueva Dirección", 
                                "nuevo@test.com", 987654321, new BigDecimal("200.00"));
        } catch (Exception e) {
            fail("No debería lanzar excepción al crear un nuevo cliente: " + e.getMessage());
        }
        
        // Act
        compra.setCliente(nuevoCliente);
        
        // Assert
        assertSame("El cliente debería ser actualizado correctamente", 
                 nuevoCliente, compra.getCliente());
    }
    
    @Test
    public void testFechaCompra() {
        // Arrange
        LocalDateTime fechaAntes = LocalDateTime.now().minusSeconds(1);
        
        // Act
        Compra nuevaCompra = new Compra();
        LocalDateTime fechaDespues = LocalDateTime.now().plusSeconds(1);
        
        // Assert
        assertNotNull("La fecha no debería ser nula", nuevaCompra.getfecha());
        assertTrue("La fecha debería ser posterior a fechaAntes", 
                 nuevaCompra.getfecha().isAfter(fechaAntes) || nuevaCompra.getfecha().equals(fechaAntes));
        assertTrue("La fecha debería ser anterior a fechaDespues", 
                 nuevaCompra.getfecha().isBefore(fechaDespues) || nuevaCompra.getfecha().equals(fechaDespues));
    }
    
    @Test
    public void testIdCompraAutoIncrement() {
        // Arrange & Act
        Compra compra1 = new Compra();
        Compra compra2 = new Compra();
        
        // Assert
        assertNotEquals("Los IDs de compras diferentes deberían ser distintos", 
                      compra1.getIDCompra(), compra2.getIDCompra());
        assertEquals("El ID de la segunda compra debería ser 1 más que el de la primera", 
                   compra1.getIDCompra() + 1, compra2.getIDCompra().intValue());
    }
}
