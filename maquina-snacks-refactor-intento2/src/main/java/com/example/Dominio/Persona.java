package com.example.Dominio;

public class Persona {
    // de esta clase se sacaran los atributos que tendra nuestra persona asi como
    // los metodos (implementados de una interface)
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

    public Persona() {
    }

    public Persona(String nombre, String edadString, String apellidos, boolean sexo, String nacionalidad,
            String direccion,
            String correoElectronico, String numeroTelefonoString) throws Exception {
        this.nombre = nombre;
        // verificamos que la edad sea un numero y que no sea menor a 0
        try {
            this.edad = Integer.parseInt(edadString);
            if (this.edad < 0 || this.edad > 120) {
                throw new IllegalArgumentException("La edad no puede ser menor a 0 o mayor a 120");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La edad no es un numero");
        }
        this.apellidos = apellidos;
        this.sexo = sexo;
        if (this.sexo == true) {
            this.sexoString = "MASCULINO";
        } else {
            this.sexoString = "FEMENINO";
        }
        this.nacionalidad = nacionalidad;
        this.direccion = direccion;
        // verificamos que el correo electronico sea correcto
        if (correoElectronico.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            this.correoElectronico = correoElectronico;
        } else {
            throw new IllegalArgumentException("El correo electronico no es correcto");
        }
        this.idPersona = contadorPersona++;
        // verficamos que el nuero de telefono sea correcto y que el tipo de dato sea un
        // numero y que este sea mayor a cero y que no sea menor a 10
        if (!numeroTelefonoString.matches("\\d{10}")) {
            throw new IllegalArgumentException("El numero de telefono no es correcto");
        } else {
            try {
                this.numeroTelefono = Long.parseLong(numeroTelefonoString);
                if (this.numeroTelefono < 0) {
                    throw new IllegalArgumentException("El numero de telefono no puede ser menor a 0");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El numero de telefono no es un numero");
            }
        }
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

    public void setEdad(String edadString) {
        // verificamos que la edad sea un numero y que no sea menor a 0
        try {
            this.edad = Integer.parseInt(edadString);
            if (this.edad < 0) {
                throw new IllegalArgumentException("La edad no puede ser menor a 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La edad no es un numero");
        }
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setSEXOString(String sexo) {
        this.sexoString = sexo;
    }

    public String getSEXOString() {
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
        // verificamos que el correo electronico sea correcto
        if (correoElectronico.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            this.correoElectronico = correoElectronico;
        } else {
            throw new IllegalArgumentException("El correo electronico no es correcto");
        }
    }

    public long getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefonoString) {
        // verficamos que el nuero de telefono sea correcto y que el tipo de dato sea un
        // numero
        try {
            if (numeroTelefonoString.matches("\\d{10}")) {
                this.numeroTelefono = Long.parseLong(numeroTelefonoString);
            } else {
                throw new IllegalArgumentException("El numero de telefono no es correcto");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("El numero de telefono no es correcto");
        }
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
