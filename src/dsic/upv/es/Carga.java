package dsic.upv.es;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import API.APIFRONT;

@Path("/Carga/")
public class Carga {

	/*@GET
	@Produces("application/xml")
	public String convertCtoF() {
		Double fahrenheit;
		Double celsius = 36.8;
		fahrenheit = ((celsius* 9) / 5) + 32;
		String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
		return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
	}*/
	
	@Path("{a}&{b}&{db1}&{db2}&{db3}&{ignoreEnviar}")
	@GET
	@Produces("text/html")
	public String getResult(@PathParam("db1") String baseDatos1,@PathParam("db2") String baseDatos2,@PathParam("db3") String baseDatos3,
			@PathParam("a") String anyoInicio, @PathParam("b") String anyoFin) {
		
		
		//Llamada jar compilado
		String errores = "";
		
		int inicio = 1970;
		int fin = 2021;
		try {
			inicio = Integer.parseInt(anyoInicio.split("=")[1]);
			if(inicio == 0)
			{
				inicio = 1970;
				throw new Exception();
			}
		}catch(Exception e) {errores += "No se ha podido obtener el año de inicio, utilizando el año " + inicio + "  por defecto \n";}
		try {
			fin = Integer.parseInt(anyoFin.split("=")[1]);
			if(fin == 0)
			{
				fin = 1970;
				throw new Exception();
			}
		}catch(Exception e) {errores += "No se ha podido obtener el año de fin, utilizando el año " + fin + "  por defecto \n";}
		
		
		
		
		//TODO Hacer lo mismo para DB
		try {
		APIFRONT.LoadDBP(inicio, fin);
		APIFRONT.LoadIEEE(inicio, fin);
		APIFRONT.LoadSchoolar(inicio, fin);
		}catch(Exception e) { errores += e.getCause(); System.out.println(e.getStackTrace());}
		//APIFRONT.Buscar(args[1], args[2], args[3], args[4], true, true, true);
		
		System.out.println("CONSULTA:" + baseDatos1 + " " + baseDatos2 + " " + baseDatos3 + " " + anyoInicio + " " + anyoFin);
		//String result = "@Produces(\"application/xml\") Output: \n\n Cargadado con exito: \n\n";
		//return "<ctofservice>" +"<ctofoutput>" +  result + "</ctofoutput>"+ "</ctofservice>";
		
		
		System.out.println("Errores: " + errores);
		return  "<html>\r\n"
				+ "<body>\r\n"
				+ "\r\n"
				+ "<h2>HTML Forms</h2>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "<p>If you click the \"Submit\" button, the form-data will be sent to a page called \"/action_page.php\".</p>\r\n"
				+ "\r\n"
				+ "</body>\r\n"
				+ "</html>" ;
		
		/*
		Double fahrenheit;
		Double celsius = c;
		fahrenheit = ((celsius* 9) / 5) + 32;
		String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
		return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
		*/
	}
}