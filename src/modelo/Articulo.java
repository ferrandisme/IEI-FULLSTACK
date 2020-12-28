package modelo;

public class Articulo extends Publicacion{
    private String pag_ini;
    private String pag_fin;

    public Articulo(String titulo, String anyo,String url, String pag_ini, String pag_fin) {
        super(titulo, anyo, url);
        this.pag_ini = pag_ini;
        this.pag_fin = pag_fin;
    }

    public String getPag_ini() {
        return pag_ini;
    }
    public String getPag_fin() {
        return pag_fin;
    }

    public void setPag_ini(String pag_ini ) {
        this.pag_ini = pag_ini;
    }
    public void set(String pag_fin) {
        this.pag_fin = pag_fin;
    }
}
