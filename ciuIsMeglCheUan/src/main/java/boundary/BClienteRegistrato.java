package boundary;

import entity.Prenotazione;
import exception.OperationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import control.GestioneSistemaPrenotazione;

import java.util.List;

/**
 * Controller Spring MVC per la gestione delle operazioni dei clienti registrati.
 *
 * <p>Questa classe gestisce gli endpoint web per le funzionalità riservate
 * ai clienti autenticati, inclusa la selezione degli accessori, la conferma
 * delle prenotazioni e la loro cancellazione.</p>
 *
 * <p>Endpoints principali:</p>
 * <ul>
 *   <li><strong>POST /selezioneAccessori</strong>: Crea prenotazione con accessori</li>
 *   <li><strong>GET /confermaPrenotazione</strong>: Conferma e salva prenotazione</li>
 *   <li><strong>GET /annullaPrenotazione</strong>: Annulla prenotazione corrente</li>
 * </ul>
 *
 * <p><strong>Prerequisiti:</strong> Il cliente deve essere autenticato nel sistema.</p>
 *
 * @author Team TouristScooters
 * @version 1.0
 * @since 2025
 *
 * @see GestioneSistemaPrenotazione
 * @see BCliente
 */

@Controller
public class BClienteRegistrato {

    private final GestioneSistemaPrenotazione gestioneSistema;

    public BClienteRegistrato() {
        this.gestioneSistema = GestioneSistemaPrenotazione.getInstance();
    }

    /**
     * Endpoint per la selezione degli accessori e creazione della prenotazione.
     *
     * <p>Questo metodo riceve i dati del form di prenotazione, valida le informazioni
     * del cliente registrato, crea una prenotazione temporanea con gli accessori
     * selezionati e calcola il costo totale.</p>
     *
     * <p>La prenotazione creata non viene ancora persistita nel database ma
     * mantenuta in memoria per la conferma successiva.</p>
     *
     * <p><strong>Parametri del form:</strong></p>
     * <ul>
     *   <li>accessori: Lista degli ID degli accessori selezionati (opzionale)</li>
     *   <li>dataRitiro: Data di inizio noleggio (obbligatorio)</li>
     *   <li>dataConsegna: Data di fine noleggio (obbligatorio)</li>
     *   <li>email: Email del cliente registrato (obbligatorio)</li>
     *   <li>targa: Targa dello scooter da prenotare (obbligatorio)</li>
     * </ul>
     *
     * @param accessoriId Lista degli ID degli accessori selezionati
     * @param dataRitiro Data di inizio noleggio nel formato yyyy-MM-dd
     * @param dataConsegna Data di fine noleggio nel formato yyyy-MM-dd
     * @param email Email del cliente registrato
     * @param password Password del cliente registrato
     * @param targa Targa dello scooter da prenotare
     * @param model Oggetto Model di Spring per passare dati alla vista
     * @return Nome del template per la conferma della prenotazione
     *
     * @throws OperationException se il cliente non è registrato o lo scooter non esiste
     */
    @PostMapping("/prenotazione")
    public String selezioneAccessori(
            @RequestParam(required = false, name = "accessori") List<Integer> accessoriId,
            @RequestParam String dataRitiro,
            @RequestParam String dataConsegna,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String targa,
            Model model) {
        try {
            Prenotazione prenotazione = gestioneSistema.selezioneAccessori(
                    accessoriId, dataRitiro, dataConsegna, email, password, targa
            );

            model.addAttribute("prenotazione", prenotazione);

            return "conferma-prenotazione";
        } catch (OperationException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * Endpoint per la conferma definitiva della prenotazione.
     *
     * <p>Salva permanentemente nel database la prenotazione precedentemente
     * creata e tutti gli accessori associati. Dopo il salvataggio, la
     * prenotazione diventa effettiva e lo scooter risulta occupato per
     * il periodo specificato.</p>
     *
     * <p><strong>URL Pattern:</strong> GET /confermaPrenotazione</p>
     *
     * @param model Oggetto Model di Spring per passare dati alla vista
     * @return Nome del template di successo o errore
     *
     * @throws OperationException se si verifica un errore durante il salvataggio
     */
    @GetMapping("/confermaPrenotazione")
    public String confermaPrenotazione(Model model) {
        try {
            String messaggio = gestioneSistema.savePrenotazione();

            model.addAttribute("success", messaggio);

            return "prenotazione-successo"; // nome della pagina di esito/successo che vuoi mostrare
        } catch (OperationException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // nome della pagina di errore
        }
    }

    /**
     * Endpoint per l'annullamento della prenotazione corrente.
     *
     * <p>Annulla la prenotazione temporanea attualmente in corso senza
     * salvarla nel database. Questo libera immediatamente lo scooter e
     * rimuove dalla memoria tutte le informazioni della prenotazione.</p>
     *
     * <p><strong>Nota:</strong> Questo endpoint può essere utilizzato solo
     * per prenotazioni non ancora confermate.</p>
     *
     * <p><strong>URL Pattern:</strong> GET /annullaPrenotazione</p>
     *
     * @param model Oggetto Model di Spring per passare dati alla vista
     * @return Nome del template di conferma annullamento o errore
     *
     * @throws OperationException se non esiste una prenotazione da annullare
     */
    @GetMapping("/annullaPrenotazione")
    public String annullaPrenotazione(Model model) {
        try {
            String messaggio = gestioneSistema.annullaPrenotazione();

            model.addAttribute("message", messaggio);

            return "prenotazione-annullata";
        } catch (OperationException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}

