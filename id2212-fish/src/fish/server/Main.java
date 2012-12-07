/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.server.entity.Client;
import fish.server.entity.ClientNetworkResources;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcel
 */
public class Main {

    private static final String USAGE = "nothing or port or port+dbname+dbusername+dbpassword";

    public static void main(String[] args) throws IOException {
        String dbhost = "192.168.1.15";
        String datasource = "fishdatabase";
        String user = "root";
        String passwd = "root";
        String port = "1238";

        if (!(args.length == 5 || args.length == 1 || args.length == 0)) {
            System.out.println("You inserted " + args.length + " parameters");
            System.out.println(USAGE);
            System.exit(1);
        }
        if (args.length == 1) {
            port = args[0];
        } else if (args.length == 5) {
            port = args[0];
            dbhost = args[1];
            datasource = args[2];
            user = args[3];
            passwd = args[4];

        }


        boolean listening = true;
        ServerSocket serverSocket = null;

        final FishServer fs = new FishServer(dbhost, datasource, user, passwd);

        fs.ConnectToDataBase();


        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
        } catch (IOException e) {
            System.err.println("Could not listen on port:" + port);
            System.exit(1);
        }
        InetAddress thisIp = InetAddress.getLocalHost();

        fs.setIp(thisIp.getHostAddress().toString());
        fs.setPort(Integer.parseInt(port));

        try {
            fs.searchSuperNode();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (listening) {

            Socket clientSocket = serverSocket.accept();

            Client cd;
            cd = new Client(new ClientNetworkResources(clientSocket));
            fs.newClientConnected(cd);

            (new ConnectionHandler(fs, cd)).start();

        }

        serverSocket.close();
    }
}