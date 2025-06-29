package application.unit;

import control.GestioneSistemaPrenotazione;
import database.AccessorioDAO;
import database.ClienteRegistratoDAO;
import database.ScooterDAO;
import entity.Accessorio;
import entity.ClienteRegistrato;
import entity.Prenotazione;
import entity.Scooter;
import exception.DAOException;
import exception.OperationException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SelezioneAccessoriTest {

    private final GestioneSistemaPrenotazione gsp = GestioneSistemaPrenotazione.getInstance();

    // 1 - Località vuota o null
    @Test
    void testRicercaScooterLocalitaVuota() {
        // Input
        String localita = "";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località non può essere vuota.", ex.getMessage());
    }

    // 2 - Località troppo lunga
    @Test
    void testRicercaScooterLocalitaTroppoLunga() {
        // Input
        String localita = "a".repeat(101);
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località non può superare i 100 caratteri.", ex.getMessage());
    }

    // 3 - Località con caratteri numerici
    @Test
    void testRicercaScooterLocalitaConNumeri() {
        // Input
        String localita = "Roma123";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località non può contenere numeri.", ex.getMessage());
    }

    // 4 - Località con caratteri speciali
    @Test
    void testRicercaScooterLocalitaCaratteriSpeciali() {
        // Input
        String localita = "Roma@#$";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località può contenere solo lettere, spazi e apostrofi.", ex.getMessage());
    }

    // 5 - Data con formato non valido
    @Test
    void testRicercaScooterFormatoDataNonValido() {
        // Input
        String localita = "Roma";
        String dataRitiro = "15/01/2025";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("Formato data non valido (deve essere yyyy-MM-dd).", ex.getMessage());
    }

    // 6 - Data ritiro successiva a data consegna
    @Test
    void testRicercaScooterDataRitiroDopoConsegna() {
        // Input
        String localita = "Roma";
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La data di ritiro deve essere precedente o uguale alla data di consegna.", ex.getMessage());
    }

    // 7 - Targa di uno scooter non presente nel sistema
    @Test
    void testSelezioneAccessoriScooterNonPresente() {
        List<Integer> listaAccessori = List.of(1,2,3);
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-26";
        String email = "alex.marino@email.it";
        String password = "alex345";
        String targa = "ZZ999CW";

        try (MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class)) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(null);

            OperationException ex = assertThrows(OperationException.class, () ->
                    gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa)
            );
            assertEquals("Scooter non trovato", ex.getMessage());
        }
    }

    // 8 - Il cliente non è registrato nel sistema
    @Test
    void testSelezioneAccessoriClienteNonRegistrato() {
        List<Integer> listaAccessori = List.of(1,2,3);
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-26";
        String email = "pippo@pippozzo.it";
        String password = "pippo";
        String targa = "AB123CD";

        try (MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)) {
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(null);

            OperationException ex = assertThrows(OperationException.class, () ->
                    gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa)
            );
            assertEquals("È richiesta la registrazione per effettuare una prenotazione", ex.getMessage());
        }
    }

    // 9 - La password immessa è sbagliata
    @Test
    void testSelezioneAccessoriPasswordErrata() {
        List<Integer> listaAccessori = List.of(1,2,3);
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-26";
        String email = "alex.marino@email.it";
        String password = "pippoPippozzo!";
        String targa = "AB123CD";

        entity.ClienteRegistrato fakeCliente = mock(entity.ClienteRegistrato.class);
        when(fakeCliente.getPassword()).thenReturn("alex345");

        try (MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)) {
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);

            OperationException ex = assertThrows(OperationException.class, () ->
                    gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa)
            );
            assertEquals("Credenziali errate", ex.getMessage());
        }
    }

    // 10 - Errore database lettura cliente
    @Test
    void testSelezioneAccessoriErroreDatabaseCliente() {
        List<Integer> listaAccessori = List.of(1,2,3);
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-26";
        String email = "alex.marino@email.it";
        String password = "pippoPippozzo!";
        String targa = "AB123CD";

        try (MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)) {
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenThrow(new DAOException("Errore lettura ClienteRegistrato"));

            OperationException ex = assertThrows(OperationException.class, () ->
                    gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa)
            );
            assertEquals("Errore lettura ClienteRegistrato", ex.getMessage());
        }
    }

    // 11 - Errore connessione database
    @Test
    void testSelezioneAccessoriErroreConnessioneDatabase() {
        List<Integer> listaAccessori = List.of(1,2,3);
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-26";
        String email = "alex.marino@email.it";
        String password = "pippoPippozzo!";
        String targa = "AB123CD";

        try (MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)) {
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenThrow(new DAOException("Errore connessione al database"));

            OperationException ex = assertThrows(OperationException.class, () ->
                    gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa)
            );
            assertEquals("Errore connessione al database", ex.getMessage());
        }
    }

    // 12 - Lista accessori nulla e alta stagione
    @Test
    void testSelezioneAccessoriListaAccessoriNullAltaStagione() {
        List<Integer> listaAccessori = null;
        String dataRitiro = "2025-07-25"; // alta stagione
        String dataConsegna = "2025-07-26";
        String email = "alex.marino@email.it";
        String password = "alex345!";
        String targa = "AB123CD";

        Scooter fakeScooter = mock(Scooter.class);
        ClienteRegistrato fakeCliente = mock(ClienteRegistrato.class);
        when(fakeCliente.getPassword()).thenReturn("alex345!");
        when(fakeScooter.getPrezzoPerGiornoNoleggioAltaStagione()).thenReturn(40f);
        when(fakeScooter.getPrezzoPerGiornoNoleggioBassaStagione()).thenReturn(25f);

        try (
                MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class);
                MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)
        ) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(fakeScooter);
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);

            assertDoesNotThrow(() -> {
                Prenotazione prenotazione = gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa);
                assertNotNull(prenotazione);
            });
        }
    }

    // 13 - Lista accessori nulla e bassa stagione
    @Test
    void testSelezioneAccessoriListaAccessoriNullBassaStagione() {
        List<Integer> listaAccessori = null;
        String dataRitiro = "2025-01-25"; // bassa stagione
        String dataConsegna = "2025-01-26";
        String email = "alex.marino@email.it";
        String password = "alex345!";
        String targa = "AB123CD";

        Scooter fakeScooter = mock(Scooter.class);
        ClienteRegistrato fakeCliente = mock(ClienteRegistrato.class);
        when(fakeCliente.getPassword()).thenReturn("alex345!");
        when(fakeScooter.getPrezzoPerGiornoNoleggioAltaStagione()).thenReturn(40f);
        when(fakeScooter.getPrezzoPerGiornoNoleggioBassaStagione()).thenReturn(25f);

        try (
                MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class);
                MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)
        ) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(fakeScooter);
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);

            assertDoesNotThrow(() -> {
                Prenotazione prenotazione = gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa);
                assertNotNull(prenotazione);
            });
        }
    }

    // 14 - Prenotazione corretta in alta stagione
    @Test
    void testSelezioneAccessoriPrenotazioneCorrettaAltaStagione() {
        List<Integer> listaAccessori = List.of(1);
        String dataRitiro = "2025-07-25";
        String dataConsegna = "2025-07-26";
        String email = "alex.marino@email.it";
        String password = "alex345!";
        String targa = "AB123CD";

        Scooter fakeScooter = mock(Scooter.class);
        ClienteRegistrato fakeCliente = mock(ClienteRegistrato.class);
        Accessorio fakeAccessorio = mock(Accessorio.class);
        when(fakeCliente.getPassword()).thenReturn("alex345!");
        when(fakeScooter.getPrezzoPerGiornoNoleggioAltaStagione()).thenReturn(40f);
        when(fakeScooter.getPrezzoPerGiornoNoleggioBassaStagione()).thenReturn(25f);
        when(fakeAccessorio.getPrezzo()).thenReturn(10f);

        try (
                MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class);
                MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class);
                MockedStatic<AccessorioDAO> accessorioMock = mockStatic(AccessorioDAO.class)
        ) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(fakeScooter);
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);
            accessorioMock.when(() -> AccessorioDAO.readAccessorio(1)).thenReturn(fakeAccessorio);

            assertDoesNotThrow(() -> {
                Prenotazione prenotazione = gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa);
                assertNotNull(prenotazione);
            });
        }
    }

    // 15 - Id di un accessorio non presente in database
    @Test
    void testSelezioneAccessoriIdAccessorioNonPresente() {
        List<Integer> listaAccessori = List.of(30);
        String dataRitiro = "2025-07-25";
        String dataConsegna = "2025-07-26";
        String email = "alex.marino@email.it";
        String password = "alex345!";
        String targa = "AB123CD";

        Scooter fakeScooter = mock(Scooter.class);
        ClienteRegistrato fakeCliente = mock(ClienteRegistrato.class);
        when(fakeCliente.getPassword()).thenReturn("alex345!");
        when(fakeScooter.getPrezzoPerGiornoNoleggioAltaStagione()).thenReturn(40f);
        when(fakeScooter.getPrezzoPerGiornoNoleggioBassaStagione()).thenReturn(25f);

        try (
                MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class);
                MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class);
                MockedStatic<AccessorioDAO> accessorioMock = mockStatic(AccessorioDAO.class)
        ) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(fakeScooter);
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);
            accessorioMock.when(() -> AccessorioDAO.readAccessorio(30)).thenReturn(null);

            OperationException ex = assertThrows(OperationException.class, () ->
                    gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa)
            );
            assertEquals("Accessorio non trovato", ex.getMessage());
        }
    }

    // 16 - Lista accessori vuota e bassa stagione
    @Test
    void testSelezioneAccessoriListaAccessoriVuotaBassaStagione() {
        List<Integer> listaAccessori = Collections.emptyList();
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-26";
        String email = "alex.marino@email.it";
        String password = "alex345!";
        String targa = "AB123CD";

        Scooter fakeScooter = mock(Scooter.class);
        ClienteRegistrato fakeCliente = mock(ClienteRegistrato.class);
        when(fakeCliente.getPassword()).thenReturn("alex345!");
        when(fakeScooter.getPrezzoPerGiornoNoleggioAltaStagione()).thenReturn(40f);
        when(fakeScooter.getPrezzoPerGiornoNoleggioBassaStagione()).thenReturn(25f);

        try (
                MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class);
                MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)
        ) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(fakeScooter);
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);

            assertDoesNotThrow(() -> {
                Prenotazione prenotazione = gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa);
                assertNotNull(prenotazione);
            });
        }
    }

    @Test
    void testSelezioneAccessoriListaAccessoriVuotaAltaStagione() {
        List<Integer> listaAccessori = new ArrayList<>();
        String dataRitiro = "2025-07-25"; // alta stagione
        String dataConsegna = "2025-07-26"; // alta stagione
        String email = "alex.marino@email.it";
        String password = "alex345!";
        String targa = "AB123CD";

        Scooter fakeScooter = mock(Scooter.class);
        ClienteRegistrato fakeCliente = mock(ClienteRegistrato.class);
        when(fakeCliente.getPassword()).thenReturn("alex345!");
        when(fakeScooter.getPrezzoPerGiornoNoleggioAltaStagione()).thenReturn(40f);
        when(fakeScooter.getPrezzoPerGiornoNoleggioBassaStagione()).thenReturn(25f);

        try (
                MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class);
                MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class)
        ) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(fakeScooter);
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);

            assertDoesNotThrow(() -> {
                Prenotazione prenotazione = gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa);
                assertNotNull(prenotazione);
            });
        }
    }

    // 18 - Prenotazione corretta in alta stagione con accessori presenti
    @Test
    void testSelezioneAccessoriPrenotazioneCorrettaAltaStagioneConAccessoriPresenti() {
        List<Integer> listaAccessori = List.of(1);
        String dataRitiro = "2025-07-25"; // alta stagione
        String dataConsegna = "2025-07-26";
        String email = "alex.marino@email.it";
        String password = "alex345!";
        String targa = "AB123CD";

        Scooter fakeScooter = mock(Scooter.class);
        ClienteRegistrato fakeCliente = mock(ClienteRegistrato.class);
        Accessorio fakeAccessorio = mock(Accessorio.class);
        when(fakeCliente.getPassword()).thenReturn("alex345!");
        when(fakeScooter.getPrezzoPerGiornoNoleggioAltaStagione()).thenReturn(40f);
        when(fakeScooter.getPrezzoPerGiornoNoleggioBassaStagione()).thenReturn(25f);
        when(fakeAccessorio.getPrezzo()).thenReturn(10f);

        try (
                MockedStatic<ScooterDAO> scooterMock = mockStatic(ScooterDAO.class);
                MockedStatic<ClienteRegistratoDAO> clienteMock = mockStatic(ClienteRegistratoDAO.class);
                MockedStatic<AccessorioDAO> accessorioMock = mockStatic(AccessorioDAO.class)
        ) {
            scooterMock.when(() -> ScooterDAO.readScooter(targa)).thenReturn(fakeScooter);
            clienteMock.when(() -> ClienteRegistratoDAO.readClienteRegistrato(email)).thenReturn(fakeCliente);
            accessorioMock.when(() -> AccessorioDAO.readAccessorio(1)).thenReturn(fakeAccessorio);

            assertDoesNotThrow(() -> {
                Prenotazione prenotazione = gsp.selezioneAccessori(listaAccessori, dataRitiro, dataConsegna, email, password, targa);
                assertNotNull(prenotazione);
            });
        }
    }
}