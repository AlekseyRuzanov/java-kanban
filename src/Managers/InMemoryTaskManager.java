package Managers;

import Enums.Status;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Exceptions.InvalidInputException;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private static int counter = 0;

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public void createTask(Task task) {
        ++counter;
        task.setId(counter);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        ++counter;
        epic.setId(counter);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask, Integer epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        ++counter;
        subTask.setId(counter);
        subTask.setLinkedEpicId(epicId);
        subTasks.put(subTask.getId(), subTask);
        linkSubTaskToEpic(subTask.getId(), epicId);
        calculateEpicStatus(epicId);
    }

    @Override
    public void updateTask(Task task) throws InvalidInputException {
        if (!tasks.containsKey(task.getId())) {
            throw new InvalidInputException("Задача не найдена");
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) throws InvalidInputException {
        if (!epics.containsKey(epic.getId())) {
            throw new InvalidInputException("Эпик не найден");
        }
        epic.setEpicSubTasks(epics.get(epic.getId()).getEpicSubTasks()); //Переносим список сабтасок со старого эпика на новый
        epics.put(epic.getId(), epic);
        calculateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) throws InvalidInputException {
        if (!subTasks.containsKey(subTask.getId())) {
            throw new InvalidInputException("Подзадача не найдена");
        }
        Integer epicId = subTasks.get(subTask.getId()).getLinkedEpicId();
        subTask.setLinkedEpicId(epicId); //переносим связь с эпиком со старой сабтаски на новую
        subTasks.put(subTask.getId(), subTask);
        calculateEpicStatus(subTask.getLinkedEpicId());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new LinkedList<>(epics.values());
    }

    @Override
    public List<Task> getAllTasks() {
        return new LinkedList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new LinkedList<>(subTasks.values());
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(Integer epicId) throws InvalidInputException {
        List<SubTask> allSubTasksListFromEpic = new LinkedList<>();
        if (!epics.containsKey(epicId)) {
            throw new InvalidInputException("Эпик не найден");
        }
        for (Integer id : epics.get(epicId).getEpicSubTasks()) {
            allSubTasksListFromEpic.add(subTasks.get(id));
        }
        return allSubTasksListFromEpic;
    }

    @Override
    public Task getTaskById(Integer taskId) throws InvalidInputException {
        if (!tasks.containsKey(taskId)) {
            throw new InvalidInputException("Задача не найдена");
        }
        historyManager.addTask(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(Integer epicId) throws InvalidInputException {
        if (!epics.containsKey(epicId)) {
            throw new InvalidInputException("Эпик не найден");
        }
        historyManager.addTask(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(Integer subTaskId) throws InvalidInputException {
        if (!subTasks.containsKey(subTaskId)) {
            throw new InvalidInputException("Подзадача не найдена");
        }
        historyManager.addTask(subTasks.get(subTaskId));
        return subTasks.get(subTaskId);
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubTasks();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        deleteAllEpicSubTasks();
        calculateEpicStatus();
    }

    @Override
    public void deleteEpicById(Integer epicId) throws InvalidInputException {
        if (!epics.containsKey(epicId)) {
            throw new InvalidInputException("Эпик не найден");
        }
        for (Integer subTaskId : epics.get(epicId).getEpicSubTasks()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(epicId);
    }

    @Override
    public void deleteTaskById(Integer taskId) throws InvalidInputException {
        if (!tasks.containsKey(taskId)) {
            throw new InvalidInputException("Задача не найдена");
        }
        tasks.remove(taskId);
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId) throws InvalidInputException {
        if (!subTasks.containsKey(subTaskId)) {
            throw new InvalidInputException("Подзадача не найдена");
        }
        Integer epicId = subTasks.get(subTaskId).getLinkedEpicId();
        subTasks.remove(subTaskId);
        deleteEpicSubTasks(subTaskId, epicId);
        calculateEpicStatus(epicId);
    }

    private void calculateEpicStatus(Integer epicId) {
        if (epics.get(epicId).getEpicSubTasks().isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
            return;
        }
        int statusNew = 0;
        int statusInProgress = 0;
        int statusDone = 0;
        for (Integer subTaskId : epics.get(epicId).getEpicSubTasks()) {
            if (subTasks.get(subTaskId).getStatus().equals(Status.NEW)) {
                statusNew += 1;
            } else if (subTasks.get(subTaskId).getStatus().equals(Status.IN_PROGRESS)) {
                statusInProgress += 1;
            } else if (subTasks.get(subTaskId).getStatus().equals(Status.DONE)) {
                statusDone += 1;
            }
        }
        boolean isNew = false;
        boolean isDone = false;
        if (statusDone == 0 && statusInProgress == 0 && statusNew != 0) {
            isNew = true;
        } else if (statusDone != 0 && statusInProgress == 0 && statusNew == 0) {
            isDone = true;
        }

        if (isNew) {
            epics.get(epicId).setStatus(Status.NEW);
        } else if (isDone) {
            epics.get(epicId).setStatus(Status.DONE);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    private void calculateEpicStatus() {
        for (Integer epicId : epics.keySet()) {
            epics.get(epicId).setStatus(Status.NEW);
        }
    }


    public void linkSubTaskToEpic(Integer subTaskId, Integer epicId) {
        if (!epics.get(epicId).getEpicSubTasks().contains(subTaskId)) {
            epics.get(epicId).getEpicSubTasks().add(subTaskId);
        }
    }

    public void deleteEpicSubTasks(Integer subTaskId, Integer epicId) {
        epics.get(epicId).getEpicSubTasks().remove(subTaskId);
    }

    public void deleteAllEpicSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getEpicSubTasks().clear();
        }
    }
}
