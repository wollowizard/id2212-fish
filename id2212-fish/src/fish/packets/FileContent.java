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
public class FileContent extends Payload implements Serializable {

    private String name;
    private byte[] content;
    

    /**
     *
     * @param filename
     * @param c
     */
    public FileContent(String filename, byte[] c) {
        this.name = filename;
        content = c;
    }

    /**
     *
     * @return
     */
    public byte[] getContent() {
        return content;
    }

    /**
     *
     * @param content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @Override
    public String printSummary() {
        return "File " + this.name + ": " + this.content.length + "bytes";
    }
}
