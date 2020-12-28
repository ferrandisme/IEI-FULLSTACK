package modelo;

public class Ejemplar {
    private String volumen;
    private String numero;
    private String mes;

    public Ejemplar(String volumen, String numero, String mes) {
        this.volumen = volumen;
        this.numero = numero;
        this.mes = mes;
    }

    public String getVolumen() {
        return volumen;
    }
    public String getNumero() {
        return numero;
    }
    public String getMes() { return mes; }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public void setMes(String mes) {
        this.mes = mes;
    }
}
