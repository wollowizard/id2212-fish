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

    /**
     *
     * @param filename
     * @param ownerName
     */
    public FishFile(String filename, String ownerName) {
        this.filename = filename;
        this.ownerName = ownerName;
    }

    /**
     *
     * @return
     */
    public synchronized String getFilename() {
        return filename;
    }

    /**
     *
     * @return
     */
    public synchronized String getOwnerName() {
        return ownerName;
    }

    /**
     *
     * @param filename
     */
    public synchronized void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     *
     * @param owner
     */
    public synchronized void setOwner(String owner) {
        this.ownerName = owner;
    }

   
}
