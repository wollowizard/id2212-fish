/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.exceptions;

import java.io.IOException;

/**
 * An exception thrown if an invalid file for server list was selected
 * @author alfredo
 */
public class NotServerListFileException extends IOException {

    /**
     *constructor
     * @param msg the message
     */
    public NotServerListFileException(String msg) {
        super(msg);
        
    }
    
}
