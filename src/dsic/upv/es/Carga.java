package dsic.upv.es;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import API.APIFRONT;

@Path("/Carga")
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
	
	@Path("{db1}/{db2}/{db3}/{a}/{b}")
	@GET
	@Produces("application/xml")
	public String getResult(@PathParam("db1") String baseDatos1,@PathParam("db2") String baseDatos2,@PathParam("db3") String baseDatos3,
			@PathParam("a") int anyoInicio, @PathParam("b") int anyoFin) {
		
		
		//Llamada jar compilado
		APIFRONT.LoadDBP(0, 3000);
		//APIFRONT.Buscar(args[1], args[2], args[3], args[4], true, true, true);
		
		System.out.println("CONSULTA:" + baseDatos1 + " " + baseDatos2 + " " + baseDatos3 + " " + anyoInicio + " " + anyoFin);
		String result = "@Produces(\"application/xml\") Output: \n\n Cargadado con exito: \n\n";
		return "<ctofservice>" +"<ctofoutput>" + result + "</ctofoutput>"+ "</ctofservice>";
		
		/*
		Double fahrenheit;
		Double celsius = c;
		fahrenheit = ((celsius* 9) / 5) + 32;
		String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
		return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
		*/
	}
}