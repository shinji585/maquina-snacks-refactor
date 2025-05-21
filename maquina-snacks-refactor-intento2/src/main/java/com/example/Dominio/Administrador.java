package com.example.Dominio;

public class Administrador extends Persona {

    private int administradorID;
    private static int id = 1;


    public Administrador(String nombre, String edad, String apellidos, boolean sexo, String nacionalidad, String direccion, String correoElectronico, String numeroTelefono) throws Exception {
        super(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono);
        this.administradorID = id++;
    }

    public Administrador(int administradorID, String nombre, String edad, String apellidos, boolean sexo, String nacionalidad, String direccion, String correoElectronico, String numeroTelefono) throws Exception {
        super(nombre, edad, apellidos, sexo, nacionalidad, direccion, correoElectronico, numeroTelefono);
        this.administradorID = administradorID;
        if (administradorID >= id) {
            id = administradorID + 1;
        }
    }

    public int getAdministradorID() {
        return administradorID;
    }

    public void setAdministradorID(int administradorID) {
        this.administradorID = administradorID;
    }

    public static void reiniciarContador() {
        id = 1;
    }

    public static void establecerContador(int nuevoValor) {
        if (nuevoValor > 0) {
            id = nuevoValor;
        }
    }

    public static int obtenerContadorActual() {
        return id;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "administradorID=" + administradorID +
                ", nombre='" + getNombre() + '\'' +
                ", apellidos='" + getApellidos() + '\'' +
                ", correoElectronico='" + getCorreoElectronico() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Administrador)) return false;
        Administrador that = (Administrador) o;
        return administradorID == that.administradorID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(administradorID);
    }
}
