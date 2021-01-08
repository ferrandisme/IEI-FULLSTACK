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
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.BibTeXDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    private static WebDriver driver = null;
    static Writer writer = null;


    //EJEMPLO LLAMADA A MAIN
    // "busqueda" "" "Information Uses and Gratifications Related to Crisis: Student Perceptions since the Egyptian Uprising" "0" "2020"
    public static void main(String[] args) throws TokenMgrException, ParseException, IOException{
    	
    	
    	//Ejemplo comentado
    	//APIFRONT.Buscar("","Developing a Framework for Assessing Responsible Conduct of Research","0","2020",true,false,false);
    	//System.out.println("Fin ejecucion");
    	
    	
    	//Chrome(1970, 1990);
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
                System.out.println("--DATOS BUSQUEDA-- \n autor: " + args[1]  +" \n titulo: "+ args[2] +" \n aÃ±o inicio: "+ args[3] +" \n aÃ±o fin: "+ args[4] +"\n------------------");
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

    public static void Chrome(int inicio, int fin) throws IOException{
        String exepath = "C:\\Users\\jaime\\IdeaProjects\\IEI\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exepath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://scholar.google.es/");
        WebElement elemento = driver.findElement(By.xpath("html/body/div/div[7]/a[1]"));
        elemento.click();
        WebElement configuracion = driver.findElement(By.xpath("/html/body/div/div[6]/div/div[2]/div[3]/a"));
        configuracion.click();
        WebElement bibtex = driver.findElement(By.xpath("/html/body/div/form[2]/div[2]/div[2]/div/div[1]/div[3]/div/div[2]/span[1]"));
        bibtex.click();
        WebElement number = driver.findElement(By.xpath("/html/body/div/form[2]/div[2]/div[2]/div/div[1]/div[1]/div/table/tbody/tr/td[1]/div/button"));
        number.click();
        WebElement twenty = driver.findElement(By.xpath("/html/body/div/form[2]/div[2]/div[2]/div/div[1]/div[1]/div/table/tbody/tr/td[1]/div/div/div/a[2]"));
        twenty.click();
        WebElement save = driver.findElement(By.xpath("/html/body/div/form[2]/div[2]/div[2]/div/div[6]/button[1]"));
        save.click();
        WebElement elemento2 = driver.findElement(By.xpath("html/body/div/div[7]/a[1]"));
        elemento2.click();
        WebElement element = driver.findElement(By.xpath("html/body/div/div[6]/div/div[2]/div[2]/a"));
        element.click();
        WebElement anyoInicio = driver.findElement(By.xpath("html/body/div/div[4]/div/div[2]/form/div[9]/div[2]/div[1]/div[1]/input"));
        anyoInicio.sendKeys(inicio + "");
        WebElement anyoFin = driver.findElement(By.xpath("html/body/div/div[4]/div/div[2]/form/div[9]/div[2]/div[1]/div[2]/input"));
        anyoFin.sendKeys(fin + "");
        WebElement buscar = driver.findElement(By.xpath("html/body/div/div[4]/div/div[1]/div/button/span/span[1]"));
        buscar.click();
        WebElement citas = driver.findElement(By.xpath("/html/body/div/div[10]/div[1]/div/ul[3]/li[2]/a/span[2]"));
        citas.click();
        boolean present = true;
        //while(present) {
        List<WebElement> listaElementos = driver.findElements(By.xpath("//*[contains(@class, 'gs_r gs_or gs_scl')]"));
		System.out.println("Número de elementos de la lista: " + listaElementos.size());
		// Obtener cada uno de los artículos
		int j=1;
		for (int i=0; i<listaElementos.size(); i++){
			List<WebElement> listaElementos2 = driver.findElements(By.xpath("//*[contains(@class, 'gs_r gs_or gs_scl')]"));
			WebElement elementoActual, navegacion, text;
			elementoActual = listaElementos2.get(i);
			/*boolean staleElement = true; 
			while(staleElement){
				 try{
				    elementoActual.findElement(By.xpath("/html/body/div/div[10]/div[2]/div[2]/div[2]/div[1]/div/div[3]/a[6]" ));
				    staleElement = false;

				 } catch(StaleElementReferenceException e){
				    staleElement = true;
				 }
			}*/
		navegacion = elementoActual.findElement(By.xpath("/html/body/div/div[10]/div[2]/div[2]/div[2]/div[" + j + "]" + "/div/div[3]/a[6]" ));
		navegacion.click();
		text = driver.findElement(By.xpath("/html/body/pre"));
		System.out.println(text.getText());
		//writer.write(text.getText());
		driver.navigate().back();
		j++;
		}
		try {
			WebElement siguiente = driver.findElement(By.xpath("/html/body/div/div[10]/div[2]/div[2]/div[3]/div[2]/center/table/tbody/tr/td[12]/a/b"));
			siguiente.click();
		} catch (NoSuchElementException e) {
			present = false;
		}
        //}
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
