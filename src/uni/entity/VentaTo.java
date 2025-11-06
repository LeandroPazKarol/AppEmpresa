package uni.entity;

import java.util.List;

public class VentaTo {
    // atributos;

    private int idventa;
    private String idcliente;
    private String idempleaado;
    private String tipodoc;
    private String nrodoca;
    private double total;
    private List<DetalleTo> lista;
    // constructor

    public VentaTo() {
    }

    public VentaTo(int idventa, String idcliente, String idempleaado, String tipodoc, String nrodoca, double total, List<DetalleTo> lista) {
        this.idventa = idventa;
        this.idcliente = idcliente;
        this.idempleaado = idempleaado;
        this.tipodoc = tipodoc;
        this.nrodoca = nrodoca;
        this.total = total;
        this.lista = lista;
    }
    // metodos get y set

    public int getIdventa() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }

    public String getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(String idcliente) {
        this.idcliente = idcliente;
    }

    public String getIdempleaado() {
        return idempleaado;
    }

    public void setIdempleaado(String idempleaado) {
        this.idempleaado = idempleaado;
    }

    public String getTipodoc() {
        return tipodoc;
    }

    public void setTipodoc(String tipodoc) {
        this.tipodoc = tipodoc;
    }

    public String getNrodoca() {
        return nrodoca;
    }

    public void setNrodoca(String nrodoca) {
        this.nrodoca = nrodoca;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetalleTo> getLista() {
        return lista;
    }

    public void setLista(List<DetalleTo> lista) {
        this.lista = lista;
    }
    
}
