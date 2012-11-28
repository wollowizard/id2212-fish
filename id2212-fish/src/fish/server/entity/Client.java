/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server.entity;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
 * @author alfredo
 */
public class Client {
    private ClientNetworkResources netResources;
    
    private static final String FILEALREADYINCLIENTLIST="File already in client list!";
    private static final String FILENOTINCLIENTLIST="File not in client list!";
    private Integer listeningServerPort=-1;
    
    
    /**
     *
     * @return
     */
    public String getRemoteIpAddress(){
        InetAddress addr = this.netResources.getSocket().getInetAddress();
       
        return addr.getHostAddress();
        
    }
    
    /**
     *
     * @return
     */
    public Integer getRemotePort(){
        InetSocketAddress addr = (InetSocketAddress)this.netResources.getSocket().getRemoteSocketAddress();
        return addr.getPort();
    }
    
    
    /**
     *
     * @param nr
     */
    public Client(ClientNetworkResources nr){
        this.netResources=nr;
    }
    
    /**
     *
     * @return
     */
    public synchronized ClientNetworkResources getNetResources() {
        return netResources;
    }
    
   

    /**
     *
     * @param port
     */
    public void setListeningServerPort(Integer port) {
        this.listeningServerPort=port;
    }

    /**
     *
     * @return
     */
    public Integer getListeningServerPort() {
        return this.listeningServerPort;
    }
    
   
 
    
    
}
