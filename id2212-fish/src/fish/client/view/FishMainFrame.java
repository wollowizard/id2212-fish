/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.view;

import fish.client.EventEnum;
import fish.client.FishSettings;
import fish.client.controller.ClientController;
import fish.exceptions.NotDirectoryException;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/**
 *
 * @author alfredo
 */
public class FishMainFrame extends javax.swing.JFrame implements Observer {

    ClientController client;
    private FishMainPanel panel;

    /**
     * Creates new form NewJFrame
     */
    public FishMainFrame() {
        client = new ClientController();
        panel = new FishMainPanel(client);


        initComponents();

        this.panel.disableConnectedPanel();
        client.addObserver(this);



        this.setContentPane(panel);




        this.pack();
        this.validate();
        this.setResizable(false);

        //setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        Preferences prefs = Preferences.userNodeForPackage(fish.client.view.FishMainFrame.class);
        try {
            client.getSettings().setFolder(prefs.get(FishSettings.SHARED_FOLDER, "C:\\"));

        } catch (NotDirectoryException ex) {
            JOptionPane.showMessageDialog(this, "Invalid directory");
        }
        try {
            File dir = new File(".");
            client.getSettings().setDownloadFolder(prefs.get(FishSettings.DOWNLOAD_FOLDER, dir.getAbsolutePath()));
        } catch (NotDirectoryException ex) {
            JOptionPane.showMessageDialog(this, "Invalid download directory");
        }

        try {
            if (prefs.get(FishSettings.SERVER_FOLDER, null) != null) {
                client.getSettings().setServerListFile(prefs.get(FishSettings.SERVER_FOLDER, null));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Invalid server list file");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        PreferencesMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu2.setText("Edit");

        PreferencesMenuItem.setText("Preferences");
        PreferencesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreferencesMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(PreferencesMenuItem);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 476, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PreferencesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreferencesMenuItemActionPerformed
        PreferencesFrame pf = new PreferencesFrame(client);
        pf.setVisible(true);

    }//GEN-LAST:event_PreferencesMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FishMainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FishMainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FishMainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FishMainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FishMainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem PreferencesMenuItem;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        EventEnum event = (EventEnum) arg;

        this.panel.update(o, arg);


    }
}
