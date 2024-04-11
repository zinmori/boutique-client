/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import tg.univlome.epl.boutique.client.utils.CategorieClient;
import tg.univlome.epl.boutique.client.utils.ProduitClient;
import tg.univlome.epl.boutique.entite.Categorie;
import tg.univlome.epl.boutique.entite.Produit;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class ProduitGui extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel titleLabel;
    private ProduitClient client;

    public ProduitGui() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        String[] columnNames = {"Id", "Libelle", "Prix Unitaire", "Date de Peremption", "Categorie"};

        client = ProduitClient.getInstance();

        List<Produit> produits = client.lister();

        Object[][] data = new Object[produits.size()][5];
        for (int i = 0; i < produits.size(); i++) {
            Produit p = produits.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getLibelle();
            data[i][2] = p.getPrixUnitaire();
            data[i][3] = p.getDatePeremption();
            data[i][4] = p.getCategorie();
        }
        // Créez un modèle de liste
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        titleLabel = new JLabel("Gérer les Produits");

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

        CategorieClient cClient = CategorieClient.getInstance();
        List<Categorie> categories = cClient.lister();

        JDialog dialog = new JDialog();
        dialog.setTitle("Ajouter un nouveau produit");
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel libelleLabel = new JLabel("Libellé :");
        JTextField libelleTextField = new JTextField(20);
        dialog.add(libelleLabel);
        dialog.add(libelleTextField);

        JLabel prixLabel = new JLabel("Prix Unitaire :");
        JTextField prixTextField = new JTextField(20);
        prixTextField.setText("0");
        dialog.add(prixLabel);
        dialog.add(prixTextField);

        JLabel dateLabel = new JLabel("Date de peremption :");
        JDateChooser dateField = new JDateChooser();
        dateField.getDateEditor().setEnabled(false);
        dialog.add(dateLabel);
        dialog.add(dateField);

        JLabel categorieLabel = new JLabel("Categorie :");
        JComboBox categorieField = new JComboBox();
        for (Categorie cat : categories) {
            categorieField.addItem(cat);
        }
        dialog.add(categorieLabel);
        dialog.add(categorieField);

        if (action == "modifier") {
            int selectedIndex = table.getSelectedRow();
            libelleTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 1)));
            prixTextField.setText(String.valueOf(table.getValueAt(selectedIndex, 2)));
            dateField.setDate(Date.from(((LocalDate) table.getValueAt(selectedIndex, 3)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            categorieField.setSelectedItem((Categorie) table.getValueAt(selectedIndex, 4));
        }

        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.green);
        JButton cancelButton = new JButton("Annuler");
        dialog.add(okButton);
        dialog.add(cancelButton);

        dialog.setPreferredSize(new Dimension(400, 300));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (action == "ajout") {
                    ajouter(libelleTextField, prixTextField, dateField, categorieField);
                } else {
                    modifier(libelleTextField, prixTextField, dateField, categorieField);
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

    private void ajouter(JTextField libelleTextField, JTextField prixTextField, JDateChooser dateField, JComboBox categorieField) {

        CategorieClient cClient = CategorieClient.getInstance();
        List<Categorie> categories = cClient.lister();
        long id = generateNextId();
        String libelle = libelleTextField.getText();
        double prix = Double.parseDouble(prixTextField.getText());
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateField.getDate()));
        Categorie cat = (Categorie) categorieField.getSelectedItem();

        Produit produit = new Produit(id, libelle, prix, date, cat);
        client.ajouter(produit);
        System.out.println(produit);

        tableModel.addRow(new Object[]{id, libelle, prix, date.toString(), cat});
    }

    private void modifier(JTextField libelleTextField, JTextField prixTextField, JDateChooser dateField, JComboBox categorieField) {

        CategorieClient cClient = CategorieClient.getInstance();
        List<Categorie> categories = cClient.lister();
        int selectedIndex = table.getSelectedRow();
        long id = (long) table.getValueAt(selectedIndex, 0);
        String libelle = libelleTextField.getText();
        double prix = Double.parseDouble(prixTextField.getText());
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateField.getDate()));
        Categorie cat = (Categorie) categorieField.getSelectedItem();

        // Créez une nouvelle catégorie
        Produit produit = new Produit(id, libelle, prix, date, cat);
        client.modifier(produit);

        // Ajoutez la nouvelle catégorie à la table
        tableModel.setValueAt(libelle, selectedIndex, 1);
        tableModel.setValueAt(prix, selectedIndex, 2);
        tableModel.setValueAt(date.toString(), selectedIndex, 3);
        tableModel.setValueAt(cat, selectedIndex, 4);

    }

    private void supprimer() {
        int choix = JOptionPane.showConfirmDialog(null,
                "Êtes-vous sûr de vouloir supprimer ce produit ?",
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
        for (Produit pro : client.lister()) {
            maxId = Math.max(maxId, pro.getId());
        }
        return maxId + 1;
    }
}
