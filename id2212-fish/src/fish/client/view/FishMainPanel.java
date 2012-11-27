/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client.view;

import fish.client.EventEnum;
import fish.client.controller.ClientController;
import fish.packets.FilenameAndAddress;
import fish.packets.Server;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alfredo
 */
public class FishMainPanel extends javax.swing.JPanel {

    private ClientController client;
    private DefaultListModel listmodel = new DefaultListModel();
    private DefaultListModel serverlistmodel = new DefaultListModel();

    /**
     * Creates new form FishMainPanel
     */
    public FishMainPanel(ClientController c) {
        initComponents();
        client = c;

        this.jList1.setModel(listmodel);
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


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        serverList = new javax.swing.JList();
        refreshServersButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ResultTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        statisticsTxt = new javax.swing.JLabel();

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(refreshServersButton)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(refreshServersButton)
                .addContainerGap(541, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );

        jTabbedPane1.addTab("Servers", jPanel5);

        jLabel1.setText("Parameter:");

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
                .addContainerGap(154, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Search", jPanel1);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Downloads", jPanel2);

        statisticsTxt.setText("...");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statisticsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(538, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(statisticsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(496, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Statistics", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed

        client.search(this.searchTextField.getText());
    }//GEN-LAST:event_searchButtonActionPerformed

    private void refreshServersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshServersButtonActionPerformed
        // TODO add your handling code here:

        client.refreshListOfServers();
    }//GEN-LAST:event_refreshServersButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ResultTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
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
    // End of variables declaration//GEN-END:variables

    public void update(Observable o, Object arg) {

        EventEnum event = (EventEnum) arg;

        if (event == EventEnum.CONNECTED) {

            this.enableConnectedPanel();


        } else if (event == EventEnum.CONNECTINGTO) {

            this.refreshListOfServers();

        } else if (event == EventEnum.DISCONNECT) {

            this.disableConnectedPanel();

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

        } else if (event == EventEnum.NEWSTATISTICS) {
            this.statisticsTxt.setText("Num Clients: " + client.getNumClients()
                    + " Num Files: " + client.getNumFiles());
        } else if (event == EventEnum.DISCONNECT) {
            this.statisticsTxt.setText("Num Clients: " + client.getNumClients()
                    + " Num Files: " + client.getNumFiles());
        } else if (event == EventEnum.NEWERRORMESSAGE) {
            JOptionPane.showMessageDialog(this, client.getLastErrorMessage());
        } else if (event == EventEnum.DOWNLOADFINISHED) {

            this.listmodel.clear();
            for (String s : client.getDownloadedFiles()) {
                this.listmodel.addElement(s);
            }
        } else if (event == EventEnum.NEWLISTOFSERVERS) {
            refreshListOfServers();
        }

    }

    public void enableConnectedPanel() {
        searchButton.enable();
        searchTextField.enable();
        /*
         Component[] components = this.searchPanel.getComponents();
         for (Component c : components) {
         c.setEnabled(true);
         }*/
    }

    public void disableConnectedPanel() {

        searchButton.disable();
        searchTextField.disable();
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
            Server conn = client.getSettings().currentServer;
            /*if (s.getAddress().equals(conn.getAddress()) && s.getPortForClients().equals(conn.getPortForClients())) {
                towrite += "CONNECTING!!!!!!!!!!";
            }*/
            this.serverlistmodel.addElement(towrite);
        }
    }
}
