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
import tg.univlome.epl.boutique.entite.Employe;

/**
 *
 * @author DELL LATITUDE 3500
 */
public class EmployeClient {
    private static EmployeClient instance;
    private static final String BASE_URL = "http://localhost:8080/boutique2/rs/";

    private final Client client;

    private EmployeClient() {
        this.client = ClientBuilder.newClient();
    }

    public synchronized static EmployeClient getInstance() {
        if (instance == null) {
            instance = new EmployeClient();
        }
        return instance;
    }

    public List<Employe> lister() {
        Response response = client.target(BASE_URL)
                .path("employe")
                .request()
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Employe>>() {
            });
        } else {
            return null;
        }
    }

    public void ajouter(Employe emp) {

        client.target(BASE_URL)
                .path("employe")
                .request()
                .header("Content-Type", "application/json")
                .post(Entity.entity(emp, "application/json"));
    }

    public void modifier(Employe emp) {
        client.target(BASE_URL)
                .path("employe")
                .request()
                .header("Content-Type", "application/json")
                .put(Entity.entity(emp, "application/json"));
    }

    public void supprimer(String id) {
        client.target(BASE_URL)
                .path("employe")
                .queryParam("id", id)
                .request()
                .delete();
    }
}
