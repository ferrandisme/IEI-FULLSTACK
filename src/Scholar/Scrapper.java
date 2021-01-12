package Scholar;

import java.io.FileReader;
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
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;
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

	static String argumento;
    	
	/*
	Chrome(1970, 1990);
    	
    	reader = new FileReader(argumento);
    	json = new JSONObject();
    	
    	Collection<BibTeXString> listado;
    	
    	parser = new BibTeXParser();
    	database = parser.parse(reader);
    	
    	listado = database.getStrings().values();
	 */
    	

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
		argumento += text.getText() + "\n";
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
		JSONfromBibtex(argumento);
        //}
    }
    
    public static JSONObject JSONfromBibtex(String bibtexPublication) {
        StringReader bibtexReader = new StringReader(bibtexPublication);
        Collection<BibTeXEntry> entriesCollection;
        JSONObject jsonPublication = new JSONObject();
        try {
            BibTeXParser bibtexParser = new BibTeXParser();
            BibTeXDatabase database = bibtexParser.parse(bibtexReader);
            entriesCollection = database.getEntries().values();

            BibTeXEntry entry = entriesCollection.iterator().next();
            Map<Key,Value> bibtexFields = entry.getFields();
            Set<Key> bibtexKeys = bibtexFields.keySet();

            for(Key key : bibtexKeys) {
                String keyValue = bibtexFields.get(key).toUserString();
                jsonPublication.put(key.toString(), keyValue);
            }

        } catch (TokenMgrException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonPublication;
    }
}
