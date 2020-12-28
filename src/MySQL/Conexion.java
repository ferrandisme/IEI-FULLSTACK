package MySQL;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Conexion {

    private static Connection conexion = null;
    public static String path = System.getProperty("user.home") + "\\AppData\\LocalLow\\IEI";

    // Lectura del password
    public static String LeerPass(){
        try {
            String contents = new String(Files.readAllBytes((Paths.get(path + "\\password.txt"))));
            return contents;
        }catch(Exception e){
            //System.err.println("Necesitas un archivo llamado password.txt donde pongas la contraseña del MYSQL");
            return "1234";
        }
    }

    // Apertura y cierra de la conexion a la base de datos
    public static Connection abrirConexion() {
        String password = LeerPass();
        //String password = "1234";
        //System.out.println("Usando como contraseña MYSQL: '" + password + "'");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
        	conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb" + "?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    "root",
                    password); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión con la Base de Datos");
            }
        }
    }

    /*// Obtener datos de la base de datos
    public static ArrayList<String> todos (String columna, String tabla) throws SQLException  {
        // Obtener los valores únicos de la COLUMNA de la TABLA
        ArrayList<String> valores = new ArrayList<String>();
        Connection con = Conexion.abrirConexion();
        if (con != null) {
            String sql = "SELECT DISTINCT " + columna + " FROM " + tabla + ";";
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    valores.add(result.getString(columna));
                }
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return(valores);
    }*/

    public static int obtenerIdPublicacion (String titulo, String anyo, String URL) {
        Connection con = Conexion.abrirConexion();
        int aux = -1;
        if (con != null && titulo != null && titulo != "" && anyo != null && anyo != "") {
            String sql = "SELECT idpublicacion FROM publicacion WHERE titulo = ? AND anyo = ?;";
            try{
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, titulo);
                statement.setString(2, anyo);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    aux = result.getInt("idpublicacion");
                } else {
                    aux = -9;
                }
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    public static int obtenerIdPersona (String nombre, String apellidos) {
        Connection con = Conexion.abrirConexion();
        int aux = -1;
        if (con != null && nombre != null && nombre != "" && apellidos != null && apellidos != "") {
            String sql = "SELECT idpersona FROM persona WHERE nombre = ? AND apellidos = ?;";
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, nombre);
                statement.setString(2, apellidos);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    aux = result.getInt("idpersona");
                } else {
                    aux = -9;
                }
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    public static int obtenerIdEjemplar (String volumen, String numero, int idrevista) {
        Connection con = Conexion.abrirConexion();
        int aux = -1;
        if (con != null && volumen != null && volumen != "" && numero != null && numero != "") {
            String sql = "SELECT idejemplar FROM ejemplar WHERE volumen = ? AND numero = ?;";
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, volumen);
                statement.setString(2, numero);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    aux = result.getInt("idejemplar");
                } else {
                    aux = -9;
                }
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    public static int obtenerIdRevista (String nombre) {
        Connection con = Conexion.abrirConexion();
        int aux = -1;
        if (con != null && nombre != null && nombre != "") {
            String sql = "SELECT idrevista FROM revista WHERE nombre = ?;";
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, nombre);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    aux = result.getInt("idrevista");
                } else {
                    aux = -9;
                }
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    // Meter los datos en la base de datos
    public static void meterPublicacion (String titulo, String anyo, String URL) {
        Connection con = Conexion.abrirConexion();
        if (con != null && titulo != null && titulo != "" && anyo != null && anyo != "") {
            String SQL = "INSERT INTO publicacion (titulo, anyo, URL) VALUES (?, ?, ?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setString(1, titulo);
                statement.setString(2, anyo);
                if (URL == "")
                    statement.setString(3, null);
                else
                    statement.setString(3, URL);
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void meterLibro (int id, String editorial) {
        Connection con = Conexion.abrirConexion();
        if (con != null && editorial != null && editorial != "") {
            String SQL = "INSERT INTO libro (idpublicacion, editorial) VALUES (?, ?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setInt(1, id);
                statement.setString(2, editorial);
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void meterComunicacion (int id, String edicion, String lugar, String ini, String fin) {
        System.out.println("id: " + id + " " + edicion + " " + lugar + " " + ini + " " + fin);
        Connection con = Conexion.abrirConexion();
        if (con != null) {
            String SQL = "INSERT INTO comunicacion (idpublicacion, edicion, lugar, paginainicio, paginafin) VALUES " +
                    "(?, ?, ?, ?, ?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setInt(1, id);
                statement.setString(2, edicion);
                statement.setString(3, lugar);
                if (ini == "")
                    statement.setString(4, null);
                else
                    statement.setInt(4, Integer.parseInt(ini));
                if (fin == "")
                    statement.setString(5, null);
                else
                    statement.setInt(5, Integer.parseInt(fin));
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void meterArticulo (int idPub, int idEje, int ini, int fin) {
        Connection con = Conexion.abrirConexion();
        if (con != null) {
            String SQL = "INSERT INTO articulo (idpublicacion, id_ejemplar, paginainicio, paginafin) VALUES " +
                    "(?, ?, ?, ?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setInt(1, idPub);
                if (idEje == -1)
                    statement.setString(2, null);
                else
                    statement.setInt(2, idEje);
                statement.setInt(3, ini);
                statement.setInt(4, fin);
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void meterRelacion (int idPub, int idPer) {
        Connection con = Conexion.abrirConexion();
        if (con != null) {
            String SQL = "INSERT INTO relacion (idpublicacion, idpersona) VALUES (?, ?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setInt(1, idPub);
                statement.setInt(2, idPer);
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void meterPersona ( String nombre, String apellidos) {
        Connection con = Conexion.abrirConexion();
        if (con != null && nombre != null && nombre != "" && apellidos != null && apellidos != "") {
            String SQL = "INSERT INTO persona (nombre, apellidos) VALUES (?, ?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setString(1, nombre);
                statement.setString(2, apellidos);
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void meterEjemplar (String volumen, String numero, String mes, int idrevista) {
        Connection con = Conexion.abrirConexion();
        if (con != null && volumen != null && volumen != "" && numero != null && numero != "") {
            String SQL = "INSERT INTO ejemplar (volumen, numero, mes, id_revista) VALUES " +
                    "(?, ?, ?, ?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setString(1, volumen);
                statement.setString(2, numero);
                statement.setString(3, mes);
                if (idrevista == -1)
                    statement.setString(4, null);
                else
                    statement.setInt(4, idrevista);
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void meterRevista (String nombre) {
        Connection con = Conexion.abrirConexion();
        if (con != null && nombre != null && nombre != "") {
            String SQL = "INSERT INTO revista (nombre) VALUES (?);";
            try {
                PreparedStatement statement = con.prepareStatement(SQL);
                statement.setString(1, nombre);
                statement.executeUpdate();
                Conexion.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
