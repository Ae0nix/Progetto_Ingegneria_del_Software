package application.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RicercaScooterFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

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

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());

        String body = response.getBody();
        assertThat(body).doesNotContain("non sono stati trovati scooter disponibili");
        assertThat(body).contains("Napoli");
    }


    @Test
    void functionalTestRicercaScooterLocalitàVuotaNulla() {
        /* Test Case ID: 2
           Descrizione: Stringa località vuota o nulla
           Input: {Località: "", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: stringa località non specificata
        */

        String localita = "";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("località non specificata") ||
                        body.toLowerCase().contains("località non può essere vuota")
        ).isTrue();
        assertThat(body).contains("Error");
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

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("località non valida") ||
                        body.toLowerCase().contains("può contenere solo lettere")
        ).isTrue();
        assertThat(body).contains("Error");
    }

    @Test
    void functionalTestRicercaScooterStringaLocalitaConNumeri() {
        /* Test Case ID: 4
           Descrizione: Stringa località con numeri
           Input: {Località: "N4p0l1", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: stringa località non può contenere numeri
        */

        String localita = "N4p0l1";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("località non valida") ||
                        body.toLowerCase().contains("non può contenere numeri")
        ).isTrue();
        assertThat(body).contains("Error");
    }

    @Test
    void functionalTestRicercaScooterStringaLocalitaLunghezzaMaggiore100Caratteri() {
        /* Test Case ID: 5
           Descrizione: Stringa località di lunghezza > 100 caratteri
           Input: {Località: "aaaaaaaa... (101 caratteri)", DataRitiro: "2025-06-07", DataConsegna: "2025-06-10"}
           Output attesi: Error: stringa località non può superare i 100 caratteri
        */

        String localita = "a".repeat(101);
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-10";

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("località non valida") ||
                        body.toLowerCase().contains("non può superare i 100 caratteri")
        ).isTrue();
        assertThat(body).contains("Error");
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

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("dataritiro in formato non valido") ||
                        body.toLowerCase().contains("formato data non valido")
        ).isTrue();
        assertThat(body).contains("Error");
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

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("dataconsegna in formato non valido") ||
                        body.toLowerCase().contains("formato data non valido")
        ).isTrue();
        assertThat(body).contains("Error");
    }

    @Test
    void functionalTestRicercaScooterDataConsegnaPrecedenteADataRitiro() {
        /* Test Case ID: 8
           Descrizione: Data in formato valido (aaaa-mm-gg), ma DataConsegna precedente a DataRitiro
           Input: {Località: "Napoli", DataRitiro: "2025-06-07", DataConsegna: "2025-06-05"}
           Output attesi: Error: DataConsegna precedente a DataRitiro
        */

        String localita = "Napoli";
        String dataRitiro = "2025-06-07";
        String dataConsegna = "2025-06-05";

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("dataconsegna precedente a dataritiro") ||
                        body.toLowerCase().contains("data di ritiro deve essere precedente")
        ).isTrue();
        assertThat(body).contains("Error");
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

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("data ritiro non specificata") ||
                        body.toLowerCase().contains("formato data non valido")
        ).isTrue();
        assertThat(body).contains("Error");
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

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("data consegna non specificata") ||
                        body.toLowerCase().contains("formato data non valido")
        ).isTrue();
        assertThat(body).contains("Error");
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

        String url = "/scooters?localita=" + localita
                + "&dataRitiro=" + dataRitiro
                + "&dataConsegna=" + dataConsegna;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        String body = response.getBody();
        Assertions.assertNotNull(body);
        assertThat(
                body.toLowerCase().contains("non sono stati trovati scooter disponibili") ||
                        body.toLowerCase().contains("criteri di ricerca selezionati")
        ).isTrue();
    }
}