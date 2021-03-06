package Scholar;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Scrapper {

	private static WebDriver driver = null;
    static Reader reader;
	static JSONObject json;
	
	static BibTeXParser parser;
	static BibTeXDatabase database;

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
        String entradas = "";
        String argumento = "";
        JSONObject scholar = null;
        JSONObject prueba = new JSONObject();
        JSONArray libros = new JSONArray();
        JSONArray articulos = new JSONArray();
        JSONArray incollection = new JSONArray();
        JSONArray inproceedings = new JSONArray();
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
        		// Obtener cada uno de los art�culos
        		for (int i=0; i<listaElementos.size(); i++){
        			List<WebElement> listaImports = driver.findElements(By.xpath("//*[contains(@class, 'gs_nta gs_nph')]"));
        			WebElement navegacion, text;
        			navegacion = listaImports.get(i);
        			navegacion.click();
        			text = driver.findElement(By.xpath("/html/body/pre"));
        			argumento = text.getText();
        			String aux = argumento.replace("{\\'o}", "�");
        			aux = aux.replace("{\\'a}", "�");
        			aux = aux.replace("{\\'e}", "�");
        			aux = aux.replace("{\\'u}", "�");
        			aux = aux.replace("{\\'\\i}", "�");
        			aux = aux.replace("{\\~n}", "�");
        			aux = aux.replace("{\\`\\i}", "�");
        			driver.navigate().back();
        			scholar = JSONfromBibtex(aux);
        			if(aux.contains("@book")) {
        				libros.put(scholar);
        			}
        			if(aux.contains("@article")) {
        				articulos.put(scholar);
        			}
        			if(aux.contains("@incollection")) {
        				incollection.put(scholar);
        			}
        			if(aux.contains("@inproceedings")) {
        				inproceedings.put(scholar);
        			}
        			
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
        prueba.put("books", libros);
        prueba.put("articles", articulos);
        prueba.put("incollection", incollection);
        prueba.put("inproceedings", inproceedings);
        entradas = prueba.toString(4);

		String jsonFile = ExtractorScholar.path + "\\Scholar.json";
		
			try(FileWriter fileWriter = new FileWriter(jsonFile)){
				fileWriter.write(entradas);
				fileWriter.close();
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
            entriesCollection = database.getEntries().values();

            BibTeXEntry entry = entriesCollection.iterator().next();
            Map<Key,Value> bibtexFields = entry.getFields();
            Set<Key> bibtexKeys = bibtexFields.keySet();

            for(Key key : bibtexKeys) {
                String keyValue = bibtexFields.get(key).toUserString();
                String aux2 = keyValue.replace("{\\'o}", "�");
    			aux2 = aux2.replace("{\\'a}", "�");
    			aux2 = aux2.replace("{\\'e}", "�");
    			aux2 = aux2.replace("{\\'u}", "�");
    			aux2 = aux2.replace("{\\'\\i}", "�");
    			aux2 = aux2.replace("{\\~n}", "�");
    			aux2 = aux2.replace("{\\`\\i}", "�");
    			aux2 = aux2.replace("{\\\"u}", "�");
                jsonPublication.put(key.toString(), aux2);
            }
            
        } catch (TokenMgrException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonPublication;
    }
}
