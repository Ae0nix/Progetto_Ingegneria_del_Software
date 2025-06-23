package database;

import entity.Prenotazione;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrenotazioneDAO {

    public static int createPrenotazione(Prenotazione prenotazione) throws DAOException, DBConnectionException {
        try {
            Connection conn = DBManager.getConnection();

            String query = "INSERT INTO Prenotazioni(`dataRitiro`, `dataConsegna`, `costoTotale`, `clienteRegistratoId`, `scooterTarga`) VALUES (?, ?, ?, ?, ?);"; //l'id viene generato ad autoincremento dal database

            try {
                PreparedStatement ps = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, prenotazione.getDataRitiro());
                ps.setString(2, prenotazione.getDataConsegna());
                ps.setFloat(3, prenotazione.getCostoTotale());
                ps.setInt(4, prenotazione.getClienteRegistratoId());
                ps.setString(5,prenotazione.getScooterTarga());

                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // <-- QUESTO È L'ID GENERATO
                    } else {
                        throw new DAOException("Inserimento prenotazione riuscito ma nessun ID generato ottenuto.");
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Errore scrittura Prenotazione");
            }
            finally {
                DBManager.closeConnection();
            }
        } catch (SQLException e) {
            throw new DBConnectionException("Errore connessione al database");
        }
    }

    public static Prenotazione readPrenotazione(){
        return  null;
    }

    public static int updatePrenotazione(){
        return 0;
    }

    public static int deletePrenotazione(){
        return 0;
    }

}
