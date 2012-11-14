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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

/**
 *
 * @author alfredo
 */
public class Client extends Observable{

    private Socket s;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<File> filesToAdd = new ArrayList<>();
    private ArrayList<File> filesToRemove = new ArrayList<>();
    private int numClients;
    private int numFiles;
    private boolean connected=false;
    private ArrayList<FilenameAndAddress> lastresult;
    
    

    public void setSocket(Socket s) {
        this.s = s;
    }

    public void setInStream(ObjectInputStream in) {
        this.in = in;
    }

    public void setOutStream(ObjectOutputStream out) {
        this.out = out;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        this.setChanged();
        
        
        SwingUtilities.invokeLater(new Runnable() {
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

    public void submitInitialFileList(String path) {

        FileWalker fw=new FileWalker(this);
        fw.walk(path);
        

    }

    public void share(String ip, Integer port) {
        Connector c = new Connector(ip, port, this);
        c.start();
    }
    
    public void unshare(){
        System.exit(1);
    }

    public void addWatcher(String folderPath) {
        TimerTask task = new DirWatcher(folderPath, "*") {
            @Override
            protected void onChange(File file, String action) {


                if (action.equals("add")) {
                    addFile(file);
                } else if (action.equals("delete")) {
                    removeFile(file);
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

    void search(String file) {
        Header h = new Header(PacketType.SEARCH);

        ParameterToSearch par = new ParameterToSearch(file);

        FishPacket packet = new FishPacket(h, par);
        Sender c = new Sender(packet, in, out, this);

        c.start();

    }
    
    void getStatistics() {
        Header h = new Header(PacketType.STATISTICS);

        ServerStatistics sts = new ServerStatistics(0,0);

        FishPacket packet = new FishPacket(h, sts);
        Sender c = new Sender(packet, in, out, this);

        c.start();
    }

    void startReceiverThread() {
        new Receiver(in, out, this).start();
    }

    void setStatistics(int numClients, int numFiles) {
        this.numClients=numClients;
        this.numFiles=numFiles;
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
}
