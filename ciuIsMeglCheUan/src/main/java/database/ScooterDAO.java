package database;

import entity.Scooter;
import exception.DAOException;
import exception.DBConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScooterDAO {
    private static final Logger logger = LoggerFactory.getLogger(ScooterDAO.class);

    public static int createScooter(){
        return 0;
    }

    public static Scooter readScooter(String targa) throws DAOException, DBConnectionException {
        try {
            Connection conn=DBManager.getConnection();

            String query="SELECT * FROM Scooter WHERE targa=?;";

            try {
                PreparedStatement ps= conn.prepareStatement(query);

                ps.setString(1,targa);

                ResultSet rs=ps.executeQuery();

                if(rs.next()){
                    String tar=rs.getString("targa");
                    int cil=rs.getInt("cilindrata");
                    float pas=rs.getFloat("prezzoPerGiornoNoleggioAltaStagione");
                    float pbs=rs.getFloat("prezzoPerGiornoNoleggioBassaStagione");
                    String stato=rs.getString("stato");
                    String tipologia=rs.getString("tipologia");
                    int agenziaId=rs.getInt("agenziaId");

                    Scooter s=new Scooter(tar,cil,pas,pbs,tipologia,agenziaId);

                    return s;

                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura Accessorio");
            } finally {
                DBManager.closeConnection();
            }
        }catch(SQLException e){
            throw new DBConnectionException("Errore connessione al database");
        }
        return null;
    }

    public static List<Scooter> readScooter(String localita, String dataRitiro, String dataConsegna) throws DAOException, DBConnectionException {
        List<Scooter> scooters = new ArrayList<>();

        try {
            Connection conn=DBManager.getConnection();

            String query="SELECT s.* FROM Scooter s JOIN Agenzie a On s.agenziaId=a.Id JOIN Prenotazioni p ON p.scooterTarga=s.targa WHERE a.località=? AND s.stato = 'in-servizio' AND (p.id IS NULL OR (p.dataConsegna <= ? OR p.dataRitiro >= ?))";

            try {
                PreparedStatement ps= conn.prepareStatement(query);

                ps.setString(1,localita);
                ps.setString(2,dataRitiro);
                ps.setString(3,dataConsegna);

                ResultSet rs=ps.executeQuery();

                logger.debug("Query eseguita");

                while(rs.next()){

                    String tar=rs.getString("targa");
                    int cil=rs.getInt("cilindrata");
                    float pas=rs.getFloat("prezzoPerGiornoNoleggioAltaStagione");
                    float pbs=rs.getFloat("prezzoPerGiornoNoleggioBassaStagione");
                    String stato=rs.getString("stato");
                    String tipologia=rs.getString("tipologia");
                    int agenziaId=rs.getInt("agenziaId");

                    logger.debug("Scooter trovato: targa={}, cilindrata={}", tar, cil);

                    scooters.add(new Scooter(tar,cil,pas,pbs,tipologia,agenziaId));

                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura Scooter");
            } finally {
                DBManager.closeConnection();
            }
        }catch(SQLException e){
            throw new DBConnectionException("Errore connessione al database");
        }
        return scooters;
    }

    public static int updateScooter(){
        return 0;
    }

    public static int deleteScooter(){
        return 0;
    }
}
