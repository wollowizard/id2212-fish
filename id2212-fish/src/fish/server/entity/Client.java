/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server.entity;

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
    
    
    public String getRemoteIpAddress(){
        InetSocketAddress addr = (InetSocketAddress)this.netResources.getSocket().getRemoteSocketAddress();
        return addr.getHostName();
        
    }
    
    public Integer getRemotePort(){
        InetSocketAddress addr = (InetSocketAddress)this.netResources.getSocket().getRemoteSocketAddress();
        return addr.getPort();
    }
    
    
    public Client(ClientNetworkResources nr){
        this.netResources=nr;
    }
    
    public synchronized ClientNetworkResources getNetResources() {
        return netResources;
    }
    
   

    public void setListeningServerPort(Integer port) {
        this.listeningServerPort=port;
    }

    public Integer getListeningServerPort() {
        return this.listeningServerPort;
    }
    
   
 
    
    
}
