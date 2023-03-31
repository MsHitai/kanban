package ru.yandex.practicum.server;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.Managers;
import ru.yandex.practicum.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private final TaskManager taskManager = Managers.getDefaultFile();
    private final HttpTaskServer server = new HttpTaskServer(taskManager);

    private final HttpClient client = HttpClient.newHttpClient();

    Gson gson = new Gson();

    private Task task;
    private SubTask subtask;
    private Epic epic;

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        server.start();
        task = new Task("testTask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        taskManager.createTask(task);

        epic = new Epic("testEpic", "testDescription", 0, Status.NEW);
        taskManager.createEpic(epic);

        subtask = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.createSubTask(subtask);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getTasks());

        assertEquals(body, tasks);
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getTask(task.getUniqueID()));

        assertEquals(body, tasks);
    }
}