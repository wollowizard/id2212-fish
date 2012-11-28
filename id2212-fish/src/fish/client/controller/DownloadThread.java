/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import fish.client.EventEnum;
import fish.packets.DownloadRequest;
import fish.packets.FileContent;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.PacketType;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class DownloadThread extends Thread{

    private ClientController client;
    String fname;
    String address;
    String port;

    /**
     *
     * @param c
     * @param filename
     * @param address
     * @param port
     */
    public DownloadThread(ClientController c, String filename, String address, String port) {
        this.client = c;
        this.fname = filename;
        this.address = address;
        this.port = port;

    }
    
    

    /**
     *
     */
    @Override
    public void run() {
        Header h = new Header(PacketType.DOWNLOAD);
        DownloadRequest p = new DownloadRequest(fname);


        FishPacket packet = new FishPacket(h, p);
        int ppp = Integer.parseInt(port);
        try {

            System.out.println("Trying to download from" + address + ":" + ppp);
            Socket sock = new Socket(address, ppp);
            ObjectOutputStream out1 = new ObjectOutputStream(sock.getOutputStream());

            Sender c = new Sender(packet, out1, client);

            c.start();
            ObjectInputStream in1 = new ObjectInputStream(sock.getInputStream());
            FishPacket fp = (FishPacket) in1.readObject();
            in1.close();
            out1.close();
            sock.close();
            manageDownloadReceived(fp);
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param fp
     */
    public void manageDownloadReceived(FishPacket fp) {

        if (fp.getHeader().getType() == PacketType.FILENOLONGERAVAILABLE) {
            System.out.println("File no longer available");
        } else if (fp.getHeader().getType() == PacketType.FILECONTENT) {

            FileContent fc = (FileContent) fp.getPayload();
            try {
                byte[] bytes = fc.getContent();
                String downFold = client.getSettings().getDownloadFolder();
                String fname = downFold + fc.getName();

                File file = new File(fname);
                boolean created = file.createNewFile();
                if (!created) {
                    System.out.println("File already exists.");
                } else {
                    FileOutputStream fos = new FileOutputStream(file);
                    //create an object of BufferedOutputStream
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bos.write(bytes);
                    bos.flush();
                    bos.close();
                    client.downloadFolderContentChanged();

                    System.out.println("file created");



                }
            } catch (IOException ex) {
                Logger.getLogger(ClientController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Unrecognized packet received as download");
        }
        
        client.forceChange();
    }
}
