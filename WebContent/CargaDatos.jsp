<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="ISO-8859-1">
<title>B�squeda bibliogr�fica - IEI</title>

<style>  

	.todo {
		 margin: auto;
		 background: white;
		 padding: 10px;
		 font-family: georgia,garamond,serif;
	}

	.titulo {
		text-align: center;		
	}
	
	.subtitulo {
		text-align: center;
		color: #888180;
	}
	
	.encabezado {
		padding-left: 50px;
	}
	
	.formulario {
		padding-left: 100px;
		
	}
	
	.button1 {
	  background-color: #4CAF50;
	  border: none;
	  color: white;
	  padding: 15px 32px;
	  text-align: center;
	  text-decoration: none;
	  display: inline-block;
	  font-size: 14px;
	  margin: 4px 2px;
	  cursor: pointer;
	}
	
	.button2 {
	  background-color: #F32108;
	  border: none;
	  color: white;
	  padding: 15px 32px;
	  text-align: center;
	  text-decoration: none;
	  display: inline-block;
	  font-size: 14px;
	  margin: 4px 2px;
	  cursor: pointer;
	}
	
	.button3 {
	  background-color: #088AF3;
	  border: none;
	  color: white;
	  padding: 15px 32px;
	  text-align: center;
	  text-decoration: none;
	  display: inline-block;
	  font-size: 14px;
	  margin: 4px 2px;
	  cursor: pointer;
	}
    
</style>

</head>

<body>

<div class="todo">

	<div class="titulo">
		<h1>CARGAR DATOS DESDE LA WEB</h1>
	</div>
	
	<div class="subtitulo">
		<h3>( IEI )</h3>
	</div>
	
	<div class="encabezado">
		<h4>Seleccionar par�metros de carga:</h4>
	</div>
	
	<!-- Lo siguiente viene de aqu�: https://stackoverflow.com/questions/547821/two-submit-buttons-in-one-form -->
	<!-- Faltar�a el c�digo que dice en esa web -->
	<div class="formulario">
		<form action="http://localhost:8081/IEI/servicios/Carga/" method="GET" id="datos">
	      <label>Desde el a�o:</label><br>
	      
	      <input type="number" input id="buscarAnyoIni" name="anyoIni" size="50" ><br><br>
	      
	      <label>Hasta el a�o:</label><br>
	      <input type="number" input id="buscarAnyoFin" name="anyoFin"  size="50"><br><br>
	      
	      <label>Fuente de datos:</label><br>
	      <input type="checkbox" input id="dat1" name="checkDBLP">
		  <label for="dat1"> DBLP</label><br>
		  
		  <input type="checkbox" input id="dat2" name="checkIEEE">
		  <label for="dat2"> IEEE Xplore</label><br>
		  
		  <input type="checkbox" input id="dat3" name="checkGOOG">
		  <label for="dat3"> Google Scholar</label><br><br><br>
	      
	      <input class = "button1"  type="submit" name="submit" value="Enviar" />
	      <input class = "button3"  type="button" name="delete" value="Limpiar"  onclick="limpiar()" />
	      <input class = "button2"  type="button" name="back" value="Cancelar" onclick="location.href='http://localhost:8081/IEI/main.jsp'"/>
	    </form> 
	</div>
</div>

<script>
function limpiar() {
  document.getElementById("datos").reset();
}
</script>

</body>
</html>
