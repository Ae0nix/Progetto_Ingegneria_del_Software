package entity;

public class Scooter {
    private String targa;
    private int cilindrata;
    private float prezzoPerGiornoNoleggioAltaStagione;
    private float prezzoPerGiornoNoleggioBassaStagione;
    private String stato;
    private String tipologia;
    private Agenzia agenzia;
    private int agenziaId;

    public Scooter(String targa, int cilindrata, float prezzoPerGiornoNoleggioAltaStagione, float prezzoPerGiornoNoleggioBassaStagione, String tipologia, Agenzia agenzia) {
        this.targa = targa;
        this.cilindrata = cilindrata;
        this.prezzoPerGiornoNoleggioAltaStagione = prezzoPerGiornoNoleggioAltaStagione;
        this.prezzoPerGiornoNoleggioBassaStagione = prezzoPerGiornoNoleggioBassaStagione;
        this.stato = "in-servizio";
        this.tipologia = tipologia;
        this.agenzia = agenzia;
        this.agenziaId = this.agenzia.getId();
    }

    public Scooter(String targa, int cilindrata, float prezzoPerGiornoNoleggioAltaStagione, float prezzoPerGiornoNoleggioBassaStagione, String tipologia, int agenziaId) {
        this.targa = targa;
        this.cilindrata = cilindrata;
        this.prezzoPerGiornoNoleggioAltaStagione = prezzoPerGiornoNoleggioAltaStagione;
        this.prezzoPerGiornoNoleggioBassaStagione = prezzoPerGiornoNoleggioBassaStagione;
        this.stato = "in-servizio";
        this.tipologia = tipologia;
        this.agenziaId = agenziaId;
    }

    public String getTarga() {
        return targa;
    }

    public float getPrezzoPerGiornoNoleggioBassaStagione() {
        return prezzoPerGiornoNoleggioBassaStagione;
    }
}
