package com.example.Servicio;


import java.time.LocalDateTime;

import com.example.Dominio.Compra;
import com.google.common.collect.Table;

public interface IservicioCompra {
    /**
     * Crea una nueva compra para un cliente específico
     * @param idCliente ID del cliente que realiza la compra
     * @return La compra creada o null si el cliente no existe
     */
    Compra crearCompra(int idCliente);
    
    /**
     * Finaliza una compra, procesando el pago y actualizando el inventario
     * @param compra La compra a finalizar
     * @return true si la compra se finalizó correctamente, false en caso contrario
     */
    boolean finalizarCompra(Compra compra);
    
    /**
     * Cancela una compra existente
     * @param idCompra ID de la compra a cancelar
     * @return true si la compra se canceló correctamente, false en caso contrario
     */
    boolean cancelarCompra(int idCompra);
    
    /**
     * Obtiene todas las compras realizadas por un cliente específico
     * @param idCliente ID del cliente
     * @return Tabla con las compras del cliente organizadas por fecha
     */
    Table<LocalDateTime, Integer, Compra> obtenerComprasporCliente(int idCliente);
    
    /**
     * Genera un recibo en formato texto para una compra específica
     * @param idCompra ID de la compra
     * @return String con el recibo formateado
     */
    String generarReciboCompra(int idCompra);

    boolean agregarSnackACompra(Integer idCompra, int idSnack);

    // creamos un metodo que nos permita comprar por dropset dependiendo de si el usuario quiere o no 
    Compra comprarDropset(Integer idCliente) throws Exception;
}