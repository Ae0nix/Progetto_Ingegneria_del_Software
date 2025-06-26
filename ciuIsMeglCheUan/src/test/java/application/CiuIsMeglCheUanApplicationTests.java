package application;

import control.GestioneSistemaPrenotazione;
import database.ScooterDAO;
import entity.Scooter;
import exception.DAOException;
import exception.OperationException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class CiuIsMeglCheUanApplicationTests {

    GestioneSistemaPrenotazione gsp=GestioneSistemaPrenotazione.getInstance();

    @Test
    void testRicercaScooterLocalitaVuota() {
        String localita = "";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località non può essere vuota.", ex.getMessage());
    }

    @Test
    void testRicercaScooterLocalitaTroppoLunga() {
        String localita = "A".repeat(101);
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località non può superare i 100 caratteri.", ex.getMessage());
    }

    @Test
    void testRicercaScooterLocalitaConNumeri() {
        String localita = "Roma123";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località non può contenere numeri.", ex.getMessage());
    }

    @Test
    void testRicercaScooterLocalitaCaratteriNonValidi() {
        String localita = "Roma@#$";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La località può contenere solo lettere, spazi e apostrofi.", ex.getMessage());
    }

    @Test
    void testRicercaScooterFormatoDataNonValido() {
        String localita = "Roma";
        String dataRitiro = "15/01/2025"; // formato errato
        String dataConsegna = "2025-01-20";

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("Formato data non valido (deve essere yyyy-MM-dd).", ex.getMessage());
    }

    @Test
    void testRicercaScooterDataRitiroDopoConsegna() {
        String localita = "Roma";
        String dataRitiro = "2025-01-25";
        String dataConsegna = "2025-01-20"; // consegna prima del ritiro

        OperationException ex = assertThrows(OperationException.class, () ->
                gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
        );
        assertEquals("La data di ritiro deve essere precedente o uguale alla data di consegna.", ex.getMessage());
    }

    @Test
    void testRicercaScooterSuccesso() {
        // Presuppone che nel sistema ci sia almeno uno scooter disponibile per questa località e date.
        String localita = "Roma";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        assertDoesNotThrow(() -> {
            List<Scooter> scooterList = gsp.ricercaScooter(localita, dataRitiro, dataConsegna);
            assertNotNull(scooterList);
        });
    }

    @Test
    void testRicercaScooterDAOException() throws Exception {
        String localita = "Roma";
        String dataRitiro = "2025-01-15";
        String dataConsegna = "2025-01-20";

        // Mocka il metodo statico
        try (MockedStatic<ScooterDAO> mockedStatic = mockStatic(database.ScooterDAO.class)) {
            mockedStatic
                    .when(() -> database.ScooterDAO.readScooter(localita, dataRitiro, dataConsegna))
                    .thenThrow(new DAOException("Errore database"));

            OperationException ex = assertThrows(
                    OperationException.class,
                    () -> gsp.ricercaScooter(localita, dataRitiro, dataConsegna)
            );
            assertEquals("Errore database", ex.getMessage());
        }
    }
}
