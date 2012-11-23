/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.controller;

import fish.client.EventEnum;
import fish.client.FishSettings;
import fish.client.dir.DirWatcher;
import fish.client.dir.FileWalker;
import fish.exceptions.NotDirectoryException;
import fish.packets.DownloadRequest;
import fish.packets.FileContent;
import fish.packets.FileList;
import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.ListeningServerPortNumber;
import fish.packets.PacketType;
import fish.packets.ParameterToSearch;
import fish.packets.ServerStatistics;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alfredo
 */
public class ClientController extends Observable {

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
    private Integer listeningThreadPort = -2;
    private ArrayList<String> downloadFolderContent = new ArrayList<>();
    private long DOWNLOADFOLDERREFRESHTIME = 10000; //10 SECONDS
    private long SHAREFOLDERREFRESHTIME = 2000; //2 SECONDS

    public void setListeningThreadPort(Integer listeningThreadPort) {
        this.listeningThreadPort = listeningThreadPort;
    }

    public Integer getListeningThreadPort() {
        return listeningThreadPort;
    }

    public ClientController() {
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

    public void startConnection() {
        try {
            setConnected();
            startReceiverThread();
            startListeningServerThread();
            submitInitialFileList();

            startStatisticsThread();

        } catch (NotDirectoryException ex) {

            setErrorMessage(ex.getMessage());
            ViewNotifier.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setChanged();
                    notifyObservers(EventEnum.NEWERRORMESSAGE);
                }
            });


        }
    }

    public void setConnected() {

        synchronized (this) {
            this.connected = true;

        }

        ViewNotifier.invokeLater(new Runnable() {
            @Override
            public void run() {
                setChanged();
                notifyObservers(EventEnum.CONNECTED);
            }
        });

    }

    public void setDisconnected() {
        synchronized (this) {
            this.connected = false;
            this.setChanged();
        }


        ViewNotifier.invokeLater(new Runnable() {
            @Override
            public void run() {
                notifyObservers(EventEnum.DISCONNECT);
            }
        });
    }

    public ArrayList<FilenameAndAddress> getLastresult() {
        return lastresult;
    }

    public void setLastresult(ArrayList<FilenameAndAddress> lastresult) {


        this.lastresult = lastresult;
        this.setChanged();
        ViewNotifier.invokeLater(new Runnable() {
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


        this.addWatcher(this.settings.getFolder());

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
            ViewNotifier.invokeLater(new Runnable() {
                @Override
                public void run() {
                    notifyObservers(EventEnum.DISCONNECT);
                }
            });



        } catch (IOException ex) {
            Logger.getLogger(ClientController.class
                    .getName()).log(Level.SEVERE, null, ex);
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
        timer.schedule(task, new Date(), SHAREFOLDERREFRESHTIME);

    }

    public void startListeningServerThread() {
        ListeningServerThread thread = new ListeningServerThread(this);
        thread.start();

    }

    public void sendListeningServerPort() {
        Header h = new Header(PacketType.LISTENINGSERVERPORT);

        ListeningServerPortNumber pn = new ListeningServerPortNumber(this.listeningThreadPort);
        FishPacket packet = new FishPacket(h, pn);

        SynchronousSender c = new SynchronousSender(packet, out, this);
        c.send();

    }

    public void sendFileList() {


        Header h = new Header(PacketType.ADDFILE);

        FileList fl = new FileList(filesToAdd, filesToRemove);
        FishPacket packet = new FishPacket(h, fl);
        Sender c = new Sender(packet, out, this);

        c.start();
    }

    public void search(String file) {
        Header h = new Header(PacketType.SEARCH);

        ParameterToSearch par = new ParameterToSearch(file);

        FishPacket packet = new FishPacket(h, par);
        Sender c = new Sender(packet, out, this);

        c.start();

    }

    public void startDownloadThread(final String fname, final String address, final String port) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                download(fname, address, port);
            }
        });
        thread.start();
    }

    public void download(String fname, String address, String port) {
        Header h = new Header(PacketType.DOWNLOAD);
        DownloadRequest p = new DownloadRequest(fname);


        FishPacket packet = new FishPacket(h, p);
        int ppp = Integer.parseInt(port);
        try {
            Socket sock = new Socket(address, ppp);
            ObjectOutputStream out1 = new ObjectOutputStream(sock.getOutputStream());

            Sender c = new Sender(packet, out1, this);

            c.start();
            ObjectInputStream in1 = new ObjectInputStream(sock.getInputStream());
            FishPacket fp = (FishPacket) in1.readObject();
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

    public void startStatisticsThread() {
        StatThread st = new StatThread(this, this.settings.getRefreshInterval());
        st.start();

    }

    public void getStatistics() {
        Header h = new Header(PacketType.STATISTICS);

        ServerStatistics sts = new ServerStatistics(0, 0);

        FishPacket packet = new FishPacket(h, sts);
        Sender c = new Sender(packet, out, this);

        c.start();
    }

    void startReceiverThread() {
        new Receiver(in, this).start();
    }

    void setStatistics(int numClients, int numFiles) {
        this.numClients = numClients;
        this.numFiles = numFiles;
        this.setChanged();
        ViewNotifier.invokeLater(new Runnable() {
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
        this.setChanged();
        this.lastError = message;

    }

    public String getLastErrorMessage() {
        return this.lastError;
    }

    public FishSettings getSettings() {
        return this.settings;
    }

    public ArrayList<String> getDownloadedFiles() {
        return this.downloadFolderContent;
    }

    public void manageDownloadReceived(FishPacket fp) {

        if (fp.getHeader().getType() == PacketType.FILENOLONGERAVAILABLE) {
            System.out.println("File no longer available");
        } else if (fp.getHeader().getType() == PacketType.FILECONTENT) {

            FileContent fc = (FileContent) fp.getPayload();
            try {
                byte[] bytes = fc.getContent();
                String downFold = getSettings().getDownloadFolder();
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
                    downloadFolderContentChanged();

                    System.out.println("file created");



                }
            } catch (IOException ex) {
                Logger.getLogger(ClientController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Unrecognized packet received as download");
        }
    }

    void startDownloadFolderWatcher() {
        this.downloadFolderContentChanged();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                downloadFolderContentChanged();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, new Date(), DOWNLOADFOLDERREFRESHTIME);

    }

    private void downloadFolderContentChanged() {
        File folder = new File(this.getSettings().getDownloadFolder());

        File[] listOfFiles = folder.listFiles();
        this.downloadFolderContent.clear();
        for (File f : listOfFiles) {
            this.downloadFolderContent.add(f.getAbsolutePath());
        }

        this.setChanged();
        ViewNotifier.invokeLater(new Runnable() {
            @Override
            public void run() {

                notifyObservers(EventEnum.DOWNLOADFINISHED);
            }
        });

    }
}
