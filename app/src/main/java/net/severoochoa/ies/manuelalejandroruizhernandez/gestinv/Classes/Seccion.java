package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Seccion {
    private long id;
    private long idAlmacen;
    private String nombre;

    public Seccion(long id, long idAlmacen, String nombre) {
        this.id = id;
        this.idAlmacen = idAlmacen;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
