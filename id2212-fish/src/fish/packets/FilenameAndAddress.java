/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 *
 * @author alfredo
 */
public class FilenameAndAddress implements Serializable{
    
    private String filename;
    private SocketAddress address;

    public FilenameAndAddress(String filename, SocketAddress address) {
        this.address = address;
        this.filename=filename;
    }

    public String getFilename() {
        return filename;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }
    
    
    
    
    
}
