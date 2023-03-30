import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.server.HttpTaskServer;
import ru.yandex.practicum.service.Managers;
import ru.yandex.practicum.service.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws IOException {

        TaskManager taskManager = Managers.getDefaultFile();

        //final TaskManager taskManager = Managers.getDefault();

        //taskManager.createTask(new Task("simple task", "no big deal", 0, Status.NEW)); для проверки сортировки в конец по null

        taskManager.createTask(new Task("simple task", "no big deal", 0, Status.NEW,
                15, LocalDateTime.of(2023, Month.MARCH, 16, 15, 4)));

        taskManager.createEpic(new Epic("epic epic", "got three subtasks", 0, Status.NEW));
        taskManager.createSubTask(new SubTask("first subtask", "am I the startTime?", 0,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 2),
                2));
        taskManager.createSubTask(new SubTask("second subtask",
                "needed for date testing", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 20), 2));

        taskManager.createSubTask(new SubTask("third subtask",
                "needed for time conflicts testing", 0, Status.DONE, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 7), 2));

        System.out.println("Создаем задачи...");

        /*System.out.println("У нас создались следующие задачи: ");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());

        System.out.println();

        System.out.println("Сортировка по приоритету: ");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("История");
        taskManager.getHistory();

        System.out.println("Удалим подзадачу под номером 4");
        taskManager.deleteSubTask(4);

        System.out.println("История");
        taskManager.getHistory();

        System.out.println("Сортировка по приоритету: ");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("История");
        taskManager.getHistory();

        System.out.println("Удалим все подзадачи.. ");
        taskManager.deleteAllSubTasks();

        System.out.println("Сортировка по приоритету: ");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("История");
        taskManager.getHistory();*/

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);

        httpTaskServer.start();

    }
}
/*
Код проверки в Main.main перестанет работать. потому что Managers.getDefault() теперь возвращает новую
реализацию менеджера задач, а она не может работать без запуска сервера. Вам нужно это исправить.
Добавьте запуск KVServer в Main.main и перезапустите пример использования менеджера. Убедитесь,
что всё работает и состояние задач теперь хранится на сервере.
 */