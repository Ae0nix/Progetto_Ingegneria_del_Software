package control;

import entity.Agenzia;
import entity.Scooter;

public class GestioneSistemaPrenotazione {
    private static GestioneSistemaPrenotazione gsp = null;

    public static GestioneSistemaPrenotazione getInstance(){
        if (gsp==null)
            gsp=new GestioneSistemaPrenotazione();
        return gsp;
    }

    public int registraScooter(String targa, String cilindrata, String prezzoPerGiornoNoleggioAltaStagione, String prezzoPerGiornoNoleggioBassaStagione, String tipologia, Agenzia agenzia){
        if (!isTargaValida(targa)) {
            System.out.println("Targa non valida. Inserire una targa conforme al formato europeo (es. AA123BB)");
            return 1;
        }

        Scooter scooter = new Scooter(targa, cilindrata, prezzoPerGiornoNoleggioAltaStagione, prezzoPerGiornoNoleggioBassaStagione, tipologia, agenzia);

        Scooter scooter=new Scooter(targa, cilindrata, prezzoPerGiornoNoleggioAltaStagione, prezzoPerGiornoNoleggioBassaStagione, tipologia, agenzia);

        //registrazione su DB dello scooter attraverso il DAO e return 0 se non si sono verificati errori, 1 altrimenti
    }

    public int modificaStatoScooter(String targa, String stato){
        if(stato!="in-servizio" || stato!="dismesso") return 1;
        else{
            //modifica stato dello scooter sul database
        }
    }

    public int registrazioneCliente(String nome, String cognome, String dataDiNascita, String email, String password){

    }

    private static boolean isTargaValida(String targa) {
        if (targa == null) return false;

        // Rimuove spazi o trattini per semplificare la validazione
        String normalizzata = targa.replaceAll("[-\\s]", "").toUpperCase();

        // Regola generica: 2 lettere, 3 cifre, 2 lettere
        return normalizzata.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$");
    }

}
