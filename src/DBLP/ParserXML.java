package DBLP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.XML;

public class ParserXML {
	
	public static void pasarXMLaJSON(){
		try {
			int PRETTY_PRINT_INDENT_FACTOR = 4;
			//String xmlString = null; 
			//String xmlFile = ExtractorDBLP.path + "\\dblpXML.xml";
			File file = new File(ExtractorDBLP.path + "\\dblpXML.xml");
			FileInputStream fin = new FileInputStream(file);
			byte[] xmlData = new byte[(int) file.length()];
			fin.read(xmlData);
			fin.close();
			String TEST_XML_STRING = new String(xmlData, "UTF-8");
			
			JSONObject xmlJSONObj = null;
			xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
			String jsonFile = ExtractorDBLP.path + "\\DBLP.json";
			String jsonPrettyPrintString = null;
			
				try(FileWriter fileWriter = new FileWriter(jsonFile)){
					fileWriter.write(xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR));
					jsonPrettyPrintString= xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
					//System.out.println(jsonPrettyPrintString);
				} catch (IOException e) {
					System.out.println("No se ha podido escribir el JSON");
					System.out.println(e.getMessage());
				}
				
				
		}catch(Exception e) {
			System.out.println("No se ha pasar de XML a JSON");
			System.out.println(e.getMessage());
		}
				
	}

}
