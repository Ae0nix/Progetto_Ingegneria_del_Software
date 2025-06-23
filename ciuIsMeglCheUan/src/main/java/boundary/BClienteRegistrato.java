package boundary;

import entity.Accessorio;
import entity.Prenotazione;
import exception.OperationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import control.GestioneSistemaPrenotazione;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BClienteRegistrato {

    private final GestioneSistemaPrenotazione gestioneSistema;

    public BClienteRegistrato() {
        this.gestioneSistema = GestioneSistemaPrenotazione.getInstance();
    }

    @PostMapping("/selezioneAccessori")
    public String selezionaAccessori(
            @RequestParam(required = false, name = "accessori") List<Integer> accessoriId,
            @RequestParam String dataRitiro,
            @RequestParam String dataConsegna,
            @RequestParam String email,
            @RequestParam String targa,
            Model model) {
        try {
            // Chiama la funzione del controller di dominio (come da tua richiesta iniziale)
            Prenotazione prenotazione = gestioneSistema.selezioneAccessori(
                    accessoriId, dataRitiro, dataConsegna, email, targa
            );

            // A questo punto puoi aggiungere la prenotazione al model e andare a una pagina di conferma
            model.addAttribute("prenotazione", prenotazione);



            return "conferma-prenotazione";
        } catch (OperationException e) {
            model.addAttribute("error", e.getMessage());
            return "";
        }
    }

/*
    @GetMapping("/confermaPrenotazione")
    public String confermaPrenotazione(Model model) {


    }
*/
}

