package boundary;

import entity.Prenotazione;
import exception.OperationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import control.GestioneSistemaPrenotazione;

import java.util.List;

@Controller
public class BClienteRegistrato {

    private final GestioneSistemaPrenotazione gestioneSistema;

    public BClienteRegistrato() {
        this.gestioneSistema = GestioneSistemaPrenotazione.getInstance();
    }

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

