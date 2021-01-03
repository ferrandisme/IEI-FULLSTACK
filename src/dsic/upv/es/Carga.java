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
		}catch(Exception e) {errores += "No se ha podido obtener el a&ntilde;o de inicio, utilizando el a&ntilde;o " + inicio + "  por defecto" + saltoLinea;}
		try {
			fin = Integer.parseInt(anyoFin);
			if(fin == 0)
			{
				fin = 1970;
				throw new Exception();
			}
		}catch(Exception e) {errores += "No se ha podido obtener el a&ntilde;o de fin, utilizando el a&ntilde;o " + fin + "  por defecto" + saltoLinea;}
		
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
		
		return  "<!DOCTYPE html lang=\"es\" >\r\n"
				+"<html>\r\n"
				+"\r\n"
				+"<head>\r\n"
				+"\r\n"
				+"<meta charset=\"ISO-8859-1\">\r\n"
				+"<title>B&uacute;squeda bibliogr&aacute;fica - IEI</title>\r\n"
				+"\r\n"
				+"<style>\r\n"
				+"\r\n"
				+"	.todo {\r\n"
				+"		 margin: auto;\r\n"
				+"		 background: white;\r\n"
				+"		 padding: 10px;\r\n"
				+"		 font-family: georgia,garamond,serif;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"	.titulo {\r\n"
				+"		text-align: center;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"	.subtitulo {\r\n"
				+"		text-align: center;\r\n"
				+"		color: #888180;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"	.encabezado {\r\n"
				+"		padding-left: 50px;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"	.formulario {\r\n"
				+"		padding-left: 100px;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"	.button1 {\r\n"
				+"	  background-color: #4CAF50;\r\n"
				+"	  border: none;\r\n"
				+"	  color: white;\r\n"
				+"	  padding: 15px 32px;\r\n"
				+"	  text-align: center;\r\n"
				+"	  text-decoration: none;\r\n"
				+"	  display: inline-block;\r\n"
				+"	  font-size: 14px;\r\n"
				+"	  margin: 4px 2px;\r\n"
				+"	  cursor: pointer;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"	.button2 {\r\n"
				+"	  background-color: #F32108;\r\n"
				+"	  border: none;\r\n"
				+"	  color: white;\r\n"
				+"	  padding: 15px 32px;\r\n"
				+"	  text-align: center;\r\n"
				+"	  text-decoration: none;\r\n"
				+"	  display: inline-block;\r\n"
				+"	  font-size: 14px;\r\n"
				+"	  margin: 4px 2px;\r\n"
				+"	  cursor: pointer;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"	.button3 {\r\n"
				+"	  background-color: #088AF3;\r\n"
				+"	  border: none;\r\n"
				+"	  color: white;\r\n"
				+"	  padding: 15px 32px;\r\n"
				+"	  text-align: center;\r\n"
				+"	  text-decoration: none;\r\n"
				+"	  display: inline-block;\r\n"
				+"	  font-size: 14px;\r\n"
				+"	  margin: 4px 2px;\r\n"
				+"	  cursor: pointer;\r\n"
				+"	}\r\n"
				+"\r\n"
				+"</style>\r\n"
				+"\r\n"
				+"</head>\r\n"
				+"\r\n"
				+"<body>\r\n"
				+"\r\n"
				+"<div class=\"todo\">\r\n"
				+"\r\n"
				+"	<div class=\"titulo\">\r\n"
				+"		<h1>DATOS CARGADOS</h1>\r\n"
				+"	</div>\r\n"
				+"\r\n"
				+"	<div class=\"subtitulo\">\r\n"
				+"		<h3>( IEI )</h3>\r\n"
				+"	</div>\r\n"
				+"\r\n"
				+"	<div class=\"encabezado\">\r\n"
				+"		<h4>Datos</h4>\r\n"
				+"	</div>\r\n"
				+"\r\n"
				+"	<div class=\"encabezado\">\r\n"
				+"		<p>\r\n"
				+            datosCargados
				+"      </p>\r\n"
				+"	</div>\r\n"
				+"\r\n"
				+"	<div class=\"encabezado\">\r\n"
				+"		<h4>Errores</h4>\r\n"
				+"	</div>\r\n"
				+"\r\n"
				+"	<div class=\"encabezado\">\r\n"
				+"		<p>\r\n"
				+            errores
				+"      </p>\r\n"
				+"	</div>\r\n"
				+"\r\n"
				+"	<div class=\"encabezado\">\r\n"
				+"	    <input class = \"button3\"  type=\"button\" name=\"data\" value=\"Cancelar\" onclick=\"location.href='http://localhost:8081/IEI/CargaDatos.jsp'\" />\r\n"
				+"	</div>\r\n"
				+"\r\n"
				+"</div>\r\n"
				+"\r\n"
				+"</body>\r\n"
				+"</html>\r\n"
				;
	}
}