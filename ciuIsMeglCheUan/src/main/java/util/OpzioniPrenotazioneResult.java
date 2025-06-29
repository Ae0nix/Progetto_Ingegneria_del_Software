package util;

import entity.Accessorio;
import entity.Scooter;

import java.util.List;

/**
 * Classe di utilità per incapsulare i risultati delle opzioni di prenotazione.
 *
 * <p>Questa classe serve come Data Transfer Object (DTO) per trasportare
 * in modo organizzato tutte le informazioni necessarie per visualizzare
 * le opzioni di prenotazione di uno scooter specifico.</p>
 *
 * <p>Contiene:</p>
 * <ul>
 *   <li>Informazioni complete dello scooter</li>
 *   <li>Prezzo base per la bassa stagione</li>
 *   <li>Lista completa degli accessori disponibili</li>
 * </ul>
 *
 * <p>Viene utilizzata principalmente nel flusso di prenotazione per
 * separare la logica di business dalla presentazione dei dati.</p>
 *
 * @author Team TouristScooters
 * @version 1.0
 * @since 2025
 */
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

