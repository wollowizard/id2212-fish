/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private void close() throws Exception {
	if (initialized) {
            conn.close();
        }
        System.out.println();
        System.out.println("Connection closed...");
    }
    
    public void createTable(String name, String fileName, String clientName, String addrss) throws Exception {
        ResultSet result = conn.getMetaData().getTables(null, null, "ACCOUNT", null);
        if (result.next()) {
            dropTable();
        }

        statement.executeUpdate(
                "CREATE TABLE FILES (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "filename VARCHAR(255), "
                + "owner VARCHAR(255), "
                + "address VARCHAR(255))");
        initialized = true;
        insertStatement = conn.prepareStatement("INSERT INTO FILES (filename,owner,address) VALUES (?, ?, ?)");
        //updateStatement = conn.prepareStatement("UPDATE ACCOUNT SET balance=? WHERE name=?");
        selectStatement = conn.prepareStatement(
                "SELECT * FROM FILES WHERE filename=? AND clientName=?");
        deleteStatement = conn.prepareStatement("DELETE FROM ACCOUNT WHERE filename=? AND clientName=?");
        System.out.println();
        System.out.println("table created...");
    }
    
    private void insert(String filename, String clientname, String addrs) throws Exception {
        insertStatement.setString(1, filename);
        insertStatement.setString(2, clientname);
        insertStatement.setString(3, addrs);
        
        int noOfAffectedRows = insertStatement.executeUpdate();
        System.out.println();
        System.out.println("data inserted in " + noOfAffectedRows + " row(s).");
    }

    /*private void update(String name, float amount) throws Exception {
        float balance = 0;
        selectStatement.setString(1, name);
        ResultSet result = selectStatement.executeQuery();
        if (result.next()) {
            balance = result.getFloat("balance");
        }
        result.close();
        if (amount + balance < 0) {
            throw new Exception("Negative balance is not allowed");
        }
        balance += amount;
        updateStatement.setDouble(1, balance);
        updateStatement.setString(2, name);
        int noOfAffectedRows = updateStatement.executeUpdate();
        System.out.println();
        System.out.println("data updated in " + noOfAffectedRows + " row(s)");
    }*/

    private void delete(String filename, String clientname) throws Exception {
        deleteStatement.setString(1, filename);
        deleteStatement.setString(2, clientname);
        int noOfAffectedRows = deleteStatement.executeUpdate();
        System.out.println();
        System.out.println("data deleted from " + noOfAffectedRows + " row(s)");
    }

    private void selectAll() throws Exception {
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

    private void dropTable() throws Exception {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE ACCOUNT");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");
    }
    
}
