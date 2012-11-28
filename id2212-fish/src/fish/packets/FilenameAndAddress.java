/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 *
 * @author alfredo
 */
public class FilenameAndAddress implements Serializable{
    
    private String filename;
    private String ipaddress;
    private Integer port;

    /**
     *
     * @param filename
     * @param address
     * @param port
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
