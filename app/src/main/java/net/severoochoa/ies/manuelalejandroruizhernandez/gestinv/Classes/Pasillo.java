package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Pasillo {
    private long id;
    private long idSeccion;
    private long idAlmacen;
    private long idProducto;
    private String nombre;
    private int cantidadAlmacenada;

    public Pasillo(long id, long idAlmacen, long idSeccion, long idProducto, String nombre, int cantidadAlmacenada) {
        this.id = id;
        this.nombre = nombre;
        this.idSeccion = idSeccion;
        this.idAlmacen = idAlmacen;
        this.idProducto = idProducto;
        this.cantidadAlmacenada = cantidadAlmacenada;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(long idSeccion) {
        this.idSeccion = idSeccion;
    }

    public long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidadAlmacenada() {
        return cantidadAlmacenada;
    }

    public void setCantidadAlmacenada(int cantidadAlmacenada) {
        this.cantidadAlmacenada = cantidadAlmacenada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
