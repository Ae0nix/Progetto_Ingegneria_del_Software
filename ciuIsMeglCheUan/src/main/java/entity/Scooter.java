package entity;

public class Scooter {
    private String targa;
    private String cilindrata;
    private String prezzoPerGiornoNoleggioAltaStagione;
    private String prezzoPerGiornoNoleggioBassaStagione;
    private String stato;
    private String tipologia;
    private Agenzia agenzia;
    private int agenziaId;

    public Scooter(String targa, String cilindrata, String prezzoPerGiornoNoleggioAltaStagione, String prezzoPerGiornoNoleggioBassaStagione, String tipologia, Agenzia agenzia) {
        this.targa = targa;
        this.cilindrata = cilindrata;
        this.prezzoPerGiornoNoleggioAltaStagione = prezzoPerGiornoNoleggioAltaStagione;
        this.prezzoPerGiornoNoleggioBassaStagione = prezzoPerGiornoNoleggioBassaStagione;
        this.stato = "in-servizio";
        this.tipologia = tipologia;
        this.agenzia = agenzia;
        this.agenziaId = this.agenzia.getId();
    }

    public String getTarga() {
        return targa;
    }

}
