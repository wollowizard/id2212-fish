/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * Abstract class. all the payload must implement it
 * @author alfredo
 */
public abstract class Payload  implements Serializable{
    
    /**
     * prints a summary of the packet. must be implemented
     * @return
     */
    public abstract String printSummary();
    
}
