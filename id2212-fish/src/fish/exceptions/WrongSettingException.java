/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.exceptions;

/**
 * An exception thrown if wrong settings are selected
 * @author alfredo
 */
public class WrongSettingException extends Exception {

    /**
     * Constructor
     * @param msg the message
     */
    public WrongSettingException(String msg) {
        super(msg);
    }
}
