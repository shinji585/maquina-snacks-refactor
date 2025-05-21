package com.raven.service;

import java.util.Map;

import com.example.Dominio.Cliente;
import com.example.Servicio.JSONStructureHelper;
import com.example.Servicio.ServicioClienteArchivo;
import com.raven.model.ModelLogin;
import com.raven.model.ModelUser;

public class ServiceUser {

    private final ServicioClienteArchivo servicioCliente;

    public ServiceUser() {
        this.servicioCliente = new ServicioClienteArchivo();
        // Asegúrate de que el archivo JSON esté correctamente inicializado
        JSONStructureHelper.inicializarArchivoJSON();
    }

    public ModelUser login(ModelLogin login) {
    Map<Integer, Cliente> clientes = servicioCliente.obtenerTodosLosClientes();

    for (Cliente cliente : clientes.values()) {
        if (cliente.getCorreoElectronico().equalsIgnoreCase(login.getEmail()) &&
            cliente.getContraseña().equals(login.getPassword())) {

            return new ModelUser(
                cliente.getID(),
                cliente.getNombre(),
                cliente.getCorreoElectronico(),
                "" // No devuelvas la contraseña
            );
        }
    }

    return null;
}


    public boolean checkDuplicateEmail(String email) {
        if (email == null) return false;
        
        Map<Integer, Cliente> clientesMap = JSONStructureHelper.asegurarMapaClientes();
        
        for (Cliente cliente : clientesMap.values()) {
            String correoCliente = cliente.getCorreoElectronico();
            if (correoCliente != null && correoCliente.equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDuplicateUser(String nombre) {
        if (nombre == null) return false;
        
        Map<Integer, Cliente> clientesMap = JSONStructureHelper.asegurarMapaClientes();
        
        for (Cliente cliente : clientesMap.values()) {
            String nombreCliente = cliente.getNombre();
            if (nombreCliente != null && nombreCliente.equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public boolean registerUser(ModelUser nuevoUsuario) {
        if (nuevoUsuario == null) return false;

        // Verificar si el nombre de usuario ya existe
        if (checkDuplicateUser(nuevoUsuario.getUserName())) {
            return false;
        }
        // Verificar si el correo ya existe
        if (checkDuplicateEmail(nuevoUsuario.getEmail())) {
            return false;
        }  
        
        // Usar el helper para registrar
        return JSONStructureHelper.registrarNuevoUsuario(nuevoUsuario);
    }

    public boolean verifyCodeWithUser(int userID, String inputCode) {
        if (inputCode == null) return false;
        
        Map<Integer, Cliente> clientesMap = JSONStructureHelper.asegurarMapaClientes();
        Cliente cliente = clientesMap.get(userID);
        
        String codigoCliente = cliente != null ? cliente.getCodigoVerificacion() : null;
        return codigoCliente != null && codigoCliente.equals(inputCode);
    }

    public void doneVerify(int userID) {
        Map<Integer, Cliente> clientesMap = JSONStructureHelper.asegurarMapaClientes();
        Cliente cliente = clientesMap.get(userID);
        
        if (cliente != null) {
            cliente.setVerificado(true);
            cliente.setCodigoVerificacion(null); // Limpiar el código
            
            // Actualizar el mapa y guardar
            clientesMap.put(userID, cliente);
            JSONStructureHelper.guardarMapaClientes(clientesMap);
        }
    }
}