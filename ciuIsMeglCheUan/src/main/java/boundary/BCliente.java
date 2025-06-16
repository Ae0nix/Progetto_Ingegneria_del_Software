package boundary;

import control.GestioneSistemaPrenotazione;
import entity.Scooter;
import exception.OperationException;
import util.OpzioniScooterResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BCliente {

    private final GestioneSistemaPrenotazione gestioneSistema;

    public BCliente() {
        this.gestioneSistema = GestioneSistemaPrenotazione.getInstance();
    }

    /**
     * Endpoint per la ricerca degli scooter disponibili
     * GET /api/cliente/ricerca?localita=Roma&dataRitiro=01/06/2024&dataConsegna=05/06/2024
     */
    @GetMapping("/ricerca")
    public ResponseEntity<List<Scooter>> ricercaScooter(
            @RequestParam("localita") String localita,
            @RequestParam("dataRitiro") String dataRitiro,
            @RequestParam("dataConsegna") String dataConsegna) {

        try {
            // Validazione parametri
            if (localita == null || localita.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            if (dataRitiro == null || dataRitiro.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            if (dataConsegna == null || dataConsegna.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Chiamata al sistema di controllo
            List<Scooter> scooterDisponibili = gestioneSistema.ricercaScooter(
                    localita.trim(),
                    dataRitiro.trim(),
                    dataConsegna.trim()
            );

            return ResponseEntity.ok(scooterDisponibili);

        } catch (OperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint per la selezione dello scooter e visualizzazione opzioni
     * GET /api/cliente/seleziona-scooter?targa=AB123CD
     */
    @GetMapping("/selezionaScooter")
    public ResponseEntity<OpzioniScooterResult> selezionaScooter(@RequestParam("targa") String targa) {

        try {
            // Validazione della targa
            if (targa == null || targa.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Chiamata al sistema di controllo
            OpzioniScooterResult opzioni = gestioneSistema.selezionaScooter(targa.trim());

            return ResponseEntity.ok(opzioni);

        } catch (OperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}