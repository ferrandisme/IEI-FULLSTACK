package sample;

import API.APIFRONT;
import DBLP.*;
import IEEE.ExtractorIEEE;
import IEEE.LlamadaAPI;
import MySQL.Conexion;
import Scholar.ExtractorScholar;
import Scholar.Scrapper;
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

	
    public static void main(String[] args){

    }
    
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
