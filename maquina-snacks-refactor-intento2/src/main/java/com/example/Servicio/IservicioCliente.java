package com.example.Servicio;
import java.io.File;
import java.math.BigDecimal;
import java.util.Map;
import com.example.Dominio.Cliente;
import com.example.Dominio.Compra;
/**
 * Interfaz que define las operaciones de servicio relacionadas con clientes
 */
public interface IservicioCliente {
   
    /**
     * Registra un nuevo cliente en el sistema
     * @param cliente El cliente a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    boolean registrarCliente(Cliente cliente);
   
    /**
     * Obtiene un cliente por su ID
     * @param id ID del cliente a buscar
     * @return El cliente encontrado o null si no existe
     */
    Cliente obtenerClientePorID(Integer id);
   
    /**
     * Obtiene todos los clientes registrados en el sistema
     * @return Mapa con todos los clientes, donde la clave es el ID del cliente
     */
    Map<Integer, Cliente> obtenerTodosLosClientes();
   
    /**
     * Actualiza la información de un cliente existente
     * @param cliente Cliente con la información actualizada
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizarCliente(Cliente cliente);
   
    /**
     * Elimina un cliente del sistema
     * @param id ID del cliente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminarCliente(Integer id);
   
    /**
     * Registra una compra para un cliente específico
     * @param cliente Cliente que realiza la compra
     * @param compra Compra a registrar
     * @return true si se registró correctamente, false en caso contrario
     */
    boolean registrarCompra(Cliente cliente, Compra compra);
   
    /**
     * Genera un PDF con el historial de compras de un cliente
     * @param idCliente ID del cliente
     * @return El archivo PDF generado o null si hubo un error
     */
    File generarHistorialClientePDF(Integer idCliente);
   
    /**
     * Calcula el gasto total histórico de un cliente
     * @param idCliente ID del cliente
     * @return El total gastado por el cliente
     */
    BigDecimal calcularGastoTotalCliente(Integer idCliente);
   
    /**
     * Cuenta el número de compras realizadas por un cliente
     * @param idCliente ID del cliente
     * @return Número de compras realizadas
     */
    int contarComprasCliente(Integer idCliente);
   
    /**
     * Verifica si existe un cliente con el ID proporcionado
     * @param id ID del cliente a verificar
     * @return true si el cliente existe, false en caso contrario
     */
    boolean existeCliente(Integer id);
   
    /**
     * Genera una factura (recibo) para una compra específica
     * @param idCompra ID de la compra
     * @return El archivo PDF generado o null si hubo un error
     */
    File generarFacturaCompra(Integer idCompra);
    
    /**
     * Busca un cliente por su ID (método adicional para ServicioCompraArchivo)
     * @param idCliente ID del cliente a buscar
     * @return El cliente encontrado o null si no existe
     */
    Cliente buscarCliente(int idCliente);
    
    /**
     * Guarda el historial de compras de un cliente (método adicional para ServicioCompraArchivo)
     * @param idCliente ID del cliente cuyo historial se guardará
     */
    void guardarHistorial(int idCliente);
}