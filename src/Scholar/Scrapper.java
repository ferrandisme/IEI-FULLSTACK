package Scholar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;
import org.json.JSONObject;
import org.json.XML;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import DBLP.ExtractorDBLP;

public class Scrapper {

	private static WebDriver driver = null;
    static Reader reader;
	static JSONObject json;
	
	static BibTeXParser parser;
	static BibTeXDatabase database;
    	
	/*
	Chrome(1970, 1990);
    	
    	reader = new FileReader(argumento);
    	json = new JSONObject();
    	
    	Collection<BibTeXString> listado;
    	
    	parser = new BibTeXParser();
    	database = parser.parse(reader);
    	
    	listado = database.getStrings().values();
	 */
	
	//public static String path = System.getProperty("user.home") + "\\AppData\\LocalLow\\IEI";

    public static void Chrome(int inicio, int fin) throws IOException, InterruptedException{
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
        String argumento = "";
        boolean present = true;
        int maximoElementos= 20; //Para simplificar la busqueda en entornos de demo
        int total = 0;
        int numeroMaximoIntentos = 10;
        int intentosActuales = 0;
        while (maximoElementos > total && numeroMaximoIntentos > intentosActuales && present)
        {
            if(total >= maximoElementos)
                break;        
                
            try{
                //leer elemento
            	List<WebElement> listaElementos = driver.findElements(By.xpath("//*[contains(@class, 'gs_r gs_or gs_scl')]"));
        		//System.out.println("Número de elementos de la lista: " + listaElementos.size());
        		// Obtener cada uno de los artículos
        		for (int i=0; i<listaElementos.size(); i++){
        			//List<WebElement> listaElementos2 = driver.findElements(By.xpath("//*[contains(@class, 'gs_r gs_or gs_scl')]"));
        			List<WebElement> listaImports = driver.findElements(By.xpath("//*[contains(@class, 'gs_nta gs_nph')]"));
        			WebElement elementoActual, navegacion, text;
        			//elementoActual = listaElementos2.get(i);
        			navegacion = listaImports.get(i);
        			navegacion.click();
        			text = driver.findElement(By.xpath("/html/body/pre"));
        			//System.out.println(text.getText());
        			argumento += text.getText();
        			//writer.write(text.getText());
        			driver.navigate().back();
        			//si llega aqui, es porque existe si no salta al catch
        			total++;
        			intentosActuales = 0;
        			}
        		try {
        			WebElement siguiente = driver.findElement(By.xpath("/html/body/div/div[10]/div[2]/div[2]/div[3]/div[2]/center/table/tbody/tr/td[12]/a/b"));
        			siguiente.click();
        		} catch (NoSuchElementException e) {
        			//present = false;
        		}
            }
            catch(NoSuchElementException e){
                intentosActuales++;
                Thread.sleep(5000);
            }
        } 
        //System.out.println(argumento);

		JSONObject scholar = JSONfromBibtex(argumento);
		String jsonFile = ExtractorScholar.path + "\\Scholar.json";
		String jsonPrettyPrintString = null;
		
			try(FileWriter fileWriter = new FileWriter(jsonFile)){
				fileWriter.write(scholar.toString(4));
				jsonPrettyPrintString= scholar.toString(4);
				System.out.println(jsonPrettyPrintString);
			} catch (IOException e) {
				System.out.println("No se ha podido escribir el JSON");
				System.out.println(e.getMessage());
			}
    }
    
    public static JSONObject JSONfromBibtex(String bibtexPublication) {
        StringReader bibtexReader = new StringReader(bibtexPublication);
        Collection<BibTeXEntry> entriesCollection;
        JSONObject jsonPublication = new JSONObject();
        try {
            BibTeXParser bibtexParser = new BibTeXParser();
            BibTeXDatabase database = bibtexParser.parse(bibtexReader);
            jsonPublication.put(bibtexPublication, true);
            //entriesCollection = database.getEntries().values();

            /*BibTeXEntry entry = entriesCollection.iterator().next();
            Map<Key,Value> bibtexFields = entry.getFields();
            Set<Key> bibtexKeys = bibtexFields.keySet();

            for(Key key : bibtexKeys) {
                String keyValue = bibtexFields.get(key).toUserString();
                jsonPublication.put(key.toString(), keyValue);
            }*/

        } catch (TokenMgrException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonPublication;
    }
}
