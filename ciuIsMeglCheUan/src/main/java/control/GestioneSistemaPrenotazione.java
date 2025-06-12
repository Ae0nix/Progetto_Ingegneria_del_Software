package control;

import database.AccessorioDAO;
import database.ClienteRegistratoDAO;
import database.ScooterDAO;
import entity.*;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;
import util.OpzioniPrenotazioneResult;

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

    public OpzioniPrenotazioneResult visualizzaOpzioniDIPrenotazioneCosti(String targaScooter) throws OperationException {

        try {
            Scooter s=ScooterDAO.readScooter(targaScooter);

            if(s==null) {
                throw new OperationException("Proiezione non trovata");
            }

            float pbs=s.getPrezzoPerGiornoNoleggioBassaStagione();

            List<Accessorio> acc= null;
            acc = AccessorioDAO.readAccessorio();

            if (acc.isEmpty()) {
                throw new OperationException("Nessun accessorio trovato");
            }

            return new OpzioniPrenotazioneResult(acc,pbs);

        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
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
    private static boolean isTargaValida(String targa) {
        if (targa == null) return false;

        // Rimuove spazi o trattini per semplificare la validazione
        String normalizzata = targa.replaceAll("[-\\s]", "").toUpperCase();

        // Regola generica: 2 lettere, 3 cifre, 2 lettere
        return normalizzata.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$");
    }

}
