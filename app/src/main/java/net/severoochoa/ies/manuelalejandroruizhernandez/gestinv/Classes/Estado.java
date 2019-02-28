package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Estado {
    private long id;
    private String estado;

    public Estado(long id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return estado;
    }
}
