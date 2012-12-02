/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server.entity;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * A client as seen by the server.
 * @author alfredo
 */
public class Client {
    private ClientNetworkResources netResources;
    
    private static final String FILEALREADYINCLIENTLIST="File already in client list!";
    private static final String FILENOTINCLIENTLIST="File not in client list!";
    private Integer listeningServerPort=-1;
    
    
    /**
     * the ip of the client
     * @return the ip of the client
     */
    public String getRemoteIpAddress(){
        InetAddress addr = this.netResources.getSocket().getInetAddress();
       
        return addr.getHostAddress();
        
    }
    
    /**
     * the port of the client
     * @return the port of the client
     */
    public Integer getRemotePort(){
        InetSocketAddress addr = (InetSocketAddress)this.netResources.getSocket().getRemoteSocketAddress();
        return addr.getPort();
    }
    
    
    /**
     * Constructor
     * @param nr the net resources of the client (as sockets and the streams)
     */
    public Client(ClientNetworkResources nr){
        this.netResources=nr;
    }
    
    /**
     * gets the net resources of the client (as sockets and the streams)
     * @return the net resources of the client (as sockets and the streams)
     */
    public synchronized ClientNetworkResources getNetResources() {
        return netResources;
    }
    
   

    /**
     * sets the port the client listens on
     * @param port the port the client listens on
     */
    public void setListeningServerPort(Integer port) {
        this.listeningServerPort=port;
    }

    /**
     * gets the port the client listens on
     * @return the port the client listens on
     */
    public Integer getListeningServerPort() {
        return this.listeningServerPort;
    }
    
   
 
    
    
}
