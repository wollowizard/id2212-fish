/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.database;

import fish.packets.FilenameAndAddress;
import fish.server.FishFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Marcel
 */
public class DataBaseManager {

    
    private Connection conn;
    private Statement statement;
    private String datasource = "fishdatabase";
    private String user = "root";
    private String passwd = "root";
    private boolean initialized;
    private PreparedStatement insertStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement selectStatement;
    private PreparedStatement deleteStatement;
    private PreparedStatement selectClientStatement;
    private PreparedStatement deleteClientStatement;
    private PreparedStatement insertFile;
    private PreparedStatement selectByName;
    private PreparedStatement selectByAddress;
    private PreparedStatement deleteFile;
    private PreparedStatement deleteUser;
    private PreparedStatement updateUser;
    
    public DataBaseManager(String user, String passwd, String datasource) {
        this.user = user;
        this.passwd = passwd;
        this.datasource = datasource;
    }
    
    public void connectDatabase(String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + datasource, user, passwd);
	statement = conn.createStatement();
	System.out.println("Connected to database..." + conn.toString());
    }
    
    public void close() throws Exception {
	if (initialized) {
            conn.close();
        }
        System.out.println();
        System.out.println("Connection closed...");
    }
    
    public void createTable() throws Exception {
        ResultSet result = conn.getMetaData().getTables(null, null, "FILES", null);
        if (result.next()) {
            dropTable();
        }

        statement.executeUpdate(
                "CREATE TABLE FILES (filename VARCHAR(255), "
                + "ip VARCHAR(255), "
                + "port INTEGER, "
                + "CONSTRAINT id PRIMARY KEY (filename,ip,port))");
        initialized = true;
        insertFile = conn.prepareStatement("INSERT INTO FILES (filename,ip,port) VALUES (?, ?, ?)");
        //updateStatement = conn.prepareStatement("UPDATE ACCOUNT SET balance=? WHERE name=?");
        selectByName = conn.prepareStatement(
                "SELECT * FROM FILES WHERE filename=?");
        selectByAddress = conn.prepareStatement(
                "SELECT * FROM FILES WHERE ip=? AND port=?");
        deleteFile = conn.prepareStatement("DELETE FROM FILES WHERE filename=?");
        deleteUser = conn.prepareStatement("DELETE FROM FILES WHERE ip=? AND port=?");
        updateUser = conn.prepareStatement("UPDATE FILES SET ip=? AND port=? WHERE ip=? AND port=?");
        
        System.out.println();
        System.out.println("table created...");
    }
    
    public void insertFile(String filename, String ip, int port) throws Exception {
        insertFile.setString(1, filename);
        insertFile.setString(2, ip);
        insertFile.setInt(3, port);
        
        int noOfAffectedRows = insertFile.executeUpdate();
        System.out.println();
        System.out.println("data inserted in " + noOfAffectedRows + " row(s).");
    }

    public void updateUser(String ip, int port) throws Exception {
        updateUser.setString(1, ip);
        updateUser.setInt(2, port);
        updateUser.setString(3, ip);
        updateUser.setInt(4, port);
        int noOfAffectedRows = updateUser.executeUpdate();
        if (noOfAffectedRows==0) throw new SQLException("Client not found");
        System.out.println();
        System.out.println("data updated in " + noOfAffectedRows + " row(s)");
    }
    
    public void deleteUser(String ip, int port) throws SQLException {
        deleteUser.setString(1, ip);
        deleteUser.setInt(2, port);
        int noOfAffectedRows = deleteUser.executeUpdate();
        if (noOfAffectedRows==0) throw new SQLException("Client not found");
        System.out.println();
        System.out.println("client deleted from " + noOfAffectedRows + " row(s)");
    }

    public void delete(String filename, String clientname) throws Exception {
        deleteStatement.setString(1, filename);
        deleteStatement.setString(2, clientname);
        int noOfAffectedRows = deleteStatement.executeUpdate();
        System.out.println();
        System.out.println("file deleted from " + noOfAffectedRows + " row(s)");
    }

    public void selectAll() throws Exception {
        ResultSet result = statement.executeQuery(
                "SELECT * FROM ACCOUNT");
        System.out.println();
        System.out.println("XXXXXXXXXXXXX Selecting data from table XXXXXXXXXXXXXX");
        System.out.println("XXXXXXXX Query returned the following results XXXXXXXX");
        for (int i = 1; result.next(); i++) {
            System.out.println("row " + i + " - " + result.getString("name")
                    + "\t\t\t" + result.getFloat("balance"));
        }
        result.close();
    }

    public void dropTable() throws Exception {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE FILES");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");
    }

    public ArrayList<FishFile> searchFiles(String clientName, String parameter) {
        return null;
        //returns all the files of matching the parameter (except the ones of the client itself)
        
        
    }
    
}
