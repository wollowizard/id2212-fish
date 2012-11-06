/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
package fish.client;

import fish.client.dirWatch.DirWatcher;
import java.io.Console;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author alfredo
 */

/*
public class Main {

    static Client c;

    public static void main(String args[]) {
        String path = "c:\\temp";
        String ip = "localhost";
        String portString;
        


        if (args.length > 3) {
            System.out.println("Incorrect usage. Path, ip, port");
            System.exit(1);
        }
        if (args.length == 1) {

            portString = args[0];
        } else if (args.length == 2) {

            ip = args[0];
            portString = args[1];
        } else if (args.length == 3) {
            path = args[0];
            ip = args[1];
            portString = args[2];
        }
        while(1==1){
        Console console = System.console();
        String input = console.readLine("Enter command:");

        if (input.equals("connect")) {
            connect();
        } else if (input.equals("share")) {
            share(path);
        }
        else if (input.equals("exit")) {
            unshare(path);
            System.exit(0);
        }
        }

    }

    private static void connect() {

        c = new Client();
        c.connect("localhost", 1234);

    }

    private static void share(String path) {


        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles) {
            c.filesToAdd.add(f);
            c.sendFileList();
        }

        TimerTask task = new DirWatcher("c:/temp", "*") {
            @Override
            protected void onChange(File file, String action) {


                if (action.equals("add")) {
                    c.addFile(file);
                } else if (action.equals("delete")) {
                    c.removeFile(file);
                }
                c.sendFileList();
                System.out.println("File " + file.getName() + " action: " + action);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(), 1000);
    }

    private static void unshare(String path) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
*/