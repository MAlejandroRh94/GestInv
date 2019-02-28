package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Categoria {
    private long id;
    private String nombre, imagen, descripcion, unidadMedida;
    private long fechaAlta;

    public Categoria(long id, String nombre, long fechaAlta, String imagen, String descripcion, String unidadMedida) {
        this.id = id;
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(long fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}
