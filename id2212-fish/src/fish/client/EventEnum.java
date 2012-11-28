/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

/**
 *
 * @author alfredo
 */
public enum EventEnum {
    
    /**
     * Fired when the client connects
     */
    CONNECTED,
    /**
     * fired when new results are available, after a search
     */
    NEWRESULT,
    /**
     * fired when new statistics are received from the server
     */
    NEWSTATISTICS,
    /**
     * fired when client disconnects (for any reason)
     */
    DISCONNECT,
    /**
     * fired when a new error message must be displayed
     */
    NEWERRORMESSAGE,
    /**
     * fired when a download has completed
     */
    DOWNLOADFINISHED,
    /**
     * fired when a new list of servers is available
     */
    NEWLISTOFSERVERS
    
,
    /**
     * fired when a tentative of connection fails
     */
    CONNECTIONFAILED,
    /**
     * fired when trying to connect to some server
     */
    CONNECTINGTO}
