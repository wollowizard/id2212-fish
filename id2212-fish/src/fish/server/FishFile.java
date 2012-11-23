/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.net.SocketAddress;

/**
 *
 * @author alfredo
 */
public class FishFile {

    private String filename;
    private Client owner;

    public FishFile(String filename, Client owner) {
        this.filename = filename;
        this.owner = owner;
    }

    public synchronized String getFilename() {
        return filename;
    }

    public synchronized Client getOwner() {
        return owner;
    }

    public synchronized void setFilename(String filename) {
        this.filename = filename;
    }

    public synchronized void setOwner(Client owner) {
        this.owner = owner;
    }

    public synchronized SocketAddress getOwnerRemoteAddress() {

        return this.owner.getNetResources().getSocket().getRemoteSocketAddress();
    }
}
