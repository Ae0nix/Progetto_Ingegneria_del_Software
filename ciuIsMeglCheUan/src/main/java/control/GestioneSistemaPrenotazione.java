package control;

import database.*;
import entity.*;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;
import util.OpzioniPrenotazioneResult;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class GestioneSistemaPrenotazione {
    private static GestioneSistemaPrenotazione gsp = null;
    private final PrenotazioneControl prenotazioneControl;

    protected GestioneSistemaPrenotazione(){
        this.prenotazioneControl = new PrenotazioneControl();
    }

    private class PrenotazioneControl{

        private Prenotazione prenotazioneInCorso;

        private void creaPrenotazione(String dataRitiro, String dataConsegna, ClienteRegistrato clienteRegistrato, Scooter scooter){
            this.prenotazioneInCorso = new Prenotazione(dataRitiro, dataConsegna, clienteRegistrato, scooter);
        }

        public String destroyPrenotazione() throws OperationException {
            if (prenotazioneInCorso == null) {
                throw new OperationException("Nessuna prenotazione da annullare.");
            }

            prenotazioneInCorso = null;        // distruzione vera (gestita nel Control)

            return "Prenotazione annullata.";
        }

        public Prenotazione getPrenotazioneInCorso() {
            return prenotazioneInCorso;
        }
    }

    public static GestioneSistemaPrenotazione getInstance(){
        if (gsp==null)
            gsp=new GestioneSistemaPrenotazione();
        return gsp;
    }

    public OpzioniPrenotazioneResult visualizzaOpzioniDiPrenotazioneCosti(String targaScooter) throws OperationException {
        try {
            Scooter s=ScooterDAO.readScooter(targaScooter);

            if(s==null) {
                throw new OperationException("Scooter non trovato");
            }

            float pbs=s.getPrezzoPerGiornoNoleggioBassaStagione();

            List<Accessorio> acc = AccessorioDAO.readAccessorio();

            if (acc.isEmpty()) {
                throw new OperationException("Nessun accessorio trovato");
            }

            return new OpzioniPrenotazioneResult(acc,pbs,s);

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
    }

    public Prenotazione selezioneAccessori(List<Integer> accId, String dataRitiro, String dataConsegna, String email, String targa) throws OperationException {
        try {
            Scooter s=ScooterDAO.readScooter(targa);

            if(s==null) {
                throw new OperationException("Scooter non trovato");
            }

            ClienteRegistrato cr=ClienteRegistratoDAO.readClienteRegistrato(email);

            if(cr==null) {
                throw new OperationException("È richiesta la registrazione per effettuare una prenotazione");
            }

            this.prenotazioneControl.creaPrenotazione(dataRitiro,dataConsegna,cr,s);
            Prenotazione eP = this.prenotazioneControl.getPrenotazioneInCorso();

            List<Accessorio> acc=new ArrayList<>();
            for(int id:accId){
                Accessorio a=AccessorioDAO.readAccessorio(id);
                if (a != null) {
                    acc.add(a);
                }
            }

            eP.setAccessori(acc);
            float costo=calcoloCostoPrenotazione(eP);
            eP.setCostoTotale(costo);

            return eP;
        } catch (DBConnectionException | DAOException e) {
            throw new OperationException(e.getMessage());
        }
    }

    public String savePrenotazione() throws OperationException {
        try {
            Prenotazione eP=this.prenotazioneControl.getPrenotazioneInCorso();
            PrenotazioneDAO.createPrenotazione(eP);

            int idPrenotazione=eP.getId();
            List<Accessorio> acc=eP.getAccessori();

            for (Accessorio a:acc){
                PrenotazioneAccessorioDAO.createPrenotazioneAccessorio(idPrenotazione,a.getId());
            }

            return "La prenotazione è avvenuta con successo";
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
    }

    public void annullaPrenotazione() throws OperationException {
        this.prenotazioneControl.destroyPrenotazione();
    }

    public List<Scooter> ricercaScooter(String localita, String dataRitiro, String dataConsegna) throws OperationException {
        validaIntervalloDate(dataRitiro, dataConsegna);
        try {
            return ScooterDAO.readScooter(localita, dataRitiro, dataConsegna);
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
    }

    private float calcoloCostoPrenotazione(Prenotazione prenotazione) {
        float costo = 0;
        List<Accessorio> acc = prenotazione.getAccessori();
        for (Accessorio a : acc) {
            costo += a.getPrezzo();
        }

        // Recupera scooter e date
        Scooter scooter = prenotazione.getScooter();

        // CORRETTO formato yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ritiro = LocalDate.parse(prenotazione.getDataRitiro(), formatter);
        LocalDate consegna = LocalDate.parse(prenotazione.getDataConsegna(), formatter);

        // Calcola numero di giorni di noleggio (inclusivo)
        long giorniNoleggio = ChronoUnit.DAYS.between(ritiro, consegna) + 1;

        // Calcola costo noleggio giorno per giorno, considerando alta o bassa stagione
        for (int i = 0; i < giorniNoleggio; i++) {
            LocalDate giorno = ritiro.plusDays(i);
            if (isAltaStagione(giorno)) {
                costo += scooter.getPrezzoPerGiornoNoleggioAltaStagione();
            } else {
                costo += scooter.getPrezzoPerGiornoNoleggioBassaStagione();
            }
        }

        return costo;
    }

    private boolean isAltaStagione(LocalDate data) {
        // esempio: alta stagione da 1 giugno a 31 agosto
        MonthDay inizioAlta = MonthDay.of(6, 1);
        MonthDay fineAlta = MonthDay.of(8, 31);
        MonthDay giorno = MonthDay.from(data);

        return !giorno.isBefore(inizioAlta) && !giorno.isAfter(fineAlta);
    }

    private void validaIntervalloDate(String dataRitiro, String dataConsegna) throws OperationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ritiro, consegna;
        try {
            ritiro = LocalDate.parse(dataRitiro, formatter);
            consegna = LocalDate.parse(dataConsegna, formatter);
        } catch (Exception e) {
            throw new OperationException("Formato data non valido (deve essere yyyy-MM-dd).");
        }
        if (ritiro.isAfter(consegna)) {
            throw new OperationException("la data di ritiro deve essere precedente o uguale alla data di consegna.");
        }
    }

}
