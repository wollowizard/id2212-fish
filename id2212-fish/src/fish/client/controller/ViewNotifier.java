package fish.client.controller;

import javax.swing.SwingUtilities;

/**
 * This class is here for compatibility reasons. it is in charge to asynchronously update the view
 * @author alfredo
 */
public class ViewNotifier {
    
    // for android, get somehow the handler and call handler.post(r)
    
    /**
     * Similar to SwingUtilities.invokeLater It calls it if swings are used. otherwise it must be implemented
     * @param r The runnable to execute (ti update the view)
     */
    public static void invokeLater(Runnable r){
        SwingUtilities.invokeLater(r);
    }
    
}
