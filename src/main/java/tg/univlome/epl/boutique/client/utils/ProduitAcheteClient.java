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
import tg.univlome.epl.boutique.entite.ProduitAchete;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class ProduitAcheteClient {
    private static ProduitAcheteClient instance;
    private static final String BASE_URL = "http://localhost:8080/boutique2/rs/";

    private final Client client;

    private ProduitAcheteClient() {
        this.client = ClientBuilder.newClient();
    }

    public synchronized static ProduitAcheteClient getInstance() {
        if (instance == null) {
            instance = new ProduitAcheteClient();
        }
        return instance;
    }

    public List<ProduitAchete> lister() {
        Response response = client.target(BASE_URL)
                .path("produitachete")
                .request()
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<ProduitAchete>>() {
            });
        } else {
            return null;
        }
    }

    public void ajouter(ProduitAchete pa) {

        client.target(BASE_URL)
                .path("produitachete")
                .request()
                .header("Content-Type", "application/json")
                .post(Entity.entity(pa, "application/json"));
    }

    public void modifier(ProduitAchete pa) {
        client.target(BASE_URL)
                .path("produitachete")
                .request()
                .header("Content-Type", "application/json")
                .put(Entity.entity(pa, "application/json"));
    }

    public void supprimer(String id1, String id2) {
        client.target(BASE_URL)
                .path("produitachete")
                .queryParam("idAchat", id1)
                .queryParam("idProduit", id2)
                .request()
                .delete();
    }
}
