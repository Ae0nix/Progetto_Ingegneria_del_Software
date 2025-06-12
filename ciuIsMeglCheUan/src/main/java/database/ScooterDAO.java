package database;

import entity.Scooter;
import exception.DAOException;
import exception.DBConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScooterDAO {

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

    public static int updateScooter(){
        return 0;
    }

    public static int deleteScooter(){
        return 0;
    }
}
