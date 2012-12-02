/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * The header of a FishPacket. contains the payload type (a PacketType)
 * @author alfredo
 */
public class Header implements Serializable{
    private PacketType type;

    /**
     * constructor
     * @param payloadType the type of the payload
     */
    public Header(PacketType payloadType) {
        this.type=payloadType;
    }

    /**
     *
     * @return
     */
    public PacketType getType() {
        return type;
    }

    String printSummary() {
        return this.type.toString();
    }
    
    
    
    
}
