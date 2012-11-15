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
public abstract class Payload  implements Serializable{
    
    public abstract String printSummary();
    
}
