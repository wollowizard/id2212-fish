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
import fish.packets.FileList;
import fish.packets.FilenameAndAddress;
import fish.packets.FishPacket;
import fish.packets.Header;
import fish.packets.ListOfServer;
import fish.packets.ListeningServerPortNumber;
import fish.packets.PacketType;
import fish.packets.ParameterToSearch;
import fish.packets.Server;
import fish.packets.ServerStatistics;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
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

    private ClientNetworkData netData = new ClientNetworkData();
    private ArrayList<File> filesToAdd = new ArrayList<>();
    private ArrayList<File> filesToRemove = new ArrayList<>();
    private int numClients;
    private int numFiles;
    private boolean connected = false;
    private ArrayList<FilenameAndAddress> lastresult;
    private FishSettings settings;
    private String lastError = "";
    private ArrayList<String> downloadFolderContent = new ArrayList<>();
    private ArrayList<Integer> numbers;
    private Integer index;
    Timer timer;

    /**
     *
     * @param listeningThreadPort
     */
    public void setListeningThreadPort(Integer listeningThreadPort) {
        this.netData.setListeningThreadPort(listeningThreadPort);
    }

    /**
     *
     */
    public ClientController() {
        this.settings = new FishSettings(this);
        
        this.refreshListOfServersFromFile();


    }

    public void forceChange() {
        this.setChanged();
        notifyObservers(EventEnum.DOWNLOADEDFILE);
    }
    
    public boolean isConnected() {
        synchronized (this) {
            return connected;
        }
    }

    /**
     *
     */
    public void startConnection() {
        setConnected();
        sendServerPortAndStartServer();
        startReceiverThread();
        try {
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

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     * @return
     */
    public ArrayList<FilenameAndAddress> getLastresult() {
        return lastresult;
    }

    /**
     *
     * @param lastresult
     */
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

    /**
     *
     * @param f
     */
    public void addFile(File f) {

        this.filesToAdd.add(f);

    }

    /**
     *
     * @param f
     */
    public void removeFile(File f) {

        this.filesToRemove.add(f);

    }

    /**
     *
     */
    public void clearFilesToAdd() {

        this.filesToAdd.clear();
    }

    /**
     *
     */
    public void clearFilesToRemove() {

        this.filesToRemove.clear();

    }

    /**
     *
     * @throws NotDirectoryException
     */
    public void submitInitialFileList() throws NotDirectoryException {


        this.addWatcher(this.settings.getFolder());

        FileWalker fw = new FileWalker(this);
        fw.walk(this.settings.getFolder());


    }

    /**
     *
     */
    public void share() {
        getSettings().getServerFromFile();

        numbers = new ArrayList<>();
        for (int i = 0; i < getSettings().getCurrentServersList().size(); i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        index = 0;
        tryNextServer();

    }

    /**
     *
     */
    public void tryNextServer() {
        if (index < numbers.size()) {

            Integer i = numbers.get(index);

            Server s = getSettings().getCurrentServersList().get(i);

            index++;

            Connector c = new Connector(this, s);
            c.start();

        } else {
            this.setChanged();
            notifyObservers(EventEnum.NEWERRORMESSAGE);
        }

    }

    /**
     *
     */
    public void unshare() {
        try {
            this.netData.getSocket().close();
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

    /**
     *
     * @param folderPath
     */
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
        timer.schedule(task, new Date(), getSettings().getSharedFolderRefreshTime());

    }

    /**
     *
     * @param ss
     */
    public void startListeningServerThread(ServerSocket ss) {
        ListeningServerThread thread = new ListeningServerThread(this, ss);
        thread.start();

    }

    /**
     *
     */
    public void sendFileList() {


        Header h = new Header(PacketType.ADDFILE);

        FileList fl = new FileList(filesToAdd, filesToRemove);
        FishPacket packet = new FishPacket(h, fl);
        Sender c = new Sender(packet, netData.getOutStream(), this);

        c.start();
    }

    /**
     *
     * @param file
     */
    public void search(String file) {
        Header h = new Header(PacketType.SEARCH);

        ParameterToSearch par = new ParameterToSearch(file);

        FishPacket packet = new FishPacket(h, par);
        Sender c = new Sender(packet, netData.getOutStream(), this);

        c.start();

    }

    /**
     *
     * @param fname
     * @param address
     * @param port
     */
    public void startDownloadThread(final String fname, final String address, final String port) {


        DownloadThread thread = new DownloadThread(this, fname, address, port);
        thread.start();
    }

    /**
     *
     */
    public void startStatisticsThread() {
        StatThread st = new StatThread(this, this.settings.getRefreshInterval());
        st.start();

    }

    /**
     *
     */
    public void getStatistics() {
        Header h = new Header(PacketType.STATISTICS);

        ServerStatistics sts = new ServerStatistics(0, 0);

        FishPacket packet = new FishPacket(h, sts);
        Sender c = new Sender(packet, netData.getOutStream(), this);

        c.start();
    }

    void startReceiverThread() {
        new Receiver(netData.getInStream(), this).start();
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

    /**
     *
     * @return
     */
    public int getNumClients() {
        return this.numClients;
    }

    /**
     *
     * @return
     */
    public int getNumFiles() {
        return this.numFiles;
    }

    /**
     *
     * @param message
     */
    public void setErrorMessage(String message) {
        this.setChanged();
        this.lastError = message;

    }

    /**
     *
     * @return
     */
    public String getLastErrorMessage() {
        return this.lastError;
    }

    /**
     *
     * @return
     */
    public FishSettings getSettings() {
        return this.settings;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getDownloadedFiles() {
        return this.downloadFolderContent;
    }

    void startDownloadFolderWatcher() {
        timer = new Timer();
        this.downloadFolderContentChanged();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                downloadFolderContentChanged();
            }
        };

        timer.schedule(task, new Date(), getSettings().getDownloadFolderRefreshTime());

    }

    /**
     *
     */
    public void downloadFolderContentChanged() {

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

    /**
     *
     */
    public void sendListeningServerPort() {
        Header h = new Header(PacketType.LISTENINGSERVERPORT);

        ListeningServerPortNumber pn = new ListeningServerPortNumber(this.netData.getListeningThreadPort());
        FishPacket packet = new FishPacket(h, pn);

        SynchronousSender c = new SynchronousSender(packet, netData.getOutStream(), this);
        c.send();

    }

    void waitForListeningServerPortAck() throws IOException, ClassNotFoundException {
        FishPacket fp = (FishPacket) netData.getInStream().readObject();
        if (fp.getHeader().getType() != PacketType.LISTENINGSERVERPORTACK) {
            throw new IOException("Listening ack not received");
        }


    }

    private void sendServerPortAndStartServer() {
        try {
            ServerSocket serverSocket = null;
            serverSocket = new ServerSocket(0);

            setListeningThreadPort(serverSocket.getLocalPort());
            sendListeningServerPort();
            waitForListeningServerPortAck();
            System.out.println("Listening on " + netData.getListeningThreadPort());

            startListeningServerThread(serverSocket);

        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(ListeningServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param servers
     */
    public void newListOfServerReceived(ArrayList<Server> servers) {
        try {
            this.getSettings().updateTextFileListOfServers(servers);
            this.getSettings().getServerFromFile();

            this.setChanged();
            ViewNotifier.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setChanged();
                    notifyObservers(EventEnum.NEWLISTOFSERVERS);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    public void refreshListOfServersRemote() {

        Header h = new Header(PacketType.LISTOFSERVERS);

        FishPacket packet = new FishPacket(h, new ListOfServer(new ArrayList<Server>()));

        Sender c = new Sender(packet, netData.getOutStream(), this);
        c.start();

    }

    /**
     *
     * @return
     */
    public ClientNetworkData getNetData() {
        return this.netData;
    }

    /**
     *
     */
    public void restartDownloadFolderWatcher() {
        if (timer != null) {
            timer.cancel();
        }

        this.startDownloadFolderWatcher();
    }

    /**
     *
     */
    public void refreshListOfServersFromFile() {
        settings.getServerFromFile();
        this.newListOfServerReceived(settings.getCurrentServersList());
    }
}
