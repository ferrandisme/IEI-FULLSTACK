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

    public static void Buscar(String autor, String titulo, String inicio, String fin, boolean esArticulo, boolean esLibro, boolean esComunicacionCongreso) {
        Connection con = Conexion.abrirConexion();
        //TODO Deberiamos ver si lo contiene quiza, en vez de ver si es exactamente el mismo
        String sql = "SELECT idpublicacion FROM publicacion WHERE titulo LIKE ? AND anyo >= ? AND anyo <= ?;";
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
                        esArticulo(con, idPublicacion);
                    }
                    if (esLibro) {
                        //Comprueba si existe un Libro con ese ID
                        esLibro(con, idPublicacion);
                    }
                    if (esComunicacionCongreso) {
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
    }

    private static void esArticulo(Connection con, int idPublicacion){
        String sql = "SELECT * FROM articulo WHERE idpublicacion = ? ;";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, idPublicacion + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Encontrado Articulo " + idPublicacion);
                //Aqui podemos leer los datos, haciendo por ejemplo cosas como result.getInt("idpublicacion");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
