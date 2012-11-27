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
public class Server implements Serializable {

    private Integer id;
    private String address;
    private Integer port;

    public Server(Integer id, String address, Integer port) {
        this.id = id;
        this.address = address;
        this.port = port;
    }

    public Server(String address, Integer port) {

        this.address = address;
        this.port = port;
    }

    public Integer getPortForClients() {
        return port;
    }

    public String getAddress() {
        return this.address;

    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
