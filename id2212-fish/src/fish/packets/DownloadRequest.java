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
public class DownloadRequest extends Payload implements Serializable{

    private String filename;

    /**
     *
     * @param filename
     */
    public DownloadRequest(String filename) {
        this.filename = filename;
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
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    
    
    /**
     *
     * @return
     */
    @Override
    public String printSummary() {
        return this.filename;
    }
    
}
