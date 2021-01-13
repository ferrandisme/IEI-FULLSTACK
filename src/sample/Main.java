package sample;

import API.APIFRONT;
import DBLP.*;
import IEEE.ExtractorIEEE;
import MySQL.Conexion;
import Scholar.ExtractorScholar;
/*import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;*/
import modelo.Articulo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.JSONTokener;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;
//import org.apache.tomcat.jni.Time;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    //EJEMPLO LLAMADA A MAIN
    // "busqueda" "" "Information Uses and Gratifications Related to Crisis: Student Perceptions since the Egyptian Uprising" "0" "2020"
    public static void main(String[] args){

    	//Ejemplo comentado
    	//APIFRONT.Buscar("","Developing a Framework for Assessing Responsible Conduct of Research","0","2020",true,false,false);
    	//System.out.println("Fin ejecucion");
    	
    	
        /*
        try {
            String type = args[0]; //

            if(type.equals("carga")){
                String db = args[1];
                int startYear = Integer.parseInt(args[2]);
                int endYear = Integer.parseInt(args[3]);
                System.out.println("Carga " + db + " (" + startYear + "-" + endYear + ")");
                switch (db) {
                    case "DBLP":
                        APIFRONT.LoadDBP(startYear, endYear);
                        break;
                    case "IEEE":
                        APIFRONT.LoadIEEE(startYear, endYear);
                        break;
                    case "SCHOOLAR":
                        APIFRONT.LoadSchoolar(startYear, endYear);
                        break;
                    default:
                        throw new Exception("[1-2-3] no valido");
                }
            }else if(type.equals("busqueda")){
                //TODO cambiar true por los ticks del formulario, cuando se acabe de hacer la API
                System.out.println("--DATOS BUSQUEDA-- \n autor: " + args[1]  +" \n titulo: "+ args[2] +" \n año inicio: "+ args[3] +" \n año fin: "+ args[4] +"\n------------------");
                APIFRONT.Buscar(args[1], args[2], args[3], args[4], true, true, true);
            }
            else
                throw new Exception("[0] no valido " + args[0]);
        }catch(Exception e){
            System.out.println("Error leyendo argumentos " + args);
            e.printStackTrace();
            System.out.println("Abortando ejecucion del backend");
            System.exit(-1);
        }
        //launch(args); Hace que runee start
        System.exit(0);
        */
    }

    /*@Override
    public void start(Stage primaryStage) throws Exception{
        //ProcesarJSON();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 300, 275));
        //primaryStage.show();

        //Metodos de ejemplo

        APIFRONT.LoadSchoolar(0,2022);

        APIFRONT.Buscar("", "Information Uses and Gratifications Related to Crisis: Student Perceptions since the Egyptian Uprising", "0", "2020", true, true, true);
    }*/

    public static String TratarJSON(String argumento, JSONObject objeto)
    {
        String s = objeto.get(argumento).toString();
        return s;
    }
    public static String Tratar(String argumento, Object objeto)
    {
        if(NoJSON(objeto.toString())) {
            return objeto.toString();
        }
        else {
            return TratarJSON(argumento, (JSONObject) objeto);
        }
    }

    public static boolean NoJSON(String s)
    {
        return !s.contains("{") || !s.contains("}");
    }
}
