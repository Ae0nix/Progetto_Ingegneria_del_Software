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
        return null;
    }
}

