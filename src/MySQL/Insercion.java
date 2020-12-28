package MySQL;

import modelo.*;

import java.util.ArrayList;
import java.util.List;

public class Insercion {

    public static void insertar(Ejemplar ejemplar, Articulo articulo, Revista revista, List<Persona> personas) {
        List<Integer> idpersonas;
        idpersonas = insertarPersona(personas);
        int idpublicacion = insertarPublicacion(articulo);
        int idrevista = insertarRevista(revista);
        int idejemplar = insertarEjemplar(idrevista, ejemplar);
        insertarArticulo(idejemplar, idpublicacion, articulo);
        insertarRelacion(idpersonas, idpublicacion);
    }

    public static void insertarRelacion(List<Integer> idpersonas, int idpublicacion) {
        for(int i = 0; i < idpersonas.size(); i++)
            Conexion.meterRelacion(idpublicacion, idpersonas.get(i));
    }

    public static void insertarArticulo(int idejemplar, int idpublicacion, Articulo articulo) {
        Conexion.meterArticulo(idpublicacion, idejemplar, Integer.parseInt(articulo.getPag_ini()), Integer.parseInt(articulo.getPag_fin()));
    }

    public static int insertarEjemplar(int idrevista, Ejemplar ejemplar) {
        //System.out.println(ejemplar.getVolumen() + " - " + ejemplar.getNumero() + " - " + idrevista);
        int existe = Conexion.obtenerIdEjemplar(ejemplar.getVolumen(), ejemplar.getNumero(), idrevista);
        if (existe == -9) {
            Conexion.meterEjemplar(ejemplar.getVolumen(), ejemplar.getNumero(), ejemplar.getMes(), idrevista);
            return Conexion.obtenerIdEjemplar(ejemplar.getVolumen(), ejemplar.getNumero(), idrevista);
        } else if (existe == -1) {
            //System.out.println("Error en la conexión a la base de datos.");
            return -1;
        } else {
            //System.out.println("Ya existe el ejemplar.");
            return existe;
        }
    }

    public static int insertarPublicacion(Publicacion publicacion) {
        int existe = Conexion.obtenerIdPublicacion(publicacion.getTitulo(), publicacion.getAnyo(), publicacion.getUrl());
        if (existe == -9) {
            Conexion.meterPublicacion(publicacion.getTitulo(), publicacion.getAnyo(), publicacion.getUrl());
            return  Conexion.obtenerIdPublicacion(publicacion.getTitulo(), publicacion.getAnyo(), publicacion.getUrl());
        } else if (existe == -1) {
            //System.out.println("Error en la conexión a la base de datos.");
            return -1;
        } else {
            //System.out.println("Ya existe la publicación.");
            return existe;
        }
    }

    public static List<Integer> insertarPersona(List<Persona> personas) {
        List<Integer> idpersonas = new ArrayList<>();
        for(int i = 0; i < personas.size(); i++) {
            String nombre = personas.get(i).getNombre();
            String apellidos = personas.get(i).getApellidos();
            int existe = Conexion.obtenerIdPersona(nombre, apellidos);
            if (existe == -9) {
                Conexion.meterPersona(nombre, apellidos);
                idpersonas.add(Conexion.obtenerIdPersona(nombre,apellidos));
            } else if (existe == -1) {
                //System.out.println("Error en la conexión a la base de datos.");
            } else {
                //System.out.println("Ya existe la revista.");
            }
        }
        return idpersonas;
    }

    public static int insertarRevista(Revista revista) {
        int existe = Conexion.obtenerIdRevista(revista.getNombre());
        if (existe == -9) {
            Conexion.meterRevista(revista.getNombre());
            return Conexion.obtenerIdRevista(revista.getNombre());
        } else if (existe == -1) {
            //System.out.println("Error en la conexión a la base de datos.");
            return -1;
        } else {
            //System.out.println("Ya existe la revista.");
            return existe;
        }
    }

    public static void insertarLibro(int idpublicacion, Libro libro) {
        Conexion.meterLibro(idpublicacion, libro.getEditorial());
    }

    public static void insertarComunicacionCongreso(int idpublicacion, ComunicacionCongreso comunicacionCongreso) {
        Conexion.meterComunicacion(idpublicacion, comunicacionCongreso.getEdicion(), comunicacionCongreso.getLugar(), comunicacionCongreso.getPag_ini(), comunicacionCongreso.getPag_fin());
    }
}
