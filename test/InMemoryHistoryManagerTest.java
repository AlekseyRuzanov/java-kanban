import Exceptions.InvalidInputException;
import Managers.Managers;
import Managers.TaskManager;
import Managers.HistoryManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @BeforeEach
    public void clearHistory() {
        historyManager.clearHistory();
    }

    @Test
    public void getHistoryWhenItsEmpty() throws InvalidInputException {
        Exception exception = Assertions.assertThrows(InvalidInputException.class, () -> historyManager.getHistory());
        String expectedMessage = "История просмотров пуста";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getHistoryWhenListSizeIsOne() throws InvalidInputException {
        int epicId = taskManager.getCounter() + 1;
        Epic epic = new Epic("Название Epic", "Описание Epic");
        taskManager.createEpic(epic);
        taskManager.getEpicById(epicId);

        assertEquals(taskManager.getEpicById(epicId), historyManager.getHistory().getFirst());
    }

    @Test
    public void getHistoryWhenViewsIsNine() throws InvalidInputException {
        int epicId = taskManager.getCounter() + 1;
        int subTaskId = taskManager.getCounter() + 2;
        int taskId = taskManager.getCounter() + 3;

        Epic epic = new Epic("Название Epic", "Описание Epic");
        Task task = new Task("Название Task 1", "Описание Task 1");
        SubTask subTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, epicId);
        taskManager.createTask(task);
        for (int i = 0; i < 3; i++) {
            taskManager.getEpicById(epicId);
            taskManager.getSubTaskById(subTaskId);
            taskManager.getTaskById(taskId);
        }
        int position = 0;
        for (int i = 0; i < 3; i++) {
            assertEquals(taskManager.getAllEpics().getFirst(), historyManager.getHistory().get(position++));
            assertEquals(taskManager.getAllSubTasks().getFirst(), historyManager.getHistory().get(position++));
            assertEquals(taskManager.getAllTasks().getFirst(), historyManager.getHistory().get(position++));

        }
    }

    @Test
    public void getHistoryWhenViewsIsTen() throws InvalidInputException {
        int epicId = taskManager.getCounter() + 1;
        int subTaskId = taskManager.getCounter() + 2;
        int taskId = taskManager.getCounter() + 3;

        Epic epic = new Epic("Название Epic", "Описание Epic");
        Task task = new Task("Название Task 1", "Описание Task 1");
        SubTask subTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, epicId);
        taskManager.createTask(task);
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            if (counter < 9) {
                taskManager.getEpicById(epicId);
                counter++;
                taskManager.getSubTaskById(subTaskId);
                counter++;
                taskManager.getTaskById(taskId);
                counter++;
            } else if (counter == 9) {
                taskManager.getEpicById(epicId);
            }
        }
        counter = 0;
        for (int i = 0; i < 4; i++) {
            if (counter < 9) {
                assertEquals(taskManager.getAllEpics().getFirst(), historyManager.getHistory().get(counter++));
                assertEquals(taskManager.getAllSubTasks().getFirst(), historyManager.getHistory().get(counter++));
                assertEquals(taskManager.getAllTasks().getFirst(), historyManager.getHistory().get(counter++));
            } else if (counter == 9) {
                assertEquals(taskManager.getAllEpics().getFirst(), historyManager.getHistory().get(counter++));
            }
        }
    }

    @Test
    public void getHistoryWhenViewsIsEleven() throws InvalidInputException {
        int epicId = taskManager.getCounter() + 1;
        int subTaskId = taskManager.getCounter() + 2;
        int taskId = taskManager.getCounter() + 3;

        Epic epic = new Epic("Название Epic", "Описание Epic");
        Task task = new Task("Название Task 1", "Описание Task 1");
        SubTask subTask = new SubTask("Название SubTask 1", "Описание SubTask 1");
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, epicId);
        taskManager.createTask(task);
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            if (counter < 9) {
                taskManager.getEpicById(epicId);
                counter++;
                taskManager.getSubTaskById(subTaskId);
                counter++;
                taskManager.getTaskById(taskId);
                counter++;
            } else if (counter == 9) {
                taskManager.getEpicById(epicId);
                counter++;
                taskManager.getSubTaskById(subTaskId);
            }
        }
        counter = 0;
        for (int i = 0; i < 4; i++) {
            if (counter < 9) {
                assertEquals(taskManager.getAllSubTasks().getFirst(), historyManager.getHistory().get(counter++));
                assertEquals(taskManager.getAllTasks().getFirst(), historyManager.getHistory().get(counter++));
                assertEquals(taskManager.getAllEpics().getFirst(), historyManager.getHistory().get(counter++));
            } else if (counter == 9) {
                assertEquals(taskManager.getAllSubTasks().getFirst(), historyManager.getHistory().get(counter++));
            }
        }
    }
}
