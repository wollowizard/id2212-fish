/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server.database;

import fish.packets.FilenameAndAddress;
import fish.packets.Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private PreparedStatement insertServer;
    private PreparedStatement deleteServer;
    private PreparedStatement selectServerByAddress;
    private PreparedStatement selectServerBySupernode;
    private PreparedStatement selectAllServers;
    private PreparedStatement selectByAddress;
    private PreparedStatement deleteFile;
    private PreparedStatement deleteUser;
    private PreparedStatement updateUser;
    private PreparedStatement promoteToSupernode;
    private PreparedStatement truncateServer;

    /**
     *
     * @param user
     * @param passwd
     * @param datasource
     */
    public DataBaseManager(String user, String passwd, String datasource) {
        this.user = user;
        this.passwd = passwd;
        this.datasource = datasource;
    }

    /**
     *
     * @param username
     * @param password
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void connectDatabase(String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + datasource, user, passwd);

        statement = conn.createStatement();
        initialized = true;
        System.out.println("Connected to database..." + conn.toString());

    }

    /**
     *
     * @throws Exception
     */
    public void close() throws Exception {
        if (initialized) {
            conn.close();
        }
        System.out.println();
        System.out.println("Connection closed...");
    }

    /**
     *
     * @throws Exception
     */
    public void createTable() throws Exception {
        ResultSet result = conn.getMetaData().getTables(null, null, "FILES", null);
        if (!result.next()) {
            statement.executeUpdate(
                    "CREATE TABLE FILES (filename VARCHAR(255), "
                    + "ip VARCHAR(255), "
                    + "port INTEGER, "
                    + "CONSTRAINT id PRIMARY KEY (filename,ip,port))");
        }

        ResultSet result2 = conn.getMetaData().getTables(null, null, "SERVERS", null);
        if (!result2.next()) {
            statement.executeUpdate(
                    "CREATE TABLE SERVERS (id INTEGER AUTO_INCREMENT, "
                    + "ip VARCHAR(255), "
                    + "port INTEGER, "
                    + "supernode INTEGER, "
                    + "PRIMARY KEY (id))");
        }

        insertServer = conn.prepareStatement("INSERT INTO SERVERS (ip,port,supernode) VALUES ( ?, ?,?)");
        deleteServer = conn.prepareStatement("DELETE FROM SERVERS WHERE id=?");
        selectServerByAddress = conn.prepareStatement("SELECT * FROM SERVERS WHERE ip=? AND port=?");
        selectServerBySupernode = conn.prepareStatement("SELECT * FROM SERVERS WHERE supernode=1");
        selectAllServers = conn.prepareStatement("SELECT * FROM SERVERS");
        insertFile = conn.prepareStatement("INSERT INTO FILES (filename,ip,port) VALUES (?, ?, ?)");
        promoteToSupernode = conn.prepareStatement("UPDATE SERVERS SET supernode=1 WHERE ip=? AND port=?");
        selectAllServers = conn.prepareStatement("SELECT * FROM SERVERS");
        truncateServer = conn.prepareStatement("DELETE FROM SERVERS");


        selectByAddress = conn.prepareStatement(
                "SELECT * FROM FILES WHERE ip=? AND port=?");
        deleteFile = conn.prepareStatement("DELETE FROM FILES WHERE filename=? AND ip=? AND port=?");
        deleteUser = conn.prepareStatement("DELETE FROM FILES WHERE ip=? AND port=?");
        updateUser = conn.prepareStatement("UPDATE FILES SET ip=? AND port=? WHERE ip=? AND port=?");

        System.out.println();
        System.out.println("table created...");
    }

    /**
     *
     * @param filename
     * @param ip
     * @param port
     * @throws Exception
     */
    public void insertFile(String filename, String ip, int port) throws Exception {
        insertFile.setString(1, filename);
        insertFile.setString(2, ip);
        insertFile.setInt(3, port);

        int noOfAffectedRows = insertFile.executeUpdate();
        System.out.println();
        System.out.println("data inserted in " + noOfAffectedRows + " row(s).");
    }

    /**
     *
     * @param ip
     * @param port
     * @throws Exception
     */
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

    /**
     *
     * @param ip
     * @param port
     * @throws SQLException
     */
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

    /**
     *
     * @param filename
     * @param ip
     * @param port
     * @throws Exception
     */
    public void deleteFile(String filename, String ip, int port) throws Exception {
        deleteFile.setString(1, filename);
        deleteFile.setString(2, ip);
        deleteFile.setInt(3, port);
        int noOfAffectedRows = deleteFile.executeUpdate();
        System.out.println();
        System.out.println("file deleted from " + noOfAffectedRows + " row(s)");
    }

    /**
     *
     * @param ip
     * @param port
     * @return
     * @throws SQLException
     */
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

    /**
     *
     * @param filename
     * @param ip
     * @param port
     * @return
     * @throws SQLException
     */
    public HashSet <FilenameAndAddress> selectByFileName(String filename, String ip, Integer port) throws SQLException {
        String forSql = "%" + filename + "%";
        String sql = "select * from FILES where (filename like ?) AND (ip, port) NOT IN ( SELECT DISTINCT ip, port from FILES where ip = ? and port= ? )";


        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, forSql);
        ps.setString(2, ip);
        ps.setString(3, port.toString());

      
        ResultSet r = ps.executeQuery();
        ArrayList<FilenameAndAddress> tmp = new ArrayList<>();
        HashSet <FilenameAndAddress> set=new HashSet<>();
        
        while (r.next()) {
            //tmp.add(new FilenameAndAddress(r.getString("filename"), r.getString("ip"), r.getInt("port")));
            set.add(new FilenameAndAddress(r.getString("filename"), r.getString("ip"), r.getInt("port")));
        }
        return set;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public ArrayList<FilenameAndAddress> selectAll() throws Exception {
        ResultSet r = statement.executeQuery(
                "SELECT * FROM FILES");
        ArrayList<FilenameAndAddress> tmp = new ArrayList<>();
        while (r.next()) {
            tmp.add(new FilenameAndAddress(r.getString("filename"), r.getString("ip"), r.getInt("port")));
        }
        return tmp;

    }

    /**
     *
     * @throws Exception
     */
    public void dropTable() throws Exception {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE FILES");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");

    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public int getFileCount() throws SQLException {
        Integer count = 0;
        ResultSet res = statement.executeQuery("SELECT COUNT(*) FROM FILES");
        while (res.next()) {
            count = res.getInt(1);
        }
        return count;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ArrayList<Server> getListOfServers() throws SQLException {


        ArrayList<Server> ret = new ArrayList<>();
        ResultSet r = selectAllServers.executeQuery();


        while (r.next()) {
            Server s = new Server(r.getInt("id"), r.getString("ip"), r.getInt("port"));
            ret.add(s);
        }

        return ret;


    }

    /**
     *
     * @param server
     * @throws SQLException
     */
    public void removeServer(Server server) throws SQLException {
        System.out.println("deleting " + server.getId());
        deleteServer.setString(1, server.getId().toString());

        int noOfAffectedRows = deleteServer.executeUpdate();
        System.out.println();
        System.out.println("server deleted from " + noOfAffectedRows + " row(s)");

    }

    /**
     *
     * @param ip
     * @param port
     * @return
     * @throws SQLException
     */
    public Server addServer(String ip, Integer port) throws SQLException {

        selectServerByAddress.setString(1, ip);
        selectServerByAddress.setString(2, port.toString());

        ResultSet r = selectServerByAddress.executeQuery();
        if (r.next()) {
        } else {
            //insert only if not present
            insertServer.setString(1, ip);
            insertServer.setString(2, port.toString());
            insertServer.setInt(3, 0);


            int noOfAffectedRows = insertServer.executeUpdate();
            System.out.println();
            System.out.println("data inserted in " + noOfAffectedRows + " row(s).");

        }


        selectServerByAddress.setString(1, ip);
        selectServerByAddress.setString(2, port.toString());

        r = selectServerByAddress.executeQuery();
        Server s = null;
        while (r.next()) {
            s = new Server(r.getInt("id"), r.getString("ip"), r.getInt("port"));
        }
        return s;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Server getSuperNode() throws SQLException {

        ResultSet r = selectServerBySupernode.executeQuery();
        Server s = null;
        while (r.next()) {
            s = new Server(r.getInt("id"), r.getString("ip"), r.getInt("port"));
        }
        
        return s;
    }

    /**
     *
     * @param ip
     * @param port
     * @throws SQLException
     */
    public void promoteSupernode(String ip, Integer port) throws SQLException {
        promoteToSupernode.setString(1, ip);
        promoteToSupernode.setInt(2, port);
        promoteToSupernode.executeUpdate();

    }

    /**
     *
     * @param ip
     * @param port
     * @return
     * @throws SQLException
     */
    public Server getServer(String ip, Integer port) throws SQLException {
        selectServerByAddress.setString(1, ip);
        selectServerByAddress.setInt(2, port);
        ResultSet r = selectServerByAddress.executeQuery();


        Server s = null;
        while (r.next()) {

            s = new Server(r.getInt("id"), r.getString("ip"), r.getInt("port"));
        }
        return s;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Integer getMinId() throws SQLException {
        Statement stmt = conn.createStatement();
        String query = "SELECT MIN(id) FROM SERVERS";
        ResultSet rs = stmt.executeQuery(query);
        //Extact result from ResultSet rs
        while (rs.next()) {
            Integer x = rs.getInt("MIN(id)");
            System.out.println("MIN was" + x);
            return x;

        }
        return -1;

    }

    /**
     *
     * @param ip
     * @param port
     * @throws SQLException
     */
    public void addSupernode(String ip, Integer port) throws SQLException {
        insertServer.setString(1, ip);
        insertServer.setString(2, port.toString());
        insertServer.setInt(3, 1);


        int noOfAffectedRows = insertServer.executeUpdate();
        System.out.println();
        System.out.println("data inserted in " + noOfAffectedRows + " row(s).");
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public int getClientsCount() throws SQLException {

        Statement stmt = conn.createStatement();
        String query = "SELECT COUNT(*) FROM (SELECT DISTINCT ip, port FROM FILES) AS internalQuery";
        ResultSet rs = stmt.executeQuery(query);
        //Extact result from ResultSet rs
        if (rs.next()) {
            Integer x = rs.getInt(1);
            return x;
        }
        return 0;

    }

    /**
     *
     * @throws SQLException
     */
    public void truncateServerTable() throws SQLException {
        System.out.println("truncating server table");
        
        int noOfAffectedRows = truncateServer.executeUpdate();
        System.out.println();
        
    }
}
