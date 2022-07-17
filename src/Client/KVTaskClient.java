package Client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private transient HttpClient client;
    private String token;
    private String key;

    public HttpClient getClient() {
        return client;
    }

    public KVTaskClient(URI url) {
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            this.token = response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        this.key = key;
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = requestBuilder
                .POST(body)
                .uri(URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + getToken()))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        client.send(request, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpRequest request = requestBuilder
                .GET()
                .uri(URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + getToken()))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, handler);
        if (!response.body().equals("null")) {
            return parseJson(response.body());
        } else return null;
    }

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }

    private String parseJson(String str) {
        String json = str.substring(1, str.length() - 1);
        json = json.replaceAll("\\\\\"", "\"");
        return json;
    }
}
