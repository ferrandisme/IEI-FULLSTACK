package modelo;

public class ComunicacionCongreso extends Publicacion{
    private String edicion;
    private String lugar;
    private String pag_ini;
    private String pag_fin;

    public ComunicacionCongreso(String titulo, String anyo,String url, String edicion, String lugar, String pag_ini, String pag_fin) {
        super(titulo, anyo, url);
        this.edicion = edicion;
        this.lugar = lugar;
        this.pag_ini = pag_ini;
        this.pag_fin = pag_fin;
    }

    public String getEdicion() {
        return edicion;
    }
    public String getLugar() {
        return lugar;
    }
    public String getPag_ini() {
        return pag_ini;
    }
    public String getPag_fin() {
        return pag_fin;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
    public void setPag_ini(String pag_ini ) {
        this.pag_ini = pag_ini;
    }
    public void set(String pag_fin) {
        this.pag_fin = pag_fin;
    }
}
