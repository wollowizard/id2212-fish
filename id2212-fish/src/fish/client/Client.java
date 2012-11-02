/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.packets.FileListPacket;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author alfredo
 */
public class Client {
    public Socket s;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public ArrayList<File> filesToAdd=new ArrayList<>();
    public ArrayList<File> filesToRemove=new ArrayList<>();
    
    
    
    

    public void setSocket(Socket s) {
        this.s = s;
    }

    public void setInStream(ObjectInputStream in) {
        this.in = in;
    }

    public void setOutStream(ObjectOutputStream out) {
        this.out = out;
    }
    
    public void addFile(File f){

            this.filesToAdd.add(f);
        
    }
    
    public void removeFile(File f){
        
            this.filesToRemove.add(f);
       
    }
    
    public void clearFilesToAdd(){
        
            this.filesToAdd.clear();
      
    }
    
    public void clearFilesToRemove(){
        
            this.filesToRemove.clear();
       
        
    }
    
    
    
    public void connect(String ip, Integer port){
        Connector c=new Connector(ip, port, this);
        c.start();
    }

    
    
    
    public void  sendFileList()  {
             
        FileListPacket packet=new FileListPacket(filesToAdd,filesToRemove);
        
        Communicator c=new Communicator(packet, in, out, this);
        c.start();
    }
    
}
