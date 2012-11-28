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
public class FishPacket implements Serializable{
    private Header header;
    private Payload payload;

    /**
     *
     * @param h
     * @param fl
     */
    public FishPacket(Header h, Payload fl) {
        this.header=h;
        this.payload=fl;
    }

    /**
     *
     * @return
     */
    public Header getHeader() {
        return header;
    }

    /**
     *
     * @return
     */
    public Payload getPayload() {
        return payload;
    }

    /**
     *
     * @param header
     */
    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     *
     * @param payload
     */
    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    /**
     *
     * @return
     */
    public String printSummary() {
        return "Header: " + this.header.printSummary()+ "\nPayload: " + this.payload.printSummary();
    }
    
    
}
