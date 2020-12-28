package modelo;

import java.util.List;

public class Revista {
    private String nombre;

    public Revista(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}