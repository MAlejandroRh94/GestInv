package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Recepcion {
    private long id;
    private long idAlmacen;
    private long idProducto;
    private long idEstado;
    private long fechaCreacion;
    private int cantidadRecibida;

    public Recepcion(long id, long idAlmacen, long idProducto, long idEstado, long fechaCreacion, int cantidadRecibida) {
        this.id = id;
        this.idAlmacen = idAlmacen;
        this.idProducto = idProducto;
        this.idEstado = idEstado;
        this.fechaCreacion = fechaCreacion;
        this.cantidadRecibida = cantidadRecibida;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(long idEstado) {
        this.idEstado = idEstado;
    }

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(int cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
