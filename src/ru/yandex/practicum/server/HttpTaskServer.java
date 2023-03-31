package ru.yandex.practicum.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;

    private final HttpServer httpServer;
    private final Gson gson;

    private final TaskManager taskManager;

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

    private void handleDeleteTasks(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2) {
            System.out.println("Нужно указать тип задач для удаления");
            httpExchange.sendResponseHeaders(405, 0);
            return;
        }
        String type = pathParts[2];

        switch (type) {
            case "task":
                if (pathParts.length == 3) {
                    taskManager.deleteAllTasks();
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    int id = parsePathId(pathParts[3]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        taskManager.deleteTask(id); // имеет ли смысл обрабатывать вариант, если id не найден, то код ответа не 200?
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                }
                break;
            case "subtask":
                if (pathParts.length == 3) {
                    taskManager.deleteAllSubTasks();
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    int id = parsePathId(pathParts[3]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        taskManager.deleteSubTask(id);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                }
                break;
            case "epic":
                if (pathParts.length == 3) {
                    taskManager.deleteAllEpics();
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    int id = parsePathId(pathParts[3]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        taskManager.deleteEpic(id);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                }
                break;
            default:
                System.out.println("Тип задач не поддерживается");
                httpExchange.sendResponseHeaders(405, 0);
        }
    }

    private void handlePostTasks(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2) {
            System.out.println("Нужно указать тип задач для создания");
            httpExchange.sendResponseHeaders(405, 0);
            return;
        }
        String type = pathParts[2];

        switch (type) {
            case "task":
                if (pathParts.length == 3) {
                    String taskString = readText(httpExchange);
                    Task task = gson.fromJson(taskString, Task.class);
                    taskManager.createTask(task);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    int id = parsePathId(pathParts[3]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        String taskString = readText(httpExchange);
                        Task task = gson.fromJson(taskString, Task.class);
                        taskManager.updateTask(task);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                }
                break;
            case "subtask":
                if (pathParts.length == 3) {
                    String subtaskString = readText(httpExchange);
                    SubTask subtask = gson.fromJson(subtaskString, SubTask.class);
                    taskManager.createSubTask(subtask);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    int id = parsePathId(pathParts[3]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        String subtaskString = readText(httpExchange);
                        SubTask subtask = gson.fromJson(subtaskString, SubTask.class);
                        taskManager.updateSubTask(subtask);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                }
                break;
            case "epic":
                if (pathParts.length == 3) {
                    String epicString = readText(httpExchange);
                    Epic epic = gson.fromJson(epicString, Epic.class);
                    taskManager.createEpic(epic);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    int id = parsePathId(pathParts[3]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        String epicString = readText(httpExchange);
                        Epic epic = gson.fromJson(epicString, Epic.class);
                        taskManager.updateEpic(epic);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                }
                break;
            default:
                System.out.println("Тип задач не поддерживается");
                httpExchange.sendResponseHeaders(405, 0);
        }
    }

    private void handleGetTasks(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        String response;
        if (pathParts.length == 2) {
            response = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(httpExchange, response);
            return;
        }
        String type = pathParts[2];

        switch (type) {
            case "task":
                if (pathParts.length == 3) {
                    response = gson.toJson(taskManager.getTasks());
                    sendText(httpExchange, response);
                } else {
                    int id = parsePathId(pathParts[3]);
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
                    return;
                }
                if (pathParts[3].equals("epic")) { // ситуация где нам нужно вернуть подзадачи по эпику
                    if (pathParts[4] == null) {
                        System.out.println("Идентификатор не может быть пустым");
                        httpExchange.sendResponseHeaders(405, 0);
                        return;
                    }
                    int id = parsePathId(pathParts[4]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                        return;
                    }
                    Epic epic = taskManager.getEpic(id);
                    response = gson.toJson(taskManager.getSubTasksByEpics(epic));
                    sendText(httpExchange, response);
                } else {
                    int id = parsePathId(pathParts[3]); // ситуация где нам нужно обновить нужную подзадачу по id
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
                    int id = parsePathId(pathParts[3]);
                    if (id == -1) {
                        System.out.println("Некорректный идентификатор");
                        httpExchange.sendResponseHeaders(405, 0);
                    } else {
                        response = gson.toJson(taskManager.getEpic(id));
                        sendText(httpExchange, response);
                    }
                }
                break;
            case "history":
                response = gson.toJson(taskManager.getHistory());
                sendText(httpExchange, response);
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