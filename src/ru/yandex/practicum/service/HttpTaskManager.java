package ru.yandex.practicum.service;


public class HttpTaskManager extends FileBackedTaskManager{



    public HttpTaskManager(String path) { // todo принимать URL к серверу KVServer
        super(path);
    }
}

/*
HttpTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера.
Вам нужно заменить вызовы сохранения состояния в файлах на вызов клиента
 */

// todo В конце обновите статический метод getDefault() в утилитарном классе Managers, чтобы он
//  возвращал HttpTaskManager.

/*
Теперь можно добавить тесты для HttpTaskManager аналогично тому как сделали для
FileBackedTasksManager , отличие только, вместо проверки восстановления состояния менеджера из
файла, данные будут восстанавливаться с KVServerсервера.

Если запускать новый сервер перед каждым тестом на том же порту, то потребуется остановить
предыдущий. Для этого реализуйте метод stop() в KVServer. Его вызов поместите в отдельный метод
в тестах. Пометьте его аннотацией @AfterEach.
 */