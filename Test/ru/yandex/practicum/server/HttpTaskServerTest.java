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

    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getPrioritizedTasks());

        assertEquals(body, tasks);
    }

    @Test
    void shouldGetSubtasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getSubtasks());

        assertEquals(body, tasks);
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getSubtask(subtask.getUniqueID()));

        assertEquals(body, tasks);
    }

    @Test
    void shouldGetEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getEpics());

        assertEquals(body, tasks);
    }

    @Test
    void shouldGetEpicsSubtasks () throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getSubTasksByEpics(epic));

        assertEquals(body, tasks);
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getHistory());

        assertEquals(body, tasks);
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        String tasks = gson.toJson(taskManager.getEpic(epic.getUniqueID()));

        assertEquals(body, tasks);
    }

    @Test /* должны получить 405 код ошибки, если не указать ид эпика в пути */
    void shouldGet405ErrorForGettingEpicsSubtasksWithEmptyId () throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);
    }

    @Test /* должны получить ошибку, если указать вместо ид букву */
    void shouldGetErrorWithNotNumberId() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);
    }

    @Test /* должны получить ошибку, если указать неверный путь после /tasks */
    void shouldGetErrorWithWrongPath() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/blooper");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);
    }

    @Test /* должны получить ошибку, если указать неверный метод */
    void shouldGetErrorWithWrongMethod() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        String someTask = gson.toJson(new Task("Test", "description", 0, Status.NEW));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(405, code);
    }

    @Test /* должны получить ошибку, если не указать тип задачи для создания */
    void shouldGetErrorIfTypeOfTaskIsNotSpecifiedForPOST() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        String someTask = gson.toJson(new Task("Test", "description", 0, Status.NEW));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);
    }

    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        String someTask = gson.toJson(new Task("Test", "description", 0, Status.NEW));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    void shouldCreateSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String someTask = gson.toJson(new SubTask("Test", "description", 0, Status.NEW, 2));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(2, taskManager.getSubtasks().size());
    }

    @Test
    void shouldCreateEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String someTask = gson.toJson(new Epic("Test", "description", 0, Status.NEW));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(2, taskManager.getEpics().size());
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/1");
        String someTask = gson.toJson(new Task("Test", "description", 1, Status.DONE));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(Status.DONE, taskManager.getTask(1).getStatus());
    }

    @Test
    void shouldUpdateSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/3");
        String someTask = gson.toJson(new SubTask("Test", "description", 3, Status.DONE, 2));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(Status.DONE, taskManager.getSubtask(3).getStatus());
    }

    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/2");
        String someTask = gson.toJson(new Epic("Test", "changed description", 2, Status.NEW));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        String description = taskManager.getEpic(2).getDescription();

        assertEquals("changed description", description);
    }

    @Test /* должны получить ошибку, если указать неправильный тип задачи для создания / обновления */
    void shouldGetErrorWithWrongTypeOfTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/oops");
        String someTask = gson.toJson(new Task("Test", "description", 0, Status.NEW));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);
    }

    @Test /* должны получить ошибку, если указать вместо цифры ид указать букву */
    void shouldGetErrorWhenUpdatingTaskWithWrongId() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/a");
        String someTask = gson.toJson(new Task("Test", "description", 1, Status.DONE));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);

        assertEquals(Status.NEW, taskManager.getTask(1).getStatus());
    }

    @Test /* должны получить ошибку, если указать неправильный номер ид для конкретной задачи */
    void shouldGetErrorIfUpdateWithWrongNumberId() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/2");
        String someTask = gson.toJson(new Task("Test", "description", 1, Status.DONE));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(someTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);

        assertEquals(Status.NEW, taskManager.getTask(1).getStatus());
    }

    @Test /* должны получить ошибку, если не указать тип задачи для удаления */
    void shouldGetErrorIfTypeOfTaskIsNotSpecifiedForDelete() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);
    }

    @Test
    void shouldDeleteAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void shouldDeleteTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void shouldDeleteAllSubtasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void shouldDeleteSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void shouldDeleteAllEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void shouldDeleteEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(0, taskManager.getEpics().size());
    }

    @Test /* подзадача не удалится с неверным идентификатором */
    void shouldNotDeleteSubtaskWithWrongId() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(200, code);

        assertEquals(1, taskManager.getSubtasks().size());
    }

    @Test /* должны получить ошибку, если указать букву вместо цифры ид для удаления */
    void shouldGetErrorWhenDeleteNotNumberId() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/a");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);

        assertEquals(1, taskManager.getSubtasks().size());
    }

    @Test /* должны получить ошибку, если указать неверный тип задач для удаления */
    void shouldGetErrorWhenDeleteWrongTypeTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/foo");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();

        assertEquals(400, code);
    }
}