import java.util.*;

public class Manager {
    protected Map<Integer, Epic> epics = new HashMap<>();
    Map<Integer, Task> tasks = new HashMap<>();
    Map<Integer, SubTask> subTasks = new HashMap<>();
    Map<Integer, List<Integer>> epic_subTasks = new HashMap<>();
    protected static int counter = 0;

    public void createEpic(Epic epic) {
        ++counter;
        epic.setId(counter);
        epics.put(epic.getId(), epic);
        epic_subTasks.put(epic.getId(), new ArrayList<>());
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
        epic_subTasks.get(epicId).add(subTask.getId());
        calculateEpicStatus(epicId);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        calculateEpicStatus(epic.getId());
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        Integer linkToEpic = subTasks.get(subTask.getId()).getLinkedEpicId();
        subTask.setlinkedEpicId(linkToEpic);
        subTasks.put(subTask.getId(), subTask);
        calculateEpicStatus(subTask.getLinkedEpicId());
    }

    public List<Epic> getAllEpics() {
        List<Epic> allEpicsList = new ArrayList<>();
        for (Integer epicId : epics.keySet()) {
            allEpicsList.add(epics.get(epicId));
        }
        return allEpicsList;
    }

    public List<Task> getAllTasks() {
        List<Task> allTasksList = new ArrayList<>();
        for (Integer taskId : tasks.keySet()) {
            allTasksList.add(tasks.get(taskId));
        }
        return allTasksList;
    }

    public List<SubTask> getAllSubTasks() {
        List<SubTask> allSubTasksList = new ArrayList<>();
        for (Integer subTaskId : subTasks.keySet()) {
            allSubTasksList.add(subTasks.get(subTaskId));
        }
        return allSubTasksList;
    }

    public List<SubTask> getSubTaskByEpicId(Integer epicId) {
        List<SubTask> allSubTasksListFromEpic = new ArrayList<>();
        if (!epics.containsKey(epicId)) {
            return null;
        }
        for (Integer id : epic_subTasks.get(epicId)) {
            allSubTasksListFromEpic.add(subTasks.get(id));
        }
        return allSubTasksListFromEpic;
    }

    public Task getTaskById(Integer taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicById(Integer epicId) {
        return epics.get(epicId);
    }

    public SubTask getSubTaskById(Integer subTaskId) {
        return subTasks.get(subTaskId);
    }

    public void deleteAllEpics() {
        epics.clear();
        epic_subTasks.clear();
        deleteAllSubTasks();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        calculateEpicStatus();
    }

    public void deleteEpicById(Integer id) {
        for (Integer subTaskId : epic_subTasks.get(id)) {
            subTasks.remove(subTaskId);
        }
        epic_subTasks.remove(id);
        epics.remove(id);
    }

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    public void deleteSubTaskById(Integer id) {
        Integer epicIdForStatusRecalculation = subTasks.get(id).getLinkedEpicId();
        epic_subTasks.get(epicIdForStatusRecalculation).remove(id);
        subTasks.remove(id);
        calculateEpicStatus(epicIdForStatusRecalculation);
    }

    private void calculateEpicStatus(Integer epicId) {
        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Integer subTaskId : epic_subTasks.get(epicId)) {
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
