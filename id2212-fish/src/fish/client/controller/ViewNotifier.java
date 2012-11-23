package fish.client.controller;

import javax.swing.SwingUtilities;

/**
 *
 * @author alfredo
 */
public class ViewNotifier {
    public static void invokeLater(Runnable r){
        SwingUtilities.invokeLater(r);
    }
    
}
