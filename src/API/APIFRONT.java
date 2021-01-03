package API;

import DBLP.ExtractorDBLP;
import IEEE.ExtractorIEEE;
import MySQL.Conexion;
import Scholar.ExtractorScholar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class APIFRONT {


       /*

                                        API CARGA
     */

    public static int LoadDBP(int inicio, int fin) {
        ExtractorDBLP DBLP = new ExtractorDBLP(inicio, fin);
        return DBLP.Empezar();
    }

    public static int LoadIEEE(int inicio, int fin) {
        ExtractorIEEE IEEE = new ExtractorIEEE(inicio, fin);
        return IEEE.Empezar();
    }

    public static int LoadSchoolar(int inicio, int fin) {
        ExtractorScholar scholar = new ExtractorScholar(inicio, fin);
        return scholar.Empezar();
    }


    /*

                                        API BUSQUEDA
     */

    public static List<String> Buscar(String autor, String titulo, String inicio, String fin, boolean esArticulo, boolean esLibro, boolean esComunicacionCongreso) {
        Connection con = Conexion.abrirConexion();
        //TODO Deberiamos ver si lo contiene quiza, en vez de ver si es exactamente el mismo
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
                        		articulo += autores.get(i) + "\n";
                        	
                        	articulo =  "ARTICULO \n"+
                        				"Titulo: " + result.getString("titulo") + "\n"+
                        				"Año: " + result.getString("anyo") + "\n"+
                        				"URL: " + result.getString("URL") + "\n"
                        				+ articulo;
                        	
                        	System.out.println("-------------ARTICULO ENCONTRADO-------------  \n" + articulo + "\n" + "-------------------------------");
                        	//resultado.put(articulo, id++);
                        	resultado.add(articulo);
                        }
                    }
                    if (esLibro) { //NOT DONE
                        //Comprueba si existe un Libro con ese ID
                        esLibro(con, idPublicacion);
                    }
                    if (esComunicacionCongreso) { //NOT DONE
                        //Comprueba si existe una Comunicacion con ese ID
                        esComunicacionCongreso(con, idPublicacion);
                    }
                }
            }
                //TODO devolver de una forma que en el servidor se pueda crear la respuesta

            /*if (result.next()) {
                System.out.println("PUBLICACION ENCONTRADA:" + result.getInt("idpublicacion"));
                //Hay que comprobar el autor, y el tipo de publicacion.

            } else {
                System.out.println("No se han encontrado publicaciones");
            }*/
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
                String articulo = "Pagina inicio:" + result.getInt("paginainicio") + "\n" +
                				"Pagina fin:" + result.getInt("paginafin") + "\n";
                articulo += getEjemplarYRevista(con, result.getInt("id_ejemplar"));
                return articulo;
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void esLibro(Connection con, int idPublicacion){
        String sql = "SELECT * FROM libro WHERE idpublicacion = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idPublicacion + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Encontrado Libro " + idPublicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void esComunicacionCongreso(Connection con, int idPublicacion){
        String sql = "SELECT * FROM comunicacion WHERE idpublicacion = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idPublicacion + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Encontrada Comunicacion " + idPublicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                		"Volumen:" + result.getString("volumen") + "\n" + 
                				"Numero:" + result.getString("numero") + "\n"+
                				"Mes:" + result.getString("mes") + "\n";
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
                		"Nombre:" + result.getString("nombre") + "\n";
                return revista;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    
}
