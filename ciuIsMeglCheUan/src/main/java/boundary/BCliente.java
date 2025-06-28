package boundary;

import control.GestioneSistemaPrenotazione;
import entity.Scooter;
import exception.OperationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import util.OpzioniPrenotazioneResult;

import java.util.List;

@Controller
public class BCliente {

    private final GestioneSistemaPrenotazione gestioneSistema;

    public BCliente() {
        this.gestioneSistema = GestioneSistemaPrenotazione.getInstance();
    }

    /**
     * Endpoint per la ricerca degli scooter disponibili (restituisce template Thymeleaf)
     * GET /scooters?localita=Roma&dataRitiro=01/06/2024&dataConsegna=05/06/2024
     */
    @GetMapping("/scooters")
    public String ricercaScooter(
            @RequestParam("localita") String localita,
            @RequestParam("dataRitiro") String dataRitiro,
            @RequestParam("dataConsegna") String dataConsegna,
            Model model) {

        try {
            // Validazione parametri
            if (localita == null || localita.trim().isEmpty()) {
                model.addAttribute("error", "Località non specificata");
                return "error";
            }

            if (dataRitiro == null || dataRitiro.trim().isEmpty()) {
                model.addAttribute("error", "Data ritiro non specificata");
                return "error";
            }

            if (dataConsegna == null || dataConsegna.trim().isEmpty()) {
                model.addAttribute("error", "Data consegna non specificata");
                return "error";
            }

            // Chiamata al sistema di controllo
            List<Scooter> scooterDisponibili = gestioneSistema.ricercaScooter(
                    localita.trim(),
                    dataRitiro.trim(),
                    dataConsegna.trim()
            );

            model.addAttribute("scooters", scooterDisponibili);
            model.addAttribute("localita", localita);
            model.addAttribute("dataRitiro", dataRitiro);
            model.addAttribute("dataConsegna", dataConsegna);

            return "risultati-ricerca";

        } catch (OperationException e) {
            model.addAttribute("error", "Errore durante la ricerca: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Errore interno del sistema");
            return "error";
        }
    }

    /**
     * Endpoint per visualizzare le opzioni di prenotazione e i costi di uno scooter.
     * Da visualizzare in popup (modal) nella pagina Thymeleaf.
     * GET /opzioniPrenotazione?targa=ABC123
     */
    @GetMapping("/opzioniPrenotazione")
    public String visualizzaOpzioniPrenotazione(
            @RequestParam("targa") String targaScooter,
            @RequestParam(value = "dataRitiro", required = false) String dataRitiro,
            @RequestParam(value = "dataConsegna", required = false) String dataConsegna,
            Model model) {
        try {
            if (targaScooter == null || targaScooter.trim().isEmpty()) {
                model.addAttribute("error", "Targa scooter non specificata");
                return "fragments/errore-popup :: errorePopupContent";
            }

            OpzioniPrenotazioneResult result = gestioneSistema.visualizzaOpzioniDiPrenotazioneCosti(targaScooter.trim());

            model.addAttribute("scooter", result.getScooter());
            model.addAttribute("prezzoBassaStagione", result.getPrezzoBassaStagione());
            model.addAttribute("accessori", result.getAccessori());
            // AGGIUNGI QUESTE RIGHE:
            model.addAttribute("dataRitiro", dataRitiro);
            model.addAttribute("dataConsegna", dataConsegna);

            return "fragments/opzioni-prenotazione :: opzioniPrenotazioneContent";
        } catch (OperationException e) {
            model.addAttribute("error", "Errore durante il recupero delle opzioni: " + e.getMessage());
            return "fragments/errore-popup :: errorePopupContent";
        } catch (Exception e) {
            model.addAttribute("error", "Errore interno del sistema");
            return "fragments/errore-popup :: errorePopupContent";
        }
    }

    /**
     * Endpoint per mostrare il form di ricerca
     * GET /
     */
    @GetMapping("/")
    public String mostraFormRicerca() {
        return "home";
    }
}