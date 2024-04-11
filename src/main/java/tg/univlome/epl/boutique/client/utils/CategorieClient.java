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
import tg.univlome.epl.boutique.entite.Categorie;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class CategorieClient {

    private static CategorieClient instance;
    private static final String BASE_URL = "http://localhost:8080/boutique2/rs/";

    private final Client client;

    private CategorieClient() {
        this.client = ClientBuilder.newClient();
    }

    public synchronized static CategorieClient getInstance() {
        if (instance == null) {
            instance = new CategorieClient();
        }
        return instance;
    }

    public List<Categorie> lister() {
        Response response = client.target(BASE_URL)
                .path("categorie")
                .request()
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Categorie>>() {
            });
        } else {
            return null;
        }
    }

    public void ajouter(Categorie cat) {

        client.target(BASE_URL)
                .path("categorie")
                .request()
                .header("Content-Type", "application/json")
                .post(Entity.entity(cat, "application/json"));
    }

    public void modifier(Categorie cat) {
        client.target(BASE_URL)
                .path("categorie")
                .request()
                .header("Content-Type", "application/json")
                .put(Entity.entity(cat, "application/json"));
    }

    public void supprimer(String id) {
        client.target(BASE_URL)
                .path("categorie")
                .queryParam("id", id)
                .request()
                .delete();
    }
}
