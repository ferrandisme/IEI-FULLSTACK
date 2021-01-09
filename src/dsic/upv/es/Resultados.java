package dsic.upv.es;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import API.APIFRONT;

@Path("/Resultados/")
public class Resultados {
	
	@GET
	@Produces("text/html")
	public String getResult(
			@QueryParam("buscarAutor") String autor,
			@QueryParam("buscarTitulo") String titulo,
			@QueryParam("buscarAnyoIni") String inicio,
			@QueryParam("buscarAnyoFin") String fin,
			@QueryParam("pub1") String articulo,
			@QueryParam("pub2") String libro,
			@QueryParam("pub3") String comunicacion)
	 {
		
		String errores = "";
		String saltoLinea = "<br>\r\n";
		
		
		try {
			if(Integer.parseInt(inicio) < 1900 || Integer.parseInt(inicio) > LocalDateTime.now().getYear())
				throw new Exception();
		}catch(Exception e) {
			inicio = "1970";
			errores += "No se ha podido obtener el a&ntilde;o de inicio, utilizando el a&ntilde;o " + inicio + "  por defecto " + saltoLinea;
		}
		try {
			if(Integer.parseInt(fin) < 1900 || Integer.parseInt(fin) > LocalDateTime.now().getYear())
				throw new Exception();
		}catch(Exception e) {
			fin = LocalDateTime.now().getYear() + "";
			errores += "No se ha podido obtener el a&ntilde;o de fin, utilizando el a&ntilde;o " + fin + "  por defecto" + saltoLinea;}
		
		
			boolean esArticulo = false, esLibro = false, esComunicacion = false;
			try {
			
			if(articulo != null && articulo.equals("on")) {
				esArticulo = true;
			}
			if(libro != null && libro.equals("on")) {
				esLibro = true;
			}
			if(comunicacion != null && comunicacion.equals("on")) {
				esComunicacion = true;
			}
			
		}catch(Exception e) { errores += e.getCause() + "\n"; System.out.println(e.getStackTrace());}

			
		List<String> res = APIFRONT.Buscar(autor, titulo, inicio, fin, esArticulo, esLibro, esComunicacion);
		
		if(errores.length() > 0)
			System.out.println("Errores: " + errores);
		else
			errores = "No se ha encontrado ningun error";
		
		String resultados = "";
		
		for(int i = 0; i < res.size(); i++)
			resultados += res.get(i) + saltoLinea;
		
		if(resultados.length() == 0)
			resultados = "No se ha encontrado ningun resultado";
		
		
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
		+"		<h1>DATOS CARGADOS </h1>\r\n"
		+"	</div>\r\n"
		+"\r\n"
		+"	<div class=\"subtitulo\">\r\n"
		+"		<h3>( IEI )</h3>\r\n"
		+"	</div>\r\n"
		+"\r\n"
		+"	<div class=\"encabezado\">\r\n"
		+"		<h4>Datos " + "(total:" + res.size() +")" +"</h4>\r\n"
		+"	</div>\r\n"
		+"\r\n"
		+"	<div class=\"encabezado\">\r\n"
		+"		<p>\r\n"
		+            resultados
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
		+"	    <input class = \"button3\"  type=\"button\" name=\"data\" value=\"Cancelar\" onclick=\"location.href='http://localhost:8081/IEI/main.jsp'\" />\r\n"
		+"	</div>\r\n"
		+"\r\n"
		+"</div>\r\n"
		+"\r\n"
		+"</body>\r\n"
		+"</html>\r\n"
		;
	}

}
