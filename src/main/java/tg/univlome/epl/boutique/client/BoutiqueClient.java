/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package tg.univlome.epl.boutique.client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import tg.univlome.epl.boutique.client.gui.Home;
//import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
//import com.jtattoo.plaf.texture.TextureLookAndFeel;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class BoutiqueClient {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
            SwingUtilities.invokeLater(() -> new Home());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
