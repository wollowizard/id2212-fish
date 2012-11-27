/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

/**
 *
 * @author alfredo
 */
public class FishFile {

    private String filename;
    private String ownerName;

    public FishFile(String filename, String ownerName) {
        this.filename = filename;
        this.ownerName = ownerName;
    }

    public synchronized String getFilename() {
        return filename;
    }

    public synchronized String getOwnerName() {
        return ownerName;
    }

    public synchronized void setFilename(String filename) {
        this.filename = filename;
    }

    public synchronized void setOwner(String owner) {
        this.ownerName = owner;
    }

   
}
