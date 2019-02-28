package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes;

public class Atributo {
    private long id;
    private String valor;

    public Atributo(long id, String valor) {
        this.id = id;
        this.valor = valor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
