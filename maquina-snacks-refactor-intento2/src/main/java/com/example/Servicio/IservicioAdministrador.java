/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.Servicio;

/**
 *
 * @author Flia Seplveda Mendoz
 */
public interface IservicioAdministrador {
    void verProductosVendidos(IservicioCliente servicioCliente);
    void verGanancias(IservicioCliente servicioCliente);
    void verReportesOrdenados(IservicioCliente servicioCliente, String criterio);
    
}
