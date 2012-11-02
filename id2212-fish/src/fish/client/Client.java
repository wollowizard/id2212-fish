/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FileListPacket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author alfredo
 */
public class Client {
    public Socket s;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public ArrayList<String> filesToAdd=new ArrayList<>();
    public ArrayList<String> filesToRemove=new ArrayList<>();

    public void setSocket(Socket s) {
        this.s = s;
    }

    public void setInStream(ObjectInputStream in) {
        this.in = in;
    }

    public void setOutStream(ObjectOutputStream out) {
        this.out = out;
    }
    
    
    
    
    
    public void connect(String ip, Integer port){
        Connector c=new Connector(ip, port, this);
        c.start();
    }

    
    
    
    public synchronized void  sendFileList()  {
                
        FileListPacket packet=new FileListPacket(filesToAdd,filesToRemove);
        
        Communicator c=new Communicator(packet, in, out, this);
        c.start();
    }
    
}
