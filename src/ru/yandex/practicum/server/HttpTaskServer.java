package ru.yandex.practicum.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.service.Managers;
import ru.yandex.practicum.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;

    private HttpServer httpServer;
    private Gson gson;

    private final TaskManager taskManager;

    public HttpTaskServer() {
        this.taskManager = Managers.getDefaultFile();
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handleTasks);
        gson = new Gson();
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {

            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET":
                    handleGetTasks(httpExchange);
                    break;
                case "POST":
                    handlePostTasks(httpExchange);
                    break;
                case "DELETE":
                    handleDeleteTasks(httpExchange);
                    break;
                default:
                    System.out.println(method + " метод не поддерживается. Принимаются запросы GET, POST, или DELETE");
                    httpExchange.sendResponseHeaders(405, 0);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleDeleteTasks(HttpExchange httpExchange) {


    }

    private void handlePostTasks(HttpExchange httpExchange) {

    }

    private void handleGetTasks(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        String type = pathParts[2];
        String response;

        switch (type) {
            case "task":
                if (pathParts.length == 3) {
                    response = gson.toJson(taskManager.getTasks());
                    sendText(httpExchange, response);
                } else {
                    String forId = pathParts[3].replaceFirst("id=", "").substring(1);
                    int id = parsePathId(forId);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        response = gson.toJson(taskManager.getTask(id));
                        sendText(httpExchange, response);
                    }
                }
                break;
            case "subtask":
                if (pathParts.length == 3) {
                    response = gson.toJson(taskManager.getSubtasks());
                    sendText(httpExchange, response);
                } else {
                    String forId = pathParts[3].replaceFirst("id=", "").substring(1);
                    int id = parsePathId(forId);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        response = gson.toJson(taskManager.getSubtask(id));
                        sendText(httpExchange, response);
                    }
                }
                break;
            case "epic":
                if (pathParts.length == 3) {
                    response = gson.toJson(taskManager.getEpics());
                    sendText(httpExchange, response);
                } else {
                    String forId = pathParts[3].replaceFirst("id=", "").substring(1);
                    int id = parsePathId(forId);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        response = gson.toJson(taskManager.getEpic(id));
                        sendText(httpExchange, response);
                    }
                }
                break;
            default:
                System.out.println("Тип задач не поддерживается");
                httpExchange.sendResponseHeaders(405, 0);
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}

/*
HttpClient client = HttpClient.newHttpClient();
URI url = URI.create("http://localhost:8080/tasks/task/");
HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

URI url = URI.create("http://localhost:8080/tasks/task/");
Gson gson = new Gson();
String json = gson.toJson(newTask);
final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

HttpClient client = HttpClient.newHttpClient();
URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
 */

/*
if (Pattern.matches("^/tasks/$", path) {
String response = gson.toJson(taskManager.getAll());
sendText(httpExchange, response);



if (Pattern.matches("^/tasks/task/\\d+$", path) {
String pathId = path.replaceFirst("/tasks/task/", "");
int id = parsePathId(pathId);

if (id !=-1) {
taskManager.delete(id);
sout("Удалили задачу по ид" + id);
httpExchange.sendResponseHeaders(200, 0);

else {
sout (получен некорректный ид)
httpExchange.sendResponseHeaders(405, 0);
}
 */