package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class StockMinimo {
    private long id;
    private long idProd;
    private long idAlm;
    int cantMin;

    public StockMinimo(long id, long idProd, long idAlm, int cantMin) {
        this.id = id;
        this.idProd = idProd;
        this.idAlm = idAlm;
        this.cantMin = cantMin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdProd() {
        return idProd;
    }

    public void setIdProd(long idProd) {
        this.idProd = idProd;
    }

    public long getIdAlm() {
        return idAlm;
    }

    public void setIdAlm(long idAlm) {
        this.idAlm = idAlm;
    }

    public int getCantMin() {
        return cantMin;
    }

    public void setCantMin(int cantMin) {
        this.cantMin = cantMin;
    }


}
