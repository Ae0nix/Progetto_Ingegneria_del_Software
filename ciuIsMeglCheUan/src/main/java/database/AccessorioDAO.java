package database;

import entity.Accessorio;
import exception.DAOException;
import exception.DBConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccessorioDAO {

    public static int createAccessorio(Accessorio accessorio) throws DAOException, DBConnectionException  {

        try {
            Connection conn = DBManager.getConnection();

            String query = "INSERT INTO Accessori VALUES (?, ?, ?);"; //l'id viene generato ad autoincremento dal database

            try {
                PreparedStatement ps = conn.prepareStatement(query);

                ps.setString(1, accessorio.getDescrizione());
                ps.setFloat(2, accessorio.getPrezzo());
                ps.setString(3, accessorio.getTipo());

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Errore scrittura Accessorio");
            }
            finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore connessione al database");
        }

        return 0;
    }

    public static List<Accessorio> readAccessorio() throws DAOException, DBConnectionException {
        List<Accessorio> accessori = new ArrayList<>();

        try {
            Connection conn = DBManager.getConnection();

            String query = "SELECT * FROM Accessori;";

            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String descrizione = rs.getString("descrizione");
                    float prezzo = rs.getFloat("prezzo");
                    String tipo = rs.getString("tipo");

                    Accessorio acc = new Accessorio(descrizione, prezzo, tipo);
                    acc.setId(id);
                    accessori.add(acc);
                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura Accessorio");
            } finally {
            DBManager.closeConnection();
            }
        }catch(SQLException e){
            throw new DBConnectionException("Errore connessione al database");
        }
        return accessori;
    }



    public static int updateAccessorio(int idDaModificare, Accessorio accessorio) throws DAOException, DBConnectionException {
        try {
            Connection conn = DBManager.getConnection();

            String query = "UPDATE Accessori SET descrizione = ?, prezzo = ?, tipo = ? WHERE id = ?";

            try {
                PreparedStatement ps = conn.prepareStatement(query);

                ps.setString(1, accessorio.getDescrizione());
                ps.setFloat(2, accessorio.getPrezzo());
                ps.setString(3, accessorio.getTipo());
                ps.setInt(4, idDaModificare);

                ps.executeUpdate();

            } catch (SQLException e) {
                throw new DAOException("Errore durante la modifica all'accessorio");
            }
            finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore connessione al database");
        }

        return 0;
    }



    public static int deleteAccessorio(int idAccessorio) throws DAOException, DBConnectionException {
        try{
            Connection conn = DBManager.getConnection();

            String query = "DELETE FROM Accessori WHERE idAccessorio = ?";
            try{
                PreparedStatement ps = conn.prepareStatement(query);

                ps.setInt(1, idAccessorio);

                ps.executeUpdate();
            }
            catch (SQLException e){
                throw new DAOException("Errore nell'eliminazione dell'accessorio");
            }

        }
        catch (SQLException e){
            throw new DAOException("Errore connessione al database");
        }

        return 0;
    }
}
