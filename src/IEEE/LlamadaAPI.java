package IEEE;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mysql.cj.xdevapi.JsonArray;

import DBLP.ExtractorDBLP;

public class LlamadaAPI {
	
	
	
	private static String cargarJSON(String urlCarga) throws IOException, URISyntaxException {
		
		//Conectamos con la web
		URL url = new URL(urlCarga);
        URLConnection conexion = url. openConnection();
        conexion.connect();
		
        //Proceso de conversion a JSON
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

	      String linea;
	      StringBuilder contenido = new StringBuilder();
	      while ((linea = bufferedReader.readLine()) != null)
	      {
	    	  contenido.append(linea + "\n");
	      }
	      bufferedReader.close();
	    String textoAPI = contenido.toString();
	   
		return textoAPI;
	}
	
	public static void cargaAPI(int inicio, int fin){
		try {
			
			System.out.println("Iniciando consulta a la API");
			
			String web = "http://ieeexploreapi.ieee.org/api/v1/search/articles?"
					+ "apikey=efv84mzqq6ydx4dbd59jhdcn"
					+ "&format=json&max_records=200&start_record=1&sort_order=asc&sort_field=article_number"
					+ "&start_year="  + inicio
					+ "&end_year=" + fin;

	        
	        String textoAEscribir =  cargarJSON(web);
	        
	        String jsonFile = ExtractorIEEE.path + "\\IEEE.json";
	        FileWriter fileWriter = new FileWriter(jsonFile);
	        fileWriter.write(textoAEscribir);
	        fileWriter.close();
	        
		}
		catch(Exception e) {
			System.out.println("Error al trabajar con la api IEEE " + e.getCause());
			e.printStackTrace();
		}
		System.out.println("Fin de la consulta a la API");
	
	}

}
