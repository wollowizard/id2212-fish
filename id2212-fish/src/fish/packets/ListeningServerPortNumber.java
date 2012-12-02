/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * this packet is exchanged when a client tells the server which port it is listening on
 * @author alfredo
 */
public class ListeningServerPortNumber extends Payload implements Serializable {

    /**
     * the port the client is listening on
     */
    public Integer port;

    /**
     * Constructor
     * @param p the port number
     */
    public ListeningServerPortNumber(Integer p) {
        this.port = p;
    }

    /**
     *
     * @return
     */
    public Integer getPortNumber() {

        return this.port;
    }

    /**
     *
     * @return
     */
    @Override
    public String printSummary() {
        return "ListeningServerPortNumber: " + port;
    }
}
