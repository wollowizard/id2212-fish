/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.exceptions;

import java.io.IOException;

/**
 *  An exception thrown if an invalid directory was detected
 * @author alfredo
 */
public class NotDirectoryException extends IOException{

    /**
     * Constructor
     * @param message the message
     */
    public NotDirectoryException(String message) {
        super(message);
    }
    
    
    
}
