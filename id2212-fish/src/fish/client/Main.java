package fish.client;

import fish.client.dir.FileWalker;
import java.nio.file.NotDirectoryException;

/**
 *
 * @author alfredo
 */
public class Main {

    public static void main(String args[]) throws NotDirectoryException {
        
        FileWalker.findFile("aa cc.txt", "c:\\temp");
    }
}
