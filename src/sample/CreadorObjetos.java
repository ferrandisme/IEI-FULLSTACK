package sample;

import MySQL.Insercion;
import modelo.*;

import java.util.ArrayList;
import java.util.List;

public class CreadorObjetos {

    public static Publicacion CrearPublicacion(String titulo, String anyo, String URL) {
        if (!titulo.equals("")) {
            return new Publicacion(titulo, anyo, URL);
        }
        return null;
    }

    public static Articulo CrearArticulo(String titulo, String anyo, String URL, String pagIni, String pagFin){
        if(!titulo.equals("")){
            return new Articulo(titulo,anyo,URL,pagIni,pagFin);
        }
        return null;
    }

    public static Ejemplar CrearEjemplar(String volumen, String numeroEjemplar){
        if(!volumen.equals("") && !numeroEjemplar.equals("")){
            return new Ejemplar(volumen,numeroEjemplar,"");
        }
        return null;
    }

    public static Revista CrearRevista(String revista){
        if(!revista.equals("")){
            return new Revista(revista);
        }
        return null;
    }

    public static Libro CrearLibro(String titulo, String anyo, String url, String editorial){
        if(!titulo.equals("")){
            return new Libro(titulo,anyo,url,editorial);
        }
        return null;
    }

    public static ComunicacionCongreso CrearComunicacion(String titulo, String anyo,String url, String edicion, String lugar, String pag_ini, String pag_fin){
        if(!titulo.equals("")){
            return new ComunicacionCongreso(titulo,anyo,url,edicion,lugar,pag_ini,pag_fin);
        }
        return null;
    }

    public static List<Persona> CrearPersona(List<String> autores)
    {
        if(autores.size() > 0)
        {
            List<Persona> personas = new ArrayList<Persona>();
            for(int i = 0; i < autores.size() ; i++)
            {
                String[] nombreCompleto;
                String nombre;
                String apellidos = "";
                if(autores.get(i).contains(",")){
                    nombreCompleto = autores.get(i).trim().split(",");
                    apellidos = nombreCompleto[0].trim();
                    nombre = nombreCompleto[1].trim();
                }else{
                    nombreCompleto = autores.get(i).split(" ");
                    nombre = nombreCompleto[0];
                    if(nombreCompleto.length > 1){
                        apellidos = autores.get(i).substring(nombre.length() + 1);
                    }
                }
                Persona persona = new Persona(nombre, apellidos);
                personas.add(persona);
            }
            return personas;
        }
        return null;
    }
}

