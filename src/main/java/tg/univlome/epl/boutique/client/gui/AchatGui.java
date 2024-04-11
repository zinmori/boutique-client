/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import tg.univlome.epl.boutique.client.utils.*;
import tg.univlome.epl.boutique.entite.*;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class AchatGui extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel titleLabel;
    private AchatClient client;
    private final ClientClient cClient;
    private final EmployeClient empClient;
    private final ProduitAcheteClient pClient;

    public AchatGui() {
        cClient = ClientClient.getInstance();
        empClient = EmployeClient.getInstance();
        pClient = ProduitAcheteClient.getInstance();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel("Gérer les Achats");

        String[] columnNames = {"Id", "Date d'achat", "Produits Achetés", "Remise", "Employé", "Client", "Total A Payer"};

        client = AchatClient.getInstance();
        List<Achat> achats = client.lister();

        Object[][] data = new Object[achats.size()][7];
        for (int i = 0; i < achats.size(); i++) {
            Achat a = achats.get(i);
            data[i][0] = a.getId();
            data[i][1] = a.getDate();
            data[i][2] = a.getProduits();
            data[i][3] = a.getRemiseTotale();
            data[i][4] = a.getEmploye();
            data[i][5] = a.getClient();
            data[i][6] = a.getTotalAPayer();
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

    private void ajouter(JComboBox empField, JComboBox cliField, JTextField remiseField, JList produitsList) {
        
        long id = generateNextId();
        LocalDate date = LocalDate.now();
        Employe emp = (Employe) empField.getSelectedItem();
        Client cli = (Client) cliField.getSelectedItem();
        double remise = Double.parseDouble(remiseField.getText());
        List<ProduitAchete> listPa = new ArrayList<>();
        DefaultListModel<ProduitAchete> pModel = (DefaultListModel<ProduitAchete>) produitsList.getModel();
        int i;
        for (i = 0; i < pModel.size(); i++) {
            ProduitAchete p = pModel.getElementAt(i);
            listPa.add(p);
        }

        Achat achat = new Achat(id, date, remise, emp, cli, listPa);
        for (ProduitAchete produitAchete : listPa) {
            produitAchete.setAchat(achat.getId());
            pClient.ajouter(produitAchete);
        }

        client.ajouter(achat);
        tableModel.addRow(new Object[]{id, date, listPa, achat.getRemiseTotale(), emp, cli, achat.getTotalAPayer()});
    }

    private void modifier(JComboBox empField, JComboBox cliField, JTextField remiseField, JList produitsList) {

        int selectedIndex = table.getSelectedRow();
        long id = (long) table.getValueAt(selectedIndex, 0);
        LocalDate date = LocalDate.now();
        Employe emp = (Employe) empField.getSelectedItem();
        Client cli = (Client) cliField.getSelectedItem();
        double remise = Double.parseDouble(remiseField.getText());
        List<ProduitAchete> listPa = new ArrayList<>();
        DefaultListModel<ProduitAchete> pModel = (DefaultListModel<ProduitAchete>) produitsList.getModel();
        
        Achat oldAchat = client.recuperer(String.valueOf(id));
        for (ProduitAchete produit : oldAchat.getProduits()) {
            pClient.supprimer(String.valueOf(id), String.valueOf(produit.getProduit().getId()));
        }
        
        for (int i = 0; i < pModel.size(); i++) {
            ProduitAchete p = pModel.getElementAt(i);
            p.setAchat(id);
            listPa.add(p);
            pClient.ajouter(p);
        }

        Achat achat = new Achat(id, date, remise, emp, cli, listPa);
        client.modifier(achat);
        
        tableModel.setValueAt(date, selectedIndex, 1);
        tableModel.setValueAt(listPa, selectedIndex, 2);
        tableModel.setValueAt(achat.getRemiseTotale(), selectedIndex, 3);
        tableModel.setValueAt(emp, selectedIndex, 4);
        tableModel.setValueAt(cli, selectedIndex, 5);
        tableModel.setValueAt(achat.getTotalAPayer(), selectedIndex, 6);

    }

    private void supprimer() {
        int choix = JOptionPane.showConfirmDialog(null,
                "Êtes-vous sûr de vouloir supprimer cet employé ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            int selectedIndex = table.getSelectedRow();
            String id = String.valueOf(table.getValueAt(selectedIndex, 0));
            tableModel.removeRow(selectedIndex);
            Achat achat = client.recuperer(id);
            client.supprimer(id);
            for (ProduitAchete pa : achat.getProduits()) {
                pClient.supprimer(id, String.valueOf(pa.getProduit().getId()));
            }
        }   
    }

    private void showInputDialog(String action) {

        List<Employe> employes = empClient.lister();
        List<Client> clients = cClient.lister();

        JDialog dialog = new JDialog();
        dialog.setLayout(new GridLayout(6, 2,10,10));
        if (action == "ajout") {
            dialog.setTitle("Enregistrer un nouvel achat");
        } else {
            dialog.setTitle("Modifier un achat");
        }

        JLabel paLabel = new JLabel(" Produits Achetés :");
        DefaultListModel produitsModel = new DefaultListModel<>();
        JList produitsList = new JList<>(produitsModel);
        JScrollPane scroll = new JScrollPane(produitsList);
        dialog.add(paLabel);
        dialog.add(scroll);

        JButton ajouterPa = new JButton(" Ajouter un produit");
        JButton supprimerPa = new JButton("Supprimer le produit");
        supprimerPa.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (produitsList.getSelectedIndex() != -1){
                    produitsModel.remove(produitsList.getSelectedIndex());
                }
            }
        });
        supprimerPa.setBackground(new Color(255, 80,80));
        dialog.add(supprimerPa);
        dialog.add(ajouterPa);

        ajouterPa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProduitAcheteGui paDialog = new ProduitAcheteGui(dialog);
                ProduitAchete pa = paDialog.getProduitAchete();
                produitsModel.addElement(pa);
            }
        });

        JLabel remiseLabel = new JLabel(" Remise :");
        JTextField remiseField = new JTextField(20);
        remiseField.setText("0");
        dialog.add(remiseLabel);
        dialog.add(remiseField);

        JLabel empLabel = new JLabel(" Employé :");
        JComboBox empField = new JComboBox();
        for (Employe emp : employes) {
            empField.addItem(emp);
        }
        dialog.add(empLabel);
        dialog.add(empField);

        JLabel cliLabel = new JLabel(" Client :");
        JComboBox cliField = new JComboBox();
        for (Client cli : clients) {
            cliField.addItem(cli);
        }
        dialog.add(cliLabel);
        dialog.add(cliField);

        if(action == "modifier"){
            int selectedIndex = table.getSelectedRow();
            produitsModel.addAll((List<ProduitAchete>) table.getValueAt(selectedIndex, 2));
            remiseField.setText(String.valueOf(table.getValueAt(selectedIndex, 3)));
            empField.setSelectedItem((Employe) table.getValueAt(selectedIndex, 4));
            cliField.setSelectedItem((Client) table.getValueAt(selectedIndex, 5));
        }
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.green);
        JButton cancelButton = new JButton("Annuler");
        dialog.add(okButton);
        dialog.add(cancelButton);

        dialog.setPreferredSize(new Dimension(500, 600));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (action == "ajout") {
                    ajouter(empField, cliField, remiseField, produitsList);
                } else {
                    modifier(empField, cliField, remiseField, produitsList);
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
    private long generateNextId() {
        long maxId = 0;
        for (Achat achat : client.lister()) {
            maxId = Math.max(maxId, achat.getId());
        }
        return maxId + 1;
    }
}
