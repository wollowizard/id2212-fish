/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.packets;

import java.io.Serializable;

/**
 * An entity representing a server, with its id, address and port
 * @author alfredo
 */
public class Server implements Serializable {

    private Integer id;
    private String address;
    private Integer port;

    /**
     * Constructor
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
     * Constructor (to be used on the client side, when an id is not available and known)
     * @param address
     * @param port
     */
    public Server(String address, Integer port) {

        this.address = address;
        this.port = port;
    }

    /**
     * returns the port the server is accepting connections on (connections from the clients)
     * @return the port the server is accepting connections on (connections from the clients)
     */
    public Integer getPortForClients() {
        return port;
    }

    /**
     * returns the ip address of the server
     * @return the ip address of the server

     */
    public String getAddress() {
        return this.address;

    }

    /**
     * sets the ip address
     * @param address the ip address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * sets the port the server is operating on
     * @param port the port number
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
