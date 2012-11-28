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

    /**
     *
     * @param parameter
     */
    public ParameterToSearch(String parameter) {
        this.parameter = parameter;
    }

    /**
     *
     * @return
     */
    public String getParameter() {
        return parameter;
    }

    /**
     *
     * @param parameter
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    
    
    
    /**
     *
     * @return
     */
    @Override
    public String printSummary() {
        String res="Search: ";
        res+= parameter;
        return res;
        
    }
    
}
