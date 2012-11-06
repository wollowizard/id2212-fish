/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

/**
 *
 * @author alfredo
 */
class FishFile {
    private String filename;
    private Client owner;

    public FishFile(String filename, Client owner) {
        this.filename = filename;
        this.owner = owner;
    }

    public String getFilename() {
        return filename;
    }

    public Client getOwner() {
        return owner;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
    
    
    
}
