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
public class FileContent extends Payload implements Serializable{

    private byte[] content;
    
    public FileContent(byte [] c){
        content=c;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
    
    
    @Override
    public String printSummary() {
        return "File : " + this.content.length + "bytes";
    }
    
}
