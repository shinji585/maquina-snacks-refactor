package com.example;

import static org.junit.Assert.*;
import org.junit.Test;

import com.example.Dominio.Persona;

public class PersonaTest {

    @Test
    public void testConstructorPersona() throws Exception {
        // Arrange
        String nombre = "Juan";
        String edad = "25";
        String apellidos = "Pérez García";
        boolean sexo = true;
        String nacionalidad = "Mexicana";
        String direccion = "Calle Principal 123";
        String correoElectronico = "juan@example.com";
        String numeroTelefono = "1234567890";
        
        // Act
        Persona persona = new Persona(nombre, edad, apellidos, sexo, nacionalidad, 
                                      direccion, correoElectronico, numeroTelefono);
        
        // Assert
        assertEquals(nombre, persona.getNombre());
        assertEquals(Integer.parseInt(edad), persona.getEdad()); // Comparar con el valor entero
        assertEquals(apellidos, persona.getApellidos());
        assertEquals(sexo, persona.isSexo());
        assertEquals("MASCULINO", persona.getSEXOString()); // Verificar la representación en texto del sexo
        assertEquals(nacionalidad, persona.getNacionalidad());
        assertEquals(direccion, persona.getDireccion());
        assertEquals(correoElectronico, persona.getCorreoElectronico());
        assertEquals(Long.parseLong(numeroTelefono), persona.getNumeroTelefono()); // Comparar con el valor long
        assertTrue("El ID de persona debe ser mayor que cero", persona.getIdPersona() > 0);
    }
    
    @Test
    public void testSettersAndGetters() throws Exception {
        // Arrange
        Persona persona = new Persona();
        String nombre = "Maria";
        String edad = "30";
        String apellidos = "López Sánchez";
        boolean sexo = false;
        String nacionalidad = "Española";
        String direccion = "Avenida Central 456";
        String correoElectronico = "maria@example.com";
        String numeroTelefono = "9876543210"; // 10 dígitos como requiere la validación
        
        // Act
        persona.setNombre(nombre);
        persona.setEdad(edad);
        persona.setApellidos(apellidos);
        persona.setSexo(sexo);
        persona.setSEXOString("FEMENINO"); // Establecer el valor de sexoString
        persona.setNacionalidad(nacionalidad);
        persona.setDireccion(direccion);
        persona.setCorreoElectronico(correoElectronico);
        persona.setNumeroTelefono(numeroTelefono);
        
        // Assert
        assertEquals(nombre, persona.getNombre());
        assertEquals(Integer.parseInt(edad), persona.getEdad());
        assertEquals(apellidos, persona.getApellidos());
        assertEquals(sexo, persona.isSexo());
        assertEquals("FEMENINO", persona.getSEXOString());
        assertEquals(nacionalidad, persona.getNacionalidad());
        assertEquals(direccion, persona.getDireccion());
        assertEquals(correoElectronico, persona.getCorreoElectronico());
        assertEquals(Long.parseLong(numeroTelefono), persona.getNumeroTelefono());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdadInvalida() throws Exception {
        new Persona("Juan", "-5", "Pérez", true, "Mexicana", 
                   "Calle 123", "juan@example.com", "1234567890");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdadNoNumerica() throws Exception {
        new Persona("Juan", "abc", "Pérez", true, "Mexicana", 
                   "Calle 123", "juan@example.com", "1234567890");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorreoElectronicoInvalido() throws Exception {
        new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                   "Calle 123", "correo-invalido", "1234567890");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNumeroTelefonoInvalido() throws Exception {
        new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                   "Calle 123", "juan@example.com", "123"); // Menos de 10 dígitos
    }
    
    @Test
    public void testEquals() throws Exception {
        // Arrange
        Persona persona1 = new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", "1234567890");
        Persona persona2 = new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", "1234567890");
        Persona personaDiferente = new Persona("Ana", "30", "López", false, "Colombiana", 
                                              "Calle 456", "ana@example.com", "9876543210");
        
        // Act & Assert
        assertFalse("Personas con diferentes IDs no deberían ser iguales", persona1.equals(persona2));
        assertFalse("Personas diferentes no deberían ser iguales", persona1.equals(personaDiferente));
        assertTrue("Una persona debería ser igual a sí misma", persona1.equals(persona1));
        assertFalse("Una persona no debería ser igual a null", persona1.equals(null));
        assertFalse("Una persona no debería ser igual a un objeto de otra clase", persona1.equals(new Object()));
    }
    
    @Test
    public void testHashCode() throws Exception {
        // Arrange
        Persona persona1 = new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", "1234567890");
        Persona persona2 = new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", "1234567890");
        
        // Act & Assert
        assertNotEquals("Personas con diferentes IDs deberían tener distintos hashCode", 
                      persona1.hashCode(), persona2.hashCode());
        assertEquals("La misma persona debería tener el mismo hashCode", 
                   persona1.hashCode(), persona1.hashCode());
    }
    
    @Test
    public void testToString() throws Exception {
        // Arrange
        Persona persona = new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                                    "Calle 123", "juan@example.com", "1234567890");
        
        // Act
        String resultado = persona.toString();
        
        // Assert
        assertTrue(resultado.contains("Juan"));
        assertTrue(resultado.contains("25"));
        assertTrue(resultado.contains("Pérez"));
        assertTrue(resultado.contains("Mexicana"));
        assertTrue(resultado.contains("Calle 123"));
        assertTrue(resultado.contains("juan@example.com"));
        assertTrue(resultado.contains("1234567890"));
    }
    
    @Test
    public void testIdPersonaAutoIncrement() throws Exception {
        // Arrange & Act
        Persona persona1 = new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                                      "Calle 123", "juan@example.com", "1234567890");
        int idPersona1 = persona1.getIdPersona();
        
        Persona persona2 = new Persona("Ana", "30", "López", false, "Colombiana", 
                                      "Calle 456", "ana@example.com", "9876543210");
        int idPersona2 = persona2.getIdPersona();
        
        // Assert
        assertNotEquals("Los IDs de personas diferentes deberían ser distintos", 
                      idPersona1, idPersona2);
        assertEquals("El ID de la segunda persona debería ser 1 más que el de la primera", 
                   idPersona1 + 1, idPersona2);
    }
    
    @Test
    public void testSexoString() throws Exception {
        // Arrange & Act
        Persona personaMasculino = new Persona("Juan", "25", "Pérez", true, "Mexicana", 
                                             "Calle 123", "juan@example.com", "1234567890");
        Persona personaFemenino = new Persona("Ana", "30", "López", false, "Colombiana", 
                                            "Calle 456", "ana@example.com", "9876543210");
        
        // Assert
        assertEquals("MASCULINO", personaMasculino.getSEXOString());
        assertEquals("FEMENINO", personaFemenino.getSEXOString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdadMayorLimite() throws Exception {
        new Persona("Juan", "121", "Pérez", true, "Mexicana", 
                   "Calle 123", "juan@example.com", "1234567890");
    }
}