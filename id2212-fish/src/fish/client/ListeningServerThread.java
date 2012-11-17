/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.client.dir.FileWalker;
import fish.packets.DownloadRequest;
import fish.packets.FileContent;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.PacketType;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class ListeningServerThread extends Thread {

    private Client client;

    public ListeningServerThread(Client c) {
        client = c;
    }

    public void run() {
        try {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(0);
                client.setListeningThreadPort(serverSocket.getLocalPort());
                client.sendListeningServerPort();
                System.out.println("Listening on " + client.getListeningThreadPort());


            } catch (IOException e) {
                System.err.println("Could not listen on port: 10007.");
            }

            Socket clientSocket = null;
            System.out.println("Waiting for connection.....");
            Boolean running = true;
            while (running) {
                try {
                    clientSocket = serverSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                    System.out.println("Connection successful");
                    System.out.println("Waiting for input.....");


                    FishPacket fp = (FishPacket) in.readObject();

                    System.out.println(fp.getPayload().printSummary());


                    if (fp.getHeader().getType() == PacketType.DOWNLOAD) {
                        System.out.println("download received.....");
                        DownloadRequest dr = (DownloadRequest) fp.getPayload();

                        String filename = dr.getFilename();

                        File f = FileWalker.findFile(filename, client.getSettings().getFolder());

                        if (f == null) {

                            fileNoLongerAvailable(filename, out);
                        } else {

                            sendFile(f, out);

                        }

                    } else {
                        System.out.println("UNRECOGNIZED MESSAGE RECEIVED FROM LISTENING THREAD!");
                    }

                    in.close();
                    out.close();
                    clientSocket.close();

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ListeningServerThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException e) {
                    System.out.println("Accept failed.");
                }
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ListeningServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fileNoLongerAvailable(String filename, ObjectOutputStream out) throws IOException {

        
        FishPacket fp;
        Header h = new Header(PacketType.FILENOLONGERAVAILABLE);
        fp = new FishPacket(h, null);
        out.writeObject(fp);
        out.flush();
        out.reset();

    }

    private void sendFile(File file, ObjectOutputStream out) throws IOException {
        
        System.out.println("abs path"  + file.getAbsolutePath());
        
        Path path = Paths.get(file.getAbsolutePath());
        System.out.println("path" + path);
        //byte[] b = Files.readAllBytes(path);
        byte[] b = "ciao".getBytes();
                
        System.out.println(new String(b,0));
        FileContent fc = new FileContent(file.getName(), b);

        Header h = new Header(PacketType.FILECONTENT);

        FishPacket fp;
        fp = new FishPacket(h, fc);

        out.writeObject(fp);

        out.flush();

        out.reset();

    }
}
