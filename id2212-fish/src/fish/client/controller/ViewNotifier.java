package fish.client.controller;

import javax.swing.SwingUtilities;

/**
 *
 * @author alfredo
 */
public class ViewNotifier {
    
    // for android, get somehow the handler and call handler.post(r)
    
    /**
     *
     * @param r
     */
    public static void invokeLater(Runnable r){
        SwingUtilities.invokeLater(r);
    }
    
}
