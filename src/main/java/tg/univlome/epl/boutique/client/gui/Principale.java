/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class Principale extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private final EmployeGui employeGui = new EmployeGui();
    private final AchatGui achatGui = new AchatGui();
    private final CategorieGui categorieGui = new CategorieGui();
    private final ProduitGui produitGui = new ProduitGui();
    private final ClientGui clientGui = new ClientGui();

    public Principale(String initialContent) {
        super("Gestion de la Boutique");
        init(initialContent);
        
    }
    
    private void init(String initialContent) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Création du menu à gauche
        JPanel menuPanel = createMenuPanel();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        switchToPanel(initialContent);
        setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(20, 5, 20, 5);

        String[] buttonLabels = {"Manage Employees", "Manage Customers", "Manage Products", "Manage Categories", "Record Purchase"};
        Image[] images = {
            new ImageIcon("empIcone.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH), // Placeholder icons, you can replace them
            new ImageIcon("clientIcone.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH), // Placeholder icons, you can replace them
            new ImageIcon("produitIcone.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH), // Placeholder icons, you can replace them
            new ImageIcon("catIcone.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH), // Placeholder icons, you can replace them
            new ImageIcon("achatIcone.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH), // Placeholder icons, you can replace them
        };
        for (int i = 0; i < buttonLabels.length; i++) {
            final String label = buttonLabels[i];

            JButton button = new JButton(label, new ImageIcon(images[i]));
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setPreferredSize(new Dimension(150, 50));
            //button.setBackground(Color.white);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchToPanel(label);
                }
            });

            gbc.gridy = i;
            menuPanel.add(button, gbc);
        }

        return menuPanel;
    }

    private void switchToPanel(String panelName) {
        JPanel newContent = createContentPanel(panelName);
        contentPanel.add(newContent, panelName);
        cardLayout.show(contentPanel, panelName);
    }

    private JPanel createContentPanel(String panelName) {
        JPanel content;
        if(panelName == "Manage Employees"){
            content = employeGui;
        }else if (panelName == "Manage Products"){
            content = produitGui;
        
        }else if (panelName == "Record Purchase"){
            content = achatGui;
        
        }else if (panelName == "Manage Customers"){
            content = clientGui;
        
        }else{
            content = categorieGui;
        }
        return content;
    }
}
