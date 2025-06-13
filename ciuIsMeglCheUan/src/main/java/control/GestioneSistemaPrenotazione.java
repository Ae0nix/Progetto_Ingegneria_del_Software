package control;

import database.AccessorioDAO;
import database.ClienteRegistratoDAO;
import database.ScooterDAO;
import entity.*;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;
import util.OpzioniPrenotazioneResult;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class GestioneSistemaPrenotazione {
    private static GestioneSistemaPrenotazione gsp = null;

    public static GestioneSistemaPrenotazione getInstance(){
        if (gsp==null)
            gsp=new GestioneSistemaPrenotazione();
        return gsp;
    }

    /*
    public int registraScooter(String targa, int cilindrata, float prezzoPerGiornoNoleggioAltaStagione, float prezzoPerGiornoNoleggioBassaStagione, String tipologia, Agenzia agenzia){

        if (!isTargaValida(targa)) {
            System.out.println("Targa non valida. Inserire una targa conforme al formato europeo (es. AA123BB)");
            return 1;
        }


        //registrazione su DB dello scooter attraverso il DAO e return 0 se non si sono verificati errori, 1 altrimenti
    }

    public int modificaStatoScooter(String targaScooter, String stato){
        if(stato!="in-servizio" || stato!="dismesso") return 1;
        else{
            //modifica stato dello scooter sul database
        }

    }

    public int registrazioneCliente(String nome, String cognome, String dataDiNascita, String email, String password){

        ClienteRegistrato clienteRegistrato=new ClienteRegistrato(nome,cognome,dataDiNascita,email,password);

        //registrazione su DB del cliente
    }*/

    public OpzioniPrenotazioneResult visualizzaOpzioniDiPrenotazioneCosti(String targaScooter, String dataRitiro, String dataConsegna, String email) throws OperationException {

        try {
            Scooter s=ScooterDAO.readScooter(targaScooter);

            if(s==null) {
                throw new OperationException("Scooter non trovato");
            }

            float pbs=s.getPrezzoPerGiornoNoleggioBassaStagione();

            List<Accessorio> acc= null;
            acc = AccessorioDAO.readAccessorio();

            if (acc.isEmpty()) {
                throw new OperationException("Nessun accessorio trovato");
            }

            ClienteRegistrato cr=ClienteRegistratoDAO.readClienteRegistrato(email);

            Prenotazione eP=new Prenotazione(dataRitiro,dataConsegna,cr,s);

            return new OpzioniPrenotazioneResult(acc,pbs,eP);

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
    }

    public Prenotazione selezioneAccessori(List<Accessorio> acc, Prenotazione prenotazione) throws OperationException {
        prenotazione.setAccessori(acc);
        float costo=calcoloCostoPrenotazione(prenotazione);
        prenotazione.setCostoTotale(costo);
        return prenotazione;
    }
/*
    public int prenotazioneScooter(String targaScooter, String dataRitiro, String dataConsegna, String email){
        //Accessorio ac=AccessorioDAO.readAccessorio();

        ClienteRegistrato cr=ClienteRegistratoDAO.readClienteRegistrato(email).getId();

        //is this necessary? If so, we need to change sd PrenotazioneScooter
        Scooter s= ScooterDAO.readScooter(targaScooter);

        Prenotazione eP=new Prenotazione(dataRitiro,dataConsegna,cr,s);


    }
*/
    private float calcoloCostoPrenotazione(Prenotazione prenotazione) {
        float costo=0;
        List<Accessorio> acc=prenotazione.getAccessori();
        for(Accessorio a:acc){
            costo+=a.getPrezzo();
        }

        // Recupera scooter e date
        Scooter scooter = prenotazione.getScooter();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

    private static boolean isTargaValida(String targa) {
        if (targa == null) return false;

        // Rimuove spazi o trattini per semplificare la validazione
        String normalizzata = targa.replaceAll("[-\\s]", "").toUpperCase();

        // Regola generica: 2 lettere, 3 cifre, 2 lettere
        return normalizzata.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$");
    }

}
