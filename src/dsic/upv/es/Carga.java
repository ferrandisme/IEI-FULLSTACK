package dsic.upv.es;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import API.APIFRONT;

@Path("/Carga/")
public class Carga {
	
	@GET
	@Produces("text/html")
	public String getResult(
			@QueryParam("checkDBLP") String baseDatos1,
			@QueryParam("checkIEEE") String baseDatos2,
			@QueryParam("checkGOOG") String baseDatos3,
			@QueryParam("anyoIni") String anyoInicio,
			@QueryParam("anyoFin") String anyoFin) {
		
		
		String errores = "";
		String saltoLinea = "<br>\r\n";
		
		int inicio = 1970;
		int fin = 2021;
		try {
			inicio = Integer.parseInt(anyoInicio);
			if(inicio == 0)
			{
				inicio = 1970;
				throw new Exception();
			}
		}catch(Exception e) {errores += "No se ha podido obtener el año de inicio, utilizando el año " + inicio + "  por defecto" + saltoLinea;}
		try {
			fin = Integer.parseInt(anyoFin);
			if(fin == 0)
			{
				fin = 1970;
				throw new Exception();
			}
		}catch(Exception e) {errores += "No se ha podido obtener el año de fin, utilizando el año " + fin + "  por defecto" + saltoLinea;}
		
		String datosCargados = "";
		
		try {
			
			//DBLP
			if(baseDatos1 != null && baseDatos1.equals("on")) {
				datosCargados += "DBLP ha cargado: " + APIFRONT.LoadDBP(inicio, fin) + " publicaciones nuevas "+  saltoLinea;
			}
			//IEEE
			if(baseDatos2 != null && baseDatos2.equals("on")) {
				datosCargados += "IEEE ha cargado: " + APIFRONT.LoadIEEE(inicio, fin) + " publicaciones nuevas "+ saltoLinea;
			}
			//SCHOOLAR
			if(baseDatos3 != null && baseDatos3.equals("on")) {
				datosCargados += "SCHOOLAR ha cargado: " + APIFRONT.LoadSchoolar(inicio, fin) + " publicaciones nuevas "+ saltoLinea;
			}
			
		}catch(Exception e) { errores += e.getCause() + "\n"; System.out.println(e.getStackTrace());}
		if(datosCargados.length() == 0) {
			errores += "No se ha seleccionado ninguna base de datos para cargar" + saltoLinea;
			datosCargados = "No se ha cargado ningun dato";
		}
		
		System.out.println("CONSULTA:" + baseDatos1 + " " + baseDatos2 + " " + baseDatos3 + " " + anyoInicio + " " + anyoFin);
		if(errores.length() > 0)
			System.out.println("Errores: " + errores);
		else
			errores = "No se ha encontrado ningun error";
		
		return  "<html>\r\n"
				+ "<body>\r\n"
				+ "<br>\r\n"
				+ "<br>\r\n"
				+ "DATOS CARGADOS"
				+ "<br>\r\n"
				+ "<br>\r\n"
				+ datosCargados
				+ "<br>\r\n"
				+ "<br>\r\n"
				+ "<br>\r\n"
				+ "Errores"
				+ "<br>\r\n"
				+ errores
				+ "<br>\r\n"
				+ "</body>\r\n"
				+ "</html>" ;
	}
}