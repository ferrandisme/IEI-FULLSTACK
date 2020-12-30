package sample;

import MySQL.Conexion;
import MySQL.Insercion;
import modelo.*;

import java.util.ArrayList;
import java.util.List;

public class Extractor {
    public Publicacion Publicacion;
    public Articulo Articulo;
    public Ejemplar Ejemplar;
    public Revista Revista;
    public Libro Libro;
    public ComunicacionCongreso Comunicacion;

    public List<String> autores;

    public String volumen;
    public String anyo;
    public String numeroEjemplar;
    public String titulo;
    public String revista;
    public String libro;
    public String tituloLibro;
    public String edicion;
    public String lugar;
    public String URL;
    public String pagIni;
    public String pagFin;
    public List<Persona> Personas;

    public int anyoFin;
    public int anyoInicio;

    public boolean CrearObjetosBaseDeDatos(){
        Publicacion = CreadorObjetos.CrearPublicacion(titulo, anyo, URL);
        Articulo = CreadorObjetos.CrearArticulo(titulo, anyo, URL, pagIni, pagFin);
        Revista = CreadorObjetos.CrearRevista(revista);
        Ejemplar = CreadorObjetos.CrearEjemplar(volumen, numeroEjemplar);
        if (Publicacion != null && Conexion.obtenerIdPublicacion(titulo, anyo, URL) == -9 && Articulo != null && Ejemplar != null & Revista != null) {
            int idpublicacion = Insercion.insertarPublicacion(Publicacion);
            if (idpublicacion > 0) {
                Personas = CreadorObjetos.CrearPersona(autores);
                if(Personas != null) {
                    List<Integer> autores = Insercion.insertarPersona(Personas);
                    Insercion.insertarRelacion(autores, idpublicacion);
                }

                int idejemplar = -1;
                int idrevista = -1;
                //Insertar revista
                    idrevista = Insercion.insertarRevista(Revista);

                //Insertar ejemplar
                    idejemplar = Insercion.insertarEjemplar(idrevista, Ejemplar);

                //insertar articulo
                    Insercion.insertarArticulo(idejemplar, idpublicacion, Articulo);
                    return true;
            }
        }
        return false;
    }

    public void ReiniciarVariables(){
        autores = new ArrayList<String>();
        anyo = "";
        numeroEjemplar = "";
        titulo = "";
        revista = "";
        libro = "";
        tituloLibro = "";
        edicion = "";
        lugar = "";
        URL = "";
        pagFin = "";
        pagIni = "";
        volumen = "";
        Articulo = null;
        Ejemplar = null;
        Publicacion = null;
        Personas = null;
        Libro = null;
        Comunicacion = null;
    }

    public Extractor(int inicio, int fin){
        anyoInicio = inicio;
        anyoFin = fin;
    }
}
