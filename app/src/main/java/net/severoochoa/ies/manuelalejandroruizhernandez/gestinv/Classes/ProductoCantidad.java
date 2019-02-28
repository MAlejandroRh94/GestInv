package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class ProductoCantidad {
    private long idProducto;
    private int cantidadProducto;


    public ProductoCantidad(long idProducto, int cantidadProducto) {
        this.idProducto = idProducto;
        this.cantidadProducto = cantidadProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }
}
