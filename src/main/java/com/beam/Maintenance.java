package com.beam;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class Maintenance extends JFrame {
    public JTable table1;
    public JPanel panel1;
    public JButton addFileButton;
    public JButton removeFileButton;
    public JButton refreshFilesButton;
    public JButton mainMenuButton;
    private JLabel NIlabel;


    public static void main(String[] args) {
    }

    //constructor
    public Maintenance(){
        //Check if index file exist, if not , create it and display message
        //NIlabel = New Index label (Display "New Index was created")
        if(!MaintUtils.checkIndexFile()){
            MaintUtils.createNewIndexFile();
            NIlabel.setVisible(true);
        }
        //Maintenance JFrame properties
        this.setTitle("Maintenance");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setSize(800, 601);
        this.setLocationRelativeTo(null);
        this.setVisible(false);
        //Maintenance->JTable properties
        table1.setVisible(true);
        table1.setFillsViewportHeight(true);
        table1.setAutoCreateRowSorter(true);
        final DefaultTableModel dtm=new DefaultTableModel() {
            //make all cells uneditable
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };
        //Maintenance->JTable columns
        dtm.addColumn("File Name");
        dtm.addColumn("Date Modified");
        dtm.addColumn("Size");
        dtm.addColumn("File Path");
        //set Default Table Model
        table1.setModel(dtm);

        //Main menu button
        // Action: Set visible false to maintenance window
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Maintenance.this.setVisible(false);
            }
        });

        final JFileChooser fc = new JFileChooser();
        //addFileButton action listener

        addFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Add File Functionality

                    //Handle open button action.
                    if (e.getSource() == addFileButton) {
                        int returnVal = fc.showOpenDialog(Maintenance.this);
                        //If user picks a file
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            //Add words to index
                            // # TO-DO: create loop of indexable words and add to file.
                            //Get information to populate JTable - file name, last modified, size, and absolutepath.
                            try {
                                Path file2 = Paths.get(fc.getSelectedFile().getAbsolutePath());
                                BasicFileAttributes attr =
                                        Files.readAttributes(file2, BasicFileAttributes.class);
                            //adds row to jtable
                            dtm.addRow(new Object[] {file.getName(),attr.lastModifiedTime(), attr.size() + " bytes",file.getAbsolutePath()});
                                MaintUtils.addToIndex(Maintenance.this, file);
                            } catch (IOException error) {
                                error.printStackTrace();
                            }


                        } else {
                            //if openening/selecting file is cancelled
                            JOptionPane.showMessageDialog(null, "Add file operation cancelled by user");
                        }
                    }
                }
        });

        refreshFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Refresh File Functionality
            }
        });
        removeFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Remove File Functionality
                int rowSelection;
                rowSelection = table1.getSelectedRow();
                dtm.removeRow(rowSelection);
                //Below for debugging only
                JOptionPane.showMessageDialog(null, "Removed row " + rowSelection);
            }
        });

    }

}
