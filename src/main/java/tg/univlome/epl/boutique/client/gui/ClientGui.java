/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import tg.univlome.epl.boutique.client.utils.ClientClient;
import tg.univlome.epl.boutique.entite.Client;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class ClientGui extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel titleLabel;
    private ClientClient cClient;

    public ClientGui() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        String[] columnNames = {"Id", "Nom", "Prenom", "Date de Naissance", "CIN", "Carte Visa"};

        cClient = ClientClient.getInstance();
        List<Client> clients = cClient.lister();

        Object[][] data = new Object[clients.size()][6];
        for (int i = 0; i < clients.size(); i++) {
            Client p = clients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getNom();
            data[i][2] = p.getPrenom();
            data[i][3] = p.getDateNaissance();
            data[i][4] = p.getCin();
            data[i][5] = p.getCarteVisa();
        }
        // Créez un modèle de liste
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        titleLabel = new JLabel("Gérer les Clients");

        // Ajouter un bouton "Ajouter" en haut
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
        // Créez une boîte de dialogue pour saisir les données de la nouvelle catégorie
        JDialog dialog = new JDialog();
        dialog.setTitle("Ajouter un nouveau client");
        dialog.setLayout(new GridLayout(6, 2, 10,10));

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

        JLabel cinLabel = new JLabel("CIN :");
        JTextField cinField = new JTextField(20);
        dialog.add(cinLabel);
        dialog.add(cinField);

        JLabel visaLabel = new JLabel("Carte Visa :");
        JTextField visaField = new JTextField(20);
        dialog.add(visaLabel);
        dialog.add(visaField);

        if(action == "modifier"){
            int selectedIndex = table.getSelectedRow();
            nomTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 1)));
            prenomTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 2)));
            dateField.setDate(Date.from(((LocalDate) table.getValueAt(selectedIndex, 3)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            cinField.setText((String) table.getValueAt(selectedIndex, 4));
            visaField.setText((String) table.getValueAt(selectedIndex, 5));
        }
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.green);
        JButton cancelButton = new JButton("Annuler");
        dialog.add(okButton);
        dialog.add(cancelButton);

        // Afficher la boîte de dialogue
        dialog.setPreferredSize(new Dimension(400, 400));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        // Gestion des événements des boutons
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (action == "ajout") {
                    ajouter(nomTextField, prenomTextField, dateField, cinField, visaField);
                } else {
                    modifier(nomTextField, prenomTextField, dateField, cinField, visaField);
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

    private void ajouter(JTextField nomTextField, JTextField prenomTextField, JDateChooser dateField, JTextField cinField, JTextField visaField) {

        long id = generateNextId();
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateField.getDate()));
        String cin = cinField.getText();
        String carte = visaField.getText();

        Client client = new Client(id, nom, prenom, date, cin, carte);
        cClient.ajouter(client);

        tableModel.addRow(new Object[]{id, nom, prenom, date, cin, carte});
    }

    private void modifier(JTextField nomTextField, JTextField prenomTextField, JDateChooser dateField, JTextField cinField, JTextField visaField) {
        
        int selectedIndex = table.getSelectedRow();
        long id = (long) table.getValueAt(selectedIndex, 0);
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateField.getDate()));
        String cin = cinField.getText();
        String carte = cinField.getText();

        Client client = new Client(id, nom, prenom, date, cin, carte);
        cClient.modifier(client);

        tableModel.setValueAt(nom, selectedIndex, 1);
        tableModel.setValueAt(prenom, selectedIndex, 2);
        tableModel.setValueAt(date, selectedIndex, 3);
        tableModel.setValueAt(cin, selectedIndex, 4);
        tableModel.setValueAt(carte, selectedIndex, 5);
    }
    
    private void supprimer() {
        int choix = JOptionPane.showConfirmDialog(null,
                "Êtes-vous sûr de vouloir supprimer ce client ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            int selectedIndex = table.getSelectedRow();
            long id = (long) table.getValueAt(selectedIndex, 0);
            if (selectedIndex != -1) {
                tableModel.removeRow(selectedIndex);
                cClient.supprimer(String.valueOf(id));
            }
        }
    }

    private long generateNextId() {
        long maxId = 0;
        for (Client cli : cClient.lister()) {
            maxId = Math.max(maxId, cli.getId());
        }
        return maxId + 1;
    }
}
