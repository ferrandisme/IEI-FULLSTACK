package modelo;

public class Publicacion {
    private String titulo;
    private String anyo;
    private String url;

    public Publicacion(String titulo, String anyo, String url) {
        this.titulo = titulo;
        this.anyo = anyo;
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAnyo() {
        return anyo;
    }

    public String getUrl() {
        return url;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAnyo(String anyo) {
        this.anyo = anyo;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}