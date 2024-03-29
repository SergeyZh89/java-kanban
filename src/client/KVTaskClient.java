package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URI url;
    private final HttpClient httpClient;
    private String apiToken;

    public KVTaskClient(URI uri) {
        this.url = uri;
        this.httpClient = HttpClient.newHttpClient();
        this.apiToken = register();
    }

    public void put(String key, String json) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save/" + key + "?" + "API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<Void> response = null;
        try {
        response = client.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Ожидался ответ 200");
        }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/load/" + key + "?" + "API_TOKEN=" + apiToken))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Ожидался ответ 200");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }

    private String register() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return httpResponse.body();
    }
}
