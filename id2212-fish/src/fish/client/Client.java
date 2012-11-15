/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.client.dirWatch.DirWatcher;
import fish.packets.FileList;
import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.PacketType;
import fish.packets.ParameterToSearch;
import fish.packets.ServerStatistics;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author alfredo
 */
public class Client extends Observable {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<File> filesToAdd = new ArrayList<>();
    private ArrayList<File> filesToRemove = new ArrayList<>();
    private int numClients;
    private int numFiles;
    private boolean connected = false;
    private ArrayList<FilenameAndAddress> lastresult;
    private FishSettings settings;
    private String lastError = "";

    public Client() {
        this.settings = new FishSettings(this);
    }

    public void setSocket(Socket s) {
        this.socket = s;
    }

    public void setInStream(ObjectInputStream in) {
        this.in = in;
    }

    public void setOutStream(ObjectOutputStream out) {
        this.out = out;
    }

    public boolean isConnected() {
        synchronized (this) {
            return connected;
        }
    }

    public void setConnected(boolean connected) {
        synchronized (this) {
            this.connected = connected;
            this.setChanged();
        }


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                notifyObservers(EventEnum.CONNECTED);
            }
        });
    }

    public ArrayList<FilenameAndAddress> getLastresult() {
        return lastresult;
    }

    public void setLastresult(ArrayList<FilenameAndAddress> lastresult) {


        this.lastresult = lastresult;
        this.setChanged();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                notifyObservers(EventEnum.NEWRESULT);
            }
        });
    }

    public void addFile(File f) {

        this.filesToAdd.add(f);

    }

    public void removeFile(File f) {

        this.filesToRemove.add(f);

    }

    public void clearFilesToAdd() {

        this.filesToAdd.clear();
    }

    public void clearFilesToRemove() {

        this.filesToRemove.clear();

    }

    public void submitInitialFileList() throws NotDirectoryException {

        FileWalker fw = new FileWalker(this);
        fw.walk(this.settings.getFolder());


    }

    public void share() {
        
        Connector c = new Connector(this);
        c.start();
    }

    public void unshare() {
        try {
            this.socket.close();
            this.notifyObservers(EventEnum.DISCONNECT);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addWatcher(String folderPath) {
        TimerTask task = new DirWatcher(folderPath, "*") {
            @Override
            protected void onChange(File file, String action) {
                switch (action) {
                    case "add":
                        addFile(file);
                        break;
                    case "delete":
                        removeFile(file);
                        break;
                }
                sendFileList();
                System.out.println("File " + file.getName() + " action: " + action);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(), 1000);

    }

    public void sendFileList() {


        Header h = new Header(PacketType.ADDFILE);

        FileList fl = new FileList(filesToAdd, filesToRemove);
        FishPacket packet = new FishPacket(h, fl);
        Sender c = new Sender(packet, in, out, this);

        c.start();
    }

    public void search(String file) {
        Header h = new Header(PacketType.SEARCH);

        ParameterToSearch par = new ParameterToSearch(file);

        FishPacket packet = new FishPacket(h, par);
        Sender c = new Sender(packet, in, out, this);

        c.start();

    }

    void startStatisticsThread() {
        StatThread st = new StatThread(this, this.settings.getRefreshInterval());
        st.start();

    }

    void getStatistics() {
        Header h = new Header(PacketType.STATISTICS);

        ServerStatistics sts = new ServerStatistics(0, 0);

        FishPacket packet = new FishPacket(h, sts);
        Sender c = new Sender(packet, in, out, this);

        c.start();
    }

    void startReceiverThread() {
        new Receiver(in, out, this).start();
    }

    void setStatistics(int numClients, int numFiles) {
        this.numClients = numClients;
        this.numFiles = numFiles;
        this.setChanged();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                notifyObservers(EventEnum.NEWSTATISTICS);
            }
        });
    }

    public int getNumClients() {
        return this.numClients;
    }

    public int getNumFiles() {
        return this.numFiles;
    }

    public void setErrorMessage(String message) {
        this.lastError = message;
    }

    public String getLastErrorMessage() {
        return this.lastError;
    }

    public FishSettings getSettings() {
        return this.settings;
    }
}
