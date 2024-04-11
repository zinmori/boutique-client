/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import tg.univlome.epl.boutique.client.utils.EmployeClient;
import tg.univlome.epl.boutique.entite.Employe;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class EmployeGui extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel titleLabel;
    private EmployeClient client;

    public EmployeGui() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        String[] columnNames = {"Id", "Nom", "Prenom", "Date de Naissance", "CNSS"};

        client = EmployeClient.getInstance();
        List<Employe> employes = client.lister();

        Object[][] data = new Object[employes.size()][5];
        for (int i = 0; i < employes.size(); i++) {
            Employe emp = employes.get(i);
            data[i][0] = emp.getId();
            data[i][1] = emp.getNom();
            data[i][2] = emp.getPrenom();
            data[i][3] = emp.getDateNaissance();
            data[i][4] = emp.getCnss();
        }
        // Créez un modèle de liste
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        titleLabel = new JLabel("Gérer les Employes");

        addButton = new JButton("Ajouter");
        addButton.setBackground(Color.green);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInputDialog("ajout");
            }
        });

        // Ajouter des boutons "Modifier" et "Supprimer" en bas
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
                // Vérifier si une ligne est sélectionnée
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    // Activer le bouton "Modifier" lorsque la ligne est sélectionnée
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    // Désactiver le bouton "Modifier" lorsque aucune ligne n'est sélectionnée
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });

        // Ajouter des composants au panneau
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
        dialog.setTitle("Ajouter un nouveau employe");
        dialog.setLayout(new GridLayout(5, 2,10,10));

        JLabel nomLabel = new JLabel("Nom :");
        JTextField nomTextField = new JTextField(20);
        dialog.add(nomLabel);
        dialog.add(nomTextField);

        JLabel prenomLabel = new JLabel("Prenom :");
        JTextField prenomTextField = new JTextField(20);
        dialog.add(prenomLabel);
        dialog.add(prenomTextField);

        JLabel dateLabel = new JLabel("Date de Naissance :");
        JDateChooser dateField = new JDateChooser();
        dateField.getDateEditor().setEnabled(false);
        dialog.add(dateLabel);
        dialog.add(dateField);

        JLabel cnssLabel = new JLabel("CNSS :");
        JTextField cnssField = new JTextField(20);
        dialog.add(cnssLabel);
        dialog.add(cnssField);
        
        if(action == "modifier"){
            int selectedIndex = table.getSelectedRow();
            nomTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 1)));
            prenomTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 2)));
            dateField.setDate(Date.from(((LocalDate) table.getValueAt(selectedIndex, 3)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            cnssField.setText((String) table.getValueAt(selectedIndex, 4));
        }

        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.green);
        JButton cancelButton = new JButton("Annuler");
        dialog.add(okButton);
        dialog.add(cancelButton);

        // Afficher la boîte de dialogue
        dialog.setPreferredSize(new Dimension(400, 300));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        // Gestion des événements des boutons
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (action == "ajout") {
                    ajouter(nomTextField, prenomTextField, dateField, cnssField);
                } else {
                    modifier(nomTextField, prenomTextField, dateField, cnssField);
                }
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    private void ajouter(JTextField nomTextField, JTextField prenomTextField, JDateChooser dateField, JTextField cnssField) {

        long id = generateNextId();
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateField.getDate()));
        String cnss = cnssField.getText();

        Employe emp = new Employe(id, nom, prenom, date, cnss);
        client.ajouter(emp);

        tableModel.addRow(new Object[]{id, nom, prenom, date, cnss});
    }

    private void modifier(JTextField nomTextField, JTextField prenomTextField, JDateChooser dateField, JTextField cnssField) {

        int selectedIndex = table.getSelectedRow();
        long id = (long) table.getValueAt(selectedIndex, 0);
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateField.getDate()));
        String cnss = cnssField.getText();

        Employe emp = new Employe(id, nom, prenom, date, cnss);
        client.modifier(emp);

        tableModel.setValueAt(nom, selectedIndex, 1);
        tableModel.setValueAt(prenom, selectedIndex, 2);
        tableModel.setValueAt(date, selectedIndex, 3);
        tableModel.setValueAt(cnss, selectedIndex, 4);
    }

    private void supprimer() {
        int choix = JOptionPane.showConfirmDialog(null,
                "Êtes-vous sûr de vouloir supprimer cet employe ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            int selectedIndex = table.getSelectedRow();
            long id = (long) table.getValueAt(selectedIndex, 0);
            if (selectedIndex != -1) {
                tableModel.removeRow(selectedIndex);
                client.supprimer(String.valueOf(id));
            }
        }
    }

    private long generateNextId() {
        long maxId = 0;
        for (Employe emp : client.lister()) {
            maxId = Math.max(maxId, emp.getId());
        }
        return maxId + 1;
    }
}
