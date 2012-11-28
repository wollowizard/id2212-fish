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

    /**
     *
     * @param id
     * @param address
     * @param port
     */
    public Server(Integer id, String address, Integer port) {
        this.id = id;
        this.address = address;
        this.port = port;
    }

    /**
     *
     * @param address
     * @param port
     */
    public Server(String address, Integer port) {

        this.address = address;
        this.port = port;
    }

    /**
     *
     * @return
     */
    public Integer getPortForClients() {
        return port;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return this.address;

    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @param port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
