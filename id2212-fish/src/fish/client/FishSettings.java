/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import java.io.File;
import java.nio.file.NotDirectoryException;

/**
 *
 * @author alfredo
 */
public class FishSettings {

    private Integer refreshInterval = 5000;
    private final static Integer MINREFRESHINTERVAL = 1000;
    private final static String MINVALUEERROR = "Minimum value: " + MINREFRESHINTERVAL.toString();
    private final static String INVALIDREFRESHVALUE = "Invalid value: you must digit a number greater than 1000";
    private final static String INVALIDFOLDER = "Invalid folder";
    private final static String INVALIDPORT = "Invalid port number";
    private String folder = "C:\\temp";
    private Integer port = 1234;
    private String ipAddress = "localhost";
    private Integer connectionTimeout=1000;
    private Integer MINIMUMCONNECTIONTIMEOUT=1000;
    private String INVALIDCONNECTIONTIMEOUT="Invalid Connection timeout. Minimum value: " + MINIMUMCONNECTIONTIMEOUT.toString();

    public FishSettings(Client aThis) {
    }

    public void setRefreshInterval(String s) throws NumberFormatException {
        try {
            Integer refrInt = Integer.parseInt(s);
            if (refrInt < MINREFRESHINTERVAL) {
                throw new NumberFormatException(MINVALUEERROR);
            }
            refreshInterval = refrInt;


        } catch (NumberFormatException e) {
            String msg = MINVALUEERROR;
            if (e.getMessage().compareTo(msg) != 0) {
                msg = INVALIDREFRESHVALUE;

            }
            throw new NumberFormatException(msg);
        }
    }

    public void setIpAddress(String text) {
        this.ipAddress = text;
    }

    public void setPort(String text) {
        try {
            Integer p = Integer.parseInt(text);
            if (p < 1024 || p > 65535) {
                throw new NumberFormatException(INVALIDPORT);
            }
            port = p;


        } catch (NumberFormatException e) {

            throw new NumberFormatException(INVALIDPORT);
        }
    }

    public Integer getPort() {
        return this.port;
    }

     public void setConnectionTimeout(String text) {
        try {
            Integer p = Integer.parseInt(text);
            if (p < MINIMUMCONNECTIONTIMEOUT) {
                throw new NumberFormatException(INVALIDCONNECTIONTIMEOUT);
            }
            connectionTimeout = p;


        } catch (NumberFormatException e) {

            throw new NumberFormatException(INVALIDCONNECTIONTIMEOUT);
        }
    }

    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }
    
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setFolder(String text) throws NotDirectoryException {
        try {
            File f = new File(text);
            if (!f.isDirectory()) {
                throw new NotDirectoryException(INVALIDFOLDER);
            } else {
                this.folder = text;
            }
        } catch (Exception ex) {
            throw new NotDirectoryException(INVALIDFOLDER);
        }

    }

    public String getFolder() {
        return this.folder;
    }

    Integer getRefreshInterval() {
        return this.refreshInterval;
    }

    public void validateSettings() throws Exception {
        setRefreshInterval(refreshInterval.toString());
        setFolder(this.folder);
        setIpAddress(ipAddress);
        setPort(getPort().toString());
        setConnectionTimeout(connectionTimeout.toString());
    }
}
