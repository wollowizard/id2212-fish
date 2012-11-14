/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

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
    
     public ClientNetworkResources(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream (socket.getOutputStream());
    }

    
    public synchronized Socket getSocket() {
        return socket;
    }

    public synchronized ObjectInputStream getInStream() {
        return in;
    }

    public synchronized ObjectOutputStream getOutStream() {
        return out;
    }

    public synchronized void setSocket(Socket socket) {
        this.socket = socket;
    }

    public synchronized void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public synchronized void setOut(ObjectOutputStream out) {
        this.out = out;
    }
    
}
