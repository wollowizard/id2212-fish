/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * Enumeration of type of payloads
 * @author alfredo
 */
public enum PacketType  implements Serializable{
    /**
     *
     */
    ADDFILE,
    /**
     *
     */
    SEARCH,
    /**
     *
     */
    SEARCHFILENOTFOUND,
    /**
     *
     */
    FILEFOUND,
    /**
     *
     */
    DOWNLOAD,
    /**
     *
     */
    FILENOTFOUND,
    /**
     *
     */
    STATISTICS,
    /**
     *
     */
    LISTENINGSERVERPORT,
    /**
     *
     */
    LISTENINGSERVERPORTACK,
    /**
     *
     */
    FILENOLONGERAVAILABLE,
    /**
     *
     */
    FILECONTENT,
    /**
     *
     */
    LISTOFSERVERS}
