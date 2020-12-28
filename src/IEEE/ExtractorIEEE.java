package IEEE;

import MySQL.Insercion;
import modelo.*;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.CreadorObjetos;
import sample.Extractor;
import sample.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExtractorIEEE extends Extractor {
    public static String path = System.getProperty("user.home") + "\\AppData\\LocalLow\\IEI";



    //public ExtractorIEEE(){}

    public ExtractorIEEE(int inicio, int fin){super(inicio,fin);}


    int total = 0;
    public int Empezar()
    {


        try {
            String contents = new String(Files.readAllBytes((Paths.get(path + "\\IEEE.json"))));
            JSONObject o = new JSONObject(contents);
            JSONArray articulos = (JSONArray) ( o.get("articles"));

            for (int i = 0; i < articulos.length(); i++) {
                System.out.println("IEEE " + i);

                try {
                    JSONObject oarticulo = articulos.getJSONObject(i);

                    //Inicio proceso
                    ReiniciarVariables();
                    TratarAnyo(oarticulo);

                    if (anyoFin >= Integer.parseInt(anyo) && anyoInicio <= Integer.parseInt(anyo)) {

                        TratarAutores(oarticulo);
                        TratarVolumen(oarticulo);
                        TratarNumeroEjemplar(oarticulo);
                        TratarPagIni(oarticulo);
                        TratarPagFin(oarticulo);
                        TratarTitulo(oarticulo);
                        TratarURL(oarticulo);
                        TratarRevista(oarticulo);

                        CrearObjetosBaseDeDatos();
                        total++;
                    } else
                        System.out.println("Omitiendo año porque no esta entre los valores dados " + anyo);
                }catch (Exception e){e.printStackTrace();}

            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return total;
    }

    public List<Persona> CrearPersona()
    {
        if(autores.size() > 0)
        {
            List<Persona> personas = new ArrayList<Persona>();
            for(int i = 0; i < autores.size() ; i++)
            {
                String[] nombreCompleto = autores.get(i).split(" ");
                String nombre = nombreCompleto[0];
                String apellidos = "";
                if(nombreCompleto.length > 1){
                    apellidos = autores.get(i).substring(nombre.length() + 1);
                }
                Persona persona = new Persona(nombre, apellidos);
                personas.add(persona);
            }
            return personas;
        }
        return null;
    }


    public boolean TratarContentType(JSONObject oarticulo)
    {
        Object oTipo = null;
        try {
            oTipo = oarticulo.get("content_type");
        }
        catch(Exception e){}
        if(oTipo == null){
        }else
        {
            String tipo = oTipo.toString();
            if(tipo.contains("Early Access Articles"))
                return true;
        }
        return false;
    }


    public void TratarRevista(JSONObject oarticulo)
    {
        Object oJournal = null;
        try {
            oJournal = oarticulo.get("publication_title");
        }
        catch(Exception e){}
        if(oJournal == null){
        }else
        {
            revista = oJournal.toString();
        }
    }

    public void TratarNumeroEjemplar(JSONObject oarticulo){
        Object oNEjemplar = null;
        try {
            oNEjemplar = oarticulo.get("publication_number");
        }
        catch(Exception e){}
        if(oNEjemplar == null){
        }else
        {
            numeroEjemplar = oNEjemplar.toString();
        }
    }

    public void TratarURL(JSONObject oarticulo){
        Object oURL = null;
        try {
            oURL = oarticulo.get("abstract_url");
        }
        catch(Exception e){}
        if(oURL == null){
        }else
        {
            URL = oURL.toString();
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

    public void TratarPagIni(JSONObject oarticulo)
    {
        Object oPagIni = null;
        try {
            oPagIni = oarticulo.get("start_page");
        }
        catch(Exception e){}
        if(oPagIni == null){
        }else
        {
            pagIni = oPagIni.toString();
        }
    }


    public void TratarPagFin(JSONObject oarticulo)
    {
        Object oPagFin = null;
        try {
            oPagFin = oarticulo.get("end_page");
        }
        catch(Exception e){}
        if(oPagFin == null){
        }else
        {
            pagFin = oPagFin.toString();
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


    public void TratarAnyo(JSONObject oarticulo)
    {
        Object oYear = null;
        try {
            oYear = oarticulo.get("publication_year");
        }
        catch(Exception e){}
        if(oYear == null){
        }else
        {
            anyo = oYear.toString();
        }
    }


    public  void TratarAutores(JSONObject oarticulo)
    {
        Object oAuthor = null;
        try {
            oAuthor = ((JSONObject) oarticulo.get("authors")).get("authors");
        }
        catch(Exception e){}

        if(oAuthor == null){
        }
        else if (oAuthor instanceof org.json.JSONArray) {
            JSONArray authors =  (JSONArray) oAuthor;

            for (int j = 0; j < authors.length(); j++) {
                String author = Main.Tratar( "full_name" , authors.get(j));
                autores.add(author);
            }
        } else {
            String author = Main.Tratar("full_name", oAuthor);
            System.out.println(author);
            autores.add(author);
        }
    }

}
