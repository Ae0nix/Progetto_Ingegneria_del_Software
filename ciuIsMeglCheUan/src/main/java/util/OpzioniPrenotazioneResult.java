package util;

import entity.Accessorio;
import entity.Prenotazione;
import entity.Scooter;

import java.util.List;

public class OpzioniPrenotazioneResult {
    private List<Accessorio> accessori;
    private float prezzoBassaStagione;
    private Scooter scooter;

    public OpzioniPrenotazioneResult(List<Accessorio> accessori, float prezzoBassaStagione, Scooter scooter) {
        this.accessori = accessori;
        this.prezzoBassaStagione = prezzoBassaStagione;
        this.scooter = scooter;
    }

    public List<Accessorio> getAccessori() {
        return accessori;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public float getPrezzoBassaStagione() {
        return prezzoBassaStagione;
    }
}

