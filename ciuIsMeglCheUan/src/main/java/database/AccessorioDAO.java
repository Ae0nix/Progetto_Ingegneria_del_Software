package database;

import entity.Accessorio;
import exception.DBConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessorioDAO {

    public static int createAccessorio(Accessorio accessorio) {

        try {
            Connection conn = DBManager.getConnection();

            
        } catch (SQLException e) {
            throw new DBConnectionException("Errore connessione al database");
        }

        return 0;
    }

    public static Accessorio readAccessorio(){
        return null;
    }

    public static int updateAccessorio() {
        return 0;
    }
    public static int deleteAccessorio() {
        return 0;
    }
}
