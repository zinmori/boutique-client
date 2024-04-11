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
import tg.univlome.epl.boutique.entite.Achat;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class AchatClient {

    private static AchatClient instance;
    private static final String BASE_URL = "http://localhost:8080/boutique2/rs/";

    private final Client client;

    private AchatClient() {
        this.client = ClientBuilder.newClient();
    }

    public synchronized static AchatClient getInstance() {
        if (instance == null) {
            instance = new AchatClient();
        }
        return instance;
    }

    public List<Achat> lister() {
        Response response = client.target(BASE_URL)
                .path("achat")
                .request()
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Achat>>() {
            });
        } else {
            return null;
        }
    }
    
    public Achat recuperer(String id){
        Response response = client.target(BASE_URL)
                .path("achat")
                .path(id)
                .request()
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(Achat.class);
        } else {
            return null;
        }
    }

    public void ajouter(Achat a) {

        System.out.println(a);
        client.target(BASE_URL)
                .path("achat")
                .request()
                .header("Content-Type", "application/json")
                .post(Entity.entity(a, "application/json"));

    }

    public void modifier(Achat a) {
        client.target(BASE_URL)
                .path("achat")
                .request()
                .header("Content-Type", "application/json")
                .put(Entity.entity(a, "application/json"));
    }

    public void supprimer(String id) {
        client.target(BASE_URL)
                .path("achat")
                .queryParam("id", id)
                .request()
                .delete();
    }
}
