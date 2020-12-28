package modelo;

public class Libro extends Publicacion{
    private String editorial;

    public Libro(String titulo, String anyo,String url, String editorial) {
        super(titulo, anyo, url);
        this.editorial = editorial;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setTitulo(String editorial) {
        this.editorial = editorial;
    }
}