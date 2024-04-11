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

/**
 *
 * @author DELL LATITUDE 3500
 */
public class ClientClient {
    private static ClientClient instance;
    private static final String BASE_URL = "http://localhost:8080/boutique2/rs/";

    private final Client client;

    private ClientClient() {
        this.client = ClientBuilder.newClient();
    }

    public synchronized static ClientClient getInstance() {
        if (instance == null) {
            instance = new ClientClient();
        }
        return instance;
    }

    public List<tg.univlome.epl.boutique.entite.Client> lister() {
        Response response = client.target(BASE_URL)
                .path("client")
                .request()
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<tg.univlome.epl.boutique.entite.Client>>() {
            });
        } else {
            return null;
        }
    }

    public void ajouter(tg.univlome.epl.boutique.entite.Client cli) {
        System.out.println(cli);
        client.target(BASE_URL)
                .path("client")
                .request()
                .header("Content-Type", "application/json")
                .post(Entity.entity(cli, "application/json"));
    }

    public void modifier(tg.univlome.epl.boutique.entite.Client cli) {
        client.target(BASE_URL)
                .path("client")
                .request()
                .header("Content-Type", "application/json")
                .put(Entity.entity(cli, "application/json"));
    }

    public void supprimer(String id) {
        client.target(BASE_URL)
                .path("client")
                .queryParam("id", id)
                .request()
                .delete();
    }
}
