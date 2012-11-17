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

    public FishPacket(Header h, Payload fl) {
        this.header=h;
        this.payload=fl;
    }

    public Header getHeader() {
        return header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String printSummary() {
        return "Header: " + this.header.printSummary()+ "\nPayload: " + this.payload.printSummary();
    }
    
    
}
