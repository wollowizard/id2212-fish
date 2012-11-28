/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.view;

import fish.client.EventEnum;
import fish.client.controller.ClientController;
import fish.exceptions.WrongSettingException;
import fish.packets.FilenameAndAddress;
import fish.packets.Server;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alfredo
 */
public class FishMainPanel extends javax.swing.JPanel {

    private ClientController client;
    private DefaultTableModel tablemodel;
    private DefaultListModel serverlistmodel = new DefaultListModel();

    /**
     * Creates new form FishMainPanel
     * @param c 
     */
    public FishMainPanel(ClientController c) {
        initComponents();

        client = c;

        tablemodel = (DefaultTableModel) downloadFolderTable.getModel();
        this.serverList.setModel(serverlistmodel);


        ResultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    String fname = (String) (ResultTable.getModel().getValueAt(row, 0)).toString();
                    String address = (String) (ResultTable.getModel().getValueAt(row, 1)).toString();
                    String port = (String) (ResultTable.getModel().getValueAt(row, 2)).toString();
                    client.startDownloadThread(fname, address, port);

                }
            }
        });
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        statusPanel.setPreferredSize(new Dimension(getWidth(), 24));
        // statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statisticsTxt.setHorizontalAlignment(SwingConstants.RIGHT);
        downloadFolderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                if (evt.getClickCount() == 2 && (SwingUtilities.isLeftMouseButton(evt))) {
                    int index = table.rowAtPoint(evt.getPoint());
                    String elementAt = (String) table.getModel().getValueAt(index, 0);
                    Desktop dt = Desktop.getDesktop();
                    File f = new File(elementAt);
                    try {
                        dt.open(f);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });
        this.downloadFolderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();

                    // get the row index that contains that coordinate
                    int rowNumber = downloadFolderTable.rowAtPoint(p);

                    // Get the ListSelectionModel of the JTable
                    ListSelectionModel model = downloadFolderTable.getSelectionModel();

                    // set the selected interval of rows. Using the "rowNumber"
                    // variable for the beginning and end selects only that one row.
                    model.setSelectionInterval(rowNumber, rowNumber);
                    String fname = (String) downloadFolderTable.getValueAt(rowNumber, 0);
                    PopUp menu = new PopUp(fname, client);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        this.searchTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }

// unused abstract methods
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        serverList = new javax.swing.JList();
        refreshServersButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ResultTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        downloadFolderTable = new javax.swing.JTable();
        statusPanel = new javax.swing.JPanel();
        ConnectionLabel = new javax.swing.JLabel();
        statisticsTxt = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 625, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );

        serverList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(serverList);

        refreshServersButton.setText("Get fresh list");
        refreshServersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshServersButtonActionPerformed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshServersButton))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(connectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshServersButton)
                        .addGap(0, 387, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Servers", jPanel5);

        jLabel1.setText("Parameter:");

        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });

        searchButton.setText("SEARCH");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        ResultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Client Address", "Client Port"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(ResultTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addGap(53, 53, 53)
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(searchButton)
                .addContainerGap(174, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Search", jPanel1);

        downloadFolderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(downloadFolderTable);
        downloadFolderTable.getColumnModel().getColumn(0).setResizable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Downloads", jPanel2);

        ConnectionLabel.setText("Disconnected");

        statisticsTxt.setText("...");

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(ConnectionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statisticsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addGap(0, 16, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statisticsTxt)
                    .addComponent(ConnectionLabel)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed

        client.search(this.searchTextField.getText());
    }//GEN-LAST:event_searchButtonActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed



        try {
            // TODO add your handling code here:
            if (!this.client.isConnected()) {
                this.client.getSettings().validateSettings();
                client.share();
            } else {
                client.unshare();
            }

        } catch (WrongSettingException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_connectButtonActionPerformed
    private void refreshServersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshServersButtonActionPerformed
        // TODO add your handling code here:

        client.refreshListOfServersRemote();
    }//GEN-LAST:event_refreshServersButtonActionPerformed

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ConnectionLabel;
    private javax.swing.JTable ResultTable;
    private javax.swing.JButton connectButton;
    private javax.swing.JTable downloadFolderTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton refreshServersButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JList serverList;
    private javax.swing.JLabel statisticsTxt;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg) {

        EventEnum event = (EventEnum) arg;

        if (event == EventEnum.CONNECTED) {
            client.getSettings().currentConnectingServer = null;
            this.enableConnectedPanel();
            this.connectButton.setText("Disconnect");
            this.ConnectionLabel.setText("Connected to: " + client.getNetData().getSocket().getRemoteSocketAddress());
            this.refreshListOfServers();

        } else if (event == EventEnum.CONNECTINGTO) {
            this.refreshListOfServers();
        } else if (event == EventEnum.DISCONNECT) {
            client.getSettings().currentConnectedServer = null;
            this.connectButton.setText("Connect");
            this.ConnectionLabel.setText("Disconnected");
            this.disableConnectedPanel();
            client.getSettings().getServerFromFile();
            client.newListOfServerReceived(client.getSettings().getCurrentServersList());


        } else if (event == EventEnum.NEWRESULT) {

            DefaultTableModel model = (DefaultTableModel) this.ResultTable.getModel();
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            ArrayList<FilenameAndAddress> files = client.getLastresult();
            if (files.isEmpty()) {

                model.addRow(new Object[]{"FILE", "NOT FOUND"});
            } else {
                for (FilenameAndAddress f : files) {
                    model.addRow(new Object[]{f.getFilename(), f.getAddress(), f.getPort()});
                }
            }

            JTableOptimizer.calcColumnWidths(ResultTable);

        } else if (event == EventEnum.NEWSTATISTICS) {
            this.statisticsTxt.setText("Num Clients: " + client.getNumClients()
                    + " Num Files: " + client.getNumFiles());
        } else if (event == EventEnum.DISCONNECT) {
            this.statisticsTxt.setText("Num Clients: " + client.getNumClients()
                    + " Num Files: " + client.getNumFiles());
        } else if (event == EventEnum.NEWERRORMESSAGE) {

            JOptionPane.showMessageDialog(this, client.getLastErrorMessage());
        } else if (event == EventEnum.DOWNLOADFINISHED) {


            while (tablemodel.getRowCount() > 0) {
                tablemodel.removeRow(0);
            }
            for (String s : client.getDownloadedFiles()) {
                System.out.println(s);
                this.tablemodel.addRow(new Object[]{s});
            }


        } else if (event == EventEnum.NEWLISTOFSERVERS) {
            refreshListOfServers();
        }

    }

    /**
     *
     */
    public void enableConnectedPanel() {
        searchButton.setEnabled(true);
        searchTextField.setEnabled(true);
        this.refreshServersButton.setEnabled(true);
        statisticsTxt.setVisible(true);
        /*
         Component[] components = this.searchPanel.getComponents();
         for (Component c : components) {
         c.setEnabled(true);
         }*/
    }

    /**
     *
     */
    public void disableConnectedPanel() {

        searchButton.setEnabled(false);
        searchTextField.setEnabled(false);
        this.refreshServersButton.setEnabled(false);
        statisticsTxt.setVisible(false);

        /*
         Component[] components = this.searchPanel.getComponents();
         for (Component c : components) {
         c.setEnabled(false);
         }*/
    }

    private void refreshListOfServers() {
        this.serverlistmodel.clear();
        ArrayList<Server> currentServers = client.getSettings().getCurrentServersList();
        for (Server s : currentServers) {
            String towrite = s.getAddress() + ":" + s.getPortForClients();
            Server conn = client.getSettings().currentConnectedServer;
            if (conn != null && s.getAddress().equals(conn.getAddress()) && s.getPortForClients().equals(conn.getPortForClients())) {
                towrite += "                  CONNECTED";
            }
            Server conn1 = client.getSettings().currentConnectingServer;
            if (conn1 != null && s.getAddress().equals(conn1.getAddress()) && s.getPortForClients().equals(conn1.getPortForClients())) {
                towrite += "                  CONNECTING";
            }

            this.serverlistmodel.addElement(towrite);
        }
    }
}
