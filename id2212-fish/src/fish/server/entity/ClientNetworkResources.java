/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author alfredo
 */
public class ClientNetworkResources {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
     /**
     *
     * @param socket
     * @throws IOException
     */
    public ClientNetworkResources(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream (socket.getOutputStream());
    }

    
    /**
     *
     * @return
     */
    public synchronized Socket getSocket() {
        return socket;
    }

    /**
     *
     * @return
     */
    public synchronized ObjectInputStream getInStream() {
        return in;
    }

    /**
     *
     * @return
     */
    public synchronized ObjectOutputStream getOutStream() {
        return out;
    }

    /**
     *
     * @param socket
     */
    public synchronized void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @param in
     */
    public synchronized void setIn(ObjectInputStream in) {
        this.in = in;
    }

    /**
     *
     * @param out
     */
    public synchronized void setOut(ObjectOutputStream out) {
        this.out = out;
    }
    
}
