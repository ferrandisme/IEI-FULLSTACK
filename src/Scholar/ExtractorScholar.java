package Scholar;

import MySQL.Conexion;
import MySQL.Insercion;
import modelo.ComunicacionCongreso;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.CreadorObjetos;
import sample.Extractor;
import sample.Main;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ExtractorScholar extends Extractor {
    public static String path = System.getProperty("user.home") + "\\AppData\\LocalLow\\IEI";


    public ExtractorScholar(int inicio, int fin){super(inicio,fin);}

    int total = 0;
    public int Empezar(){

        try {
            String contents = new String(Files.readAllBytes((Paths.get(path + "\\Scholar.json"))));
            JSONObject o = new JSONObject(contents);
            JSONArray articulos = (JSONArray) o.get("articles");
            JSONArray libros = (JSONArray) ( o.get("books"));
            JSONArray inproceedings =(JSONArray) (o.get("inproceedings"));
            //JSONArray incollection = (JSONArray) ( o.get("incollection"));
            
            
            
            

            for (int i = 0; i < articulos.length(); i++) {
                System.out.println("ScholarArticulo " + i);

                JSONObject oarticulo = articulos.getJSONObject(i);

                ReiniciarVariables();

                try {

                    TratarAnyo(oarticulo);
                    if (anyoFin >= Integer.parseInt(anyo) && anyoInicio <= Integer.parseInt(anyo)) {
                        TratarAutores(oarticulo);
                        TratarTitulo(oarticulo);
                        TratarURL(oarticulo);
                        TratarPaginas(oarticulo);
                        TratarRevista(oarticulo);
                        TratarNumeroEjemplar(oarticulo);
                        TratarVolumen(oarticulo);
                        if(CrearObjetosBaseDeDatos())
                        	total++;
                    }
                    else
                        System.out.println("Omitiendo año porque no esta entre los valores dados " + anyo);

                }
                catch(Exception e) {
                    System.err.println("Abortada la creacion por un error al interpretar el JSON");
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < libros.length(); i++) {
                System.out.println("ScholarLibro" + i);

                JSONObject olibro = libros.getJSONObject(i);

                ReiniciarVariables();

                try {

                    TratarAnyo(olibro);
                    if (anyoFin >= Integer.parseInt(anyo) && anyoInicio <= Integer.parseInt(anyo)) {
                        TratarAutores(olibro);
                        TratarTitulo(olibro);
                        TratarPaginas(olibro);
                        //TratarEditorial(olibro);
                        TratarLibro(olibro);

                        if(CrearObjetosBaseDeDatosLibros())
                        	total++;
                    }
                    else
                        System.out.println("Omitiendo año porque no esta entre los valores dados " + anyo);

                }
                catch(Exception e) {
                    System.err.println("Abortada la creacion por un error al interpretar el JSON");
                    e.printStackTrace();
                }


            }
            for (int i = 0; i < inproceedings.length(); i++) {
            System.out.println("ScholarInproccedings " + 0);

            JSONObject oinproceedings = inproceedings.getJSONObject(i);
            
            ReiniciarVariables();

            try {

                TratarAnyo(oinproceedings);
                if (anyoFin >= Integer.parseInt(anyo) && anyoInicio <= Integer.parseInt(anyo)) {
                    TratarAutores(oinproceedings);
                    TratarTitulo(oinproceedings);
                    TratarPaginas(oinproceedings);
                    TratarEdicion(oinproceedings);
                    TratarLugar(oinproceedings);
                    TratarTituloLibro(oinproceedings);

                    if(CrearObjetosBaseDeDatosComunicacion())
                        total++;
                }
                else
                    System.out.println("Omitiendo año porque no esta entre los valores dados " + anyo);

            }
            catch(Exception e) {
                System.err.println("Abortada la creacion por un error al interpretar el JSON");
                e.printStackTrace();
            }
            }

            /*System.out.println("ScholarIncollection " + 0);

            ReiniciarVariables();

            try {
                TratarAutores(incollection);
                TratarAnyo(incollection);
                TratarTitulo(incollection);
                TratarPaginas(incollection);
                //TratarEditorial(incollection);
                TratarTituloLibro(incollection);

                if(CrearObjetosBaseDeDatosLibros())
                	total++;
            }
            catch(Exception e) {
                System.err.println("Abortada la creacion por un error al interpretar el JSON");
                e.printStackTrace();
            }*/

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return total;
    }

    public boolean CrearObjetosBaseDeDatosLibros() {
        Publicacion = CreadorObjetos.CrearPublicacion(titulo, anyo, URL);
        Libro = CreadorObjetos.CrearLibro(titulo, anyo, URL, libro);
        if (Publicacion != null && Conexion.obtenerIdPublicacion(titulo, anyo, URL) == -9 && Libro != null) {
            int idpublicacion = Insercion.insertarPublicacion(Publicacion);
            if (idpublicacion > 0) {
                Personas = CreadorObjetos.CrearPersona(autores);
                if (Personas != null) {
                    List<Integer> autores = Insercion.insertarPersona(Personas);
                    Insercion.insertarRelacion(autores, idpublicacion);
                }

                    Insercion.insertarLibro(idpublicacion, Libro);
                return true;
            }
        }
        return false;
    }

    public boolean CrearObjetosBaseDeDatosComunicacion(){
        Publicacion = CreadorObjetos.CrearPublicacion(titulo, anyo, URL);
        Comunicacion = CreadorObjetos.CrearComunicacion(titulo, anyo, URL, edicion, lugar, pagIni, pagFin);
        if (Publicacion != null && Conexion.obtenerIdPublicacion(titulo, anyo, URL) == -9 && Comunicacion != null) {
            int idpublicacion = Insercion.insertarPublicacion(Publicacion);
            if (idpublicacion > 0) {
                Personas = CreadorObjetos.CrearPersona(autores);
                if (Personas != null) {
                    List<Integer> autores = Insercion.insertarPersona(Personas);
                    Insercion.insertarRelacion(autores, idpublicacion);
                }
                    Insercion.insertarComunicacionCongreso(idpublicacion, Comunicacion);
                   return true; 
            }
        }
        return false;
    }

    public  void TratarAutores(JSONObject oarticulo)
    {
        Object oAuthor = null;
        try {
            oAuthor = oarticulo.get("author");
        }
        catch(Exception e){}

        if(oAuthor == null){
        }
        else  {
            String author = Main.Tratar("$", oAuthor);
            String[] aux = author.split("and");
            for(String a : aux){
                autores.add(a);
            }
        }
    }

    public void TratarTituloLibro(JSONObject oarticulo)
    {
        Object oTituloLibro = null;
        try {
            oTituloLibro = oarticulo.get("booktitle");
        }
        catch(Exception e){}
        if(oTituloLibro == null){
        }else
        {
            libro = oTituloLibro.toString();
        }
    }

    public void TratarPaginas(JSONObject oarticulo)
    {
        Object oPaginas = null;
        try {
            oPaginas = oarticulo.get("pages");
        } catch (Exception e) {
        }
        if (oPaginas == null) {
        } else{
            String[] pagina = oPaginas.toString().split("--");

            if(pagina.length > 1){
                pagIni = TratarPagina(pagina[0]);
                pagFin = TratarPagina(pagina[1]);
            }else{
                pagIni = TratarPagina(pagina[0]);
                pagFin = TratarPagina(pagina[0]);
            }
        }
    }
    public String TratarPagina(String pagina) {
        if(pagina.contains("e")){
            pagina = pagina.substring(1);
        }
        if(pagina.contains(":")){
            String[] aux = pagina.split(":");
            pagina = aux[1];

        }
        return pagina;
    }

    public void TratarAnyo(JSONObject oarticulo)
    {
        Object oYear = null;
        try {
            oYear = oarticulo.get("year");
        }
        catch(Exception e){}
        if(oYear == null){
        }else
        {
            anyo = oYear.toString();
        }
    }

    public void TratarNumeroEjemplar(JSONObject oarticulo)
    {
        Object oNumber = null;
        try {
            oNumber = oarticulo.get("number");
        }
        catch(Exception e){}
        if(oNumber == null){
        }else
        {
            numeroEjemplar = oNumber.toString();
        }
    }
    
    public void TratarVolumen(JSONObject oarticulo)
    {
        Object oVolume = null;
        try {
            oVolume = oarticulo.get("volume");
        }
        catch(Exception e){}
        if(oVolume == null){
        }else
        {
            volumen = oVolume.toString();
        }
    }

    public void TratarTitulo(JSONObject oarticulo)
    {
        Object oTitle = null;
        try {
            oTitle = oarticulo.get("title");
        }
        catch(Exception e){}
        if(oTitle == null){
        }else
        {
            titulo = oTitle.toString();
        }
    }

    public void TratarRevista(JSONObject oarticulo)
    {
        Object oJournal = null;
        try {
            oJournal = oarticulo.get("journal");
        }
        catch(Exception e){}
        if(oJournal == null){
        }else
        {
            revista = oJournal.toString();
        }
    }

    public void TratarLibro(JSONObject oarticulo)
    {
        Object oTitutlo = null;
        try {
            oTitutlo = oarticulo.get("title");
        }
        catch(Exception e){}
        if(oTitutlo == null){
        }else
        {
            libro = oTitutlo.toString();
        }
    }

    public void TratarEditorial(JSONObject oarticulo)
    {
        Object oEditorial = null;
        try {
            oEditorial = oarticulo.get("publisher");
        }
        catch(Exception e){}
        if(oEditorial == null){
        }else
        {
            libro = oEditorial.toString();
        }
    }

    public void TratarURL(JSONObject oarticulo){
        URL = "";
    }

    public void TratarEdicion(JSONObject oarticulo){
        edicion = "";
    }

    public void TratarLugar(JSONObject oarticulo){
        lugar = "";
    }
}
