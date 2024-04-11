/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tg.univlome.epl.boutique.client.utils;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import tg.univlome.epl.boutique.entite.Produit;
/**
 *
 * @author DELL LATITUDE 3500
 */
public class ProduitClient {
    
    private static ProduitClient instance;
    private static final String BASE_URL = "http://localhost:8080/boutique2/rs/";

    private final Client client;

    private ProduitClient() {
        this.client = ClientBuilder.newClient();
    }

    public synchronized static ProduitClient getInstance() {
        if (instance == null) {
            instance = new ProduitClient();
        }
        return instance;
    }

    public List<Produit> lister() {
        Response response = client.target(BASE_URL)
                .path("produit")
                .request()
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Produit>>() {});
        } else {
            return null;
        }
    }

    public void ajouter(Produit p) {

        client.target(BASE_URL)
                .path("produit")
                .request()
                .header("Content-Type", "application/json")
                .post(Entity.entity(p, "application/json"));
        System.out.println(p);
    }
 
    public void modifier(Produit p) {
        client.target(BASE_URL)
                .path("produit")
                .request()
                .header("Content-Type", "application/json")
                .put(Entity.entity(p, "application/json"));
    }

    public void supprimer(String id) {
        client.target(BASE_URL)
                .path("produit")
                .queryParam("id", id)
                .request()
                .delete();
    }
}
