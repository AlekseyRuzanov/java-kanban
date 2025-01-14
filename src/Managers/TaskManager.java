package Managers;

import Exceptions.InvalidInputException;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.List;

public interface TaskManager {

    int getCounter();

    void createEpic(Epic epic);

    void createTask(Task task);

    void createSubTask(SubTask subTask, Integer epicId);

    void updateEpic(Epic epic) throws InvalidInputException;

    void updateTask(Task task) throws InvalidInputException;

    void updateSubTask(SubTask subTask) throws InvalidInputException;

    List<Epic> getAllEpics();

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<SubTask> getSubTasksByEpicId(Integer epicId) throws InvalidInputException;

    Task getTaskById(Integer taskId) throws InvalidInputException;

    Epic getEpicById(Integer epicId) throws InvalidInputException;

    SubTask getSubTaskById(Integer subTaskId) throws InvalidInputException;

    void deleteAllEpics();

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteEpicById(Integer epicId) throws InvalidInputException;

    void deleteTaskById(Integer taskId) throws InvalidInputException;

    void deleteSubTaskById(Integer subTaskId) throws InvalidInputException;
}
