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

    public FilenameAndAddress(String filename, String address,Integer port) {
        this.ipaddress = address;
        this.filename=filename;
        this.port=port;
    }

    public String getFilename() {
        return filename;
    }

    public String getAddress() {
        return ipaddress;
    }

    public Integer getPort(){
        return port;
    }
    
    public void setPort(Integer port){
        this.port=port;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setAddress(String address) {
        this.ipaddress = address;
    }
    
    
    
    
    
}
