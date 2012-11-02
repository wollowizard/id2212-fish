package fish.client.dirWatch;

import fish.client.dirWatch.DirWatcher;
import java.io.*;
import java.util.*;

public class Id2212Fish {
  public static void main(String args[]) {
      
    TimerTask task = new DirWatcher("c:/temp", "*" ) {
        @Override
      protected void onChange( File file, String action ) {
        // here we code the action on a change
        System.out.println
           ( "File "+ file.getName() +" action: " + action );
      }
    };

    Timer timer = new Timer();
    timer.schedule( task , new Date(), 1000 );
  }
}