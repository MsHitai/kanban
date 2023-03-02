package ru.yandex.practicum.service;

import ru.yandex.practicum.enums.Tasks;
import ru.yandex.practicum.exceptions.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String path;
    private final List<Tasks> tasksToSave = new ArrayList<>();


    public FileBackedTaskManager(String path) {
        super();
        this.path = path;
    }

    private void save() throws ManagerSaveException, IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.path, false))) {
            bw.write("id,type,name,status,description,epic \n");
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Ошибка сохранения файла.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/save.csv");
        fileBackedTaskManager.save();
    }
}
