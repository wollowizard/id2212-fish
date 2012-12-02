/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * Payload to request a download
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
     * gets the filename
     * @return the name of the file  to download
     */
    public String getFilename() {
        return filename;
    }

    /**
     * sets the file name
     * @param filename the name of the file to download
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    
    
    /**
     * prints a summary of the files to download
     * @return the file to download
     */
    @Override
    public String printSummary() {
        return this.filename;
    }
    
}
