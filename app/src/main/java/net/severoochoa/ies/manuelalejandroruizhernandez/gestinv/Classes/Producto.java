package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Producto {
    private long id;
    private long idCategoria;
    private String nombre;

    public Producto(long id, String nombre, long idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.idCategoria = idCategoria;
    }

    public Producto(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Producto(String nombre, long idCategoria) {
        this.nombre = nombre;
        this.idCategoria = idCategoria;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
