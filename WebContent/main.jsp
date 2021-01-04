<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="ISO-8859-1">
<title>Búsqueda bibliográfica - IEI</title>

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
		<h1>BÚSQUEDA BIBLIOGRÁFICA</h1>
	</div>
	
	<div class="subtitulo">
		<h3>( IEI )</h3>
	</div>
	
	<div class="encabezado">
		<h4>Buscar por:</h4>
	</div>
	
	<!-- Lo siguiente viene de aquí: https://stackoverflow.com/questions/547821/two-submit-buttons-in-one-form -->
	<!-- Faltaría el código que dice en esa web -->
	<div class="formulario">
		<form action="http://localhost:8081/IEI/Resultados.jsp" method="get" id="datos">
	      <label>Autor/es:</label><br>
	      <input type="text" id="buscarAutor" size="50"><br><br>
	      
	      <label>Título:</label><br>
	      <input type="text" id="buscarTitulo" size="50"><br><br>
	      
	      <label>Desde el año:</label><br>
	      <input type="number" id="buscarAnyoIni" size="50"><br><br>
	      
	      <label>Hasta el año:</label><br>
	      <input type="number" id="buscarAnyoFin" size="50"><br><br>
	      
	      <label>Tipo de publicación:</label><br>
	      <input type="checkbox" id="pub1">
		  <label for="pub1"> Artículo</label><br>
		  
		  <input type="checkbox" id="pub2">
		  <label for="pub2"> Libro</label><br>
		  
		  <input type="checkbox" id="pub3">
		  <label for="pub3"> Comunicación en congreso</label><br><br><br>
	      
	      <input class = "button1"  type="submit" name="update" value="Enviar" />
	      <input class = "button2"  type="button" name="delete" value="Limpiar"  onclick="limpiar()" />
	      <input class = "button3"  type="button" name="data" value="Cargar datos" onclick="location.href='http://localhost:8081/IEI/CargaDatos.jsp'" />

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