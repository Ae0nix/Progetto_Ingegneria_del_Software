package boundary;

import control.GestioneSistemaPrenotazione;
import entity.Scooter;
import exception.OperationException;
import util.OpzioniScooterResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BCliente {

    private final GestioneSistemaPrenotazione gestioneSistema;

    public BCliente() {
        this.gestioneSistema = GestioneSistemaPrenotazione.getInstance();
    }

    /**
     * Endpoint per la ricerca degli scooter disponibili (restituisce template Thymeleaf)
     * GET /ricerca?localita=Roma&dataRitiro=01/06/2024&dataConsegna=05/06/2024
     */
    @GetMapping("/ricerca")
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
     * Endpoint per la selezione dello scooter (restituisce template Thymeleaf)
     * GET /seleziona-scooter?targa=AB123CD
     */
    @GetMapping("/seleziona-scooter")
    public String selezionaScooter(@RequestParam("targa") String targa, Model model) {

        try {
            // Validazione della targa
            if (targa == null || targa.trim().isEmpty()) {
                model.addAttribute("error", "Targa non specificata");
                return "error";
            }

            // Chiamata al sistema di controllo
            OpzioniScooterResult opzioni = gestioneSistema.selezionaScooter(targa.trim());

            model.addAttribute("opzioni", opzioni);
            model.addAttribute("targa", targa);

            return "opzioni-scooter";

        } catch (OperationException e) {
            model.addAttribute("error", "Errore durante la selezione: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Errore interno del sistema");
            return "error";
        }
    }

    /**
     * Endpoint per mostrare il form di ricerca
     * GET /cliente/ricerca-form
     */
    @GetMapping("/home")
    public String mostraFormRicerca() {
        return "home";
    }
}