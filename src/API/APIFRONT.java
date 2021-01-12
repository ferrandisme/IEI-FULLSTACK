package API;

import DBLP.ExtractorDBLP;
import DBLP.ParserXML;
import IEEE.ExtractorIEEE;
import MySQL.Conexion;
import Scholar.ExtractorScholar;
import Scholar.Scrapper;
import sample.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class APIFRONT {
	
	private static String saltoLinea = "<br>\r\n";
	
	private static boolean dblpActualizado = false;


       /*

                                        API CARGA
     */

    public static int LoadDBP(int inicio, int fin) {
    	if(!dblpActualizado) {
    		System.out.println("Parseando XML a JSON en DBLP");
    		ParserXML.pasarXMLaJSON();
    		dblpActualizado = true;
    	}
        ExtractorDBLP DBLP = new ExtractorDBLP(inicio, fin);
        return DBLP.Empezar();
    }

    public static int LoadIEEE(int inicio, int fin) {
        ExtractorIEEE IEEE = new ExtractorIEEE(inicio, fin);
        return IEEE.Empezar();
    }

    public static int LoadSchoolar(int inicio, int fin) {
    	try {
			Scrapper.Chrome(inicio, fin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ExtractorScholar scholar = new ExtractorScholar(inicio, fin);
        return scholar.Empezar();
    }


    /*

                                        API BUSQUEDA
     */

    public static List<String> Buscar(String autor, String titulo, String inicio, String fin, boolean esArticulo, boolean esLibro, boolean esComunicacionCongreso) {
        Connection con = Conexion.abrirConexion();
        String sql = "SELECT * FROM publicacion WHERE titulo LIKE ? AND anyo >= ? AND anyo <= ?;";
        int id = 0;
        //JSONObject resultado = new JSONObject();
        List<String> resultado = new ArrayList<String>();
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, "%"+titulo+"%");
            statement.setString(2, inicio);
            statement.setString(3, fin);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int idPublicacion = result.getInt("idpublicacion");
                System.out.println("PUBLICACION ENCONTRADA:" + idPublicacion + " , procedemos a comprobar si coincide con algun tipo");
                if(existeAutor(con,idPublicacion,autor)) {
                    List<String> autores = getAllAutores(con,idPublicacion);
                    System.out.println("Encontrados " + autores.size() +" autor/es");
                    if (esArticulo) {
                        //Comprueba si existe un articulo con ese ID
                        String articulo = esArticulo(con, idPublicacion);
                        if(articulo != null && articulo.length() > 0) {
                        	articulo += "AUTORES \n";
                        	for(int i = 0; i < autores.size(); i++)
                        		articulo += autores.get(i) + saltoLinea;
                        	
                        	articulo =  "ARTICULO \n"+
                        				"Titulo: " + result.getString("titulo") + saltoLinea+
                        				"A&ntilde;o: " + result.getString("anyo") + saltoLinea+
                        				"URL: " + result.getString("URL") + saltoLinea
                        				+ articulo;
                        	
                        	System.out.println("-------------ARTICULO ENCONTRADO-------------  \n" + articulo + saltoLinea + "-------------------------------");
                        	//resultado.put(articulo, id++);
                        	resultado.add(articulo);
                        }
                    }
                    if (esLibro) {
                    	String libro = esLibro(con, idPublicacion);
                        if(libro != null && libro.length() > 0) {
                        	libro += "AUTORES \n";
                        	for(int i = 0; i < autores.size(); i++)
                        		libro += autores.get(i) + saltoLinea;
                        	
                        	libro =  "LIBRO \n"+
                        				"Titulo: " + result.getString("titulo") + saltoLinea+
                        				"A&ntilde;o: " + result.getString("anyo") + saltoLinea+
                        				"URL: " + result.getString("URL") + saltoLinea
                        				+ libro;
                        	
                        	System.out.println("-------------LIBRO ENCONTRADO-------------  \n" + libro + saltoLinea + "-------------------------------");
                        	//resultado.put(libro, id++);
                        	resultado.add(libro);
                        }
                    }
                    if (esComunicacionCongreso) {
                       
                    	String comunicacion = esComunicacionCongreso(con, idPublicacion);
                        if(comunicacion != null && comunicacion.length() > 0) {
                        	comunicacion += "AUTORES \n";
                        	for(int i = 0; i < autores.size(); i++)
                        		comunicacion += autores.get(i) + saltoLinea;
                        	
                        	comunicacion =  "COMUNICACION CONGRESO \n"+
                        				"Titulo: " + result.getString("titulo") + saltoLinea+
                        				"A&ntilde;o: " + result.getString("anyo") + saltoLinea+
                        				"URL: " + result.getString("URL") + saltoLinea
                        				+ comunicacion;
                        	
                        	System.out.println("-------------COMUNICACION ENCONTRADA-------------  \n" + comunicacion + saltoLinea + "-------------------------------");
                        	//resultado.put(comunicacion, id++);
                        	resultado.add(comunicacion);
                        }
                    }
                }
            }
               
            Conexion.cerrarConexion();
        } catch (SQLException e) { e.printStackTrace();
        }
        Conexion.cerrarConexion();
        return resultado;
    }

    private static String esArticulo(Connection con, int idPublicacion){
        String sql = "SELECT * FROM articulo WHERE idpublicacion = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idPublicacion + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Encontrado Articulo " + idPublicacion);
                String articulo = "Pagina inicio:" + result.getInt("paginainicio") + saltoLinea +
                					"Pagina fin:" + result.getInt("paginafin") + saltoLinea;
                articulo += getEjemplarYRevista(con, result.getInt("id_ejemplar"));
                return articulo;
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String esLibro(Connection con, int idPublicacion){
        String sql = "SELECT * FROM libro WHERE idpublicacion = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idPublicacion + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Encontrado Libro " + idPublicacion);
                String articulo = "Editorial:" + result.getString("editorial") + saltoLinea;
                return articulo;
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String esComunicacionCongreso(Connection con, int idPublicacion){
        String sql = "SELECT * FROM comunicacion WHERE idpublicacion = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idPublicacion + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Encontrada Comunicacion " + idPublicacion);
                String comunicacion = "Edicion:" + result.getString("edicion") + saltoLinea + 
                					  "Lugar:" + result.getString("lugar") + saltoLinea +
                					  "Pagina Inicio:" + result.getInt("paginainicio") + saltoLinea +
                					  "Pagina Fin:" + result.getString("edicion") + saltoLinea ;
                return comunicacion;
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static boolean existeAutor(Connection con, int idPublicacion, String autor){
        List<Integer> idrelacion = relacionID(con,idPublicacion);
        for(int i = 0; i < idrelacion.size(); i++) {
            String sql = "SELECT * FROM persona WHERE idpersona = ? ;";
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, idrelacion.get(i) + "");
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    System.out.println("Encontrado Autor, comprobando si el nombre coincide... " + idPublicacion);
                    String nombreCompleto = result.getString("nombre") + " " + result.getString("apellidos");
                    if(nombreCompleto.contains(autor)){
                        System.out.println("Encontrado autor coincidente...");
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static List<String> getAllAutores(Connection con, int idPublicacion){
        List<String> res = new ArrayList<String>();
        List<Integer> idrelacion = relacionID(con,idPublicacion);
        for(int i = 0; i < idrelacion.size(); i++) {
            String sql = "SELECT * FROM persona WHERE idpersona = ? ;";
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, idrelacion.get(i) + "");
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    res.add(result.getString("nombre") + " " + result.getString("apellidos"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private static List<Integer> relacionID(Connection con, int idPublicacion){
        List<Integer> res = new ArrayList<Integer>();
        String sql = "SELECT idpersona FROM relacion WHERE idpublicacion = ?;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idPublicacion + "");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                res.add(result.getInt("idpersona"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    private static String getEjemplarYRevista(Connection con, int idEjemplar) {
    
        String sql = "SELECT * FROM ejemplar WHERE idejemplar = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idEjemplar + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String res = "Ejemplar \n"+ 
                				"Volumen:" + result.getString("volumen") + saltoLinea + 
                				"Numero:" + result.getString("numero") + saltoLinea+
                				"Mes:" + result.getString("mes") + saltoLinea;
                res += getRevista(con, result.getInt("id_revista"));
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return "";
    }
    	
    private static String getRevista(Connection con, int idrevista){
        String sql = "SELECT * FROM revista WHERE idrevista = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idrevista + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String revista = "REVISTA \n"+
                		"Nombre:" + result.getString("nombre") + saltoLinea;
                return revista;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    
}
