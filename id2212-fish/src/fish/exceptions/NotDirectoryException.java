/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.exceptions;

import java.io.IOException;

/**
 *
 * @author alfredo
 */
public class NotDirectoryException extends IOException{

    /**
     *
     * @param message
     */
    public NotDirectoryException(String message) {
        super(message);
    }
    
    
    
}
