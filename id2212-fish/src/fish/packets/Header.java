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
public class Header implements Serializable{
    private PacketType type;

    /**
     *
     * @param payloadType
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
