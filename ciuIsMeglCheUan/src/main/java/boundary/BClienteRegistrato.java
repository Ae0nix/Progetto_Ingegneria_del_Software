package boundary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import control.GestioneSistemaPrenotazione;
import entity.Accessorio;
import entity.Prenotazione;
import exception.OperationException;
import util.OpzioniPrenotazioneResult;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BClienteRegistrato {

    private final GestioneSistemaPrenotazione sistema = GestioneSistemaPrenotazione.getInstance();

    @PostMapping("/prenotaScooter")
    public ResponseEntity<?> prenotaScooter(
            @RequestParam String targa,
            @RequestParam String dataRitiro,
            @RequestParam String dataConsegna,
            @RequestParam String email,
            @RequestParam List<Integer> accessoriIds
    ) {
        try {
            OpzioniPrenotazioneResult opzioni = sistema.visualizzaOpzioniDiPrenotazioneCosti(
                    targa, dataRitiro, dataConsegna, email
            );

            List<Accessorio> tuttiAccessori = opzioni.getAccessori();
            List<Accessorio> selezionati = tuttiAccessori.stream()
                    .filter(a -> accessoriIds.contains(a.getId()))
                    .collect(Collectors.toList());

            Prenotazione prenotazione = sistema.selezioneAccessori(selezionati, opzioni.getPrenotazione());
            Prenotazione confermata = sistema.savePrenotazione(prenotazione);

            return ResponseEntity.ok(confermata);

        } catch (OperationException e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}

