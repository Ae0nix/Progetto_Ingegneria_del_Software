package application.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Functional tests for the boundary BClienteRegistrato (selezione accessori/prenotazione).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SelezioneAccessoriFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void functionalTestSelezioneAccessoriInputValidi() {
        /* Test Case ID: 1
         * Descrizione: Tutti input validi
         * Input: Targa, DataRitiro, DataConsegna, Accessori, Email, Password - tutti validi
         * Output atteso: Prenotazione completata con successo (view: "conferma-prenotazione")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("conferma-prenotazione").doesNotContain("error");
    }

    @Test
    void functionalTestSelezioneAccessoriTargaVuota() {
        /* Test Case ID: 2
         * Descrizione: Stringa targa vuota o nulla
         * Input: Targa vuota, altri parametri validi
         * Output atteso: Messaggio di errore relativo alla targa non valida (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("targa").contains("non valida");
    }

    @Test
    void functionalTestSelezioneAccessoriTargaFormatoErrato() {
        /* Test Case ID: 3
         * Descrizione: Stringa targa con formato errato
         * Input: Targa con formato errato, altri parametri validi
         * Output atteso: Messaggio di errore relativo alla targa non valida (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "123-ABC");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("targa").contains("non valida");
    }

    @Test
    void functionalTestSelezioneAccessoriDataRitiroFormatoErrato() {
        /* Test Case ID: 4
         * Descrizione: DataRitiro con formato non valido
         * Input: DataRitiro in formato errato, altri parametri validi
         * Output atteso: Messaggio di errore relativo al formato della data di ritiro (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "01-06-2025");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("formato data").contains("non valido");
    }

    @Test
    void functionalTestSelezioneAccessoriDataRitiroMancante() {
        /* Test Case ID: 5
         * Descrizione: DataRitiro mancante
         * Input: DataRitiro mancante, altri parametri validi
         * Output atteso: Messaggio di errore relativo a DataRitiro mancante (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("formato data non valido");
    }

    @Test
    void functionalTestSelezioneAccessoriDataConsegnaPrecedenteDataRitiro() {
        /* Test Case ID: 6
         * Descrizione: DataConsegna precedente a DataRitiro
         * Input: DataConsegna precedente a DataRitiro, altri parametri validi
         * Output atteso: Messaggio di errore relativo all'ordine delle date (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-11");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("error").contains("data di consegna").contains("precedente");
    }

    @Test
    void functionalTestSelezioneAccessoriDataConsegnaFormatoErrato() {
        /* Test Case ID: 7
         * Descrizione: DataConsegna con formato non valido
         * Input: DataConsegna in formato errato, altri parametri validi
         * Output atteso: Messaggio di errore relativo al formato della data di consegna (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-110");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("formato data non valido");
    }

    @Test
    void functionalTestSelezioneAccessoriDataConsegnaMancante() {
        /* Test Case ID: 8
         * Descrizione: DataConsegna mancante
         * Input: DataConsegna mancante, altri parametri validi
         * Output atteso: Messaggio di errore relativo a DataConsegna mancante (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("formato data non valido");
    }

    @Test
    void functionalTestSelezioneAccessoriListaAccessoriVuota() {
        /* Test Case ID: 9
         * Descrizione: Lista accessori vuota
         * Input: Accessori non specificati, altri parametri validi
         * Output atteso: Prenotazione completata con successo (view: "conferma-prenotazione")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("conferma-prenotazione");
    }

    @Test
    void functionalTestSelezioneAccessoriListaConCaratteriNonNumerici() {
        /* Test Case ID: 10
         * Descrizione: Lista con caratteri non numerici
         * Input: Accessori con valori non numerici, altri parametri validi
         * Output atteso: Messaggio di errore sulla lista accessori (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "a");
        params.add("accessori", "b");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void functionalTestSelezioneAccessoriEmailFormatoErrato() {
        /* Test Case ID: 11
         * Descrizione: Stringa email con formato non valido
         * Input: Email non valida, altri parametri validi
         * Output atteso: Messaggio di errore sul formato email (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrariemail.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("richiesta la registrazione");
    }

    @Test
    void functionalTestSelezioneAccessoriEmailVuota() {
        /* Test Case ID: 12
         * Descrizione: Stringa email vuota o nulla
         * Input: Email vuota, altri parametri validi
         * Output atteso: Messaggio di errore su email mancante (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "");
        params.add("password", "CorrectPwd123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("richiesta la registrazione");
    }

    @Test
    void functionalTestSelezioneAccessoriPasswordVuota() {
        /* Test Case ID: 13
         * Descrizione: Stringa password vuota o nulla
         * Input: Password vuota, altri parametri validi
         * Output atteso: Messaggio di errore su password mancante (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "user@example.com");
        params.add("password", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("richiesta la registrazione");
    }

    @Test
    void functionalTestSelezioneAccessoriPasswordNonCorretta() {
        /* Test Case ID: 14
         * Descrizione: Stringa password non corretta per l’email fornita
         * Input: Password errata, altri parametri validi
         * Output atteso: Messaggio di errore su credenziali errate (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "user@example.com");
        params.add("password", "IncorrectPwd123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("richiesta la registrazione");
    }

    @Test
    void functionalTestSelezioneAccessoriTargaNonPresente() {
        /* Test Case ID: 15
         * Descrizione: Targa non presente nel sistema
         * Input: Targa non registrata, altri parametri validi
         * Output atteso: Messaggio di errore su targa non presente (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "AB405AB");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "1");
        params.add("accessori", "2");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("scooter").contains("non trovato");
    }

    @Test
    void functionalTestSelezioneAccessoriAccessoriNonEsistenti() {
        /* Test Case ID: 16
         * Descrizione: Uno o più accessori non esistono nel sistema
         * Input: Accessori inesistenti, altri parametri validi
         * Output atteso: Messaggio di errore su accessorio non presente (view: "error")
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targa", "KL890MN");
        params.add("dataRitiro", "2025-06-01");
        params.add("dataConsegna", "2025-06-10");
        params.add("accessori", "9999");
        params.add("accessori", "10000");
        params.add("email", "luca.ferrari@email.it");
        params.add("password", "luca123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/prenotazione", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().toLowerCase()).contains("error").contains("accessorio").contains("non trovato");
    }
}