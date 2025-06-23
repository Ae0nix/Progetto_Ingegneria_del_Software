package database;

import entity.Prenotazione;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrenotazioneAccessorioDAO {

    public static int createPrenotazioneAccessorio(int idPrenotazione, int idAccessorio) throws DAOException, DBConnectionException {
        try {
            Connection conn = DBManager.getConnection();

            String query = "INSERT INTO Prenotazioni_Accessori VALUES (?, ?);"; //l'id viene generato ad autoincremento dal database

            try {
                PreparedStatement ps = conn.prepareStatement(query);

                ps.setInt(1, idPrenotazione);
                ps.setInt(2, idAccessorio);

                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("Errore scrittura associazione prenotazione-accessorio");
            }
            finally {
                DBManager.closeConnection();
            }
        } catch (SQLException e) {
            throw new DBConnectionException("Errore connessione al database");
        }
        return 0;
    }

    public static Prenotazione readPrenotazioneAccessorio() {
        return null;
    }

    public static int updatePrenotazioneAccessorio() {
        return 0;
    }

    public static int deletePrenotazioneAccessorio() {
        return 0;
    }

}
