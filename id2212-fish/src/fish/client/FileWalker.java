/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import java.io.File;

/**
 *
 * @author alfredo
 */
public class FileWalker {
    
    private Client client;
    
    public FileWalker(Client c){
        this.client=c;
    }
    
     public void walk( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
                System.out.println( "Dir:" + f.getAbsoluteFile() );
                client.addWatcher(f.getAbsolutePath());
            }
            else {
                System.out.println( "File:" + f.getAbsoluteFile() );
                client.addFile(f);
            }
        }
        client.sendFileList();
    }
     
   
     
}
