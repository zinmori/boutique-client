/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import tg.univlome.epl.boutique.client.utils.ProduitClient;
import tg.univlome.epl.boutique.entite.Produit;
import tg.univlome.epl.boutique.entite.ProduitAchete;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class ProduitAcheteGui extends JDialog {

    private JComboBox produitField;
    private JSpinner quantiteField;
    private JTextField remiseField;

    private ProduitAchete produitAchete;

    public ProduitAcheteGui(JDialog parent) {
        super(parent, "Ajouter un produit acheté", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        

        JPanel panel = new JPanel(new GridLayout(4, 2));

        ProduitClient client = ProduitClient.getInstance();
        List<Produit> produits = client.lister();
        
        JLabel produitLabel = new JLabel("Produit:");
        produitField = new JComboBox();
        for (Produit produit : produits) {
            produitField.addItem(produit);
        }
        panel.add(produitLabel);
        panel.add(produitField);

        JLabel quantiteLabel = new JLabel("Quantité:");
        SpinnerNumberModel s = new SpinnerNumberModel();
        s.setMinimum(1);
        s.setValue(1);
        quantiteField = new JSpinner(s);
        panel.add(quantiteLabel);
        panel.add(quantiteField);

        JLabel remiseLabel = new JLabel("Remise:");
        remiseField = new JTextField();
        remiseField.setText("0");
        panel.add(remiseLabel);
        panel.add(remiseField);

        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validerChamps()) {
                    produitAchete = new ProduitAchete(
                            (Produit) produitField.getSelectedItem(),
                            Double.parseDouble(remiseField.getText()),
                            (int)quantiteField.getValue()
                    );
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(parent, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(ajouterButton);
        

        add(panel);
        setVisible(true);
    }

    private boolean validerChamps() {
        return !remiseField.getText().isEmpty();
    }

    public ProduitAchete getProduitAchete() {
        setVisible(false);
        ProduitAchete pa = produitAchete; 
        dispose();
        return pa;
    }
}

