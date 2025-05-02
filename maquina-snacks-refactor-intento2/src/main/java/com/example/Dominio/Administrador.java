/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Dominio;

import com.example.Servicio.ServicioClienteArchivo;
import com.example.Servicio.ServicioSnacksArchivo;
import com.example.Servicio.ServicioSnacksTable;
import com.google.common.collect.HashBasedTable;

/**
 *
 * @author Flia Seplveda Mendoz
 */
public class Administrador extends Persona {
    
    private int AdminID;
    private static int id = 1;
    private ServicioClienteArchivo servicioClientes;
    private ServicioSnacksArchivo servicioSnacks;
    private ServicioSnacksTable servicioSnacksReportes;
    
    // creamos un metodo privado inicializador 
    private void inicializadorAdministrador(){
          // aumentamos el id del cliente en cada instanica
          this.AdminID = id++;
          this.servicioClientes = servicioClientes;
          this.servicioSnacks = servicioSnacks;
          this.servicioSnacksReportes= servicioSnacksReportes;  
    }
    
    public Administrador(){
        inicializadorAdministrador();
    }

    public Administrador(int AdminID, String nombre, int edad, String apellidos, boolean sexo, String nacionalidad, String direccion, String correoElectronico, int numeroTelefono) {
        super(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono);
        inicializadorAdministrador();
    }   
}
