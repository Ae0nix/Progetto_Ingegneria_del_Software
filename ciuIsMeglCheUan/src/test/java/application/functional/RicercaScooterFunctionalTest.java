package application.functional;

import control.GestioneSistemaPrenotazione;
import entity.Scooter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RicercaScooterFunctionalTest {

    private final GestioneSistemaPrenotazione gsp=GestioneSistemaPrenotazione.getInstance();

    @Test
    void functionalTestRicercaScooterInputValidi() {
        /* Test Case ID: 1
           Descrizione: Tutti input validi
           Classi di equivalenza: Località valida, DataRitiro valida, DataConsegna valida, ElementoDiSistemaDatabase valido
           Input: {Località: "Napoli", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Scooter disponibili
        */

        String localita = "Napoli";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        assertDoesNotThrow(() -> {
            List<Scooter> scooterList = gsp.ricercaScooter(localita, dataRitiro, dataConsegna);
            assertNotNull(scooterList);
            assertFalse(scooterList.isEmpty(), "Il sistema deve mostrare gli scooter disponibili al cliente");
        });
    }

    @Test
    void functionalTestRicercaScooterLocalitàVuotaNulla() {
        /* Test Case ID: 2
           Descrizione: Stringa località vuota o nulla
           Input: {Località: "", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: stringa località non valida
        */

        String localita = "";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("località") && ex.getMessage().contains("non valida") ||
                ex.getMessage().contains("località non può essere vuota"));
    }

    @Test
    void functionalTestRicercaScooterStringaLocalitaConCaratteriSpeciali() {
        /* Test Case ID: 3
           Descrizione: Stringa località con caratteri speciali
           Input: {Località: "Napol!", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: stringa località non valida
        */

        String localita = "Napol!";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("località") && ex.getMessage().contains("non valida") ||
                ex.getMessage().contains("può contenere solo lettere"));
    }

    @Test
    void functionalTestRicercaScooterStringaLocalitaConNumeri() {
        /* Test Case ID: 4
           Descrizione: Stringa località con numeri
           Input: {Località: "N4p0l1", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: stringa località non valida
        */

        String localita = "N4p0l1";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("località") && ex.getMessage().contains("non valida") ||
                ex.getMessage().contains("non può contenere numeri"));
    }

    @Test
    void functionalTestRicercaScooterStringaLocalitaLunghezzaMaggiore100Caratteri() {
        /* Test Case ID: 5
           Descrizione: Stringa località di lunghezza > 100 caratteri
           Input: {Località: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa...", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: stringa località non valida
        */

        String localita = "a".repeat(101);
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("località") && ex.getMessage().contains("non valida") ||
                ex.getMessage().contains("non può superare i 100 caratteri"));
    }

    @Test
    void functionalTestRicercaScooterDataRitiroFormatoNonValido() {
        /* Test Case ID: 6
           Descrizione: DataRitiro con formato non valido
           Input: {Località: "Napoli", DataRitiro: "07/06/2025", DataConsegna: "2025-06-10"}
           Output attesi: Error: DataRitiro in formato non valido
        */

        String localita = "Napoli";
        String dataRitiro = "07/06/2025";
        String dataConsegna = "2025-06-10";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("DataRitiro") && ex.getMessage().contains("formato non valido") ||
                ex.getMessage().contains("Formato data non valido"));
    }

    @Test
    void functionalTestRicercaScooterDataConsegnaFormatoNonValido() {
        /* Test Case ID: 7
           Descrizione: DataConsegna con formato non valido
           Input: {Località: "Napoli", DataRitiro: "2025-06-07", DataConsegna: "2025/06/2025"}
           Output attesi: Error: DataConsegna in formato non valido
        */

        String localita = "Napoli";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025/06/2025";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("DataConsegna") && ex.getMessage().contains("formato non valido") ||
                ex.getMessage().contains("Formato data non valido"));
    }

    @Test
    void functionalTestRicercaScooterDataConsegnaPrecedenteADataRitiro() {
        /* Test Case ID: 8
           Descrizione: Data in formato valido (gg/mm/aaaa), precedente a DataRitiro
           Input: {Località: "Napoli", DataRitiro: "2025-06-07", DataConsegna: "2025-06-05"}
           Output attesi: Error: DataConsegna precedente a DataRitiro
        */

        String localita = "Napoli";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-05";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("DataConsegna precedente a DataRitiro") ||
                ex.getMessage().contains("data di ritiro deve essere precedente"));
    }

    @Test
    void functionalTestRicercaScooterDataRitiroMancante() {
        /* Test Case ID: 9
           Descrizione: DataRitiro mancante
           Input: {Località: "Napoli", DataRitiro: "", DataConsegna: "2025-06-05"}
           Output attesi: Error: DataRitiro mancante
        */

        String localita = "Napoli";
        String dataRitiro = "";
        String dataConsegna = "2025-06-05";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("DataRitiro mancante") ||
                ex.getMessage().contains("Formato data non valido"));
    }

    @Test
    void functionalTestRicercaScooterDataConsegnaMancante() {
        /* Test Case ID: 10
           Descrizione: DataConsegna mancante
           Input: {Località: "Napoli", DataRitiro: "2025-06-07", DataConsegna: ""}
           Output attesi: Error: DataConsegna mancante
        */

        String localita = "Napoli";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "";

        Exception ex = assertThrows(Exception.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertTrue(ex.getMessage().contains("DataConsegna mancante") ||
                ex.getMessage().contains("Formato data non valido"));
    }

    @Test
    void functionalTestRicercaScooterNessunaAgenziaPerQuellaLocalita() {
        /* Test Case ID: 11
           Descrizione: Nessuna agenzia per quella località
           Input: {Località: "Foggia", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: non sono stati trovati scooter disponibili per i criteri di ricerca selezionati
        */

        String localita = "Foggia";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        assertDoesNotThrow(() -> {
            List<Scooter> scooterList = gsp.ricercaScooter(localita, dataRitiro, dataConsegna);
            assertNotNull(scooterList);
            assertTrue(scooterList.isEmpty(),
                    "Error: non sono stati trovati scooter disponibili per i criteri di ricerca selezionati");
        });
    }
}
