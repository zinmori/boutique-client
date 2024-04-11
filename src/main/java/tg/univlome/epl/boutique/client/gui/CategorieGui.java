/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import tg.univlome.epl.boutique.client.utils.CategorieClient;
import tg.univlome.epl.boutique.entite.Categorie;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class CategorieGui extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel titleLabel;
    private CategorieClient client;

    public CategorieGui() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel("Gérer les Categories");

        String[] columnNames = {"Id", "libelle", "description"};

        client = CategorieClient.getInstance();
        List<Categorie> categories = client.lister();

        Object[][] data = new Object[categories.size()][3];
        for (int i = 0; i < categories.size(); i++) {
            Categorie cat = categories.get(i);
            data[i][0] = cat.getId();
            data[i][1] = cat.getLibelle();
            data[i][2] = cat.getDescription();
        }
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        addButton = new JButton("Ajouter");
        addButton.setBackground(Color.green);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInputDialog("ajout");
            }
        });

        editButton = new JButton("Modifier");
        editButton.setBackground(Color.yellow);
        editButton.setEnabled(false);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInputDialog("modifier");
            }
        });

        deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(Color.red);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimer();
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        northPanel.add(titleLabel);
        northPanel.add(addButton);
        add(northPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void showInputDialog(String action) {

        JDialog dialog = new JDialog();
        if (action == "ajout") {
            dialog.setTitle("Ajouter une nouvelle catégorie");
        } else {
            dialog.setTitle("Modifier une categorie");
        }
        dialog.setLayout(new GridLayout(3, 2,10,10));

        JLabel libelleLabel = new JLabel("Libellé :");
        JTextField libelleTextField = new JTextField(20);
        dialog.add(libelleLabel);
        dialog.add(libelleTextField);

        JLabel descriptionLabel = new JLabel("Description :");
        JTextField descriptionTextField = new JTextField(20);
        dialog.add(descriptionLabel);
        dialog.add(descriptionTextField);

        if(action == "modifier"){
            int selectedIndex = table.getSelectedRow();
            libelleTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 1)));
            descriptionTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 2)));
        }
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.green);
        JButton cancelButton = new JButton("Annuler");
        dialog.add(okButton);
        dialog.add(cancelButton);

        dialog.setPreferredSize(new Dimension(400, 200));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (action == "ajout"){
                    ajouter(dialog, libelleTextField, descriptionTextField);
                }else{
                    modifier(dialog, libelleTextField, descriptionTextField);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    private void ajouter(JDialog dialog, JTextField libelleTextField, JTextField descriptionTextField) {
        int id = generateNextId();
        String libelle = libelleTextField.getText();
        String description = descriptionTextField.getText();

        Categorie categorie = new Categorie(id, libelle, description);
        client.ajouter(categorie);

        tableModel.addRow(new Object[]{id, libelle, description});

        dialog.dispose();
    }

    private void modifier(JDialog dialog, JTextField libelleTextField, JTextField descriptionTextField) {
        int selectedIndex = table.getSelectedRow();
        int id = (int) table.getValueAt(selectedIndex, 0);
        String libelle = libelleTextField.getText();
        String description = descriptionTextField.getText();

        Categorie categorie = new Categorie(id, libelle, description);
        client.modifier(categorie);

        tableModel.setValueAt(libelle,selectedIndex,0);
        tableModel.setValueAt(description,selectedIndex,1);

        dialog.dispose();
    }

    private void supprimer() {
        int choix = JOptionPane.showConfirmDialog(null,
                "Êtes-vous sûr de vouloir supprimer cette categorie ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            int selectedIndex = table.getSelectedRow();
            int id = (int) table.getValueAt(selectedIndex, 0);
            if (selectedIndex != -1) {
                tableModel.removeRow(selectedIndex);
                client.supprimer(String.valueOf(id));
            }
        }
    }
    private int generateNextId() {
        int maxId = 0;
        for (Categorie cat : client.lister()) {
            maxId = Math.max(maxId, cat.getId());
        }
        return maxId + 1;
    }
}
