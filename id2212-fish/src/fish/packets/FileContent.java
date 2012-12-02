/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * Payload that contains the files to download (the actual bytes)
 * @author alfredo
 */
public class FileContent extends Payload implements Serializable {

    private String name;
    private byte[] content;
    

    /**
     * The Constructor
     * @param filename the file name
     * @param c the array of bytes (content of the file)
     */
    public FileContent(String filename, byte[] c) {
        this.name = filename;
        content = c;
    }

    /**
     * Gets the array of bytes of the file
     * @return the array of bytes of the file (content)
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * sets the array of bytes of the file
     *
     * @param content the array of bytes of the file (content)
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * gets the name of the file
     * @return the name of the file
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the file
     * @param name the name of the file
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * prints the file name and its length
     * @return the file name and its length as a string
     */
    @Override
    public String printSummary() {
        return "File " + this.name + ": " + this.content.length + "bytes";
    }
}
