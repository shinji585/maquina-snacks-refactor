package com.example.Servicio;

import java.util.Map;

import com.example.Dominio.Snack;
import com.google.common.collect.Table;

public interface IservicioSnakcs {
    
    // creamos los metodos de nuestra interface 
    void agregarSnack(Snack snack);
    void mostrarSnacks();
    Table<Integer,String,Snack> getSnacks();
    boolean comprarSnack(int id);
    Snack comprarSnack2(int id);
    public Map<Integer, Snack> obtenerTodosLosSnacksComoMapa() ;
    public Snack comprarSnackJSON(int idSnack) throws Exception ;

}
