import java.util.*;

public class Manager {
    protected Map<Integer, Epic> epics = new HashMap<>();
    Map<Integer, Task> tasks = new HashMap<>();
    Map<Integer, SubTask> subTasks = new HashMap<>();
    protected static int counter = 0;

    public void createEpic(Epic epic) {
        ++counter;
        epic.setId(counter);
        epics.put(epic.getId(), epic);
    }

    public void createTask(Task task) {
        ++counter;
        task.setId(counter);
        tasks.put(task.getId(), task);
    }

    public void createSubTask(SubTask subTask, Integer epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        ++counter;
        subTask.setId(counter);
        subTask.setlinkedEpicId(epicId);
        subTasks.put(subTask.getId(), subTask);
        epics.get(epicId).addEpicSubTasks(subTask.getId());
        calculateEpicStatus(epicId);
    }

    public void updateEpic(Epic epic) {
        epic.setEpicSubTasks(epics.get(epic.getId()).getEpicSubTasks()); //Переносим список сабтасок со старого эпика на новый
        epics.put(epic.getId(), epic);
        calculateEpicStatus(epic.getId());
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        Integer linkToEpic = subTasks.get(subTask.getId()).getLinkedEpicId();
        subTask.setlinkedEpicId(linkToEpic); //переносим связь с эпиком со старой сабтаски на новую
        subTasks.put(subTask.getId(), subTask);
        calculateEpicStatus(subTask.getLinkedEpicId());
    }

    public Collection<Epic> getAllEpics() {
        return epics.values();
    }

    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    public Collection<SubTask> getAllSubTasks() {
        return subTasks.values();
    }

    public List<SubTask> getSubTaskByEpicId(Integer epicId) {
        List<SubTask> allSubTasksListFromEpic = new ArrayList<>();
        if (!epics.containsKey(epicId)) {
            return allSubTasksListFromEpic;
        }
        for (Integer id : epics.get(epicId).getEpicSubTasks()) {
            allSubTasksListFromEpic.add(subTasks.get(id));
        }

        return allSubTasksListFromEpic;
    }

    public Task getTaskById(Integer taskId) throws InvalidInputException {
        if (tasks.get(taskId) == null) {
            throw new InvalidInputException("Задача не найдена");
        }
        return tasks.get(taskId);
    }

    public Epic getEpicById(Integer epicId) throws InvalidInputException {
        if (epics.get(epicId) == null) {
            throw new InvalidInputException("Эпик не найден");
        }
        return epics.get(epicId);
    }

    public SubTask getSubTaskById(Integer subTaskId) throws InvalidInputException {
        if (subTasks.get(subTaskId) == null) {
            throw new InvalidInputException("Подзадача не найдена");
        }
        return subTasks.get(subTaskId);
    }

    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubTasks();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteAllEpicSubTasks();
        }
        calculateEpicStatus();
    }

    public void deleteEpicById(Integer epicId) {
        for (Integer subTaskId : epics.get(epicId).getEpicSubTasks()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(epicId);
    }

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    public void deleteSubTaskById(Integer subTaskId) {
        Integer linkedEpicId = subTasks.get(subTaskId).getLinkedEpicId();
        subTasks.remove(subTaskId);
        epics.get(linkedEpicId).deleteEpicSubTasks(subTaskId);
        calculateEpicStatus(linkedEpicId);
    }

    private void calculateEpicStatus(Integer epicId) {
        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Integer subTaskId : epics.get(epicId).getEpicSubTasks()) {
            if (subTasks.get(subTaskId).getStatus().equals(Status.NEW)) {
                isAllDone = false;
            } else if (subTasks.get(subTaskId).getStatus().equals(Status.IN_PROGRESS)) {
                isAllNew = false;
                isAllDone = false;
                break;
            } else if (subTasks.get(subTaskId).getStatus().equals(Status.DONE)) {
                isAllNew = false;
                isAllDone = true;
            }
        }

        if (isAllNew && epics.get(epicId).getStatus().equals(Status.NEW)) {
            return;
        } else if (isAllNew && !epics.get(epicId).getStatus().equals(Status.NEW)) {
            epics.get(epicId).setStatus(Status.NEW);
        } else if (isAllDone) {
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
}
