package ru.yandex.practicum.service;

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
    private String apiToken;

    public KVTaskClient(URI url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();

    }

    public void register() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "register/DEBUG")).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
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
/*
Далее проверьте код клиента в main. Для этого запустите KVServer, создайте экземпляр KVTaskClient.
Затем сохраните значение под разными ключами и проверьте, что при запросе возвращаются нужные
данные. Удостоверьтесь, что если изменить значение, то при повторном вызове вернётся уже не
старое, а новое
 */