package boundary;

import control.GestioneSistemaPrenotazione;
import entity.Scooter;
import exception.OperationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import util.OpzioniPrenotazioneResult;

import java.util.List;

/**
 * Controller Spring MVC per la gestione delle operazioni dei clienti non registrati.
 *
 * <p>Questa classe gestisce gli endpoint web per le funzionalità accessibili
 * ai clienti generici, principalmente la ricerca degli scooter disponibili
 * e la visualizzazione delle opzioni di prenotazione.</p>
 *
 * <p>Endpoints principali:</p>
 * <ul>
 *   <li><strong>GET /scooters</strong>: Ricerca scooter disponibili</li>
 *   <li><strong>GET /opzioniPrenotazione</strong>: Visualizza opzioni e costi</li>
 *   <li><strong>GET /home</strong>: Pagina principale con form di ricerca</li>
 * </ul>
 *
 * @author Team TouristScooters
 * @version 1.0
 * @since 2025
 *
 * @see GestioneSistemaPrenotazione
 * @see BClienteRegistrato
 */

@Controller
public class BCliente {

    private final GestioneSistemaPrenotazione gestioneSistema;

    public BCliente() {
        this.gestioneSistema = GestioneSistemaPrenotazione.getInstance();
    }

    /**
     * Endpoint per la ricerca degli scooter disponibili.
     *
     * <p>Gestisce le richieste GET per cercare scooter disponibili in una
     * specifica località per un determinato periodo. Effettua validazione
     * dei parametri di input e gestione degli errori.</p>
     *
     * <p><strong>URL Pattern:</strong> GET /scooters?localita=Roma&dataRitiro=2024-06-01&dataConsegna=2024-06-05</p>
     *
     * @param localita La località dove cercare gli scooter
     * @param dataRitiro Data di inizio noleggio (formato yyyy-MM-dd)
     * @param dataConsegna Data di fine noleggio (formato yyyy-MM-dd)
     * @param model Oggetto Model di Spring per passare dati alla vista
     * @return Nome del template Thymeleaf da renderizzare
     *
     * @throws OperationException se si verifica un errore durante la ricerca
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
     * Endpoint per visualizzare le opzioni di prenotazione e i costi.
     *
     * <p>Questo endpoint è progettato per essere utilizzato in popup (modal)
     * e restituisce un frammento Thymeleaf con le informazioni dettagliate
     * dello scooter, gli accessori disponibili e i prezzi.</p>
     *
     * <p><strong>URL Pattern:</strong> GET /opzioniPrenotazione?targa=ABC123&dataRitiro=2024-06-01&dataConsegna=2024-06-05</p>
     *
     * @param targaScooter Targa dello scooter per cui visualizzare le opzioni
     * @param dataRitiro Data di inizio noleggio (opzionale)
     * @param dataConsegna Data di fine noleggio (opzionale)
     * @param model Oggetto Model di Spring per passare dati alla vista
     * @return Nome del frammento Thymeleaf da renderizzare nel popup
     *
     * @throws OperationException se lo scooter non viene trovato
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
     * Endpoint per la pagina principale con il form di ricerca.
     *
     * <p>Visualizza la home page dell'applicazione contenente il modulo
     * per la ricerca degli scooter disponibili.</p>
     *
     * <p><strong>URL Pattern:</strong> GET /home</p>
     *
     * @return Nome del template Thymeleaf della home page
     */
    @GetMapping("/")
    public String mostraFormRicerca() {
        return "home";
    }
}