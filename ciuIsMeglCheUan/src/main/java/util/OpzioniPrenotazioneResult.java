package util;

import entity.Accessorio;
import entity.Prenotazione;

import java.util.List;

public class OpzioniPrenotazioneResult {
    private List<Accessorio> accessori;
    private float prezzoBassaStagione;
    private Prenotazione prenotazione;

    public OpzioniPrenotazioneResult(List<Accessorio> accessori, float prezzoBassaStagione, Prenotazione prenotazione) {
        this.accessori = accessori;
        this.prezzoBassaStagione = prezzoBassaStagione;
        this.prenotazione = prenotazione;
    }

    public List<Accessorio> getAccessori() {
        return accessori;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }
}

