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
    

    public FileContent(String filename, byte[] c) {
        this.name = filename;
        content = c;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String printSummary() {
        return "File " + this.name + ": " + this.content.length + "bytes";
    }
}
