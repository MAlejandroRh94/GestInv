package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Transferencia {
    private long idTransferencia;
    private long idProducto;
    private long idEstado;
    private long idAlmacen;
    private long fechaCreacion;
    private int cantidadTransferida;

    public Transferencia(long idTransferencia, long idProducto, long idEstado, long idAlmacen, long fechaCreacion, int cantidadTransferida) {
        this.idTransferencia = idTransferencia;
        this.idProducto = idProducto;
        this.idEstado = idEstado;
        this.idAlmacen = idAlmacen;
        this.fechaCreacion = fechaCreacion;
        this.cantidadTransferida = cantidadTransferida;
    }

    public long getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(long idTransferencia) {
        this.idTransferencia = idTransferencia;
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

    public long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public int getCantidadTransferida() {
        return cantidadTransferida;
    }

    public void setCantidadTransferida(int cantidadTransferida) {
        this.cantidadTransferida = cantidadTransferida;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
