package fr.unice.smart_campus.middleware.collector;

import org.postgresql.ds.PGSimpleDataSource;

import javax.naming.*;
import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DataAccess class
 * Singleton class which allows to insert values in the Message Queue Database (PostgreSQL)
 */
public class DataAccess {

    private Connection con;
    private static DataAccess instance;

    /**
     * Get the instance
     * @return DataAccess instance
     */
    public static DataAccess getInstance() {
        if (instance == null) {
            instance = new DataAccess();
        }
        return instance;
    }

    /**
     * Private constructor
     * Get connection from data source (defined in jetty-env.xml)
     */
    private DataAccess() {

        try {
            Context initialContext = new InitialContext();
            if ( initialContext == null){
                System.err.println("Cannot get InitalContext");
            }
            else {
                // Get the data source from the context
                PGSimpleDataSource datasource = (PGSimpleDataSource)initialContext.lookup("java:/comp/env/jdbc/MessagesQueue");
                if (datasource != null){
                    // Get the connection from the datasource
                    con = datasource.getConnection();
                }
                else {
                    System.err.println("Failed to get datasource");
                }
            }
        } catch (NamingException ex) {
            System.err.println("Cannot get connection: " + ex);
        } catch (SQLException ex){
            System.err.println("Cannot get connection: " + ex);
        } catch (Exception ex){
            System.err.println("General error on attempting connection: " + ex);
        }
    }

    /**
     * Add value of sensor in the message queues
     * @param ident sensor identifier
     * @param time time of the data
     * @param value value of the data
     */
    public void addValue(String ident, String time, String value) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String stm = "INSERT INTO \"public\".\"MESSAGES\" (identifier, time, value) VALUES(?, ?, ?)";
            st = con.prepareStatement(stm);
            st.setString(1, ident);
            st.setString(2, time);
            st.setString(3, value);
            st.executeUpdate();

            if (st.executeUpdate() == 1) {
                System.out.println("Valeur ajoutée dans la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the connection from the data source
     */
    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataAccess.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}