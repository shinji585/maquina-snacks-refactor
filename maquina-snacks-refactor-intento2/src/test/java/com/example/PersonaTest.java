package com.example;

import static org.junit.Assert.*;
import org.junit.Test;

import com.example.Dominio.Persona;

public class PersonaTest {

    @Test
    public void testConstructorPersona() {
        // Arrange
        String nombre = "Juan";
        int edad = 25;
        String apellidos = "Pérez García";
        boolean sexo = true;
        String nacionalidad = "Mexicana";
        String direccion = "Calle Principal 123";
        String correoElectronico = "juan@example.com";
        int numeroTelefono = 123456789;
        
        // Act
        Persona persona = new Persona(nombre, edad, apellidos, sexo, nacionalidad, 
                                      direccion, correoElectronico, numeroTelefono);
        
        // Assert
        assertEquals(nombre, persona.getNombre());
        assertEquals(edad, persona.getEdad());
        assertEquals(apellidos, persona.getApellidos());
        assertEquals(sexo, persona.isSexo());
        assertEquals(nacionalidad, persona.getNacionalidad());
        assertEquals(direccion, persona.getDireccion());
        assertEquals(correoElectronico, persona.getCorreoElectronico());
        assertEquals(numeroTelefono, persona.getNumeroTelefono());
        assertTrue("El ID de persona debe ser mayor que cero", persona.getIdPersona() > 0);
    }
    
    @Test
    public void testSettersAndGetters() {
        // Arrange
        Persona persona = new Persona();
        String nombre = "Maria";
        int edad = 30;
        String apellidos = "López Sánchez";
        boolean sexo = false;
        String nacionalidad = "Española";
        String direccion = "Avenida Central 456";
        String correoElectronico = "maria@example.com";
        int numeroTelefono = 987654321;
        
        // Act
        persona.setNombre(nombre);
        persona.setEdad(edad);
        persona.setApellidos(apellidos);
        persona.setSexo(sexo);
        persona.setNacionalidad(nacionalidad);
        persona.setDireccion(direccion);
        persona.setCorreoElectronico(correoElectronico);
        persona.setNumeroTelefono(numeroTelefono);
        
        // Assert
        assertEquals(nombre, persona.getNombre());
        assertEquals(edad, persona.getEdad());
        assertEquals(apellidos, persona.getApellidos());
        assertEquals(sexo, persona.isSexo());
        assertEquals(nacionalidad, persona.getNacionalidad());
        assertEquals(direccion, persona.getDireccion());
        assertEquals(correoElectronico, persona.getCorreoElectronico());
        assertEquals(numeroTelefono, persona.getNumeroTelefono());
    }
    
    @Test
    public void testEquals() {
        // Arrange
        Persona persona1 = new Persona("Juan", 25, "Pérez", true, "Mexicana", 
                                       "Calle 123", "juan@example.com", 123456789);
        Persona persona2 = new Persona("Juan", 25, "Pérez", true, "Mexicana", 
                                       "Calle 123", "juan@example.com", 123456789);
        Persona personaDiferente = new Persona("Ana", 30, "López", false, "Colombiana", 
                                              "Calle 456", "ana@example.com", 987654321);
        
        // Act & Assert
        assertFalse("Personas con diferentes IDs no deberían ser iguales", persona1.equals(persona2));
        assertFalse("Personas diferentes no deberían ser iguales", persona1.equals(personaDiferente));
        assertTrue("Una persona debería ser igual a sí misma", persona1.equals(persona1));
        assertFalse("Una persona no debería ser igual a null", persona1.equals(null));
        assertFalse("Una persona no debería ser igual a un objeto de otra clase", persona1.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        // Arrange
        Persona persona1 = new Persona("Juan", 25, "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", 123456789);
        Persona persona2 = new Persona("Juan", 25, "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", 123456789);
        
        // Act & Assert
        assertNotEquals("Personas con diferentes IDs deberían tener distintos hashCode", 
                      persona1.hashCode(), persona2.hashCode());
        assertEquals("La misma persona debería tener el mismo hashCode", 
                   persona1.hashCode(), persona1.hashCode());
    }
    
    @Test
    public void testToString() {
        // Arrange
        Persona persona = new Persona("Juan", 25, "Pérez", true, "Mexicana", 
                                    "Calle 123", "juan@example.com", 123456789);
        
        // Act
        String resultado = persona.toString();
        
        // Assert
        assertTrue(resultado.contains("Juan"));
        assertTrue(resultado.contains("25"));
        assertTrue(resultado.contains("Pérez"));
        assertTrue(resultado.contains("Mexicana"));
        assertTrue(resultado.contains("Calle 123"));
        assertTrue(resultado.contains("juan@example.com"));
        assertTrue(resultado.contains("123456789"));
    }
    
    @Test
    public void testIdPersonaAutoIncrement() {
        // Arrange & Act
        Persona persona1 = new Persona("Juan", 25, "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", 123456789);
        int idPersona1 = persona1.getIdPersona();
        
        Persona persona2 = new Persona("Ana", 30, "López", false, "Colombiana", 
                                      "Calle 456", "ana@example.com", 987654321);
        int idPersona2 = persona2.getIdPersona();
        
        // Assert
        assertNotEquals("Los IDs de personas diferentes deberían ser distintos", 
                      idPersona1, idPersona2);
        assertEquals("El ID de la segunda persona debería ser 1 más que el de la primera", 
                   idPersona1 + 1, idPersona2);
    }
}