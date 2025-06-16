package util;

import entity.Accessorio;

import java.util.List;

public class OpzioniScooterResult {
    private List<Accessorio> accessori;
    private float prezzoBassaStagione;

    public OpzioniScooterResult(List<Accessorio> accessori, float prezzoBassaStagione) {
        this.accessori = accessori;
        this.prezzoBassaStagione = prezzoBassaStagione;
    }
}

