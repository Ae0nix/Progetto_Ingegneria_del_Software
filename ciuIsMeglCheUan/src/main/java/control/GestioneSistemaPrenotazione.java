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

/**
 * Classe controller principale per la gestione del sistema di prenotazione scooter.
 *
 * <p>Questa classe implementa il pattern Singleton e fornisce tutte le funzionalità
 * necessarie per gestire le prenotazioni di scooter nel sistema TouristScooters.
 * Include operazioni per la ricerca scooter, gestione delle prenotazioni, calcolo
 * dei costi e validazione dei dati.</p>
 *
 * <p>Le principali responsabilità includono:</p>
 * <ul>
 *   <li>Ricerca scooter disponibili per località e date</li>
 *   <li>Gestione del ciclo di vita delle prenotazioni</li>
 *   <li>Calcolo automatico dei costi basato su stagionalità e accessori</li>
 *   <li>Validazione delle date, targhe e località</li>
 *   <li>Autenticazione degli utenti</li>
 * </ul>
 *
 * @author Team TouristScooters
 * @version 1.0
 * @since 2025
 *
 * @see Prenotazione
 * @see Scooter
 * @see ClienteRegistrato
 */
public class GestioneSistemaPrenotazione {

    private static GestioneSistemaPrenotazione gsp = null;

    private final PrenotazioneControl prenotazioneControl;


    /**
     * Costruttore protetto per implementare il pattern Singleton.
     * Inizializza il controller interno per la gestione delle prenotazioni.
     */
    protected GestioneSistemaPrenotazione(){
        this.prenotazioneControl = new PrenotazioneControl();
    }

    /**
     * Classe interna per la gestione delle prenotazioni in corso.
     *
     * <p>Mantiene lo stato della prenotazione corrente durante il processo
     * di creazione e fornisce metodi per la sua gestione.</p>
     */
    private class PrenotazioneControl{

        private Prenotazione prenotazioneInCorso;


        /**
         * Crea una nuova prenotazione con i parametri specificati.
         *
         * @param dataRitiro Data di ritiro nel formato yyyy-MM-dd
         * @param dataConsegna Data di consegna nel formato yyyy-MM-dd
         * @param clienteRegistrato Cliente che effettua la prenotazione
         * @param scooter Scooter da prenotare
         */
        private void creaPrenotazione(String dataRitiro, String dataConsegna, ClienteRegistrato clienteRegistrato, Scooter scooter){
            this.prenotazioneInCorso = new Prenotazione(dataRitiro, dataConsegna, clienteRegistrato, scooter);
        }


        /**
         * Annulla e distrugge la prenotazione corrente.
         *
         * @return Messaggio di conferma dell'annullamento
         * @throws OperationException se non esiste una prenotazione da annullare
         */
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


    /**
     * Visualizza le opzioni di prenotazione e i costi per uno scooter specifico.
     *
     * <p>Recupera le informazioni dello scooter, il prezzo base per la bassa stagione
     * e tutti gli accessori disponibili. Effettua anche la validazione della targa.</p>
     *
     * @param targaScooter La targa dello scooter per cui visualizzare le opzioni
     * @return Oggetto contenente scooter, prezzo base e lista accessori
     * @throws OperationException se la targa non è valida, lo scooter non viene trovato
     *                           o non ci sono accessori disponibili
     *
     * @see #isTargaValida(String)
     */
    public OpzioniPrenotazioneResult visualizzaOpzioniDiPrenotazioneCosti(String targaScooter) throws OperationException {
        isTargaValida(targaScooter);
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



    /**
     * Crea una prenotazione selezionando gli accessori desiderati e autenticando l'utente.
     *
     * <p>Questo metodo:</p>
     * <ol>
     *   <li>Valida la targa dello scooter e l'intervallo di date</li>
     *   <li>Verifica l'esistenza dello scooter</li>
     *   <li>Autentica il cliente con email e password</li>
     *   <li>Crea una nuova prenotazione</li>
     *   <li>Aggiunge gli accessori selezionati (se presenti)</li>
     *   <li>Calcola il costo totale includendo stagionalità</li>
     * </ol>
     *
     * @param accId Lista degli ID degli accessori selezionati (può essere vuota)
     * @param dataRitiro Data di ritiro nel formato yyyy-MM-dd
     * @param dataConsegna Data di consegna nel formato yyyy-MM-dd
     * @param email Email del cliente registrato
     * @param password Password del cliente per l'autenticazione
     * @param targa Targa dello scooter da prenotare
     * @return La prenotazione creata con il costo calcolato
     * @throws OperationException se la targa non è valida, le date non sono valide,
     *                           lo scooter non esiste, l'autenticazione fallisce
     *                           o un accessorio non viene trovato
     *
     * @see #isTargaValida(String)
     * @see #validaIntervalloDate(String, String)
     * @see #autenticazioneUtente(String, String)
     * @see #calcoloCostoPrenotazione(Prenotazione)
     */
    public Prenotazione selezioneAccessori(List<Integer> accId, String dataRitiro, String dataConsegna, String email, String password, String targa) throws OperationException {
        isTargaValida(targa);
        validaIntervalloDate(dataRitiro, dataConsegna);
        try {
            Scooter s=ScooterDAO.readScooter(targa);

            if(s==null) {
                throw new OperationException("Scooter non trovato");
            }

            ClienteRegistrato cr=autenticazioneUtente(email,password);

            this.prenotazioneControl.creaPrenotazione(dataRitiro,dataConsegna,cr,s);
            Prenotazione eP = this.prenotazioneControl.getPrenotazioneInCorso();

            if(!accId.isEmpty()) {
                List<Accessorio> acc=new ArrayList<>();
                for(int id:accId){
                    Accessorio a=AccessorioDAO.readAccessorio(id);
                    if (a != null) {
                        acc.add(a);
                    }else throw new OperationException("Accessorio non trovato");
                }
                eP.setAccessori(acc);
            }

            float costo=calcoloCostoPrenotazione(eP);
            eP.setCostoTotale(costo);

            return eP;
        } catch (DBConnectionException | DAOException e) {
            throw new OperationException(e.getMessage());
        }
    }


    /**
     * Autentica un utente verificando email e password.
     *
     * <p>Recupera il cliente dal database utilizzando l'email e verifica
     * che la password fornita corrisponda a quella memorizzata.</p>
     *
     * @param email Email del cliente da autenticare
     * @param password Password del cliente
     * @return Il cliente registrato se l'autenticazione è riuscita
     * @throws OperationException se il cliente non è registrato o le credenziali sono errate
     */
    public ClienteRegistrato autenticazioneUtente(String email, String password) throws OperationException {
        try{
        ClienteRegistrato cr=ClienteRegistratoDAO.readClienteRegistrato(email);

        if(cr==null) {
            throw new OperationException("È richiesta la registrazione per effettuare una prenotazione");
        }

        if(!cr.getPassword().equals(password)) {
            throw new OperationException("Credenziali errate");
        }
        return cr;
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
    }



    /**
     * Salva definitivamente la prenotazione corrente nel database.
     *
     * <p>Persiste la prenotazione e tutti gli accessori associati nelle
     * rispettive tabelle del database.</p>
     *
     * @return Messaggio di conferma del salvataggio
     * @throws OperationException se si verifica un errore durante il salvataggio
     */
    public String savePrenotazione() throws OperationException {
        try {
            Prenotazione eP=this.prenotazioneControl.getPrenotazioneInCorso();

            int idPrenotazione=PrenotazioneDAO.createPrenotazione(eP);
            List<Accessorio> acc=eP.getAccessori();

            for (Accessorio a:acc){
                PrenotazioneAccessorioDAO.createPrenotazioneAccessorio(idPrenotazione,a.getId());
            }

            return "La prenotazione è avvenuta con successo";
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
    }


    /**
     * Annulla la prenotazione corrente.
     *
     * @return Messaggio di conferma dell'annullamento
     * @throws OperationException se non esiste una prenotazione da annullare
     */
    public String annullaPrenotazione() throws OperationException {
        this.prenotazioneControl.destroyPrenotazione();
        return "La tua prenotazione è stata annullata";
    }

    /**
     * Ricerca gli scooter disponibili per una specifica località e periodo.
     *
     * <p>Effettua la validazione della località e delle date prima di procedere
     * con la ricerca nel database. Le date devono essere nel formato yyyy-MM-dd
     * e la data di ritiro deve essere precedente o uguale alla data di consegna.</p>
     *
     * @param localita La località dove cercare gli scooter disponibili
     * @param dataRitiro Data di inizio noleggio (formato yyyy-MM-dd)
     * @param dataConsegna Data di fine noleggio (formato yyyy-MM-dd)
     * @return Lista degli scooter disponibili per il periodo specificato
     * @throws OperationException se la località non è valida, le date non sono valide
     *                           o si verifica un errore nel database
     *
     * @see #isValidaLocalita(String)
     * @see #validaIntervalloDate(String, String)
     */
    public List<Scooter> ricercaScooter(String localita, String dataRitiro, String dataConsegna) throws OperationException {
        isValidaLocalita(localita);
        validaIntervalloDate(dataRitiro, dataConsegna);
        try {
            return ScooterDAO.readScooter(localita, dataRitiro, dataConsegna);
        } catch (DAOException | DBConnectionException e) {
            throw new OperationException(e.getMessage());
        }
    }


    /**
     * Calcola il costo totale di una prenotazione.
     *
     * <p>Il calcolo include:</p>
     * <ul>
     *   <li>Costo di tutti gli accessori selezionati</li>
     *   <li>Costo giornaliero dello scooter basato sulla stagionalità</li>
     *   <li>Numero di giorni di noleggio (inclusivo)</li>
     * </ul>
     *
     * <p>La stagionalità è determinata automaticamente:
     * alta stagione dal 1° giugno al 31 agosto.</p>
     *
     * @param prenotazione La prenotazione per cui calcolare il costo
     * @return Il costo totale della prenotazione
     *
     * @see #isAltaStagione(LocalDate)
     */
    private float calcoloCostoPrenotazione(Prenotazione prenotazione) {
        float costo = 0;
        List<Accessorio> acc = prenotazione.getAccessori();
        for (Accessorio a : acc) {
            costo += a.getPrezzo();
        }

        // Recupera scooter e date
        Scooter scooter = prenotazione.getScooter();

        // Il formato corretto è yyyy-MM-dd
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


    /**
     * Determina se una data ricade nel periodo di alta stagione.
     *
     * <p>L'alta stagione è definita dal 1° giugno al 31 agosto.</p>
     *
     * @param data La data da verificare
     * @return true se la data è in alta stagione, false altrimenti
     */
    private boolean isAltaStagione(LocalDate data) {
        // esempio: alta stagione da 1 giugno a 31 agosto
        MonthDay inizioAlta = MonthDay.of(6, 1);
        MonthDay fineAlta = MonthDay.of(8, 31);
        MonthDay giorno = MonthDay.from(data);

        return !giorno.isBefore(inizioAlta) && !giorno.isAfter(fineAlta);
    }


    /**
     * Valida l'intervallo di date per una prenotazione.
     *
     * <p>Verifica che:</p>
     * <ul>
     *   <li>Le date siano nel formato corretto (yyyy-MM-dd)</li>
     *   <li>La data di ritiro sia precedente o uguale alla data di consegna</li>
     * </ul>
     *
     * @param dataRitiro Data di ritiro da validare
     * @param dataConsegna Data di consegna da validare
     * @throws OperationException se le date non sono valide o l'intervallo è errato
     */
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
            throw new OperationException("La data di ritiro deve essere precedente o uguale alla data di consegna.");
        }
    }


    /**
     * Valida il formato di una targa italiana per scooter.
     *
     * <p>Verifica che la targa rispetti il formato standard italiano:
     * 2 lettere, 3 cifre, 2 lettere (es: AB123CD).
     * La validazione normalizza la targa rimuovendo spazi e trattini.</p>
     *
     * @param targa La targa da validare
     * @throws OperationException se la targa è null o non rispetta il formato corretto
     */
    private void isTargaValida(String targa) throws OperationException {
        if (targa == null) throw new OperationException("Targa non valida");

        // Rimuove spazi o trattini per semplificare la validazione
        String normalizzata = targa.replaceAll("[-\\s]", "").toUpperCase();

        // Regola generica: 2 lettere, 3 cifre, 2 lettere
        if(!normalizzata.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$")){
            throw new OperationException("Targa non valida");
        }
    }


    /**
     * Valida il formato e il contenuto di una località.
     *
     * <p>Verifica che la località:</p>
     * <ul>
     *   <li>Non sia null o vuota</li>
     *   <li>Non superi i 100 caratteri</li>
     *   <li>Non contenga numeri</li>
     *   <li>Contenga solo lettere (anche accentate), spazi e apostrofi</li>
     * </ul>
     *
     * @param localita La località da validare
     * @throws OperationException se la località non rispetta i criteri di validazione
     */
    public void isValidaLocalita(String localita) throws OperationException {
        if (localita == null || localita.trim().isEmpty()) {
            throw new OperationException("La località non può essere vuota.");
        }
        if (localita.length() > 100) {
            throw new OperationException("La località non può superare i 100 caratteri.");
        }
        if (localita.matches(".*\\d.*")) {
            throw new OperationException("La località non può contenere numeri.");
        }
        // Consenti lettere (anche accentate), spazi e apostrofi
        if (!localita.matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ' ]+$")) {
            throw new OperationException("La località può contenere solo lettere, spazi e apostrofi.");
        }
    }
}