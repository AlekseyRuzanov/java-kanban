import Enums.Status;
import Exceptions.InvalidInputException;
import Manager.Manager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {

    @Test
    public void checkCreatingAndGettingTaskById() throws InvalidInputException {
        Manager manager = new Manager();
        Task expectedTask = new Task("Название Task 1", "Описание Task 1");
        manager.createTask(expectedTask);
        assertEquals(expectedTask, manager.getTaskById(expectedTask.getId()));
        assertEquals(Status.NEW, manager.getTaskById(expectedTask.getId()).getStatus());
    }

    @Test
    public void checkCreatingAndGettingEpicById() throws InvalidInputException {
        Manager manager = new Manager();
        Epic expectedEpic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(expectedEpic);
        assertEquals(expectedEpic, manager.getEpicById(expectedEpic.getId()));
        assertTrue(expectedEpic.getEpicSubTasks().isEmpty());
        assertEquals(Status.NEW, manager.getEpicById(expectedEpic.getId()).getStatus());
    }

    @Test
    public void checkCreatingAndGettingSubTaskById() throws InvalidInputException {
        Manager manager = new Manager();
        SubTask expectedSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        manager.createSubTask(expectedSubTask, epic.getId());
        assertEquals(expectedSubTask, manager.getSubTaskById(expectedSubTask.getId()));
        assertEquals(Status.NEW, manager.getSubTaskById(expectedSubTask.getId()).getStatus());
        assertEquals(epic.getId(), manager.getSubTaskById(expectedSubTask.getId()).getLinkedEpicId());
    }

    @Test
    public void checkUpdatingTask() throws InvalidInputException {
        Manager manager = new Manager();
        Task task = new Task("Название Task 1", "Описание Task 1");
        Task expectedTask = new Task("Название Task 1", "Описание Task 1");
        manager.createTask(task);
        expectedTask.setId(task.getId());
        manager.updateTask(expectedTask);
        assertEquals(expectedTask, manager.getTaskById(task.getId()));
    }

    @Test
    public void checkExceptionWhenUpdatingTaskIdNotFound() {
        Manager manager = new Manager();
        Task task = new Task("Название Task 1", "Описание Task 1");
        Task expectedTask = new Task("Название Task 1", "Описание Task 1");
        manager.createTask(task);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.updateTask(expectedTask));
        String expectedMessage = "Задача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void checkUpdatingEpic() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        Epic expectedEpic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        expectedEpic.setId(epic.getId());
        manager.updateEpic(expectedEpic);
        assertEquals(expectedEpic, manager.getEpicById(epic.getId()));
    }

    @Test
    public void checkExceptionWhenUpdatingEpicIdNotFound() {
        Manager manager = new Manager();
        Epic epicExists = new Epic("Название Epic 1", "Описание Epic 1");
        Epic epicNotExists = new Epic("Название Epic 2", "Описание Epic 2");
        manager.createEpic(epicExists);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.updateEpic(epicNotExists));
        String expectedMessage = "Эпик не найден";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(epicExists, manager.getAllEpics().getFirst());
    }

    @Test
    public void checkUpdatingSubTask() throws InvalidInputException {
        Manager manager = new Manager();
        SubTask subTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        SubTask expectedSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        manager.createSubTask(subTask, epic.getId());
        expectedSubTask.setId(subTask.getId());
        manager.updateSubTask(expectedSubTask);
        assertEquals(expectedSubTask, manager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void checkExceptionWhenUpdatingSubTaskIdNotFound() {
        Manager manager = new Manager();
        SubTask subTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        SubTask expectedSubTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        manager.createSubTask(subTask, epic.getId());
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.updateSubTask(expectedSubTask));
        String expectedMessage = "Подзадача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void checkDeleteAllEpicsMethod() throws InvalidInputException {
        Manager manager = new Manager();
        SubTask subTask1 = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask subTask2 = new SubTask("Название SubTask 2", "Описание SubTask 2");
        Task task = new Task("Название Task 1", "Описание Task 1");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        manager.createSubTask(subTask1, epic.getId());
        manager.createSubTask(subTask2, epic.getId());
        manager.createTask(task);
        manager.deleteAllEpics();

        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test

    public void checkDeleteAllTasksMethod() {
        Manager manager = new Manager();
        SubTask subTask1 = new SubTask("Название SubTask 1", "Описание SubTask 1");
        Task task1 = new Task("Название Task 1", "Описание Task 1");
        Task task2 = new Task("Название Task 2", "Описание Task 2");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        manager.createSubTask(subTask1, epic.getId());
        manager.createTask(task1);
        manager.createTask(task2);
        manager.deleteAllTasks();

        assertFalse(manager.getAllEpics().isEmpty());
        assertFalse(manager.getAllSubTasks().isEmpty());
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void checkDeleteAllSubTasksMethod(){
        Manager manager = new Manager();
        SubTask subTask1 = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask subTask2 = new SubTask("Название SubTask 2", "Описание SubTask 2");
        Task task = new Task("Название Task 1", "Описание Task 1");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        manager.createSubTask(subTask1, epic.getId());
        manager.createSubTask(subTask2, epic.getId());
        manager.createTask(task);
        manager.deleteAllSubTasks();

        assertFalse(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
        assertFalse(manager.getAllTasks().isEmpty());
    }

    @Test
    public void checkGetSubTaskByIdIfEpicExists() throws InvalidInputException {
        Manager manager = new Manager();
        SubTask subTask1 = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask subTask2 = new SubTask("Название SubTask 2", "Описание SubTask 2");
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        manager.createSubTask(subTask1, epic.getId());
        manager.createSubTask(subTask2, epic.getId());
        assertEquals(2, manager.getSubTasksByEpicId(epic.getId()).size());
        assertEquals(subTask1, manager.getSubTasksByEpicId(epic.getId()).get(0));
        assertEquals(subTask2, manager.getSubTasksByEpicId(epic.getId()).get(1));
    }

    @Test
    public void checkGetSubTaskByIdIfEpicNotExists() {
        Manager manager = new Manager();
        SubTask subTask1 = new SubTask("Название SubTask 1", "Описание SubTask 1");
        SubTask subTask2 = new SubTask("Название SubTask 2", "Описание SubTask 2");
        Epic epic1 = new Epic("Название Epic 1", "Описание Epic 1");
        Epic epic2 = new Epic("Название Epic 2", "Описание Epic 2");
        manager.createEpic(epic1);
        manager.createSubTask(subTask1, epic1.getId());
        manager.createSubTask(subTask2, epic1.getId());
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.getSubTasksByEpicId(epic2.getId()));
        String expectedMessage = "Эпик не найден";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void checkGetSubTaskByIdIfSubTasksListIsEmpty() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic = new Epic("Название Epic 1", "Описание Epic 1");
        manager.createEpic(epic);
        assertTrue(manager.getSubTasksByEpicId(epic.getId()).isEmpty());

    }

    @Test
    public void checkGetEpicByIdIfEpicNotExists() {
        Manager manager = new Manager();
        Epic existEpic = new Epic("Название existEpic", "Описание existEpic");
        Epic notExistEpic = new Epic("Название notExistEpic", "Описание notExistEpic");
        manager.createEpic(existEpic);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.getEpicById(notExistEpic.getId()));
        String expectedMessage = "Эпик не найден";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void checkGetTaskByIdIfTaskNotExists() {
        Manager manager = new Manager();
        Task existTask = new Task("Название existTask", "Описание existTask");
        Task notExistTask = new Task("Название notExistTask", "Описание notExistTask");
        manager.createTask(existTask);
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.getTaskById(notExistTask.getId()));
        String expectedMessage = "Задача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void checkGetSubTaskByIdIfSubTaskNotExists(){
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask existSubTask = new SubTask("Название existSubTask", "Описание existSubTask");
        SubTask notExistSubTask = new SubTask("Название notExistSubTask", "Описание notExistSubTask");
        manager.createSubTask(existSubTask, epic.getId());
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.getSubTaskById(notExistSubTask.getId()));
        String expectedMessage = "Подзадача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void checkDeleteEpicByIdIfEpicExists() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Название epic1", "Описание epic1");
        Epic epic2 = new Epic("Название epic2", "Описание epic2");
        Epic epic3 = new Epic("Название epic3", "Описание epic3");
        Task task = new Task("Название task", "Описание task");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask2", "Описание subTask2");
        SubTask subtask3 = new SubTask("Название subTask3", "Описание subTask3");

        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createEpic(epic3);

        manager.createSubTask(subtask1,epic1.getId());
        manager.createSubTask(subtask2,epic2.getId());
        manager.createSubTask(subtask3,epic3.getId());

        manager.createTask(task);

        manager.deleteEpicById(epic2.getId());

        assertEquals(2, manager.getAllEpics().size());
        assertTrue(manager.getAllEpics().contains(epic1));
        assertTrue(manager.getAllEpics().contains(epic3));

        assertEquals(task, manager.getAllTasks().getFirst());

        assertEquals(2, manager.getAllSubTasks().size());
        assertTrue(manager.getAllSubTasks().contains(subtask1));
        assertTrue(manager.getAllSubTasks().contains(subtask3));
    }

    @Test
    public void checkDeleteEpicByIdIfEpicNotExists(){
        Manager manager = new Manager();
        Epic epic1 = new Epic("Название epic1", "Описание epic1");
        Epic epic2 = new Epic("Название epic2", "Описание epic2");
        Task task = new Task("Название task", "Описание task");
        SubTask subtask = new SubTask("Название subTask1", "Описание subTask1");

        manager.createEpic(epic1);
        manager.createSubTask(subtask,epic1.getId());
        manager.createTask(task);

        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.deleteEpicById(4));
        String expectedMessage = "Эпик не найден";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        assertEquals(task, manager.getAllTasks().getFirst());
        assertEquals(epic1, manager.getAllEpics().getFirst());
        assertEquals(subtask, manager.getAllSubTasks().getFirst());
    }

    @Test
    public void checkDeleteTaskByIdIfTaskExists() throws InvalidInputException {
        Manager manager = new Manager();
        Task task1 = new Task("Название task1", "Описание task1");
        Task task2 = new Task("Название task2", "Описание task2");
        Task task3 = new Task("Название task3", "Описание task3");
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        manager.createEpic(epic);
        manager.createSubTask(subtask1,epic.getId());

        manager.deleteTaskById(task2.getId());

        assertEquals(2, manager.getAllTasks().size());
        assertTrue(manager.getAllTasks().contains(task1));
        assertTrue(manager.getAllTasks().contains(task3));

        assertEquals(epic, manager.getAllEpics().getFirst());

        assertEquals(1, manager.getAllSubTasks().size());
        assertEquals(subtask1, manager.getAllSubTasks().getFirst());
    }

    @Test
    public void checkDeleteTaskByIdIfTaskNotExists(){
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        Task task = new Task("Название task", "Описание task");
        SubTask subtask = new SubTask("Название subTask", "Описание subTask");

        manager.createEpic(epic);
        manager.createSubTask(subtask,epic.getId());
        manager.createTask(task);

        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.deleteTaskById(4));
        String expectedMessage = "Задача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        assertEquals(task, manager.getAllTasks().getFirst());
        assertEquals(epic, manager.getAllEpics().getFirst());
        assertEquals(subtask, manager.getAllSubTasks().getFirst());
    }

    @Test
    public void checkDeleteSubTaskByIdIfSubTaskExists() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Название epic1", "Описание epic1");
        Epic epic2 = new Epic("Название epic2", "Описание epic2");
        Task task = new Task("Название task", "Описание task");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask2", "Описание subTask2");
        SubTask subtask3 = new SubTask("Название subTask3", "Описание subTask3");

        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubTask(subtask1,epic1.getId());
        manager.createSubTask(subtask2,epic1.getId());
        manager.createSubTask(subtask3,epic2.getId());
        manager.createTask(task);

        manager.deleteSubTaskById(subtask1.getId());

        assertEquals(task, manager.getAllTasks().getFirst());
        assertEquals(epic1, manager.getAllEpics().getFirst());
        assertEquals(1, manager.getSubTasksByEpicId(epic1.getId()).size());
        assertEquals(1, manager.getSubTasksByEpicId(epic2.getId()).size());
        assertEquals(2, manager.getAllSubTasks().size());
        assertEquals(subtask2, manager.getAllSubTasks().get(0));
        assertEquals(subtask3, manager.getAllSubTasks().get(1));
        assertEquals(subtask2, manager.getSubTasksByEpicId(epic1.getId()).getFirst());
        assertEquals(subtask3, manager.getSubTasksByEpicId(epic2.getId()).getFirst());
    }

    @Test
    public void checkDeleteSubTaskByIdIfSubTaskNotExists() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Название epic1", "Описание epic1");
        Task task = new Task("Название task", "Описание task");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");

        manager.createEpic(epic1);
        manager.createSubTask(subtask1,epic1.getId());
        manager.createTask(task);

        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> manager.deleteSubTaskById(40));
        String expectedMessage = "Подзадача не найдена";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        assertEquals(task, manager.getAllTasks().getFirst());
        assertEquals(epic1, manager.getAllEpics().getFirst());
        assertEquals(1, manager.getSubTasksByEpicId(epic1.getId()).size());
        assertEquals(1, manager.getAllSubTasks().size());
        assertEquals(subtask1, manager.getAllSubTasks().get(0));
        assertEquals(subtask1, manager.getSubTasksByEpicId(epic1.getId()).getFirst());
    }

    @Test
    public void checkCalculationStatusForEpicWhenAllSubTasksNew() {
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");

        manager.createEpic(epic);
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask1,epic.getId());
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask2,epic.getId());
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void checkCalculationStatusForEpicWhenOneOfAllSubTasksIsInProgress() {
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        subtask2.setStatus(Status.IN_PROGRESS);

        manager.createEpic(epic);
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask1,epic.getId());
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask2,epic.getId());
        assertEquals(Status.IN_PROGRESS, manager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void checkCalculationStatusForEpicWhenAllSubTasksInProgress() {
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);

        manager.createEpic(epic);
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask1,epic.getId());
        assertEquals(Status.IN_PROGRESS, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask2,epic.getId());
        assertEquals(Status.IN_PROGRESS, manager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void checkCalculationStatusForEpicWhenAllSubTasksInDone() {
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        manager.createEpic(epic);
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask1,epic.getId());
        assertEquals(Status.DONE, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask2,epic.getId());
        assertEquals(Status.DONE, manager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void checkCalculationStatusForEpicWhenSubTaskStatusesInDoneAndInProgress() {
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);

        manager.createEpic(epic);
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask1,epic.getId());
        assertEquals(Status.DONE, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask2,epic.getId());
        assertEquals(Status.IN_PROGRESS, manager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void checkCalculationStatusForEpicWhenSubTaskStatusesIsDoneAndNew() {
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        subtask1.setStatus(Status.DONE);

        manager.createEpic(epic);
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask1,epic.getId());
        assertEquals(Status.DONE, manager.getAllEpics().getFirst().getStatus());
        manager.createSubTask(subtask2,epic.getId());
        assertEquals(Status.IN_PROGRESS, manager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void checkEpicStatusWhenDelitingSubTask() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic = new Epic("Название epic", "Описание epic");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        manager.createEpic(epic);
        manager.createSubTask(subtask1,epic.getId());
        manager.createSubTask(subtask2,epic.getId());
        assertEquals(Status.DONE, manager.getAllEpics().getFirst().getStatus());
        manager.deleteSubTaskById(subtask1.getId());
        assertEquals(Status.DONE, manager.getAllEpics().getFirst().getStatus());
        manager.deleteSubTaskById(subtask2.getId());
        assertEquals(Status.NEW, manager.getAllEpics().getFirst().getStatus());
    }

    @Test
    public void checkEpicStatusWhenDelitingAllSubTasks() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Название epic1", "Описание epic1");
        Epic epic2 = new Epic("Название epic2", "Описание epic2");
        Epic epic3 = new Epic("Название epic3", "Описание epic3");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask3 = new SubTask("Название subTask3", "Описание subTask3");
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.IN_PROGRESS);

        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createEpic(epic3);

        manager.createSubTask(subtask1,epic1.getId());
        manager.createSubTask(subtask2,epic1.getId());
        manager.createSubTask(subtask3,epic2.getId());

        manager.deleteAllSubTasks();
        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus());
        assertEquals(Status.NEW, manager.getEpicById(epic2.getId()).getStatus());
        assertEquals(Status.NEW, manager.getEpicById(epic3.getId()).getStatus());
    }

    @Test
    public void checkEpicStatusWhenUpdatingSubTask() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Название epic1", "Описание epic1");
        Epic epic2 = new Epic("Название epic2", "Описание epic2");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask3 = new SubTask("Название subTask3", "Описание subTask3");

        SubTask subtask4 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask5 = new SubTask("Название subTask2", "Описание subTask2");
        SubTask subtask6 = new SubTask("Название subTask3", "Описание subTask3");

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.IN_PROGRESS);
        subtask4.setStatus(Status.NEW);
        subtask5.setStatus(Status.NEW);
        subtask6.setStatus(Status.NEW);

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        manager.createSubTask(subtask1,epic1.getId());
        manager.createSubTask(subtask2,epic1.getId());
        manager.createSubTask(subtask3,epic2.getId());

        assertEquals(Status.DONE, manager.getEpicById(epic1.getId()).getStatus());
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic2.getId()).getStatus());

        subtask4.setId(subtask1.getId());
        subtask5.setId(subtask2.getId());
        subtask6.setId(subtask3.getId());

        manager.updateSubTask(subtask6);
        assertEquals(Status.NEW, manager.getEpicById(epic2.getId()).getStatus());
        manager.updateSubTask(subtask5);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic1.getId()).getStatus());
        manager.updateSubTask(subtask4);
        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus());

    }

    @Test
    public void checkEpicStatusWhenUpdatingEpic() throws InvalidInputException {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Название epic1", "Описание epic1");
        Epic epic2 = new Epic("Название epic1", "Описание epic1");
        SubTask subtask1 = new SubTask("Название subTask1", "Описание subTask1");
        SubTask subtask2 = new SubTask("Название subTask1", "Описание subTask1");

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        epic2.setStatus(Status.DONE);

        manager.createEpic(epic1);
        epic2.setId(epic1.getId());

        manager.createSubTask(subtask1,epic1.getId());
        manager.createSubTask(subtask2,epic1.getId());

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic1.getId()).getStatus());

        manager.updateEpic(epic2);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic1.getId()).getStatus());
    }
}
