package entity;

public class Accessorio {
    private int id;
    private String descrizione;
    private float prezzo;
    private String tipo;

    public Accessorio(int id, String descrizione, float prezzo, String tipo) {
        this.id = id;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public String getTipo() {
        return tipo;
    }
}
