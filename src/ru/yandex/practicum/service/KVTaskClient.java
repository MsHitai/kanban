package ru.yandex.practicum.service;

import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVTaskClient {
    private final URI url;
    private final HttpClient httpClient;
    private final String apiToken;

    public KVTaskClient(URI url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        apiToken = register();
    }

    public String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "register/DEBUG")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Невозможно выполнить действие save, status code: " +
                        response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Невозможно выполнить действие save");
        }
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Невозможно выполнить действие save, status code: " +
                        response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Невозможно выполнить действие save");
        }
        System.out.println(response);
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                .GET()
                .build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Невозможно выполнить действие load, status code: " +
                        response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Невозможно выполнить действие load");
        }
        return response.body();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        URI uri = new URI("http://localhost:8078/");
        KVTaskClient client = new KVTaskClient(uri);

        client.register();
        client.put("taskManager", "ManagerHashCode");

        System.out.println(client.load("taskManager"));

        client.put("taskManager", "ManagerNewHashCode");

        System.out.println(client.load("taskManager"));

        kvServer.stop();
    }
}