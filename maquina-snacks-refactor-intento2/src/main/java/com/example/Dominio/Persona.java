package com.example.Dominio;

import org.checkerframework.checker.units.qual.s;

public class Persona {
    // de esta clase se sacaran los atributos que tendra nuestra persona asi como los metodos (implementados de una interface)
    private String nombre; 
    private int edad; 
    private String apellidos;
    private boolean sexo;
    private String nacionalidad; 
    private String direccion;
    private String correoElectronico;
    private long numeroTelefono;
    private int idPersona;
    private static int contadorPersona = 1;
    private String sexoString;


    public Persona(){}


    public Persona(String nombre, int edad, String apellidos, boolean sexo, String nacionalidad, String direccion,
            String correoElectronico, long numeroTelefono) {
        this.nombre = nombre;
        this.edad = edad;
        this.apellidos = apellidos;
        this.sexo = sexo;
        if (this.sexo == true){
          this.sexoString = "MASCULINO";
        }else{
            this.sexoString = "FEMENINO";
        }
        this.nacionalidad = nacionalidad;
        this.direccion = direccion;
        this.correoElectronico = correoElectronico;
        this.numeroTelefono = numeroTelefono;
        this.idPersona = contadorPersona++;
    }
    
    
    // creamos los getters and setters 
    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getEdad() {
        return edad;
    }


    public void setEdad(int edad) {
        this.edad = edad;
    }


    public String getApellidos() {
        return apellidos;
    }


    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setSEXOString(String sexo){
        this.sexoString = sexo;
    }

    public String getSEXOString(){
        return this.sexoString;
    }


    public boolean isSexo() {
        return sexo;
    }


    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }


    public String getNacionalidad() {
        return nacionalidad;
    }


    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }


    public String getDireccion() {
        return direccion;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getCorreoElectronico() {
        return correoElectronico;
    }


    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }


    public long getNumeroTelefono() {
        return numeroTelefono;
    }


    public void setNumeroTelefono(long numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }


    public int getIdPersona() {
        return idPersona;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + edad;
        result = prime * result + ((apellidos == null) ? 0 : apellidos.hashCode());
        result = prime * result + (sexo ? 1231 : 1237);
        result = prime * result + ((nacionalidad == null) ? 0 : nacionalidad.hashCode());
        result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
        result = prime * result + ((correoElectronico == null) ? 0 : correoElectronico.hashCode());
        result = prime * result + (int) (numeroTelefono ^ (numeroTelefono >>> 32));
        result = prime * result + idPersona;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Persona other = (Persona) obj;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (edad != other.edad)
            return false;
        if (apellidos == null) {
            if (other.apellidos != null)
                return false;
        } else if (!apellidos.equals(other.apellidos))
            return false;
        if (sexo != other.sexo)
            return false;
        if (nacionalidad == null) {
            if (other.nacionalidad != null)
                return false;
        } else if (!nacionalidad.equals(other.nacionalidad))
            return false;
        if (direccion == null) {
            if (other.direccion != null)
                return false;
        } else if (!direccion.equals(other.direccion))
            return false;
        if (correoElectronico == null) {
            if (other.correoElectronico != null)
                return false;
        } else if (!correoElectronico.equals(other.correoElectronico))
            return false;
        if (numeroTelefono != other.numeroTelefono)
            return false;
        if (idPersona != other.idPersona)
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Persona [nombre=" + nombre + ", edad=" + edad + ", apellidos=" + apellidos + ", sexo=" + sexo
                + ", nacionalidad=" + nacionalidad + ", direccion=" + direccion + ", correoElectronico="
                + correoElectronico + ", numeroTelefono=" + numeroTelefono + ", idPersona=" + idPersona + "]";
    }

    

    
}
