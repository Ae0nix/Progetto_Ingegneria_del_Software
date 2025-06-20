package entity;

import exception.OperationException;

import java.util.ArrayList;
import java.util.List;

public class Prenotazione {
    private int id;
    private String dataRitiro;
    private String dataConsegna;
    private float costoTotale;
    private ClienteRegistrato clienteRegistrato;
    private int clienteRegistratoId;
    private Scooter scooter;
    private String scooterTarga;
    private List<Accessorio> accessori;

    public Prenotazione(String dataRitiro, String dataConsegna, ClienteRegistrato clienteRegistrato, Scooter scooter) {
        this.id = -1;
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

    public void setAccessori(List<Accessorio> accessori) throws OperationException {
        if (accessori == null) {
            throw new OperationException("La lista di accessori non può essere null");
        }
        this.accessori = accessori;
    }

    public List<Accessorio> getAccessori() {
        return accessori;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public String getDataRitiro() {
        return dataRitiro;
    }

    public String getDataConsegna() {
        return dataConsegna;
    }

    public int getId() {
        return id;
    }

    public float getCostoTotale() {
        return costoTotale;
    }

    public int getClienteRegistratoId() {
        return clienteRegistratoId;
    }

    public String getScooterTarga() {
        return scooterTarga;
    }

    public ClienteRegistrato getClienteRegistrato() {
        return clienteRegistrato;
    }
}
