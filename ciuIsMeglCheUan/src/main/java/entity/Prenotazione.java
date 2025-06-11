package entity;

public class Prenotazione {
    private String id;
    private String dataRitiro;
    private String dataConsegna;
    private float costoTotale;
    private ClienteRegistrato clienteRegistrato;
    private int clienteRegistratoId;
    private Scooter scooter;
    private String scooterTarga;


    public Prenotazione(String dataRitiro, String dataConsegna, ClienteRegistrato clienteRegistrato, Scooter scooter) {
        this.id = null;
        this.dataRitiro = dataRitiro;
        this.dataConsegna = dataConsegna;
        this.clienteRegistrato = clienteRegistrato;
        this.clienteRegistratoId = this.clienteRegistrato.getId();
        this.scooter = scooter;
        this.scooterTarga = this.scooter.getTarga();
        this.costoTotale=0;
    }

    public void setCostoTotale(float costoTotale) {
        this.costoTotale = costoTotale;
    }

}
