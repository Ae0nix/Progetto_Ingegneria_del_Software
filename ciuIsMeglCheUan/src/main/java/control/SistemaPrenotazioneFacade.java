package control;

import entity.Prenotazione;
import exception.OperationException;

public class SistemaPrenotazioneFacade {

    private GestioneSistemaPrenotazione controller = GestioneSistemaPrenotazione.getInstance();

    public Prenotazione prenotazioneScooter(String targaScooter, String dataRitiro, String dataConsegna, String email) throws OperationException {
        controller.visualizzaOpzioniDiPrenotazioneCosti(targaScooter);



        Prenotazione p=null;
        return p;
    }
}