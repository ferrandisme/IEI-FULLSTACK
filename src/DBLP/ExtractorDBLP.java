package DBLP;

import MySQL.Conexion;
import MySQL.Insercion;
import modelo.*;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.CreadorObjetos;
import sample.Extractor;
import sample.Main;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExtractorDBLP extends Extractor {

    public static String path = System.getProperty("user.home") + "\\AppData\\LocalLow\\IEI";


    public ExtractorDBLP(int inicio, int fin){super(inicio,fin);}

    int total = 0;
    public  int Empezar() {
        try {
            String contents = new String(Files.readAllBytes((Paths.get(path + "\\DBLP.json"))));
            JSONObject o = new JSONObject(contents);
            JSONArray articulos = (JSONArray) ((JSONObject) o.get("dblp")).get("article");

            for (int i = 0; i < articulos.length(); i++) {
                System.out.println("DBLP " + i);
                JSONObject oarticulo = articulos.getJSONObject(i);
                ReiniciarVariables();

                //Llamada a tratamiento de variables
                try {

                    TratarAnyo(oarticulo);
                    if (anyoFin >= Integer.parseInt(anyo) && anyoInicio <= Integer.parseInt(anyo)) {
                        TratarAutores(oarticulo);
                        TratarVolumen(oarticulo);
                        TratarPaginas(oarticulo);
                        TratarNumeroEjemplar(oarticulo);
                        TratarTitulo(oarticulo);
                        TratarRevista(oarticulo);
                        TratarURL(oarticulo);
                        //Crecion del objeto con las variables dadas:

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
        } catch(IOException e) {
            e.printStackTrace();
        }
        return total;
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

    public void TratarPaginas(JSONObject oarticulo)
    {
        Object oPaginas = null;
        try {
            oPaginas = oarticulo.get("pages");
        } catch (Exception e) {
        }
        if (oPaginas == null) {
        } else{
           // System.out.println(oPaginas.toString());
            String[] pagina = oPaginas.toString().split("-");

            if(pagina.length > 1){
                pagIni = TratarPagina(pagina[0]);
                pagFin = TratarPagina(pagina[1]);
            }else{
                pagIni = TratarPagina(pagina[0]);
                pagFin = TratarPagina(pagina[0]);
            }
            if(pagIni.contains("-") || pagFin.contains("-")){
                System.out.println(oPaginas.toString());
                System.out.println(pagIni + " " + pagFin);
            }
    }
}
    public String TratarPagina(String pagina) {
        if(pagina.contains("e")){
            pagina = pagina.substring(1);
        }
        if(pagina.contains(":")){
        //    System.out.println(pagina);
            String[] aux = pagina.split(":");
            pagina = aux[1];

        }
        if(!NoEsNumeroRomano(pagina)){
           // System.out.println("numero romano " + pagina);
            pagina = romanToDecimal(pagina.toUpperCase())+"";

        }
        return pagina;
    }



        public boolean NoEsNumeroRomano (String pagina)
        {
            boolean resultado;
            try {
                Integer.parseInt(pagina);
                resultado = true;

            } catch (NumberFormatException e) {
                resultado = false;
            }
            return resultado;
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
        else if (oAuthor instanceof org.json.JSONArray) {
            JSONArray authors =  (JSONArray) oAuthor;

            for (int j = 0; j < authors.length(); j++) {
                String author = Main.Tratar("$", authors.get(j));
                autores.add(author);
            }
        } else {
            String author = Main.Tratar("$", oAuthor);
            autores.add(author);
        }
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


    public void TratarURL(JSONObject oarticulo){
        Object oURL = null;
        try {
            oURL = oarticulo.get("ee");
        }
        catch(Exception e){}

        if(oURL == null){
        }
        else if (oURL instanceof org.json.JSONArray) {
            JSONArray URLS =  (JSONArray) oURL;
            if(!Main.NoJSON(URLS.get(0).toString()))
                URL = Main.Tratar( "$" , URLS.get(0));
            else
                URL = URLS.get(0).toString();
        } else {
            if(!Main.NoJSON(oURL.toString()))
                URL = oURL.toString();
            else
                URL = Main.TratarJSON( "$" , (JSONObject) oURL);
        }
    }

        // This function returns
        // value of a Roman symbol
        int value(char r)
        {
            if (r == 'I')
                return 1;
            if (r == 'V')
                return 5;
            if (r == 'X')
                return 10;
            if (r == 'L')
                return 50;
            if (r == 'C')
                return 100;
            if (r == 'D')
                return 500;
            if (r == 'M')
                return 1000;
            return -1;
        }

        // Finds decimal value of a
        // given romal numeral
        int romanToDecimal(String str)
        {
            // Initialize result
            int res = 0;

            for (int i = 0; i < str.length(); i++)
            {
                // Getting value of symbol s[i]
                int s1 = value(str.charAt(i));

                // Getting value of symbol s[i+1]
                if (i + 1 < str.length())
                {
                    int s2 = value(str.charAt(i + 1));

                    // Comparing both values
                    if (s1 >= s2)
                    {
                        // Value of current symbol
                        // is greater or equalto
                        // the next symbol
                        res = res + s1;
                    }
                    else
                    {
                        // Value of current symbol is
                        // less than the next symbol
                        res = res + s2 - s1;
                        i++;
                    }
                }
                else {
                    res = res + s1;
                }
            }

            return res;
        }
    }


