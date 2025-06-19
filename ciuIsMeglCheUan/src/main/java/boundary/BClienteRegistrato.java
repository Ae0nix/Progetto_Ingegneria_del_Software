package boundary;

import org.springframework.web.bind.annotation.*;
import control.GestioneSistemaPrenotazione;

@RestController
public class BClienteRegistrato {

    private final GestioneSistemaPrenotazione sistema = GestioneSistemaPrenotazione.getInstance();


}

