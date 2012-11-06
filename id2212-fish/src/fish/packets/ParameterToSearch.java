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
public class ParameterToSearch extends Payload implements Serializable{

    private String parameter;

    public ParameterToSearch(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    
    
    
    @Override
    public String printSummary() {
        String res="Search: ";
        res+= parameter;
        return res;
        
    }
    
}
