package util;

import entity.Accessorio;
import java.util.List;

public class OpzioniPrenotazioneResult {
    private List<Accessorio> accessori;
    private float prezzoBassaStagione;

    public OpzioniPrenotazioneResult(List<Accessorio> accessori, float prezzoBassaStagione) {
        this.accessori = accessori;
        this.prezzoBassaStagione = prezzoBassaStagione;
    }

    public List<Accessorio> getAccessori() {
        return accessori;
    }

    public float getPrezzoBassaStagione() {
        return prezzoBassaStagione;
    }
}

