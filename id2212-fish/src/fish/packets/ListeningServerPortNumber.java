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
public class ListeningServerPortNumber extends Payload implements Serializable {

    /**
     *
     */
    public Integer port;

    /**
     *
     * @param p
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
