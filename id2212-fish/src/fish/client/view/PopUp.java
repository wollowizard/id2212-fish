package fish.client.view;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import fish.client.controller.ClientController;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * The popup menu to delete, download and open folder
 * @author alfredo
 */
class PopUp extends JPopupMenu {

    JMenuItem open;
    JMenuItem delete;
    JMenuItem openFolder;
    private String filename;
    private ClientController client;

    
    public PopUp(final String file, ClientController c) {

        filename = file;
        client = c;

        open = new JMenuItem("Open");
        add(open);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Desktop dt = Desktop.getDesktop();
                File f = new File(filename);
                try {
                    dt.open(f);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(open, ex.getMessage());
                }

            }
        });

        delete = new JMenuItem("Delete");
        add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File(filename);
                f.delete();
                client.downloadFolderContentChanged();
            }
        });


        openFolder = new JMenuItem("Open folder");
        add(openFolder);
        openFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop dt = Desktop.getDesktop();
                File f = new File(filename);
                String dir = f.getParent();
                File dirfile = new File(dir);
                try {
                    dt.open(dirfile);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(open, ex.getMessage());
                }
            }
        });

    }
}
