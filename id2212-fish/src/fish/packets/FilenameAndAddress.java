/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * Contains a file name and the location where the client can retrieve it
 * (so the address of the other peer and the port that peer is accepting on)
 * @author alfredo
 */
public class FilenameAndAddress implements Serializable{
    
    private String filename;
    private String ipaddress;
    private Integer port;

    /**
     * Constructor
     * @param filename the name of the file
     * @param address the ip address of the peer
     * @param port the port the peer is listening on
     */
    public FilenameAndAddress(String filename, String address,Integer port) {
        this.ipaddress = address;
        this.filename=filename;
        this.port=port;
    }

    /**
     *
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return ipaddress;
    }

    /**
     *
     * @return
     */
    public Integer getPort(){
        return port;
    }
    
    /**
     *
     * @param port
     */
    public void setPort(Integer port){
        this.port=port;
    }
    /**
     *
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.ipaddress = address;
    }
    
    
    
    
    
}
