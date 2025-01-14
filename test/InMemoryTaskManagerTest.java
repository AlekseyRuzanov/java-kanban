import Enums.Status;
import Exceptions.InvalidInputException;
import Managers.InMemoryTaskManager;
import Managers.Managers;
import Managers.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    public void createTask() throws InvalidInputException {
        String expectedName = "Название Task 1";
        String expectedDescription = "Описание Task 1";
        Status expectedStatus = Status.NEW;
        int expectedId = taskManager.getCounter() + 1;

        Task newTask = new Task(expectedName, expectedDescription);
        taskManager.createTask(newTask);

        assertEquals(expectedName, taskManager.getTaskById(expectedId).getName());
        assertEquals(expectedDescription, taskManager.getTaskById(expectedId).getDescription());
        assertEquals(expectedStatus, taskManager.getTaskById(expectedId).getStatus());
        assertEquals(expectedId, taskManager.getTaskById(expectedId).getId());
    }

    @Test
    public void createEpic() throws InvalidInputException {
        String expectedName = "Название Epic 1";
        String expectedDescription = "Описание Epic 1";
        Status expectedStatus = Status.NEW;
        int expectedId = taskManager.getCounter() + 1;

        Epic newEpic = new Epic(expectedName, expectedDescription);
        taskManager.createEpic(newEpic);

        assertEquals(expectedName, taskManager.getEpicById(expectedId).getName());
        assertEquals(expectedDescription, taskManager.getEpicById(expectedId).getDescription());
        assertEquals(expectedStatus, taskManager.getEpicById(expectedId).getStatus());
        assertEquals(expectedId, taskManager.getEpicById(expectedId).getId());
        assertTrue(newEpic.getEpicSubTasks().isEmpty());
    }

    @Test
    public void createSubTask() throws InvalidInputException {
        String expectedSubTaskName = "Название SubTask 1";
        String expectedSubTaskDescription = "Описание SubTask 1";
        Status expectedStatus = Status.NEW;
        int expectedEpicId = taskManager.getCounter() + 1;
        int expectedSubTaskId = taskManager.getCounter() + 2;

        SubTask expectedSubTask = new SubTask(expectedSubTaskName, expectedSubTaskDescription);
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(expectedSubTask, epic.getId());

        assertEquals(expectedSubTaskName, taskManager.getSubTaskById(expectedSubTaskId).getName());
        assertEquals(expectedSubTaskDescription, taskManager.getSubTaskById(expectedSubTaskId).getDescription());
        assertEquals(expectedStatus, taskManager.getSubTaskById(expectedSubTaskId).getStatus());
        assertEquals(expectedSubTaskId, taskManager.getSubTaskById(expectedSubTaskId).getId());
        assertEquals(expectedEpicId, taskManager.getSubTaskById(expectedSubTask.getId()).getLinkedEpicId());
    }

    @Test
    public void updateTask() throws InvalidInputException {
        String expectedUpdatedTaskName = "Название Task 2";
        String expectedUpdatedTaskDescription = "Описание Task 2";
        Status expectedUpdatedTaskStatus = Status.DONE;
        int expectedTaskId = taskManager.getCounter() + 1;

        Task oldTask = new Task("Название Task 1", "Описание Task 1");
        Task updatedTask = new Task(expectedUpdatedTaskName, expectedUpdatedTaskDescription);
        taskManager.createTask(oldTask);
        updatedTask.setId(oldTask.getId());
        updatedTask.setStatus(expectedUpdatedTaskStatus);
        taskManager.updateTask(updatedTask);

        assertEquals(expectedUpdatedTaskName, taskManager.getTaskById(expectedTaskId).getName());
        assertEquals(expectedUpdatedTaskDescription, taskManager.getTaskById(expectedTaskId).getDescription());
        assertEquals(expectedUpdatedTaskStatus, taskManager.getTaskById(expectedTaskId).getStatus());
        assertEquals(expectedTaskId, taskManager.getTaskById(expectedTaskId).getId());
    }

    @Test
    public void throwExceptionWhenUpdatingTaskIdNotFound() {
        int newTaskId = taskManager.getCounter() + 2;

        Task oldTask = new Task("Название Task 1", "Описание Task 1");
        Task newTask = new Task("Название Task 2", "Описание Task 2");
        taskManager.createTask(oldTask);
        newTask.setId(newTaskId);

        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.updateTask(newTask));
        String expectedMessage = "Задача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateEpic() throws InvalidInputException {
        String expectedUpdatedEpicName = "Название Epic 2";
        String expectedUpdatedEpicDescription = "Описание Epic 2";
        Status expectedUpdatedEpicStatus = Status.NEW;
        int expectedEpicId = taskManager.getCounter() + 1;

        Epic oldEpic = new Epic("Название Epic 1", "Описание Epic 1");
        Epic updatedEpic = new Epic(expectedUpdatedEpicName, expectedUpdatedEpicDescription);
        SubTask oldSubTaskOne = new SubTask("Название SubTask 1", "Название SubTask 1");
        SubTask oldSubTaskTwo = new SubTask("Название SubTask 2", "Название SubTask 2");
        taskManager.createEpic(oldEpic);
        taskManager.createSubTask(oldSubTaskOne, expectedEpicId);
        taskManager.createSubTask(oldSubTaskTwo, expectedEpicId);
        updatedEpic.setId(oldEpic.getId());
        taskManager.updateEpic(updatedEpic);

        assertEquals(expectedUpdatedEpicName, taskManager.getEpicById(expectedEpicId).getName());
        assertEquals(expectedUpdatedEpicDescription, taskManager.getEpicById(expectedEpicId).getDescription());
        assertEquals(expectedUpdatedEpicStatus, taskManager.getEpicById(expectedEpicId).getStatus());
        assertEquals(expectedEpicId, taskManager.getEpicById(expectedEpicId).getId());
        assertNotNull(taskManager.getSubTasksByEpicId(expectedEpicId));
    }

    @Test
    public void throwExceptionWhenUpdatingEpicIdNotFound() {
        int newEpicId = taskManager.getCounter() + 2;

        Epic oldEpic = new Epic("Название Epic 1", "Описание Epic 1");
        Epic newEpic = new Epic("Название Epic 2", "Описание Epic 2");
        taskManager.createEpic(oldEpic);
        newEpic.setId(newEpicId);

        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.updateEpic(newEpic));
        String expectedMessage = "Эпик не найден";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateSubTask() throws InvalidInputException {
        String expectedSubTaskName = "Название SubTask 2";
        String expectedSubTaskDescription = "Описание SubTask 2";
        Status expectedSubTaskStatus = Status.DONE;
        int epicId = taskManager.getCounter() + 1;
        int expectedSubTaskId = taskManager.getCounter() + 2;

        SubTask oldSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(epic);
        SubTask newSubTask = new SubTask(expectedSubTaskName, expectedSubTaskDescription);
        taskManager.createSubTask(oldSubTask, epicId);
        newSubTask.setId(expectedSubTaskId);
        newSubTask.setStatus(Status.DONE);
        taskManager.updateSubTask(newSubTask);

        assertEquals(expectedSubTaskName, taskManager.getSubTaskById(expectedSubTaskId).getName());
        assertEquals(expectedSubTaskDescription, taskManager.getSubTaskById(expectedSubTaskId).getDescription());
        assertEquals(expectedSubTaskStatus, taskManager.getSubTaskById(expectedSubTaskId).getStatus());
        assertEquals(expectedSubTaskId, taskManager.getSubTaskById(expectedSubTaskId).getId());
    }

    @Test
    public void getExceptionWhenUpdatingSubSubTaskIdNotFound() {
        int epicId = taskManager.getCounter() + 1;
        int newSubTaskId = taskManager.getCounter() + 3;

        Epic epic = new Epic("Название Epic 1", "Название Epic 1");
        SubTask oldSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask newSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        taskManager.createEpic(epic);
        taskManager.createSubTask(oldSubTask, epicId);
        newSubTask.setId(newSubTaskId);

        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.updateSubTask(newSubTask));
        String expectedMessage = "Подзадача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteAllEpics() {
        SubTask subTask1 = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask subTask2 = new SubTask("Название SubTask 2", "Описание SubTask 2");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1, epic.getId());
        taskManager.createSubTask(subTask2, epic.getId());
        taskManager.deleteAllEpics();

        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test

    public void deleteAllTasks() {
        Task task1 = new Task("Название Task 1", "Описание Task 1");
        Task task2 = new Task("Название Task 2", "Описание Task 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.deleteAllTasks();

        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void deleteAllSubTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        SubTask subTask1 = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask subTask2 = new SubTask("Название SubTask 2", "Описание SubTask 2");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1, epic.getId());
        taskManager.createSubTask(subTask2, epic.getId());
        taskManager.deleteAllSubTasks();

        assertFalse(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    public void getSubTaskBySubTaskId() throws InvalidInputException {
        String expectedFirstSubTaskName = "Название Task 1";
        String expectedFirstSubTaskDescription = "Описание Task 1";
        String expectedSecondSubTaskName = "Название Task 2";
        String expectedSecondSubTaskDescription = "Описание Task 2";
        Status expectedStatus = Status.NEW;
        int epicId = taskManager.getCounter() + 1;
        int expectedFirstTaskId = taskManager.getCounter() + 2;
        int expectedSecondTaskId = taskManager.getCounter() + 3;

        SubTask subTask1 = new SubTask(expectedFirstSubTaskName, expectedFirstSubTaskDescription);
        SubTask subTask2 = new SubTask(expectedSecondSubTaskName, expectedSecondSubTaskDescription);
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1, epicId);
        taskManager.createSubTask(subTask2, epicId);

        assertEquals(expectedFirstSubTaskName, taskManager.getSubTaskById(expectedFirstTaskId).getName());
        assertEquals(expectedFirstSubTaskDescription, taskManager.getSubTaskById(expectedFirstTaskId).getDescription());
        assertEquals(expectedStatus, taskManager.getSubTaskById(expectedFirstTaskId).getStatus());
        assertEquals(expectedFirstTaskId, taskManager.getSubTaskById(expectedFirstTaskId).getId());

        assertEquals(expectedSecondSubTaskName, taskManager.getSubTaskById(expectedSecondTaskId).getName());
        assertEquals(expectedSecondSubTaskDescription, taskManager.getSubTaskById(expectedSecondTaskId).getDescription());
        assertEquals(expectedStatus, taskManager.getSubTaskById(expectedSecondTaskId).getStatus());
        assertEquals(expectedSecondTaskId, taskManager.getSubTaskById(expectedSecondTaskId).getId());
    }

    @Test
    public void getSubTaskByEpicId() throws InvalidInputException {
        String expectedFirstSubTaskName = "Название SubTask 1";
        String expectedFirstSubTaskDescription = "Описание SubTask 1";
        String expectedSecondSubTaskName = "Название SubTask 2";
        String expectedSecondSubTaskDescription = "Описание SubTask 2";
        Status expectedStatus = Status.NEW;
        int epicId = taskManager.getCounter() + 1;
        int expectedFirstSubTaskId = taskManager.getCounter() + 2;
        int expectedSecondSubTaskId = taskManager.getCounter() + 3;

        SubTask subTask1 = new SubTask(expectedFirstSubTaskName, expectedFirstSubTaskDescription);
        SubTask subTask2 = new SubTask(expectedSecondSubTaskName, expectedSecondSubTaskDescription);
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1, epicId);
        taskManager.createSubTask(subTask2, epicId);

        assertEquals(expectedFirstSubTaskName, taskManager.getSubTasksByEpicId(epicId).get(0).getName());
        assertEquals(expectedFirstSubTaskDescription, taskManager.getSubTasksByEpicId(epicId).get(0).getDescription());
        assertEquals(expectedStatus, taskManager.getSubTasksByEpicId(epicId).get(0).getStatus());
        assertEquals(expectedFirstSubTaskId, taskManager.getSubTasksByEpicId(epicId).get(0).getId());

        assertEquals(expectedSecondSubTaskName, taskManager.getSubTasksByEpicId(epicId).get(1).getName());
        assertEquals(expectedSecondSubTaskDescription, taskManager.getSubTasksByEpicId(epicId).get(1).getDescription());
        assertEquals(expectedStatus, taskManager.getSubTasksByEpicId(epicId).get(1).getStatus());
        assertEquals(expectedSecondSubTaskId, taskManager.getSubTasksByEpicId(epicId).get(1).getId());
    }

    @Test
    public void getSubTaskByIdIfSubTasksListIsEmpty() throws InvalidInputException {
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(epic);

        assertTrue(taskManager.getSubTasksByEpicId(epic.getId()).isEmpty());
    }

    @Test
    public void getTaskByIdIfTaskNotExists() {
        int notExistTaskId = taskManager.getCounter() + 2;

        Task epic = new Task("Название Task", "Описание Task");
        taskManager.createTask(epic);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.getTaskById(notExistTaskId));
        String expectedMessage = "Задача не найдена";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getEpicByIdIfEpicNotExists() {
        int notExistEpicId = taskManager.getCounter() + 2;

        Epic epic = new Epic("Название Epic", "Описание Epic");
        taskManager.createEpic(epic);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.getEpicById(notExistEpicId));
        String expectedMessage = "Эпик не найден";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getSubTaskByIdIfSubTaskNotExists() {
        int epicId = taskManager.getCounter() + 1;
        int notExistSubTaskId = taskManager.getCounter() + 3;

        Epic epic = new Epic("Название Epic", "Описание Epic");
        SubTask subTask = new SubTask("Название existSubTask", "Описание existSubTask");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, epicId);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.getSubTaskById(notExistSubTaskId));
        String expectedMessage = "Подзадача не найдена";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteEpicById() throws InvalidInputException {
        String expectedEpicName = "Название Epic 1";
        String expectedEpicDescription = "Описание Epic 1";
        Status expectedStatus = Status.NEW;
        int expectedFirstEpicId = taskManager.getCounter() + 1;
        int expectedSecondEpicId = taskManager.getCounter() + 2;

        Epic firstEpic = new Epic(expectedEpicName, expectedEpicDescription);
        Epic secondEpic = new Epic("Название Epic 2", "Описание Epic 2");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);
        taskManager.createSubTask(firstSubTask, expectedFirstEpicId);
        taskManager.createSubTask(secondSubTask, expectedSecondEpicId);
        taskManager.deleteEpicById(expectedSecondEpicId);

        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubTasks().size());
        assertEquals(expectedEpicName, taskManager.getAllEpics().get(0).getName());
        assertEquals(expectedEpicDescription, taskManager.getAllEpics().get(0).getDescription());
        assertEquals(expectedStatus, taskManager.getAllEpics().get(0).getStatus());
        assertEquals(expectedFirstEpicId, taskManager.getAllEpics().get(0).getId());
    }

    @Test
    public void deleteEpicByIdIfEpicNotExists() {
        int notExistEpicId = taskManager.getCounter() + 2;

        Epic firstEpic = new Epic("Название Epic 1", "Описание Epic 1");
        taskManager.createEpic(firstEpic);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.deleteEpicById(notExistEpicId));
        String expectedMessage = "Эпик не найден";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteTaskById() throws InvalidInputException {
        String expectedTaskName = "Название Task 1";
        String expectedTaskDescription = "Описание Task 1";
        Status expectedStatus = Status.NEW;
        int expectedFirstTaskId = taskManager.getCounter() + 1;
        int expectedSecondTaskId = taskManager.getCounter() + 2;

        Task firstTask = new Task(expectedTaskName, expectedTaskDescription);
        Task secondTask = new Task("Название Task 2", "Описание Task 2");
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.deleteTaskById(expectedSecondTaskId);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(expectedTaskName, taskManager.getAllTasks().get(0).getName());
        assertEquals(expectedTaskDescription, taskManager.getAllTasks().get(0).getDescription());
        assertEquals(expectedStatus, taskManager.getAllTasks().get(0).getStatus());
        assertEquals(expectedFirstTaskId, taskManager.getAllTasks().get(0).getId());
    }

    @Test
    public void deleteTaskByIdIfTaskNotExists() {
        int notExistTaskId = taskManager.getCounter() + 2;

        Task firstTask = new Task("Название Epic 1", "Описание Epic 1");
        taskManager.createTask(firstTask);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.deleteTaskById(notExistTaskId));
        String expectedMessage = "Задача не найдена";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteSubTaskById() throws InvalidInputException {
        int firstEpicId = taskManager.getCounter() + 1;
        int secondEpicId = taskManager.getCounter() + 2;
        String expectedSecondSubTaskName = "Название SubTask2";
        String expectedSecondSubTaskDescription = "Описание SubTask2";
        Status expectedStatus = Status.NEW;
        int deletedSubTaskById = taskManager.getCounter() + 3;
        int expectedSubTaskById = taskManager.getCounter() + 4;

        Epic firstEpic = new Epic("Название Epic 1", "Описание Epic 1");
        Epic secondEpic = new Epic("Название Epic 2", "Описание Epic 2");
        SubTask firstSubTask = new SubTask("Название SubTask1", "Описание SubTask1");
        SubTask secondSubTask = new SubTask(expectedSecondSubTaskName, expectedSecondSubTaskDescription);
        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);
        taskManager.createSubTask(firstSubTask, firstEpicId);
        taskManager.createSubTask(secondSubTask, secondEpicId);
        taskManager.deleteSubTaskById(deletedSubTaskById);

        assertEquals(0, taskManager.getSubTasksByEpicId(firstEpic.getId()).size());
        assertEquals(1, taskManager.getSubTasksByEpicId(secondEpic.getId()).size());
        assertEquals(1, taskManager.getAllSubTasks().size());
        assertEquals(expectedSecondSubTaskName, taskManager.getAllSubTasks().get(0).getName());
        assertEquals(expectedSecondSubTaskDescription, taskManager.getAllSubTasks().get(0).getDescription());
        assertEquals(expectedStatus, taskManager.getAllSubTasks().get(0).getStatus());
        assertEquals(expectedSubTaskById, taskManager.getAllSubTasks().get(0).getId());
    }

    @Test
    public void deleteSubTaskByIdIfSubTaskNotExists() throws InvalidInputException {
        int notExistSubTaskId = taskManager.getCounter() + 3;
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask firstSubTask = new SubTask("Название SubTask", "Описание SubTask");

        taskManager.createEpic(epic);
        taskManager.createSubTask(firstSubTask, epic.getId());

        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> taskManager.deleteSubTaskById(notExistSubTaskId));
        String expectedMessage = "Подзадача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void calculateStatusForEpicWhenAllSubTasksNew() {

        Epic epic = new Epic("Название Epic", "Описание Epic");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");

        taskManager.createEpic(epic);
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(firstSubTask, epic.getId());
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(secondSubTask, epic.getId());
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void calculateStatusForEpicWhenOneOfSubTasksIsInProgress() {
        Epic epic = new Epic("Название Epic", "Описание Epic");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        secondSubTask.setStatus(Status.IN_PROGRESS);

        taskManager.createEpic(epic);
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(firstSubTask, epic.getId());
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(secondSubTask, epic.getId());
        assertEquals(Status.IN_PROGRESS, taskManager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void calculateStatusForEpicWhenAllSubTasksInProgress() {
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        firstSubTask.setStatus(Status.IN_PROGRESS);
        secondSubTask.setStatus(Status.IN_PROGRESS);

        taskManager.createEpic(epic);
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(firstSubTask, epic.getId());
        assertEquals(Status.IN_PROGRESS, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(secondSubTask, epic.getId());
        assertEquals(Status.IN_PROGRESS, taskManager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void calculateStatusForEpicWhenAllSubTasksInDone() {
        Epic epic = new Epic("Название Epic", "Описание Epic");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        firstSubTask.setStatus(Status.DONE);
        secondSubTask.setStatus(Status.DONE);

        taskManager.createEpic(epic);
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(firstSubTask, epic.getId());
        assertEquals(Status.DONE, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(secondSubTask, epic.getId());
        assertEquals(Status.DONE, taskManager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void calculateStatusForEpicWhenSubTaskStatusesInDoneAndInProgress() {
        Epic epic = new Epic("Название Epic", "Описание Epic");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        firstSubTask.setStatus(Status.DONE);
        secondSubTask.setStatus(Status.IN_PROGRESS);

        taskManager.createEpic(epic);
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(firstSubTask, epic.getId());
        assertEquals(Status.DONE, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(secondSubTask, epic.getId());
        assertEquals(Status.IN_PROGRESS, taskManager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void calculateStatusForEpicWhenSubTaskStatusesIsDoneAndNew() {
        Epic epic = new Epic("Название Epic", "Описание Epic");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        firstSubTask.setStatus(Status.DONE);

        taskManager.createEpic(epic);
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(firstSubTask, epic.getId());
        assertEquals(Status.DONE, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.createSubTask(secondSubTask, epic.getId());
        assertEquals(Status.IN_PROGRESS, taskManager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void calculateEpicStatusWhenAllSubTasksDeleted() throws InvalidInputException {
        Epic epic = new Epic("Название Epic", "Описание Epic");
        SubTask firstSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask secondSubTask = new SubTask("Название SubTask 2", "Описание SubTask 2");
        firstSubTask.setStatus(Status.DONE);
        secondSubTask.setStatus(Status.DONE);

        taskManager.createEpic(epic);
        taskManager.createSubTask(firstSubTask, epic.getId());
        taskManager.createSubTask(secondSubTask, epic.getId());
        assertEquals(Status.DONE, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.deleteSubTaskById(firstSubTask.getId());
        assertEquals(Status.DONE, taskManager.getAllEpics().getFirst().getStatus());
        taskManager.deleteSubTaskById(secondSubTask.getId());
        assertEquals(Status.NEW, taskManager.getAllEpics().getFirst().getStatus());
    }
}
