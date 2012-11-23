/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.database;

import fish.packets.FilenameAndAddress;
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
    private PreparedStatement insertFile;
   
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
        initialized = true;
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
        if (!result.next()) {
            statement.executeUpdate(
                "CREATE TABLE FILES (filename VARCHAR(255), "
                + "ip VARCHAR(255), "
                + "port INTEGER, "
                + "CONSTRAINT id PRIMARY KEY (filename,ip,port))");
        }
        insertFile = conn.prepareStatement("INSERT INTO FILES (filename,ip,port) VALUES (?, ?, ?)");
        //updateStatement = conn.prepareStatement("UPDATE ACCOUNT SET balance=? WHERE name=?");
        
        selectByAddress = conn.prepareStatement(
                "SELECT * FROM FILES WHERE ip=? AND port=?");
        deleteFile = conn.prepareStatement("DELETE FROM FILES WHERE filename=? AND ip=? AND port=?");
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
        if (noOfAffectedRows == 0) {
            throw new SQLException("Client not found");
        }
        System.out.println();
        System.out.println("data updated in " + noOfAffectedRows + " row(s)");
    }

    public void deleteUser(String ip, int port) throws SQLException {
        deleteUser.setString(1, ip);
        deleteUser.setInt(2, port);
        int noOfAffectedRows = deleteUser.executeUpdate();
        if (noOfAffectedRows == 0) {
            throw new SQLException("Client not found");
        }
        System.out.println();
        System.out.println("client deleted from " + noOfAffectedRows + " row(s)");
    }

    public void deleteFile(String filename, String ip, int port) throws Exception {
        deleteFile.setString(1, filename);
        deleteFile.setString(2, ip);
        deleteFile.setInt(3, port);
        int noOfAffectedRows = deleteFile.executeUpdate();
        System.out.println();
        System.out.println("file deleted from " + noOfAffectedRows + " row(s)");
    }

    public ArrayList<FilenameAndAddress> selectByAdrress(String ip, int port) throws SQLException {
        selectByAddress.setString(1, ip);
        selectByAddress.setInt(2, port);
        ResultSet r = selectByAddress.executeQuery();
        ArrayList<FilenameAndAddress> tmp = new ArrayList<>();
        while (r.next()) {
            tmp.add(new FilenameAndAddress(r.getString("filename"), r.getString("ip"), r.getInt("port")));
        }
        return tmp;
    }

    public ArrayList<FilenameAndAddress> selectByFileName(String filename, String ip, Integer port) throws SQLException {
        String forSql = "%" + filename + "%";
        String sql = "select * from FILES where (filename like ?) AND (ip, port) NOT IN ( SELECT DISTINCT ip, port from FILES where ip = ? and port= ? )";


        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, forSql);
        ps.setString(2, ip);
        ps.setString(3, port.toString());
        
        System.out.println(ps.toString());
        ResultSet r = ps.executeQuery();
        ArrayList<FilenameAndAddress> tmp = new ArrayList<>();
        while (r.next()) {
            tmp.add(new FilenameAndAddress(r.getString("filename"), r.getString("ip"), r.getInt("port")));
        }
        return tmp;
    }
    


    public ArrayList<FilenameAndAddress> selectAll() throws Exception {
        ResultSet r = statement.executeQuery(
                "SELECT * FROM FILES");
        ArrayList<FilenameAndAddress> tmp = new ArrayList<>();
        while (r.next()) {
            tmp.add(new FilenameAndAddress(r.getString("filename"), r.getString("ip"), r.getInt("port")));
        }
        return tmp;

    }

    public void dropTable() throws Exception {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE FILES");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");

    }

    public int getFileCount() throws SQLException {
        Integer count = 0;
        ResultSet res = statement.executeQuery("SELECT COUNT(*) FROM FILES");
        while (res.next()) {
            count = res.getInt(1);
        }
        return count;

    }
}
