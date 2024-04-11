/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author DELL LATITUDE 3500
 */
public class Home extends JFrame {
    

    public Home() {
        super("Zstore Dashboard");

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("ZStore Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(0,0,255));
        headerPanel.add(title);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        String[] cardTitles = {"Manage Employees", "Manage Customers", "Manage Products", "Manage Categories", "Record Purchase"};
        String[] imageUrls = {
            "employe.jpg",
            "customer.jpg",
            "product.jpg",
            "categorie.jpeg",
            "achat.png",
        };

        for (int i = 0; i < cardTitles.length; i++) {
            JPanel cardPanel = createCardPanel(cardTitles[i], imageUrls[i]);
            cardsPanel.add(cardPanel);
        }

        mainPanel.add(cardsPanel, BorderLayout.CENTER);

        // Set up the frame
        setContentPane(mainPanel);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createCardPanel(String title, String imageUrl) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(imageUrl).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        JButton goToButton = new JButton("Go to " + title);
        //goToButton.setBackground(new Color(45,134,255));

        goToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Principale p = new Principale(title);
                dispose();
            }
        });
        
        cardPanel.add(imageLabel, BorderLayout.CENTER);
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        cardPanel.add(goToButton, BorderLayout.SOUTH);

        return cardPanel;
    }
}
