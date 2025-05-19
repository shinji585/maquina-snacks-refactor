package com.raven.service;

import java.util.List;
import java.util.Map;

import com.example.Dominio.Cliente;
import com.example.Servicio.ServicioClienteArchivo;
import com.raven.model.ModelLogin;
import com.raven.model.ModelUser;

public class ServiceUser {

    private final ServicioClienteArchivo servicioCliente;

    public ServiceUser() {
        this.servicioCliente = new ServicioClienteArchivo();
    }

    public ModelUser login(ModelLogin login) {
        Map<Integer, Cliente> clientes = servicioCliente.obtenerTodosLosClientes();

        for (Cliente cliente : clientes.values()) {
            if (cliente.getCorreoElectronico().equalsIgnoreCase(login.getEmail())
                    && cliente.getContraseña().equals(login.getPassword())) {

                return new ModelUser(
                        cliente.getID(),
                        cliente.getNombre(),
                        cliente.getCorreoElectronico(),
                        "" // No devolver la contraseña
                );
            }
        }

        return null;
    }

    public boolean checkDuplicateEmail(String email) {
        Map<Integer, Cliente> clientes = servicioCliente.obtenerTodosLosClientes();
        for (Cliente cliente : clientes.values()) {
            if (cliente.getCorreoElectronico().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDuplicateUser(String nombre) {
        Map<Integer, Cliente> clientes = servicioCliente.obtenerTodosLosClientes();
        for (Cliente cliente : clientes.values()) {
            if (cliente.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public boolean registerUser(ModelUser nuevoUsuario) {
    // 1) Carga todos los clientes existentes
    List<Cliente> lista = servicioCliente.cargarListaClientes();

    // 2) Verifica duplicados...
    //    (como hacías con checkDuplicateEmail / checkDuplicateUser)

    // 3) Genera ID y crea el Cliente de dominio
    int nuevoID = lista.stream()
                       .mapToInt(Cliente::getID)
                       .max()
                       .orElse(0) + 1;

    Cliente nuevo = new Cliente(
        nuevoID,
        nuevoUsuario.getUserName(),
        nuevoUsuario.getEmail(),
        nuevoUsuario.getPassword()
    );

    // 4) Persiste en JSON
    servicioCliente.guardarCliente(nuevo);
    return true;
}


    public boolean verifyCodeWithUser(int userID, String inputCode) {
        Map<Integer, Cliente> clientes = servicioCliente.obtenerTodosLosClientes();
        Cliente cliente = clientes.get(userID);

        if (cliente != null && inputCode.equals(cliente.getCodigoVerificacion())) {
            return true;
        }
        return false;
    }

    public void doneVerify(int userID) {
        Map<Integer, Cliente> clientes = servicioCliente.obtenerTodosLosClientes();
        Cliente cliente = clientes.get(userID);

        if (cliente != null) {
            cliente.setVerificado(true);
            cliente.setCodigoVerificacion(null); // Opcional: limpiar el código
            servicioCliente.actualizarCliente(cliente);
        }
    }

}
