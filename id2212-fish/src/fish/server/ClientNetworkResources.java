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

    
    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getInStream() {
        return in;
    }

    public ObjectOutputStream getOutStream() {
        return out;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }
    
}
